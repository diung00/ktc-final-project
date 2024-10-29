if (localStorage.getItem("jwt")){
    location.href = "/views/get-my-profile";
}

const loginForm = document.getElementById("login-form");
const usernameInput = document.getElementById("identifier");
const passwordInput = document.getElementById("password");

loginForm.addEventListener("submit", (e)=>{
    e.preventDefault();

    const username = usernameInput.value;
    const password = passwordInput.value;

    fetch("/users/login", {
        method: "post",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
                username,
                password,
        }),
    })
        .then(response => {
            if(response.ok){
                return response.json();
            }
            else throw Error(response.status);
        })
        .then(json=> {
            localStorage.setItem("jwt", json.token);
            location.href = "/views/get-my-profile";
        })
        .catch(e=> alert(e.message));
});