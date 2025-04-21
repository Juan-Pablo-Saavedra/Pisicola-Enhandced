const API_URL = "http://localhost:8085/api/v1/food";
const SUPPLIER_API_URL = "http://localhost:8085/api/v1/supplier";

// Cargar alimentos al iniciar la página
document.addEventListener("DOMContentLoaded", function () {
    loadFoodTable();
    loadSupplierOptions();
    setupEnterKeyEvents();
});

// Función para configurar eventos de tecla Enter
function setupEnterKeyEvents() {
    // Para el formulario de registro
    document.getElementById("food-type").addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            document.getElementById("food-brand").focus();
        }
    });
    
    document.getElementById("food-brand").addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            document.getElementById("food-supplier").focus();
        }
    });

    // Para el formulario de filtros
    document.getElementById("filter-type").addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            document.getElementById("filter-brand").focus();
        }
    });
    
    document.getElementById("filter-brand").addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            applyFoodFilters();
        }
    });
}

// Función para registrar un alimento con validaciones estrictas
async function registerFood() {
    try {
        const type = document.getElementById("food-type").value.trim();
        const brand = document.getElementById("food-brand").value.trim();
        const supplierId = document.getElementById("food-supplier").value;

        if (!type || !brand || !supplierId) {
            alert("Todos los campos son obligatorios.");
            return;
        }

        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id: 0, type, brand, supplier: { id: supplierId } })
        });

        const message = await response.text();
        alert(message);

        if (response.ok) {
            resetFoodForm();
            loadFoodTable();
        }
    } catch (error) {
        console.error("Error al registrar alimento:", error);
        alert("Ocurrió un error al registrar el alimento.");
    }
}

// Función para limpiar los campos del formulario
function resetFoodForm() {
    document.getElementById("food-type").value = "";
    document.getElementById("food-brand").value = "";
    document.getElementById("food-supplier").selectedIndex = 0;
}

// Función para limpiar la tabla
function clearTable() {
    const tableBody = document.querySelector("#crud-table tbody");
    tableBody.innerHTML = "<tr><td colspan='4'>No hay alimentos disponibles.</td></tr>";
}

// Función para cargar la tabla de alimentos desde la API
async function loadFoodTable() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) {
            throw new Error(`Error ${response.status}: No se pudo cargar alimentos`);
        }

        const foods = await response.json();
        renderFoodTable(foods);
    } catch (error) {
        console.error("Error al cargar alimentos:", error);
        clearTable();
    }
}

// Función para renderizar la tabla con datos de alimentos
function renderFoodTable(foods) {
    const tableBody = document.querySelector("#crud-table tbody");
    
    if (!foods || foods.length === 0) {
        tableBody.innerHTML = "<tr><td colspan='4'>No hay alimentos registrados.</td></tr>";
        return;
    }
    
    tableBody.innerHTML = "";

    foods.forEach(food => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${food.type}</td>
            <td>${food.brand}</td>
            <td>${food.supplier?.name || "Sin proveedor"}</td>
            <td>
                <button onclick="editFood(${food.id})">Editar</button>
                <button onclick="deleteFood(${food.id})">Eliminar</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

// Función para cargar proveedores en el selector
async function loadSupplierOptions() {
    try {
        const response = await fetch(SUPPLIER_API_URL);
        if (!response.ok) throw new Error("Error al cargar proveedores.");

        const suppliers = await response.json();
        const supplierSelectAdd = document.getElementById("food-supplier");
        const supplierSelectEdit = document.getElementById("edit-supplier");

        supplierSelectAdd.innerHTML = '<option value="">Selecciona un proveedor</option>';
        supplierSelectEdit.innerHTML = '<option value="">Selecciona un proveedor</option>';

        suppliers.forEach(supplier => {
            const optionAdd = document.createElement("option");
            const optionEdit = document.createElement("option");

            optionAdd.value = supplier.id;
            optionAdd.textContent = supplier.name;
            supplierSelectAdd.appendChild(optionAdd);

            optionEdit.value = supplier.id;
            optionEdit.textContent = supplier.name;
            supplierSelectEdit.appendChild(optionEdit);
        });
    } catch (error) {
        console.error("Error al cargar proveedores:", error);
        // Añadir una opción por defecto en caso de error
        const supplierSelectAdd = document.getElementById("food-supplier");
        const supplierSelectEdit = document.getElementById("edit-supplier");
        
        supplierSelectAdd.innerHTML = '<option value="">Error al cargar proveedores</option>';
        supplierSelectEdit.innerHTML = '<option value="">Error al cargar proveedores</option>';
    }
}

// Función para editar un alimento
async function editFood(id) {
    try {
        await loadSupplierOptions(); // Cargar proveedores antes de abrir el modal

        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) throw new Error("Error al obtener alimento.");

        const food = await response.json();
        document.getElementById("edit-food-id").value = food.id;
        document.getElementById("edit-type").value = food.type;
        document.getElementById("edit-brand").value = food.brand;
        document.getElementById("edit-supplier").value = food.supplier?.id || "";

        openModal();
    } catch (error) {
        console.error("Error al cargar datos del alimento:", error);
        alert("Ocurrió un error al cargar los datos del alimento.");
    }
}

// Función para guardar cambios del alimento editado
async function saveFood() {
    const id = document.getElementById("edit-food-id").value;
    const type = document.getElementById("edit-type").value.trim();
    const brand = document.getElementById("edit-brand").value.trim();
    const supplierId = document.getElementById("edit-supplier").value;

    if (!type || !brand || !supplierId) {
        alert("Todos los campos son obligatorios.");
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id, type, brand, supplier: { id: supplierId } })
        });

        const message = await response.text();
        alert(message);
        closeModal();
        loadFoodTable();
    } catch (error) {
        console.error("Error al actualizar alimento:", error);
        alert("Ocurrió un error al actualizar el alimento.");
    }
}

// Función para eliminar un alimento
async function deleteFood(id) {
    if (!confirm("¿Estás seguro de que deseas eliminar este alimento?")) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
        const result = await response.text();

        alert(result);
        loadFoodTable();
    } catch (error) {
        console.error("Error al eliminar alimento:", error);
        alert("Ocurrió un error al eliminar el alimento.");
    }
}

// Función para aplicar filtros correctamente
async function applyFoodFilters() {
    try {
        const type = document.getElementById("filter-type").value.trim();
        const brand = document.getElementById("filter-brand").value.trim();

        let queryString = [];
        if (type) queryString.push(`type=${encodeURIComponent(type)}`);
        if (brand) queryString.push(`brand=${encodeURIComponent(brand)}`);

        const url = queryString.length > 0 ? 
            `${API_URL}/filter?${queryString.join("&")}` : 
            API_URL;

        const response = await fetch(url);
        if (!response.ok) throw new Error("Error al filtrar alimentos.");

        const foods = await response.json();
        renderFoodTable(foods);
    } catch (error) {
        console.error("Error al filtrar alimentos:", error);
        clearTable();
    }
}

// Función para abrir y cerrar el modal de edición
function openModal() {
    document.getElementById("edit-modal").style.display = "flex";
}

function closeModal() {
    document.getElementById("edit-modal").style.display = "none";
}

// Función para manejar errores de conexión al servidor
window.addEventListener('unhandledrejection', function(event) {
    console.error('Error no manejado:', event.reason);
    if (event.reason.message && event.reason.message.includes('500')) {
        alert('Error de conexión con el servidor. Asegúrate de que el servidor esté en ejecución en el puerto 8085.');
    }
});