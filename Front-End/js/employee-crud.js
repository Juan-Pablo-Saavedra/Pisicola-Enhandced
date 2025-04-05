const API_URL = "http://localhost:8085/api/v1/employee";

// Mostrar el modal
function openModal() {
    const modal = document.getElementById("edit-modal");
    modal.style.display = "flex";
}

// Cerrar el modal
function closeModal() {
    const modal = document.getElementById("edit-modal");
    modal.style.display = "none";
    document.getElementById("edit-form").reset(); // Limpia el formulario
}

// Función: Registrar un empleado
async function registerEmployee() {
    try {
        let bodyContent = JSON.stringify({
            "id": 0,
            "name": document.getElementById("nombre").value.trim(),
            "position": document.getElementById("cargo").value.trim(),
            "phone": document.getElementById("telefono").value.trim(),
            "password": document.getElementById("clave").value.trim(),
            "email": document.getElementById("email").value.trim()
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
            alert(data); // Mensaje de éxito
            loadTable(); // Recarga la tabla
        } else {
            throw new Error(`Error: ${response.statusText}`);
        }
    } catch (error) {
        console.error("Error al registrar empleado:", error);
        alert("Ocurrió un error durante el registro: " + error.message);
    }
}

// Función: Login de empleado
async function loginEmployee() {
    try {
        let bodyContent = JSON.stringify({
            "email": document.getElementById("email-login").value.trim(),
            "password": document.getElementById("password-login").value.trim()
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
            alert(data.message); // Mensaje de éxito
            window.location.href = "/Front-End/employee.html"; // Redirige al CRUD
        } else {
            let errorData = await response.json();
            alert(errorData.message); // Mensaje de error específico
        }
    } catch (error) {
        console.error("Error al iniciar sesión:", error);
        alert("Ocurrió un error durante el inicio de sesión: " + error.message);
    }
}

// Función: Cargar todos los empleados en la tabla
async function loadTable() {
    try {
        const response = await fetch(API_URL);

        if (response.ok) {
            const employees = await response.json();
            const tableBody = document.querySelector("#crud-table tbody");
            tableBody.innerHTML = ""; // Limpia la tabla antes de cargar

            employees.forEach((employee) => {
                const employeeId = employee.id || "Sin ID";

                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${employeeId}</td>
                    <td>${employee.name}</td>
                    <td>${employee.position}</td>
                    <td>${employee.phone}</td>
                    <td>${employee.email}</td>
                    <td>
                        <button onclick="editEmployee(${employeeId});">Editar</button>
                        <button onclick="deleteEmployee(${employeeId});">Eliminar</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        } else {
            throw new Error("No se pudo obtener la lista de empleados");
        }
    } catch (error) {
        console.error("Error al cargar la tabla de empleados:", error);
        alert("Ocurrió un error al cargar la lista de empleados: " + error.message);
    }
}

// Función: Editar un empleado (cargar datos en el modal)
async function editEmployee(id) {
    if (!id) {
        alert("ID inválido para editar el empleado.");
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${id}`);

        if (response.ok) {
            const employee = await response.json();

            document.getElementById("edit-employee-id").value = employee.id;
            document.getElementById("edit-nombre").value = employee.name;
            document.getElementById("edit-cargo").value = employee.position;
            document.getElementById("edit-telefono").value = employee.phone;
            document.getElementById("edit-email").value = employee.email;
            document.getElementById("edit-clave").value = employee.password;

            openModal(); // Muestra el modal
        } else {
            throw new Error("No se pudo obtener el empleado.");
        }
    } catch (error) {
        console.error("Error al editar empleado:", error);
        alert("Ocurrió un error al cargar los datos del empleado: " + error.message);
    }
}

// Función: Actualizar un empleado desde el modal con confirmación
async function saveEmployee() {
    const id = document.getElementById("edit-employee-id").value;
    const name = document.getElementById("edit-nombre").value.trim();
    const position = document.getElementById("edit-cargo").value.trim();
    const phone = document.getElementById("edit-telefono").value.trim();
    const email = document.getElementById("edit-email").value.trim();
    const password = document.getElementById("edit-clave").value.trim();

    if (!name || !position || !phone || !email || !password) {
        alert("Todos los campos son obligatorios.");
        return;
    }

    const confirmation = confirm("¿Estás seguro de que deseas confirmar los cambios realizados?");
    if (!confirmation) {
        alert("Actualización cancelada.");
        return;
    }

    const employeeData = {
        id,
        name,
        position,
        phone,
        email,
        password
    };

    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(employeeData)
        });

        if (response.ok) {
            closeModal(); // Cierra el modal
            loadTable(); // Recarga la tabla
            alert("Empleado actualizado exitosamente.");
        } else {
            throw new Error(`Error al actualizar el empleado: ${response.statusText}`);
        }
    } catch (error) {
        console.error("Error al actualizar empleado:", error);
        alert("Ocurrió un error al actualizar el empleado: " + error.message);
    }
}

// Función: Eliminar un empleado
async function deleteEmployee(id) {
    if (!id) {
        alert("ID inválido para eliminar el empleado.");
        return;
    }

    const confirmation = confirm("¿Estás seguro de que deseas eliminar este empleado?");
    if (!confirmation) {
        alert("Eliminación cancelada.");
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });

        if (response.ok) {
            loadTable();
            alert("Empleado eliminado exitosamente.");
        } else {
            throw new Error(`Error al eliminar el empleado: ${response.statusText}`);
        }
    } catch (error) {
        console.error("Error al eliminar empleado:", error);
        alert("Ocurrió un error al eliminar el empleado: " + error.message);
    }
}

// Función: Filtrar empleados en la tabla
function filterTable() {
    const query = document.getElementById("search-bar").value.toLowerCase();
    const rows = document.querySelectorAll("#crud-table tbody tr");

    rows.forEach((row) => {
        const name = row.children[1].textContent.toLowerCase();
        const position = row.children[2].textContent.toLowerCase();
        const phone = row.children[3].textContent.toLowerCase();
        const email = row.children[4].textContent.toLowerCase();

        row.style.display =
            name.includes(query) ||
            position.includes(query) ||
            phone.includes(query) ||
            email.includes(query)
                ? ""
                : "none";
    });
}

// Inicializar tabla al cargar la página
window.onload = loadTable;