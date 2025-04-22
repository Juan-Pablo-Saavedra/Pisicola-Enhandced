const API_URL = "http://localhost:8085/api/v1/batch";

// Variables globales para mapear id a nombre para cada entidad.
let fishMap = {};
let tankMap = {};
let foodMap = {};

// Cargar lotes al iniciar la página
window.onload = async function () {
    try {
        // Primero cargamos los dropdowns para asegurar que los mappings estén completos
        await loadDropdowns();
        // Luego cargamos la tabla de lotes
        await loadBatchTable();
        // Finalmente configuramos los eventos de filtrado
        setupFilterEvents();
    } catch (error) {
        console.error("Error al inicializar la página:", error);
    }
};

// Función para cargar los dropdowns de Fish, Tank y Food y actualizar los mappings
async function loadDropdowns() {
    try {
        // Cargamos primero los datos para el registro y edición
        await Promise.all([
            loadOptions("fish", "batch-fish"),
            loadOptions("tank", "batch-tank"),
            loadOptions("food", "batch-food"),
            loadOptions("fish", "edit-batch-fish"),
            loadOptions("tank", "edit-batch-tank"),
            loadOptions("food", "edit-batch-food")
        ]);
        
        // Luego cargamos los filtros
        await Promise.all([
            loadFilterOptions("fish", "filter-fish"),
            loadFilterOptions("tank", "filter-tank"),
            loadFilterOptions("food", "filter-food")
        ]);
    } catch (error) {
        console.error("Error al cargar dropdowns:", error);
    }
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
        if (!select) {
            console.warn(`El elemento con id ${selectId} no existe.`);
            return;
        }
        
        select.innerHTML = "<option value='' disabled selected>Seleccione una opción</option>"; // Opción inicial

        data.forEach(item => {
            const option = document.createElement("option");
            option.value = item.id;
            
            let displayName = "";
            if (entity === "fish") {
                displayName = item.species || `Pez ID:${item.id}`;
                fishMap[item.id] = displayName;
            } else if (entity === "tank") {
                displayName = item.name ? item.name : (item.location || `Tanque ID:${item.id}`);
                tankMap[item.id] = displayName;
            } else if (entity === "food") {
                displayName = item.name ? item.name : (item.type || `Alimento ID:${item.id}`);
                foodMap[item.id] = displayName;
            }
            
            option.textContent = displayName;
            select.appendChild(option);
        });

    } catch (error) {
        console.error(`Error al cargar ${entity}:`, error);
    }
}

// Función específica para cargar opciones en los filtros con una opción "Todos"
async function loadFilterOptions(entity, selectId) {
    try {
        const response = await fetch(`http://localhost:8085/api/v1/${entity}`);
        if (!response.ok) throw new Error(`Error al cargar ${entity}`);

        const data = await response.json();
        
        const select = document.getElementById(selectId);
        if (!select) {
            console.warn(`El elemento con id ${selectId} no existe.`);
            return;
        }
        
        select.innerHTML = "<option value=''>Todos</option>"; // Opción para mostrar todos

        if (Array.isArray(data) && data.length > 0) {
            data.forEach(item => {
                const option = document.createElement("option");
                option.value = item.id;
                
                let displayName = "";
                if (entity === "fish") {
                    displayName = item.species || `Pez ID:${item.id}`;
                } else if (entity === "tank") {
                    displayName = item.name ? item.name : (item.location || `Tanque ID:${item.id}`);
                } else if (entity === "food") {
                    displayName = item.name ? item.name : (item.type || `Alimento ID:${item.id}`);
                }
                
                option.textContent = displayName;
                select.appendChild(option);
            });
        }
    } catch (error) {
        console.error(`Error al cargar filtro ${entity}:`, error);
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

        const text = await response.text();
        let batches = [];
        if (text) {
            batches = JSON.parse(text);
        }

        updateBatchTable(batches);
    } catch (error) {
        console.error("Error al cargar lotes:", error);
        const tableBody = document.querySelector("#crud-table tbody");
        tableBody.innerHTML = "<tr><td colspan='5'>Error al cargar los lotes.</td></tr>";
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
        
        // Aseguramos que los valores se seleccionen correctamente
        const fishSelect = document.getElementById("edit-batch-fish");
        const tankSelect = document.getElementById("edit-batch-tank");
        const foodSelect = document.getElementById("edit-batch-food");
        
        if (fishSelect) fishSelect.value = batch.fishId;
        if (tankSelect) tankSelect.value = batch.tankId;
        if (foodSelect) foodSelect.value = batch.foodId;

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
        
        if (response.ok) {
            closeModal();
            loadBatchTable();
        }
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
        
        if (response.ok) {
            loadBatchTable();
        }
    } catch (error) {
        console.error("Error al eliminar lote:", error);
        alert("Ocurrió un error al eliminar el lote.");
    }
}

// Función para aplicar filtros - CORREGIDA
async function applyBatchFilters() {
    try {
        // Obtener los valores de los filtros de forma segura
        const quantityEl = document.getElementById("filter-quantity");
        const fishIdEl = document.getElementById("filter-fish");
        const tankIdEl = document.getElementById("filter-tank");
        const foodIdEl = document.getElementById("filter-food");
        
        const quantity = quantityEl && quantityEl.value.trim() !== "" ? quantityEl.value.trim() : null;
        const fishId = fishIdEl && fishIdEl.value ? fishIdEl.value : null;
        const tankId = tankIdEl && tankIdEl.value ? tankIdEl.value : null;
        const foodId = foodIdEl && foodIdEl.value ? foodIdEl.value : null;
        
        // Si no hay filtros seleccionados, cargamos todos los lotes
        if (!quantity && !fishId && !tankId && !foodId) {
            return loadBatchTable();
        }
        
        // Construir la URL con parámetros no vacíos
        let queryParams = [];
        if (quantity) queryParams.push(`quantity=${encodeURIComponent(quantity)}`);
        if (fishId) queryParams.push(`fishId=${encodeURIComponent(fishId)}`);
        if (tankId) queryParams.push(`tankId=${encodeURIComponent(tankId)}`);
        if (foodId) queryParams.push(`foodId=${encodeURIComponent(foodId)}`);
        
        const url = `${API_URL}/filter?${queryParams.join("&")}`;
        
        console.log("URL de filtro:", url); // Para depuración
        
        const response = await fetch(url);
        
        if (!response.ok) {
            console.error(`Error en respuesta: ${response.status} ${response.statusText}`);
            throw new Error(`Error al filtrar lotes: ${response.status}`);
        }

        // Intentar parsear los datos como JSON
        const text = await response.text();
        let batches = [];
        
        if (text && text.trim() !== '') {
            try {
                batches = JSON.parse(text);
            } catch (e) {
                console.error("Error al parsear respuesta JSON:", e);
                throw new Error("Formato de respuesta inválido");
            }
        }
        
        updateBatchTable(batches);
    } catch (error) {
        console.error("Error al filtrar lotes:", error);
        const tableBody = document.querySelector("#crud-table tbody");
        tableBody.innerHTML = "<tr><td colspan='5'>Error al aplicar los filtros: " + error.message + "</td></tr>";
    }
}

// Función auxiliar para actualizar la tabla con los resultados
function updateBatchTable(batches) {
    const tableBody = document.querySelector("#crud-table tbody");
    if (!tableBody) {
        console.error("No se encontró el cuerpo de la tabla");
        return;
    }
    
    tableBody.innerHTML = "";
    
    if (!Array.isArray(batches) || batches.length === 0) {
        tableBody.innerHTML = "<tr><td colspan='5'>No hay lotes que coincidan con los filtros.</td></tr>";
        return;
    }
    
    batches.forEach(batch => {
        const row = document.createElement("tr");
        
        // Usamos los mappings para mostrar nombres en lugar de IDs
        const fishName = fishMap[batch.fishId] || `ID: ${batch.fishId}`;
        const tankName = tankMap[batch.tankId] || `ID: ${batch.tankId}`;
        const foodName = foodMap[batch.foodId] || `ID: ${batch.foodId}`;
        
        row.innerHTML = `
            <td>${batch.quantity}</td>
            <td>${fishName}</td>
            <td>${tankName}</td>
            <td>${foodName}</td>
            <td>
                <button onclick="editBatch(${batch.id})">Editar</button>
                <button onclick="deleteBatch(${batch.id})">Eliminar</button>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

// Función para limpiar los filtros y mostrar todos los lotes
function resetBatchFilters() {
    const filterQuantity = document.getElementById("filter-quantity");
    const filterFish = document.getElementById("filter-fish");
    const filterTank = document.getElementById("filter-tank");
    const filterFood = document.getElementById("filter-food");
    
    if (filterQuantity) filterQuantity.value = "";
    if (filterFish) filterFish.value = "";
    if (filterTank) filterTank.value = "";
    if (filterFood) filterFood.value = "";
    
    loadBatchTable(); // Cargar todos los lotes sin filtros
}

// Configurar que los filtros se apliquen automáticamente al cambiar sus valores
function setupFilterEvents() {
    const filterElements = [
        document.getElementById("filter-quantity"),
        document.getElementById("filter-fish"),
        document.getElementById("filter-tank"),
        document.getElementById("filter-food")
    ];
    
    // Eliminar listeners anteriores para evitar duplicados
    filterElements.forEach(element => {
        if (element) {
            const eventType = element.tagName === 'SELECT' ? 'change' : 'input';
            const newElement = element.cloneNode(true);
            element.parentNode.replaceChild(newElement, element);
        }
    });
    
    // Agregar nuevos event listeners
    const updatedElements = [
        document.getElementById("filter-quantity"),
        document.getElementById("filter-fish"),
        document.getElementById("filter-tank"),
        document.getElementById("filter-food")
    ];
    
    updatedElements.forEach(element => {
        if (element) {
            const eventType = element.tagName === 'SELECT' ? 'change' : 'input';
            element.addEventListener(eventType, function() {
                setTimeout(applyBatchFilters, 100); // Pequeño retraso para asegurar que el valor se actualice
            });
        }
    });
    
    // Agregar event listener al botón de reset
    const resetButton = document.getElementById("reset-filters");
    if (resetButton) {
        const newResetButton = resetButton.cloneNode(true);
        resetButton.parentNode.replaceChild(newResetButton, resetButton);
        document.getElementById("reset-filters").addEventListener("click", resetBatchFilters);
    }
}

function openModal() {
    document.getElementById("edit-modal").style.display = "flex";
}

function closeModal() {
    document.getElementById("edit-modal").style.display = "none";
}