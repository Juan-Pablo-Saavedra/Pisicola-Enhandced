const API_URL = "http://localhost:8085/api/v1/customer";

// Función: Registrar un cliente con validaciones
async function registerCustomer() {
    try {
        const name = document.getElementById("customer-name").value.trim();
        const phone = document.getElementById("customer-phone").value.trim();
        const email = document.getElementById("customer-email").value.trim();

        if (!name || !phone || !email) {
            alert("Todos los campos son obligatorios.");
            return;
        }

        // Validación de teléfono (solo números y '+')
        const phoneRegex = /^\+?[0-9]+$/;
        if (!phoneRegex.test(phone)) {
            alert("El teléfono solo puede contener números y el signo '+'.");
            return;
        }

        // Validación de correo (debe incluir @ y un dominio)
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!emailRegex.test(email)) {
            alert("El correo electrónico debe tener un dominio válido (Ejemplo: usuario@dominio.com).");
            return;
        }

        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id: 0, name, phone, email })
        });

        const message = await response.text();
        alert(message);

        if (response.ok) {
            resetForm();
            loadTable();
        }
    } catch (error) {
        console.error("Error al registrar cliente:", error);
        alert("Ocurrió un error al registrar el cliente.");
    }
}

// Función: Limpiar campos después de registrar un cliente
function resetForm() {
    document.getElementById("customer-name").value = "";
    document.getElementById("customer-phone").value = "";
    document.getElementById("customer-email").value = "";
}

// Función: Cargar clientes en la tabla
async function loadTable() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error("Error al cargar clientes.");

        const customers = await response.json();
        const tableBody = document.querySelector("#crud-table tbody");
        tableBody.innerHTML = "";

        customers.forEach((customer) => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${customer.name}</td>
                <td>${customer.phone}</td>
                <td>${customer.email}</td>
                <td>
                    <button onclick="editCustomer(${customer.id})">Editar</button>
                    <button onclick="deleteCustomer(${customer.id})">Eliminar</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error("Error al cargar clientes:", error);
    }
}

// Función: Editar cliente
async function editCustomer(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) throw new Error("Error al obtener cliente.");

        const customer = await response.json();
        document.getElementById("edit-customer-id").value = customer.id;
        document.getElementById("edit-nombre").value = customer.name;
        document.getElementById("edit-telefono").value = customer.phone;
        document.getElementById("edit-email").value = customer.email;

        openModal();
    } catch (error) {
        console.error("Error al cargar datos del cliente:", error);
        alert("Ocurrió un error al cargar los datos del cliente.");
    }
}

// Función: Guardar cambios del cliente editado
async function saveCustomer() {
    const id = document.getElementById("edit-customer-id").value;
    const name = document.getElementById("edit-nombre").value.trim();
    const phone = document.getElementById("edit-telefono").value.trim();
    const email = document.getElementById("edit-email").value.trim();

    if (!name || !phone || !email) {
        alert("Todos los campos son obligatorios.");
        return;
    }

    // Validaciones previas a la actualización
    const phoneRegex = /^\+?[0-9]+$/;
    if (!phoneRegex.test(phone)) {
        alert("El teléfono solo puede contener números y el signo '+'.");
        return;
    }

    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailRegex.test(email)) {
        alert("El correo electrónico debe tener un dominio válido.");
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id, name, phone, email })
        });

        const message = await response.text();
        alert(message);
        closeModal();
        loadTable();
    } catch (error) {
        console.error("Error al actualizar cliente:", error);
        alert("Ocurrió un error al actualizar el cliente.");
    }
}

// Función: Eliminar cliente
async function deleteCustomer(id) {
    if (!confirm("¿Estás seguro de que deseas eliminar este cliente?")) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
        const message = await response.text();
        alert(message);
        loadTable();
    } catch (error) {
        console.error("Error al eliminar cliente:", error);
        alert("Ocurrió un error al eliminar el cliente.");
    }
}

async function applyFilters() {
    try {
        const name = document.getElementById("filter-name").value.trim();
        const phone = document.getElementById("filter-phone").value.trim();
        const email = document.getElementById("filter-email").value.trim();

        const params = new URLSearchParams();
        if (name) params.append("name", name);
        if (phone) params.append("phone", phone);
        if (email) params.append("email", email);

        const response = await fetch(`${API_URL}/filter?${params.toString()}`);

        if (!response.ok) {
            throw new Error(`Error al filtrar clientes: ${response.statusText}`);
        }

        const textResponse = await response.text();
        if (!textResponse) {
            document.querySelector("#crud-table tbody").innerHTML = ""; // Limpia la tabla si no hay coincidencias
            return;
        }

        const customers = JSON.parse(textResponse);
        
        // Si la respuesta contiene clientes, los muestra; si no, limpia la tabla
        const tableBody = document.querySelector("#crud-table tbody");
        tableBody.innerHTML = customers.length > 0 ? "" : "<tr><td colspan='4'>No hay clientes que coincidan con los filtros.</td></tr>";

        customers.forEach((customer) => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${customer.name}</td>
                <td>${customer.phone}</td>
                <td>${customer.email}</td>
                <td>
                    <button onclick="editCustomer(${customer.id})">Editar</button>
                    <button onclick="deleteCustomer(${customer.id})">Eliminar</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error("Error al filtrar clientes:", error);
    }
}


// Función: Limpiar filtros
function resetFilters() {
    document.getElementById("filter-name").value = "";
    document.getElementById("filter-phone").value = "";
    document.getElementById("filter-email").value = "";
    loadTable();
}

// Mostrar modal de edición
function openModal() {
    document.getElementById("edit-modal").style.display = "flex";
}

// Cerrar modal de edición
function closeModal() {
    document.getElementById("edit-modal").style.display = "none";
}

// Cargar clientes al abrir la página
window.onload = function () {
    loadTable();
};
