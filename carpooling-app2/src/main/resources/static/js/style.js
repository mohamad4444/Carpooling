
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