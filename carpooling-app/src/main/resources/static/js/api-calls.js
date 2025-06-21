async function apiCallLogin() {
    let loginData =readLoginForm();
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
        setGlobalUserState(result,response);
        console.log("token:  "+globalToken +"response: "+JSON.stringify(result));
        clearLoginForm();
        showTabSection(globalFullName);
        await refreshData();
        await showMap();
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
            resetGlobalUserState();
            showLoginSection();
        } else {
            throw new Error("Logout failed with status " + response.status);
        }
    } catch (error) {
        console.error("Logout error:", error);
        handleError(error);
    }
}


async function apiCallCreateUser() {
    const userData = readRegistrationForm()
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
        setGlobalUserState(result,response);
        console.log("token:  "+globalToken +"response: "+result);
        alert("Benutzer erfolgreich registriert!");


        clearRegistrationForm();
        showTabSection(globalFullName)
        await refreshData();
        await showMap();
    } catch (error) {
        console.error("Registration error:", error);
        alert("Fehler bei der Registrierung: " + error.message);
    }
}

async function apiCallCreateOffer() {
    const offerData = readAngebotForm();

    try {
        const response = await fetch(globalUrl + "/users/"+globalUserId+"/offers", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': globalToken
            },
            body: JSON.stringify(offerData)
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || "Offer creation failed");
        }

        alert("Angebot erfolgreich erstellt!");
        clearAngebotForm();
        await refreshData();

    } catch (error) {
        console.error("Error creating offer:", error);
        alert("Fehler beim Erstellen des Angebots: " + error.message);
    }
}
async function apiCallCreateSuche() {
    const sucheData = readSucheForm();

    try {
        const response = await fetch(`${globalUrl}/users/${globalUserId}/requests`, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': globalToken
            },
            body: JSON.stringify(sucheData)
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || "Suche anlegen fehlgeschlagen.");
        }

        alert("Suchanfrage erfolgreich angelegt!");
        clearSucheForm();

        // Optional: reload user's searches
        await refreshData();

    } catch (error) {
        console.error("Error creating Suche:", error);
        alert("Fehler beim Anlegen der Suche: " + error.message);
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
async function apiCallOffersFromUser(userId) {
    try {
        const response = await fetch(`${globalUrl}/users/${userId}/offers`, {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Authorization': globalToken
            }
        });

        if (!response.ok) {
            const errMsg = await response.text();
            throw new Error(errMsg || "Fehler beim Laden der Angebote.");
        }

        return await response.json(); // array of offers
    } catch (err) {
        console.error("apiCallOffersFromUser error:", err);
        alert("Angebote konnten nicht geladen werden.");
        return [];
    }
}

async function apiCallMatchingOffers(startTime) {
    try {
        const response = await fetch(`${globalUrl}/users/${globalUserId}/requests/matches`, {
            method: 'POST', 
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': globalToken
            },
            body: JSON.stringify({ startTime }) // include startTime in the request body
        });

        if (!response.ok) {
            const errMsg = await response.text();
            throw new Error(errMsg || "Fehler beim Laden der Angebote.");
        }

        return await response.json(); // returns array of MatchedOfferDtoOut
    } catch (err) {
        console.error("apiCallMatchingOffers error:", err);
        alert("Angebote konnten nicht geladen werden.");
        return [];
    }
}
async function apiCallUserRequests() {
    try {
        const response = await fetch(`${globalUrl}/users/${globalUserId}/requests`, {
            headers: {
                'Accept': 'application/json',
                'Authorization': globalToken
            }
        });

        if (!response.ok) throw new Error(await response.text());
        return await response.json(); // Array of { startTime}
    } catch (err) {
        console.error("Error loading user requests:", err);
        return [];
    }
}
