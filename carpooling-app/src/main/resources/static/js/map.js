let map;
let userMarkers = [];

let redIcon, blueIcon;
function initMap() {
    if (map !== undefined) {
        map.off();
        map.remove();
    }

    // Set view to Kaiserslautern
    map = L.map('map').setView([49.2597766, 7.3599692], 13);

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    // Custom red marker
    redIcon = new L.Icon({
        iconUrl: './images/marker-icon-red.png',
        shadowUrl: './images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41]
    });
    // Custom black marker
    const blackIcon = new L.Icon({
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


    const hsCoords = [49.2597766, 7.3599692];

    L.marker(hsCoords, { icon: blackIcon }).addTo(map)
        .bindPopup('HS-KL')
        .openPopup();



}

// Wait for DOM to load
window.onload = initMap;
