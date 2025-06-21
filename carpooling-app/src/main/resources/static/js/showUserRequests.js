async function showUserRequestsAndMatches() {
    const container = document.getElementById('searchContainer');
    container.replaceChildren();

    // Get requests
    const requests = await apiCallUserRequests();

    if (!requests.length) {
        container.textContent = "No requests found.";
        return;
    }

    // Create table and headers
    const table = document.createElement('table');
    table.classList.add('display-table');

    const thead = document.createElement('thead');
    const headerRow = document.createElement('tr');

    const headers = ['Request Start', 'Matching Offers'];
    headers.forEach(headerText => {
        const th = document.createElement('th');  // use th for header cells
        th.textContent = headerText;
        headerRow.appendChild(th);
    });
    thead.appendChild(headerRow);
    table.appendChild(thead);

    const tbody = document.createElement('tbody');

    // For each request, add a row with matching offers
    for (const request of requests) {
        const offers = await apiCallMatchingOffers(request.startTimeIso);

        const row = document.createElement('tr');

        // Request start time cell
        const requestCell = document.createElement('td');
        requestCell.textContent = request.startTimeDisplay;
        row.appendChild(requestCell);

        // Matching offers cell
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

        tbody.appendChild(row);
    }

    table.appendChild(tbody);
    container.appendChild(table);
}
