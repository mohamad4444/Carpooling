function initMap() {
    if (map !== undefined) {
        map.off();
        map.remove();
    }

    map = L.map('map').setView(hsCoords, 13);

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors'
    }).addTo(map);

    redIcon = new L.Icon({
        iconUrl: './images/marker-icon-red.png',
        shadowUrl: './images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
    });

    blackIcon = new L.Icon({
        iconUrl: './images/marker-icon-black.png',
        shadowUrl: './images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
    });

    blueIcon = new L.Icon({
        iconUrl: './images/marker-icon.png',
        shadowUrl: './images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
    });
}




function clearUserMarkers() {
    userMarkers.forEach(marker => map.removeLayer(marker));
    userMarkers = [];
}

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
                .addTo(map);
                //.bindPopup(`<b>${marker.username}</b>`);

            userMarkers.push(leafletMarker);
        });
    }
}

async function showMap() {
    try {
        const users = await getUsers();
        if (users && users.length) {
            addUsersToMap(users, globalUserId);
        }
    } catch (err) {
        console.error("Error in showMap:", err);
    }
}