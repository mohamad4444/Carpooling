//This class attaches buttons and html elements to methods
window.addEventListener('DOMContentLoaded', () => {
    initListener();  // your existing event listener setup
    initMap();       // initialize the map on DOM ready
});
function initListener() {
    document.getElementById("loginBtn").addEventListener("click", apiCallLogin);
    document.getElementById("logoutBtn").addEventListener("click", apiCallLogout);
    document.getElementById("registerBtn").addEventListener("click", apiCallCreateUser);
    document.getElementById("angebotErstellenBtn").addEventListener("click", apiCallCreateOffer);
    document.getElementById("sucheErstellenBtn").addEventListener("click", apiCallCreateSuche);
    
    

}
