window.onload = function () {
    document.getElementById("creatBtn").onclick = createNote;
    document.getElementById("loginBtn").onclick = login;
    //showNotes();
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

async function showNotes() {
    let notes = await getNotes();

    let notesDiv = document.getElementById("notes");
    notesDiv.innerHTML = "";

    let tableElement = document.createElement("table");

    let tableHeader = document.createElement("tr");
    let tableHeaderCreationTime = document.createElement("th");
    tableHeaderCreationTime.innerHTML = "Creation Time";
    tableHeader.appendChild(tableHeaderCreationTime);

    let tableHeaderContent = document.createElement("th");
    tableHeaderContent.innerHTML = "Content";
    tableHeader.appendChild(tableHeaderContent);

    let tableHeaderAction = document.createElement("th");
    tableHeaderAction.innerHTML = "Aktion";
    tableHeader.appendChild(tableHeaderAction);

    tableElement.appendChild(tableHeader);

    notes.forEach(note => {
        let trElement = document.createElement("tr");

        let tdElementCreationTime = document.createElement("td");
        tdElementCreationTime.innerHTML = note.creationTime;
        trElement.appendChild(tdElementCreationTime);

        let tdElementContent = document.createElement("td");
        tdElementContent.innerHTML = note.content;
        trElement.appendChild(tdElementContent);

        let tdElementAction = document.createElement("td");
        let deleteBtn = document.createElement("button");
        deleteBtn.innerHTML = "Delete";
        deleteBtn.onclick = function () { deleteNote(note.id); }
        tdElementAction.appendChild(deleteBtn);
        trElement.appendChild(tdElementAction);

        tableElement.appendChild(trElement);
    });

    notesDiv.appendChild(tableElement);
}


async function deleteNote(id) {
    try {
        let response = await fetch("/notes/" + id, {
            method: "DELETE",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            }
        });

        let result = await response.json();
        console.log(result);
        showNotes();
    }
    catch (err) {
        console.log(err);
    }
}

async function getNotes() {
    try {
        let response = await fetch("/notes", {
            method: "GET",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            }
        });

        let result = await response.json();
        console.log(result);

        return result;
    }
    catch (err) {
        console.log(err);
    }
}



async function createNote() {

    let inputElement = document.getElementById("notecontent");
    let noteContent = inputElement.value;

    let data = {
        "content": noteContent
    };

    try {
        let response = await fetch("/notes", {
            method: "POST",
            headers: {
                "Accept": "application/json",
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        let result = await response.json();
        console.log(result);
        inputElement.value = "";
        showNotes();
    }
    catch (err) {
        console.log(err);
    }
}
