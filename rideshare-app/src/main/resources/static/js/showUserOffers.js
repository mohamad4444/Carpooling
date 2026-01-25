async function showUserOffers(userId) {
  let offers = await apiCallOffersFromUser(userId);
  let offersDiv = document.getElementById('offersContainer');
  offersDiv.replaceChildren();

  let table = document.createElement('table');
  table.classList.add('display-table');

  let tableHeader = document.createElement('thead');
  let headerNames = ["Abfahrtszeit", "Einzugsbereich (km)", "Action"];
  let headerRow = document.createElement('tr');
  headerNames.forEach(name => {
    let th = document.createElement('th');
    th.textContent = name;
    headerRow.appendChild(th);
  });
  tableHeader.appendChild(headerRow);
  table.appendChild(tableHeader);
  offersDiv.appendChild(table);

  let attributeNames = ["startTime", "distance"];
  offers.forEach((offer) => {
    let row = document.createElement('tr');
    attributeNames.forEach(name => {
      let td = document.createElement('td');
      td.textContent = offer[name];
      row.appendChild(td);
    });

    let actionTd = document.createElement('td');
    let deleteBtn = document.createElement('button');
    deleteBtn.textContent = "Delete";
    deleteBtn.style.color = "red";
    deleteBtn.onclick = async () => {
      if (confirm("Are you sure you want to delete this offer?")) {
        const success = await deleteOffer(offer.offerId);
        if (success) {
          alert("Offer deleted.");
          await showUserOffers(userId); // refresh table
        }
      }
    };
    actionTd.appendChild(deleteBtn);
    row.appendChild(actionTd);

    table.appendChild(row);
  });
}
