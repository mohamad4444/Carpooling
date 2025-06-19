
function login()
{
    let username = document.getElementById("usernameLogin").value;
    let password = document.getElementById("passwordLogin").value;

    console.log(username + " " + password);

    //fet

    showloginDiv();
}


function showloginDiv()
{
    document.getElementById("noteDiv").style.display = "block";
    document.getElementById("loginDiv").style.display = "none";
}
 function changeTab(evt, tabId) {
    const contents = document.getElementsByClassName("tab-content");
    const links = document.getElementsByClassName("tablink");

    // Hide all tab contents
    for (let i = 0; i < contents.length; i++) {
      contents[i].classList.remove("visible");
    }

    // Remove active class from all buttons
    for (let i = 0; i < links.length; i++) {
      links[i].classList.remove("active-tab");
    }

    // Show selected tab content
    document.getElementById(tabId).classList.add("visible");

    // Add active class to clicked tab
    evt.currentTarget.classList.add("active-tab");
  }