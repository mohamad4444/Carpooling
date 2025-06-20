

function readLoginForm() {
    return {
        username: document.getElementById("username-login").value,
        password: document.getElementById("password-login").value
    };
}
function clearLoginForm() {
    document.getElementById("username-login").value = "";
    document.getElementById("password-login").value = "";
}

function showloginDiv()
{
    document.getElementById("loginDiv").style.display = "none";
}
function setGlobalUserState(result, response) {
    globalUserId = result.userId;
    globalUsername = result.username;
    globalFullName = result.fullName;
    globalToken = response.headers.get('Authorization');
}
function resetGlobalUserState() {
    globalUserId = "";
    globalUsername = "";
    globalFullName = "";
    globalToken = "";
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

function showLoginSection() {
    document.getElementById("login-section").style.display = "block";
    document.getElementById("tab-section").style.display = "none";
    document.getElementById("loginDiv").style.display = "none";
    document.getElementById('registerDiv').style.display = 'none';
    document.getElementById("userWelcome").innerText = "";
}

function showTabSection(fullName) {
    document.getElementById("login-section").style.display = "none";
    document.getElementById("tab-section").style.display = "block";
    document.getElementById("loginDiv").style.display = "none";
    document.getElementById('registerDiv').style.display = 'none';
    document.getElementById("userWelcome").innerText = fullName;
}

function handleError(err) {
    console.error("Error:", err);

    // Just reset local app state
    resetGlobalUserState();
    showLoginSection();

    alert("Something went wrong! You have been logged out.\n" + err);
}
function readRegistrationForm() {
    return {
        username: document.getElementById("username-register").value,
        password: document.getElementById("password-register").value,
        firstname: document.getElementById("vorname").value,
        lastname: document.getElementById("nachname").value,
        email: document.getElementById("email").value,
        address: {
            street: document.getElementById("strasse").value,
            streetNumber: document.getElementById("hausnummer").value,
            zip: document.getElementById("plz").value,
            city: document.getElementById("ort").value
        }
    };
}

function clearRegistrationForm() {
    const ids = [
        "username-register", "password-register", "vorname", "nachname", "email",
        "strasse", "hausnummer", "plz", "ort"
    ];
    ids.forEach(id => document.getElementById(id).value = "");
}

function readAngebotForm() {
  const dateinput = document.getElementById("abfahrtszeit");
  const localDateTimeStr = dateinput.value; // z.B. "2025-06-13T09:00"
  return{
      startTime: localDateTimeStr,
      distance: Number(document.getElementById("einzugsbereich").value),
    };
  // if (localDateTimeStr) {
  //   const date = new Date(localDateTimeStr);
  //   const instantString = date.toISOString();

  //   console.log("Für Instant geeignet:", instantString);

  //   return {
  //     startTime: instantString,
  //     distance: Number(document.getElementById("einzugsbereich").value),
  //   };
  // }
  // // Falls kein Datum angegeben, gib zumindest die Felder zurück, z.B. null oder undefined
  // return {
  //   startTime: null,
  //   distance: Number(document.getElementById("einzugsbereich").value) || 0,
  // };
}

function clearAngebotForm() {
    document.getElementById("abfahrtszeit").value = "";
    document.getElementById("einzugsbereich").value = 5;  // reset to default
}

function readSucheForm() {
    return {
        startTime: document.getElementById("suche-abfahrtszeit").value
    };
}
function clearSucheForm() {
    document.getElementById("suche-abfahrtszeit").value = "";
}
function formatDateTime(isoString) {
    const date = new Date(isoString);
    return date.toLocaleString(); // e.g., "20.06.2025, 15:30"
}
