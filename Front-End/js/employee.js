const API_URL = "http://localhost:8085/api/v1/employee";

// Función: Registrar un empleado
async function registerEmployee() {
    try {
        // Obtención y validación de datos del formulario
        const name = document.getElementById("nombre").value.trim();
        const position = document.getElementById("cargo").value.trim();
        const phone = document.getElementById("telefono").value.trim();
        const password = document.getElementById("clave").value.trim();
        const email = document.getElementById("email").value.trim();

        if (!name || !position || !phone || !password || !email) {
            alert("Todos los campos son obligatorios.");
            return;
        }

        let bodyContent = JSON.stringify({
            "id": 0,
            "name": name,
            "position": position,
            "phone": phone,
            "password": password,
            "email": email
        });

        let response = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: bodyContent
        });

        if (response.ok) {
            let data = await response.text();
            alert(data); // Mensaje en caso de éxito
            
            // Limpiar el formulario después de un registro exitoso
            document.getElementById("nombre").value = "";
            document.getElementById("cargo").value = "";
            document.getElementById("telefono").value = "";
            document.getElementById("clave").value = "";
            document.getElementById("email").value = "";
            
            // Actualizar la página
            window.location.reload();
        } else {
            let errorText = await response.text();
            alert(`Error: ${errorText}`); // Mensaje con error específico del backend
        }
    } catch (error) {
        console.error("Error al registrar empleado:", error);
        alert("Ocurrió un error durante el registro: " + error.message);
    }
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

