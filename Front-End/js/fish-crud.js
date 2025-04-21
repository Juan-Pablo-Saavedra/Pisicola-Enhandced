const API_URL = "http://localhost:8085/api/v1/fish";

// Cargar peces al iniciar la página
document.addEventListener("DOMContentLoaded", function () {
    loadFishTable();

    const speciesInput = document.getElementById("filter-species");
    const weightInput = document.getElementById("filter-weight");

    speciesInput.addEventListener("input", handleFilterChange);
    weightInput.addEventListener("input", handleFilterChange);
});

// Función para gestionar cambios en los filtros
async function handleFilterChange() {
    const species = document.getElementById("filter-species").value.trim();
    const weight = document.getElementById("filter-weight").value.trim();

    if (!species && !weight) {
        loadFishTable(); // Recarga todos los peces si los filtros están vacíos
        return;
    }

    applyFishFilters(); // Aplica filtros si hay valores ingresados
}

// Función para registrar un pez con validaciones estrictas
async function registerFish() {
    try {
        const species = document.getElementById("fish-species").value.trim();
        const size = document.getElementById("fish-size").value.trim();
        const weight = document.getElementById("fish-weight").value.trim();

        if (!species || !size || weight <= 0) {
            alert("Todos los campos son obligatorios y el peso debe ser mayor a 0.");
            return;
        }

        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id: 0, species, size, weight })
        });

        const message = await response.text();
        alert(message);

        if (response.ok) {
            resetFishForm();
            loadFishTable();
        }
    } catch (error) {
        console.error("Error al registrar pez:", error);
        alert("Ocurrió un error al registrar el pez.");
    }
}

// Función para limpiar los campos del formulario
function resetFishForm() {
    document.getElementById("fish-species").value = "";
    document.getElementById("fish-size").value = "";
    document.getElementById("fish-weight").value = "";
}

// Función para cargar la tabla de peces desde la API
async function loadFishTable() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error("Error al cargar peces.");

        const fishes = await response.json();
        renderFishTable(fishes);
    } catch (error) {
        console.error("Error al cargar peces:", error);
        clearTable();
    }
}

// Función para renderizar la tabla con datos de peces
function renderFishTable(fishes) {
    const tableBody = document.querySelector("#crud-table tbody");
    tableBody.innerHTML = fishes.length > 0
        ? ""
        : "<tr><td colspan='4'>No hay peces registrados.</td></tr>";

    fishes.forEach(fish => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${fish.species}</td>
            <td>${fish.size}</td>
            <td>${fish.weight}</td>
            <td>
                <button onclick="editFish(${fish.id})">Editar</button>
                <button onclick="deleteFish(${fish.id})">Eliminar</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

// Función para editar un pez
async function editFish(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) throw new Error("Error al obtener pez.");

        const fish = await response.json();
        document.getElementById("edit-fish-id").value = fish.id;
        document.getElementById("edit-species").value = fish.species;
        document.getElementById("edit-size").value = fish.size;
        document.getElementById("edit-weight").value = fish.weight;

        openModal();
    } catch (error) {
        console.error("Error al cargar datos del pez:", error);
        alert("Ocurrió un error al cargar los datos del pez.");
    }
}

// Función para guardar cambios del pez editado
async function saveFish() {
    const id = document.getElementById("edit-fish-id").value;
    const species = document.getElementById("edit-species").value.trim();
    const size = document.getElementById("edit-size").value.trim();
    const weight = document.getElementById("edit-weight").value.trim();

    if (!species || !size || weight <= 0) {
        alert("Todos los campos son obligatorios y el peso debe ser mayor a 0.");
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id, species, size, weight })
        });

        const message = await response.text();
        alert(message);
        closeModal();
        loadFishTable();
    } catch (error) {
        console.error("Error al actualizar pez:", error);
        alert("Ocurrió un error al actualizar el pez.");
    }
}

// Función para eliminar un pez
async function deleteFish(id) {
    if (!confirm("¿Estás seguro de que deseas eliminar este pez?")) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
        const message = await response.text();
        alert(message);
        loadFishTable();
    } catch (error) {
        console.error("Error al eliminar pez:", error);
        alert("Ocurrió un error al eliminar el pez.");
    }
}

// Función para aplicar filtros de manera automática
async function applyFishFilters() {
    try {
        const species = document.getElementById("filter-species").value.trim();
        const weight = document.getElementById("filter-weight").value.trim();

        let queryParams = [];
        if (species) queryParams.push(`species=${encodeURIComponent(species)}`);
        if (weight) queryParams.push(`weight=${weight}`);

        const queryString = queryParams.length > 0 ? `?${queryParams.join("&")}` : "";

        const response = await fetch(`${API_URL}/filter${queryString}`);
        if (!response.ok) throw new Error("Error al filtrar peces.");

        const fishes = await response.json();
        renderFishTable(fishes);
    } catch (error) {
        console.error("Error al filtrar peces:", error);
        clearTable(); // Si hay error, vaciar la tabla
    }
}

// Función para vaciar la tabla cuando no hay coincidencias
function clearTable() {
    const tableBody = document.querySelector("#crud-table tbody");
    tableBody.innerHTML = "<tr><td colspan='4'>No hay peces que coincidan con el filtro.</td></tr>";
}

// Función para abrir y cerrar el modal de edición
function openModal() {
    document.getElementById("edit-modal").style.display = "flex";
}

function closeModal() {
    document.getElementById("edit-modal").style.display = "none";
}
