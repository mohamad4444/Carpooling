async function showUserOffers(userId) {
    // REST-Call auf Controller, liefert ein Array mit JSON-Objekten
    // Benutzerdefinierte JavaScript-Funktion: apiCallOffersFromUser(userId)
    let offers = await apiCallOffersFromUser(userId);
    // *** Erzeugen der Tabelle mit den Suchergebnissen ***
    // Referenz auf das div-Element ermitteln und dessen Inhalt leeren
    let offersDiv = document.getElementById('offersContainer');
    offersDiv.replaceChildren();
    // Erzeugen eines Table-Knotens
    let table = document.createElement('table');
    table.classList.add('display-table');
    // Erzeugen der Zeile mit Table-Header
    let tableHeader = document.createElement('thead');
    let headerNames = [
        "Abfahrtszeit",
        "Einzugsbereich (km)"
    ];
    headerNames.forEach(element => {
        let td = document.createElement("td");
        td.textContent = element;
        tableHeader.appendChild(td);
    });
    table.appendChild(tableHeader);
    offersDiv.appendChild(table);
    // Erzeugen der Zeilen mit den JSON-Objekten
    let attributeNames = ["startTime", "distance"];
    offers.forEach((jsonObject) => {
        let row = document.createElement("tr");
        attributeNames.forEach(name => {
            let td = document.createElement("td");
            td.textContent = jsonObject[name];
            row.appendChild(td);
        });
        table.appendChild(row);
    });
}