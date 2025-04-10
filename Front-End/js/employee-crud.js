const API_URL = "http://localhost:8085/api/v1/employee";
const API_URL_ROLES = `${API_URL}/roles`;

// Función: Cargar roles desde el backend
async function loadRoles() {
    try {
        const response = await fetch(API_URL_ROLES);

        if (response.ok) {
            const roles = await response.json();
            const positionSelect = document.getElementById("employee-position");

            // Limpiar el <select> antes de agregar los roles
            positionSelect.innerHTML = '<option value="" disabled selected>Selecciona un rol</option>';

            // Añadir los roles como opciones del <select>
            roles.forEach((role) => {
                const option = document.createElement("option");
                option.value = role;
                option.textContent = role;
                positionSelect.appendChild(option);
            });
        } else {
            const errorText = await response.text();
            alert(`Error al cargar roles: ${errorText}`);
        }
    } catch (error) {
        console.error("Error al cargar roles:", error);
        alert("Ocurrió un error al cargar los roles: " + error.message);
    }
}

// Función: Registrar un empleado
async function registerEmployee() {
    try {
        const name = document.getElementById("employee-name").value.trim();
        const position = document.getElementById("employee-position").value; // Valor del <select>
        const phone = document.getElementById("employee-phone").value.trim();
        const password = document.getElementById("employee-password").value.trim();
        const email = document.getElementById("employee-email").value.trim();

        // Validaciones
        if (!name || !position || !phone || !password || !email) {
            alert("Todos los campos son obligatorios.");
            return;
        }

        if (password.length < 8) {
            alert("La contraseña debe tener al menos 8 caracteres.");
            return;
        }

        if (!email.includes("@")) {
            alert("El correo electrónico debe incluir '@'.");
            return;
        }

        const bodyContent = JSON.stringify({
            id: 0,
            name,
            position,
            phone,
            password,
            email
        });

        const response = await fetch(API_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: bodyContent
        });

        if (response.ok) {
            const message = await response.text();
            alert(message);
            loadTable(); // Recargar la tabla
        } else {
            const errorText = await response.text();
            alert(`Error al registrar empleado: ${errorText}`);
        }
    } catch (error) {
        console.error("Error al registrar empleado:", error);
        alert("Ocurrió un error durante el registro: " + error.message);
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
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${employee.name}</td>
                    <td>${employee.position}</td>
                    <td>${employee.phone}</td>
                    <td>${employee.email}</td>
                    <td>
                        <button onclick="editEmployee(${employee.id})">Editar</button>
                        <button onclick="deleteEmployee(${employee.id})">Eliminar</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        } else {
            const errorText = await response.text();
            alert(`Error al cargar empleados: ${errorText}`);
        }
    } catch (error) {
        console.error("Error al cargar empleados:", error);
        alert("Ocurrió un error al cargar la lista de empleados: " + error.message);
    }
}

function applyFilters() {
    const filterName = document.getElementById("filter-name").value.toLowerCase();
    const filterPhone = document.getElementById("filter-phone").value.toLowerCase();
    const filterPosition = document.getElementById("filter-position").value.toLowerCase();
    const filterEmail = document.getElementById("filter-email").value.toLowerCase();

    const rows = document.querySelectorAll("#crud-table tbody tr");

    rows.forEach((row) => {
        const name = row.children[0].textContent.toLowerCase();
        const position = row.children[1].textContent.toLowerCase();
        const phone = row.children[2].textContent.toLowerCase();
        const email = row.children[3].textContent.toLowerCase();

        const matches =
            name.includes(filterName) &&
            phone.includes(filterPhone) &&
            position.includes(filterPosition) &&
            email.includes(filterEmail);

        row.style.display = matches ? "" : "none";
    });
}


// Función: Editar un empleado
async function editEmployee(id) {
    if (!id) {
        alert("ID inválido para editar el empleado.");
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${id}`);

        if (response.ok) {
            const employee = await response.json();

            document.getElementById("edit-nombre").value = employee.name;
            document.getElementById("edit-cargo").value = employee.position;
            document.getElementById("edit-telefono").value = employee.phone;
            document.getElementById("edit-email").value = employee.email;
            document.getElementById("edit-clave").value = employee.password;

            openModal(); // Mostrar el modal con datos cargados
        } else {
            const errorText = await response.text();
            alert(`Error al obtener empleado: ${errorText}`);
        }
    } catch (error) {
        console.error("Error al cargar datos del empleado:", error);
        alert("Ocurrió un error al cargar los datos del empleado: " + error.message);
    }
}

// Función: Guardar cambios del empleado editado
async function saveEmployee() {
    const id = document.getElementById("edit-employee-id").value;

    const name = document.getElementById("edit-nombre").value.trim();
    const position = document.getElementById("edit-cargo").value.trim();
    const phone = document.getElementById("edit-telefono").value.trim();
    const email = document.getElementById("edit-email").value.trim();
    const password = document.getElementById("edit-clave").value.trim();

    // Validaciones
    if (!name || !position || !phone || !email || !password) {
        alert("Todos los campos son obligatorios.");
        return;
    }

    if (password.length < 8) {
        alert("La contraseña debe tener al menos 8 caracteres.");
        return;
    }

    if (!email.includes("@")) {
        alert("El correo electrónico debe incluir '@'.");
        return;
    }

    const employeeData = { id, name, position, phone, email, password };

    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(employeeData)
        });

        if (response.ok) {
            const message = await response.text();
            alert(message);
            closeModal(); // Cierra el modal
            loadTable(); // Recargar la tabla
        } else {
            const errorText = await response.text();
            alert(`Error al actualizar empleado: ${errorText}`);
        }
    } catch (error) {
        console.error("Error al actualizar empleado:", error);
        alert("Ocurrió un error al actualizar el empleado: " + error.message);
    }
}

// Función: Eliminar empleado
async function deleteEmployee(id) {
    const confirmation = confirm("¿Estás seguro de que deseas eliminar este empleado?");
    if (!confirmation) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });

        if (response.ok) {
            const message = await response.text();
            alert(message);
            loadTable(); // Recargar la tabla
        } else {
            const errorText = await response.text();
            alert(`Error al eliminar empleado: ${errorText}`);
        }
    } catch (error) {
        console.error("Error al eliminar empleado:", error);
        alert("Ocurrió un error al eliminar el empleado: " + error.message);
    }
}

// Mostrar modal
function openModal() {
    const modal = document.getElementById("edit-modal");
    modal.style.display = "flex";
}

// Cerrar modal
function closeModal() {
    const modal = document.getElementById("edit-modal");
    modal.style.display = "none";
    document.getElementById("edit-form").reset(); // Limpia el formulario del modal
}

// Inicializar roles y tabla al cargar la página
window.onload = function() {
    loadRoles();
    loadTable();
};
