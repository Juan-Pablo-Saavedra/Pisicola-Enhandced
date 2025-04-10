const API_URL = "http://172.30.7.229:8085/api/v1/customer";

// Registrar cliente
async function registerCustomer() {
    try {
        const name = document.getElementById("nombre").value.trim();
        const email = document.getElementById("email").value.trim();
        const phone = document.getElementById("telefono").value.trim();

        if (!name || !email || !phone) {
            alert("Todos los campos son obligatorios.");
            return;
        }

        const bodyContent = JSON.stringify({ id: 0, name, email, phone });

        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: bodyContent
        });

        if (response.ok) {
            const message = await response.text();
            alert(message); // Mensaje de éxito
            loadTable(); // Recargar tabla
        } else {
            alert("Error al registrar el cliente.");
        }
    } catch (error) {
        console.error("Error:", error);
        alert("Ocurrió un error al registrar el cliente.");
    }
}

// Cargar clientes en la tabla
async function loadTable() {
    try {
        const response = await fetch(API_URL);

        if (response.ok) {
            const customers = await response.json();
            const tableBody = document.querySelector("#crud-table tbody");
            tableBody.innerHTML = ""; // Limpiar la tabla

            customers.forEach((customer) => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${customer.id}</td>
                    <td>${customer.name}</td>
                    <td>${customer.email}</td>
                    <td>${customer.phone}</td>
                    <td>
                        <button onclick="editCustomer(${customer.id});">Editar</button>
                        <button onclick="deleteCustomer(${customer.id});">Eliminar</button>
                    </td>
                `;
                tableBody.appendChild(row);
            });
        } else {
            throw new Error("No se pudieron obtener los clientes.");
        }
    } catch (error) {
        console.error("Error:", error);
        alert("Ocurrió un error al cargar los clientes.");
    }
}

// Inicializar la tabla al cargar la página
window.onload = loadTable;