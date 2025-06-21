async function showUserRequestsAndMatches() {
    const container = document.getElementById('searchContainer');
    container.replaceChildren();

    // Step 1: Get all requests from user
    const requests = await apiCallUserRequests(); // expects array with startTimeDisplay and startTimeIso

    if (!requests.length) {
        container.textContent = "No requests found.";
        return;
    }

    for (const request of requests) {
        // Use the ISO format from the request to get matching offers
        const offers = await apiCallMatchingOffers(request.startTimeIso);

        // Create a wrapper div for this request
        const requestDiv = document.createElement('div');
        requestDiv.classList.add('request-block');

        // Display the human-friendly formatted date/time
        const title = document.createElement('h4');
        title.textContent = `Request Start: ${request.startTimeDisplay}`;
        requestDiv.appendChild(title);

        // Create dropdown (select element)
        if (offers.length > 0) {
            const select = document.createElement('select');
            select.classList.add('offer-select');

            offers.forEach((offer, index) => {
                const option = document.createElement('option');
                option.value = index;
                // Assuming offer also has startTimeDisplay for UI
                option.textContent = `Offer at ${offer.startTime}, ${offer.fullName} km, ${offer.email}`;
                select.appendChild(option);
            });

            requestDiv.appendChild(select);
        } else {
            const noMatch = document.createElement('p');
            noMatch.textContent = "No matching offers found.";
            requestDiv.appendChild(noMatch);
        }

        container.appendChild(requestDiv);
    }
}
