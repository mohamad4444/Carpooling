window.addEventListener('DOMContentLoaded', initListener);

function initListener() {
    document.getElementById("loginBtn")
        .addEventListener("click", apiCallLogin);
    document.getElementById("logoutBtn")
        .addEventListener("click", apiCallLogout);
    document.getElementById("registerBtn")
        .addEventListener("click", apiCallCreateUser);
}

var globalUserId = ""
var globalUsername = "";
var globalToken = "";
var globalFullName = "";
var globalUrl="http://localhost:8080";
async function apiCallLogin() {
    let username = document.getElementById("username-login").value;
    let password = document.getElementById("password-login").value;
    let loginData = {
        "username": username,
        "password": password
    };
    try {
        const response = await fetch(globalUrl+"/users/login",
            {
                method: 'post',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginData),
            });
        const result = await response.json();
        // Setzte globale Variablen
        globalUserId = result.userId;
        globalUsername = result.username;
        globalFullName = result.fullName;
        globalToken = response.headers.get('Authorization');
        console.log("token:  "+globalToken +"response: "+JSON.stringify(result));

        document.getElementById("username-login").value = "";
        document.getElementById("password-login").value = "";
        // Benutzerdefinierte Function, die alle Daten (Tabellen)
        // aktualisiert.
        document.getElementById("login-section").style.display = "none";
        document.getElementById("tab-section").style.display = "block";
        document.getElementById("userWelcome").innerText = globalFullName ;
        document.getElementById("loginDiv").style.display = "none";

        await refreshData();
    } catch (error) {
        console.error("Error:", error);
        handleError(error);
    }
}


async function apiCallLogout() {
    try {
        const response = await fetch(globalUrl + "/users/logout/" + globalUserId, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': globalToken
            }
        });

        if (response.ok) {
            console.log("Logged out successfully");

            // Reset all global variables
            globalUserId = "";
            globalUsername = "";
            globalToken = "";
            globalFullName = "";

            // Show login section again
            document.getElementById("tab-section").style.display = "none";
            document.getElementById("login-section").style.display = "block";
            document.getElementById("loginDiv").style.display = "none";
            document.getElementById("userWelcome").innerText = "";

        } else {
            throw new Error("Logout failed with status " + response.status);
        }
    } catch (error) {
        console.error("Logout error:", error);
        handleError(error);
    }
}

async function apiCallCreateUser() {
    const userData = {
        username: document.getElementById("username-register").value,
        password: document.getElementById("password-register").value,
        firstname: document.getElementById("vorname").value,
        lastname: document.getElementById("nachname").value,
        email: document.getElementById("email").value,
        address:{
            street: document.getElementById("strasse").value,
            streetNumber: document.getElementById("hausnummer").value,
            zip: document.getElementById("plz").value,
            city: document.getElementById("ort").value
        },

    };
    console.log("register method called");
    console.log(userData);

    try {
        const response = await fetch(globalUrl+"/users/createuser", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || "Registration failed");
        }
        const result = await response.json();
        globalUserId = result.userId;
        globalUsername = result.username;
        globalToken = response.headers.get('Authorization');
        console.log("token:  "+globalToken +"response: "+result);
        alert("Benutzer erfolgreich registriert!");
        document.getElementById('registerDiv').style.display = 'none';

        // Optional: clear form fields after success
        ["username-register", "password-register", "vorname", "nachname", "email", "strasse", "hausnummer", "plz", "ort"]
            .forEach(id => document.getElementById(id).value = "");
        // Hide Registration/Login section and show tabs
        document.getElementById("login-section").style.display = "none";
        document.getElementById("tab-section").style.display = "block";
        document.getElementById("userWelcome").innerText = globalFullName ;
        //await refreshData();
    } catch (error) {
        console.error("Registration error:", error);
        alert("Fehler bei der Registrierung: " + error.message);
    }
}

async function apiCallCreateOffer() {
    ;
}

async function refreshData() {
    // await showUserOffers(globalUserId);
    // await showUserBids(globalUserId);
    // await showOffersForUser(globalUserId);
    // await showAllSales();
    await afterLoginOrUserCreation()
    // Setzen des Begrüßungstextes
    document.getElementById("userWelcome").innerText = globalFullName;
}

async function afterLoginOrUserCreation() {
    try {
        const users = await getUsers();  // call your existing fetch function

        if (users && users.length) {
            addUsersToMap(users, globalUserId);  // add markers on map
        }

        //await refreshData();

    } catch (err) {
        console.error("Error in afterLoginOrUserCreation:", err);
    }
}

async function getUsers() {
    try {
        let response = await fetch(globalUrl+"/users", {
            method: "GET",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            }
        });

        let result = await response.json();
        console.log(result);

        return result;
    }
    catch (err) {
        console.log(err);
    }
}
function clearUserMarkers() {
    userMarkers.forEach(marker => map.removeLayer(marker));
    userMarkers = [];
}

const hsId = 'HSKL_UNIQUE_ID';  // a unique ID for HS-KL so you can identify it later

function getOffsetCoords(lat, lng, index, total) {
    const offsetAmount = 0.00005; // ~11 meters, tweak as needed

    // Calculate angle so markers spread evenly on circle
    // Add Math.PI / total to center the cluster (offset by half step)
    const angle = (2 * Math.PI / total) * index + Math.PI / total;

    const offsetLat = offsetAmount * Math.cos(angle);
    const offsetLng = offsetAmount * Math.sin(angle);

    return [lat + offsetLat, lng + offsetLng];
}


function addUsersToMap(users, currentUserId) {
    clearUserMarkers();

    // Create a copy of users plus the HS-KL marker as a "user"
    const allMarkers = users.slice(); // copy array
    allMarkers.push({
        userId: hsId,
        username: 'HS-KL',
        latitude: hsCoords[0],
        longitude: hsCoords[1]
    });

    // Group all markers by location string "lat,lng"
    const markersByLocation = {};

    allMarkers.forEach(marker => {
        const key = `${marker.latitude},${marker.longitude}`;
        if (!(key in markersByLocation)) {
            markersByLocation[key] = [];
        }
        markersByLocation[key].push(marker);
    });

    // For each location, add markers with offset
    for (const loc in markersByLocation) {
        const markersAtLoc = markersByLocation[loc];
        const [latStr, lngStr] = loc.split(',');
        const lat = parseFloat(latStr);
        const lng = parseFloat(lngStr);

        markersAtLoc.forEach((marker, idx) => {
            let markerCoords;

            if (idx === 0) {
                // First marker: no offset, place exactly on location
                markerCoords = [lat, lng];
            } else {
                // Others: offset in circle around center
                markerCoords = getOffsetCoords(lat, lng, idx - 1, markersAtLoc.length - 1);
            }

            // Use red icon for current user, black for HS-KL, blue for others
            let icon;

            if (marker.userId === hsId) {
                // HS-KL gets black icon (your existing blackIcon)
                icon = blackIcon;
            } else if (marker.userId === currentUserId) {
                icon = redIcon;
            } else {
                icon = blueIcon;
            }

            const leafletMarker = L.marker(markerCoords, { icon: icon })
                .addTo(map)
                .bindPopup(`<b>${marker.username}</b>`);

            userMarkers.push(leafletMarker);
        });
    }
}



// Funktion für eine einfache Fehlerbehandlung
function handleError(err) {
    globalUserId = "";
    globalUsername = "";
   // apiCallLogout(); // Benutzerdefinierte Funktion für den Logout
    alert("Something went wrong! \n" + err);
}

// async function showUserOffers(userId) {
//     // REST-Call auf Controller, liefert ein Array mit JSON-Objekten
//     // Benutzerdefinierte JavaScript-Funktion: apiCallOffersFromUser(userId)
//     let offers = await apiCallOffersFromUser(userId);
//     // *** Erzeugen der Tabelle mit den Suchergebnissen ***
//     // Referenz auf das div-Element ermitteln und dessen Inhalt leeren
//     let offersDiv = document.getElementById('offersContainer');
//     offersDiv.replaceChildren();
//     // Erzeugen eines Table-Knotens
//     let table = document.createElement('table');
//     // Erzeugen der Zeile mit Table-Header
//     let tableHeader = document.createElement('thead');
//     let headerNames = ["Title", "Beschreibung", "Auktionsende",
//         "Startpreis", "Aktueller Preis", "Status"];
//     headerNames.forEach(element => {
//         let td = document.createElement("td");
//         td.textContent = element;
//         tableHeader.appendChild(td);
//     });
//     table.appendChild(tableHeader);
//     offersDiv.appendChild(table);
//     // Erzeugen der Zeilen mit den JSON-Objekten
//     let attributeNames = ["title", "description", "endTime", "startingPrice",
//         "currentPrice", "status"];
//     offers.forEach((jsonObject) => {
//         let row = document.createElement("tr");
//         attributeNames.forEach(name => {
//             let td = document.createElement("td");
//             td.textContent = jsonObject[name];
//             row.appendChild(td);
//         });
//         table.appendChild(row);
//     });
// }