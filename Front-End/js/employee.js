const API_URL = "http://localhost:8085/api/v1/employee";

let lastRegisterTime = 0;
const MIN_TIME_BETWEEN_REQUESTS = 5000;

function isEmailValid(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function isPhoneValid(phone) {
    return /^\d{7,15}$/.test(phone);
}

function isNameValid(name) {
    return /^[A-Za-zÀ-ÿ\s]{2,50}$/.test(name);
}

function isPasswordStrong(password) {
    return password.length >= 6;
}

// Función: Login de empleado
async function loginEmployee() {
    try {
        // Obtención y validación de datos del formulario
        const email = document.getElementById("email-login").value.trim();
        const password = document.getElementById("password-login").value.trim();

        if (!email || !password) {
            alert("Todos los campos son obligatorios.");
            return;
        }

        let bodyContent = JSON.stringify({
            "email": email,
            "password": password
        });

        let response = await fetch(`${API_URL}/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: bodyContent
        });

        if (response.ok) {
            let data = await response.json();
            alert(data.message); // Mensaje en caso de éxito
            window.location.href = "/employee.html"; // Redirige al CRUD
        } else {
            let errorData = await response.json();
            alert(errorData.message); // Mensaje en caso de error específico
        }
    } catch (error) {
        console.error("Error al iniciar sesión:", error);
        alert("Ocurrió un error durante el inicio de sesión: " + error.message);
    }
}

async function registerEmployee() {
    try {
        const now = Date.now();
        if (now - lastRegisterTime < MIN_TIME_BETWEEN_REQUESTS) {
            alert("Por favor espera un momento antes de volver a intentar.");
            return;
        }

        const name = document.getElementById("nombre").value.trim();
        const position = document.getElementById("cargo").value.trim();
        const phone = document.getElementById("telefono").value.trim();
        const password = document.getElementById("clave").value.trim();
        const email = document.getElementById("email").value.trim();

        if (!name || !position || !phone || !password || !email) {
            alert("Todos los campos son obligatorios.");
            return;
        }

        if (!isNameValid(name)) {
            alert("Nombre inválido.");
            return;
        }

        if (!isPhoneValid(phone)) {
            alert("Número de teléfono inválido.");
            return;
        }

        if (!isPasswordStrong(password)) {
            alert("La contraseña debe tener al menos 6 caracteres.");
            return;
        }

        if (!isEmailValid(email)) {
            alert("Correo inválido.");
            return;
        }

        const captchaToken = grecaptcha.getResponse();
        if (!captchaToken) {
            alert("Por favor completa el reCAPTCHA.");
            return;
        }

        lastRegisterTime = now;

        const bodyContent = JSON.stringify({
            id: 0,
            name,
            position,
            phone,
            password,
            email,
            token: captchaToken  // ✅ El backend espera "token"
        });

        const response = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: bodyContent
        });

        const responseText = await response.text();

        if (response.ok) {
            alert("✅ Registro exitoso: " + responseText);
            ["nombre", "cargo", "telefono", "clave", "email"].forEach(id => {
                document.getElementById(id).value = "";
            });
            grecaptcha.reset();
        } else {
            alert("❌ Error: " + responseText);
            grecaptcha.reset();
        }

    } catch (error) {
        console.error("Error al registrar empleado:", error);
        alert("❌ Error inesperado en el registro.");
        grecaptcha.reset();
    }
}
