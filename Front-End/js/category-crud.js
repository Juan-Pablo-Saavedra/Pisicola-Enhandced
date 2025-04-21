const API_URL = "http://localhost:8085/api/v1/category";

// Cargar categorías al iniciar la página
document.addEventListener("DOMContentLoaded", function () {
    loadCategoryTable();
    setupEnterKeyEvents();
});

// Función para registrar una categoría con validaciones estrictas
async function registerCategory() {
    try {
        const name = document.getElementById("category-name").value.trim();

        if (!name) {
            alert("El nombre de la categoría es obligatorio.");
            return;
        }

        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id: 0, name })
        });

        const message = await response.text();
        alert(message);

        if (response.ok) {
            resetCategoryForm();
            loadCategoryTable();
        }
    } catch (error) {
        console.error("Error al registrar categoría:", error);
        alert("Ocurrió un error al registrar la categoría.");
    }
}

// Función para limpiar los campos del formulario
function resetCategoryForm() {
    document.getElementById("category-name").value = "";
}

// Función para cargar la tabla de categorías desde la API
async function loadCategoryTable() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error("Error al cargar categorías.");

        const categories = await response.json();
        renderCategoryTable(categories);
    } catch (error) {
        console.error("Error al cargar categorías:", error);
        clearTable();
    }
}

// Función para renderizar la tabla con datos de categorías (sin la columna ID)
function renderCategoryTable(categories) {
    const tableBody = document.querySelector("#crud-table tbody");
    tableBody.innerHTML = categories.length > 0
        ? ""
        : "<tr><td colspan='2'>No hay categorías registradas.</td></tr>";

    categories.forEach(category => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${category.name}</td>
            <td>
                <button onclick="editCategory(${category.id})">Editar</button>
                <button onclick="deleteCategory(${category.id})">Eliminar</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

// Función para editar una categoría
async function editCategory(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) throw new Error("Error al obtener categoría.");

        const category = await response.json();
        document.getElementById("edit-category-id").value = category.id;
        document.getElementById("edit-name").value = category.name;

        openModal();
    } catch (error) {
        console.error("Error al cargar datos de la categoría:", error);
        alert("Ocurrió un error al cargar los datos de la categoría.");
    }
}

// Función para guardar cambios de la categoría editada
async function saveCategory() {
    const id = document.getElementById("edit-category-id").value;
    const name = document.getElementById("edit-name").value.trim();

    if (!name) {
        alert("El nombre de la categoría es obligatorio.");
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id, name })
        });

        const message = await response.text();
        alert(message);
        closeModal();
        loadCategoryTable();
    } catch (error) {
        console.error("Error al actualizar categoría:", error);
        alert("Ocurrió un error al actualizar la categoría.");
    }
}

// Función para eliminar una categoría
async function deleteCategory(id) {
    if (!confirm("¿Estás seguro de que deseas eliminar esta categoría?")) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
        const result = await response.text();

        alert(result);
        loadCategoryTable();
    } catch (error) {
        console.error("Error al eliminar categoría:", error);
        alert("Ocurrió un error al eliminar la categoría.");
    }
}

// Función para aplicar filtros de manera automática
async function applyCategoryFilters() {
    try {
        const name = document.getElementById("filter-name").value.trim();

        // Construir la consulta solo si hay un filtro activo
        let queryString = name ? `?name=${encodeURIComponent(name)}` : "";

        const response = await fetch(`${API_URL}/filter${queryString}`);
        if (!response.ok) throw new Error("Error al filtrar categorías.");

        const categories = await response.json();
        renderCategoryTable(categories);
    } catch (error) {
        console.error("Error al filtrar categorías:", error);
        clearTable();
    }
}

// Función para renderizar la tabla con datos de categorías (sin la columna ID)
function renderCategoryTable(categories) {
    const tableBody = document.querySelector("#crud-table tbody");
    tableBody.innerHTML = categories.length > 0
        ? ""
        : "<tr><td colspan='2'>No hay categorías que coincidan con el filtro.</td></tr>";

    categories.forEach(category => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${category.name}</td>
            <td>
                <button onclick="editCategory(${category.id})">Editar</button>
                <button onclick="deleteCategory(${category.id})">Eliminar</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

// Función para vaciar la tabla cuando no hay coincidencias
function clearTable() {
    const tableBody = document.querySelector("#crud-table tbody");
    tableBody.innerHTML = "<tr><td colspan='2'>No hay categorías que coincidan con el filtro.</td></tr>";
}


// Función para abrir y cerrar el modal de edición
function openModal() {
    document.getElementById("edit-modal").style.display = "flex";
}

function closeModal() {
    document.getElementById("edit-modal").style.display = "none";
}

// Función para activar eventos de tecla "Enter" en los formularios
function setupEnterKeyEvents() {
    document.getElementById("crud-form").addEventListener("keypress", function (event) {
        if (event.key === "Enter") {
            event.preventDefault();
            registerCategory();
        }
    });

    document.getElementById("edit-form").addEventListener("keypress", function (event) {
        if (event.key === "Enter") {
            event.preventDefault();
            saveCategory();
        }
    });
}
