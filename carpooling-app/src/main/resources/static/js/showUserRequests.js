async function showUserRequestsAndMatches() {
  const container = document.getElementById('searchContainer');
  container.replaceChildren();

  const requests = await apiCallUserRequests();
  if (!requests.length) {
    container.textContent = "No requests found.";
    return;
  }

  const table = document.createElement('table');
  table.classList.add('display-table');

  const thead = document.createElement('thead');
  const headerRow = document.createElement('tr');
  const headers = ['Request Start', 'Matching Offers', 'Action'];
  headers.forEach(text => {
    const th = document.createElement('th');
    th.textContent = text;
    headerRow.appendChild(th);
  });
  thead.appendChild(headerRow);
  table.appendChild(thead);

  const tbody = document.createElement('tbody');

  for (const request of requests) {
    const offers = await apiCallMatchingOffers(request.startTimeIso);

    const row = document.createElement('tr');

    const requestCell = document.createElement('td');
    requestCell.textContent = request.startTimeDisplay;
    row.appendChild(requestCell);

    const offersCell = document.createElement('td');
    if (offers.length > 0) {
      const select = document.createElement('select');
      select.classList.add('offer-select');

      offers.forEach((offer, index) => {
        const option = document.createElement('option');
        option.value = index;
        option.textContent = `Offer at ${offer.startTimeDisplay}, ${offer.fullName}, ${offer.email}`;
        select.appendChild(option);
      });

      offersCell.appendChild(select);
    } else {
      offersCell.textContent = "No matching offers found.";
    }
    row.appendChild(offersCell);

    const actionTd = document.createElement('td');
    const deleteBtn = document.createElement('button');
    deleteBtn.textContent = "Delete";
    deleteBtn.style.color = "red";
    deleteBtn.onclick = async () => {
      if (confirm("Are you sure you want to delete this request?")) {
        const success = await deleteRequest(request.requestId);
        if (success) {
          alert("Request deleted.");
          await showUserRequestsAndMatches(); // refresh table
        }
      }
    };
    actionTd.appendChild(deleteBtn);
    row.appendChild(actionTd);

    tbody.appendChild(row);
  }

  table.appendChild(tbody);
  container.appendChild(table);
}
