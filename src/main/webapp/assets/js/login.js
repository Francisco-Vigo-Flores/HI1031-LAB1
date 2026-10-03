"use strict";

const passwordInput = document.getElementById("password");
const showPassword = document.getElementById("show-password");

showPassword.addEventListener("change", () => {
    if (showPassword.checked) {
        passwordInput.type = "text";
    } else {
        passwordInput.type = "password";
    }
});
