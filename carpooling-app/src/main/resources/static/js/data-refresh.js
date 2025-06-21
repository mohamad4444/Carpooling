async function refreshData() {
    await showUserOffers(globalUserId);
    await showUserRequestsAndMatches();
}