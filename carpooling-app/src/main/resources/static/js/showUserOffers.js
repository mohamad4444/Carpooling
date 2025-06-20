async function showUserOffers(userId) {
    // REST-Call auf Controller, liefert ein Array mit JSON-Objekten
    // Benutzerdefinierte JavaScript-Funktion: apiCallOffersFromUser(userId)
    let offers = await apiCallOffersFromUser(userId);
    // *** Erzeugen der Tabelle mit den Suchergebnissen ***
    // Referenz auf das div-Element ermitteln und dessen Inhalt leeren
    const tbody = document.querySelector("#angebote-table tbody");
    tbody.replaceChildren(); // clear old rows
    // Erzeugen eines Table-Knotens
    let table = document.createElement('table');
    // Erzeugen der Zeile mit Table-Header
    let tableHeader = document.createElement('thead');
    let headerNames = ["Title", "Beschreibung", "Auktionsende",
        "Startpreis", "Aktueller Preis", "Status"];
    headerNames.forEach(element => {
        let td = document.createElement("td");
        td.textContent = element;
        tableHeader.appendChild(td);
    });
    table.appendChild(tableHeader);
    offersDiv.appendChild(table);
    // Erzeugen der Zeilen mit den JSON-Objekten
    let attributeNames = ["title", "description", "endTime", "startingPrice",
        "currentPrice", "status"];
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