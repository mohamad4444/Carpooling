let map;
let userMarkers = [];

let redIcon, blueIcon, blackIcon;
const hsCoords = [49.2597766, 7.3599692];

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

// Wait for DOM to load
window.onload = initMap;
