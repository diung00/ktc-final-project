
const container = document.getElementById('container');
//Change between Sign up Form and Sign in Form
const signUpChanger = document.getElementById('signUp');
signUpChanger.addEventListener('click', () => {
    container.classList.add('right-panel-active');
});

const signInChanger = document.getElementById('signIn');
signInChanger.addEventListener('click', () => {
    container.classList.remove('right-panel-active');
});

// Login Function
const loginForm = document.getElementById("login-form");
const loginUsername = document.getElementById("loginUsername");
const loginPassword = document.getElementById("loginPassword");

loginForm.addEventListener("submit", e => {
  e.preventDefault();

  const username = loginUsername.value;
  const password = loginPassword.value;
  fetch("/users/login", {
    method: "POST",
    headers: {
        "Content-Type": "application/json",
    },
    body: JSON.stringify({ 
        username: username,
        password: password
    }),
})

.then(function(response) {
    if (response.ok) return response.json();
    else throw new Error("Login failed");
})
.then(function(json) {
    console.log(json);
    localStorage.setItem("token", json.token);
    location.href = json.redirectUrl;
    // location.href = "/views";
    if (json.role === "ADMIN") {
        location.href = "/views/admin"; // Chuyển hướng đến trang admin
    } else {
        location.href = "/views"; // Chuyển hướng đến trang người dùng
    }
})
.catch(function(error) {
    alert(error.message);
});
});

// Sign-up button
const signUpButton = document.getElementById('signUpButton');
signUpButton.addEventListener('click', signUp);

function signUp(event) {
    event.preventDefault();
    const username = document.getElementById('signUpUsername').value;
    const password = document.getElementById('signUpPassword').value;
    const passCheck = document.getElementById('signUpPassCheck').value;
    const email = document.getElementById('signUpEmail').value;

    fetch('/users/signup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: username,
            password: password,
            passCheck: passCheck,
            email: email
        })
    })
    .then(response => {
        if (response.ok) {
            alert('회원가입이 완료되었습니다. 인증 코드를 이메일로 전송합니다.');
            console.log("sign-up successful, show email verification form")
            // container.classList.add("right-panel-active");
            document.querySelector(".verification-container").style.display = "block";
            document.querySelector(".sign-up-container").style.display = "none";
        } else {
            return response.json().then(data => {
                console.log(data);
                alert(data.message);
            });
        }
    })
    .catch(error => {
        console.error(error);
        alert("회원가입 중 오류가 발생했습니다.");
    })
    .finally(() => signUpButton.disabled = false)
}

// Verification button
const verifyButton = document.getElementById('verifyButton');
verifyButton.addEventListener('click', verifyEmail);

function verifyEmail() {
    const verificationCode = document.getElementById("verificationCode").value;
    const email = document.getElementById('signUpEmail').value;

    fetch(`/users/signup/verify?email=${encodeURIComponent(email)}&code=${encodeURIComponent(verificationCode)}`, {
        method: 'POST'
    })
    .then(response => {
        if (response.ok) {
            alert("이메일 인증이 완료되었습니다! 이제 로그인 할 수 있습니다.");
            document.querySelector(".verification-container").style.display = "none";
            document.querySelector(".sign-in-container").style.display = "block";
        } else {
            return response.json().then(data => {
                console.log(data);
                alert(data.message);
            });
        }
    })
    .catch(error => {
        console.error(error);
        alert("이메일 인증 중 오류가 발생했습니다.");
    });
}