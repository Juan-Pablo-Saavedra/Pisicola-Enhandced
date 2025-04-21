const API_URL = "http://localhost:8085/api/v1/batch";

// Cargar lotes al iniciar la página
window.onload = function () {
    loadBatchTable();
    loadDropdowns();
};

// Función para cargar los dropdowns de Fish, Tank y Food
async function loadDropdowns() {
    await loadOptions("fish", "batch-fish");
    await loadOptions("tank", "batch-tank");
    await loadOptions("food", "batch-food");

    await loadOptions("fish", "edit-batch-fish");
    await loadOptions("tank", "edit-batch-tank");
    await loadOptions("food", "edit-batch-food");
}

async function loadOptions(entity, selectId) {
    try {
        const response = await fetch(`http://localhost:8085/api/v1/${entity}`);
        if (!response.ok) throw new Error(`Error al cargar ${entity}`);

        const data = await response.json();

        if (!Array.isArray(data) || data.length === 0) {
            console.warn(`No hay registros en la entidad ${entity}.`);
            return;
        }

        const select = document.getElementById(selectId);
        select.innerHTML = "<option value='' disabled selected>Seleccione una opción</option>"; // Opción inicial

        data.forEach(item => {
            const option = document.createElement("option");
            option.value = item.id;
            option.textContent = item.species || item.location || item.type; // Ajusta según el atributo correcto
            select.appendChild(option);
        });

    } catch (error) {
        console.error(`Error al cargar ${entity}:`, error);
    }
}


// Función para registrar un lote
async function registerBatch() {
    try {
        const quantity = document.getElementById("batch-quantity").value.trim();
        const fishId = document.getElementById("batch-fish").value;
        const tankId = document.getElementById("batch-tank").value;
        const foodId = document.getElementById("batch-food").value;

        if (!quantity || quantity <= 0) {
            alert("La cantidad debe ser mayor a 0.");
            return;
        }

        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id: 0, quantity, fishId, tankId, foodId })
        });

        const message = await response.text();
        alert(message);

        if (response.ok) {
            resetBatchForm();
            loadBatchTable();
        }
    } catch (error) {
        console.error("Error al registrar lote:", error);
        alert("Ocurrió un error al registrar el lote.");
    }
}

// Función para limpiar los campos del formulario
function resetBatchForm() {
    document.getElementById("batch-quantity").value = "";
    document.getElementById("batch-fish").selectedIndex = 0;
    document.getElementById("batch-tank").selectedIndex = 0;
    document.getElementById("batch-food").selectedIndex = 0;
}

// Función para cargar la tabla de lotes
async function loadBatchTable() {
    try {
        const response = await fetch(API_URL);
        if (!response.ok) throw new Error("Error al cargar lotes.");

        const batches = await response.json();
        const tableBody = document.querySelector("#crud-table tbody");
        tableBody.innerHTML = "";

        batches.forEach(batch => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${batch.quantity}</td>
                <td>${batch.fishId}</td>
                <td>${batch.tankId}</td>
                <td>${batch.foodId}</td>
                <td>
                    <button onclick="editBatch(${batch.id})">Editar</button>
                    <button onclick="deleteBatch(${batch.id})">Eliminar</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error("Error al cargar lotes:", error);
    }
}

// Función para editar un lote
async function editBatch(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        if (!response.ok) throw new Error("Error al obtener lote.");

        const batch = await response.json();
        document.getElementById("edit-batch-id").value = batch.id;
        document.getElementById("edit-batch-quantity").value = batch.quantity;
        document.getElementById("edit-batch-fish").value = batch.fishId;
        document.getElementById("edit-batch-tank").value = batch.tankId;
        document.getElementById("edit-batch-food").value = batch.foodId;

        openModal();
    } catch (error) {
        console.error("Error al cargar datos del lote:", error);
        alert("Ocurrió un error al cargar los datos del lote.");
    }
}

// Función para guardar cambios del lote editado
async function saveBatch() {
    const id = document.getElementById("edit-batch-id").value;
    const quantity = document.getElementById("edit-batch-quantity").value.trim();
    const fishId = document.getElementById("edit-batch-fish").value;
    const tankId = document.getElementById("edit-batch-tank").value;
    const foodId = document.getElementById("edit-batch-food").value;

    if (!quantity || quantity <= 0) {
        alert("La cantidad debe ser mayor a 0.");
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ id, quantity, fishId, tankId, foodId })
        });

        const message = await response.text();
        alert(message);
        closeModal();
        loadBatchTable();
    } catch (error) {
        console.error("Error al actualizar lote:", error);
        alert("Ocurrió un error al actualizar el lote.");
    }
}

// Función para eliminar un lote
async function deleteBatch(id) {
    if (!confirm("¿Estás seguro de que deseas eliminar este lote?")) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
        const message = await response.text();
        alert(message);
        loadBatchTable();
    } catch (error) {
        console.error("Error al eliminar lote:", error);
        alert("Ocurrió un error al eliminar el lote.");
    }
}

// Función para aplicar filtros
async function applyBatchFilters() {
    try {
        const quantity = document.getElementById("filter-quantity").value.trim();
        if (!quantity) return;

        const response = await fetch(`${API_URL}/filter?quantity=${quantity}`);
        if (!response.ok) throw new Error("Error al filtrar lotes.");

        const batches = await response.json();
        const tableBody = document.querySelector("#crud-table tbody");
        tableBody.innerHTML = batches.length > 0 
            ? "" 
            : "<tr><td colspan='5'>No hay lotes que coincidan con el filtro.</td></tr>";

        batches.forEach(batch => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${batch.quantity}</td>
                <td>${batch.fishId}</td>
                <td>${batch.tankId}</td>
                <td>${batch.foodId}</td>
                <td>
                    <button onclick="editBatch(${batch.id})">Editar</button>
                    <button onclick="deleteBatch(${batch.id})">Eliminar</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error("Error al filtrar lotes:", error);
    }
}

function resetBatchFilters() {
    document.getElementById("filter-quantity").value = "";
    loadBatchTable();
}

function openModal() {
    document.getElementById("edit-modal").style.display = "flex";
}

function closeModal() {
    document.getElementById("edit-modal").style.display = "none";
}
