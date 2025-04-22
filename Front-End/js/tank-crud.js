const TANK_API_URL = "http://localhost:8085/api/v1/tank";
const MAINTENANCE_API_URL = "http://localhost:8085/api/v1/maintenance";

// Al cargar la página, se cargan la tabla de tanks y se configuran los eventos.
document.addEventListener("DOMContentLoaded", function () {
  loadTankTable();
  setupEnterKeyEvents();
});

// Función para filtrar tanks por ubicación, tipo de agua y capacidad.
async function applyTankFilters() {
  try {
    const response = await fetch(TANK_API_URL);
    if (!response.ok) throw new Error("Error al cargar tanks.");
    let tanks = await response.json();

    const filterLocation = document.getElementById("filter-location").value.trim().toLowerCase();
    const filterWaterType = document.getElementById("filter-waterType").value.trim().toLowerCase();
    const filterCapacityInput = document.getElementById("filter-capacity").value.trim();
    
    let filterCapacity = null;
    if (filterCapacityInput !== "" && !isNaN(parseFloat(filterCapacityInput))) {
      filterCapacity = parseFloat(filterCapacityInput);
    }

    tanks = tanks.filter(tank => {
      let matches = true;
      if (filterLocation) {
        matches = matches && tank.location.toLowerCase().includes(filterLocation);
      }
      if (filterWaterType) {
        matches = matches && tank.waterType.toLowerCase().includes(filterWaterType);
      }
      if (filterCapacity !== null) {
        // Se filtra si la capacidad es exactamente igual al número ingresado.
        matches = matches && (tank.capacity === filterCapacity);
      }
      return matches;
    });
    
    renderTankTable(tanks);
  } catch (error) {
    console.error("Error aplicando filtros:", error);
    clearTable();
  }
}

// Función para cargar la tabla de tanks desde la API.
async function loadTankTable() {
  try {
    const response = await fetch(TANK_API_URL);
    if (!response.ok) throw new Error("Error al cargar tanks.");
    const tanks = await response.json();
    renderTankTable(tanks);
  } catch (error) {
    console.error("Error al cargar tanks:", error);
    clearTable();
  }
}

// Función para renderizar la tabla con tanks.
function renderTankTable(tanks) {
  const tableBody = document.querySelector("#crud-table tbody");
  if (tanks.length === 0) {
    tableBody.innerHTML = "<tr><td colspan='4'>No hay tanks registrados.</td></tr>";
    return;
  }
  tableBody.innerHTML = "";
  tanks.forEach(tank => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${tank.capacity}</td>
      <td>${tank.location}</td>
      <td>${tank.waterType}</td>
      <td>
        <button onclick="editTank(${tank.id})">Editar</button>
        <button onclick="deleteTank(${tank.id})">Eliminar</button>
      </td>
    `;
    tableBody.appendChild(row);
  });
}

// Función para registrar un nuevo tank.
async function registerTank() {
  try {
    const capacityInput = document.getElementById("tank-capacity").value.trim();
    const location = document.getElementById("tank-location").value.trim();
    const waterType = document.getElementById("tank-waterType").value.trim();

    if (!capacityInput || !location || !waterType) {
      alert("Todos los campos (Capacidad, Ubicación y Tipo de Agua) son obligatorios.");
      return;
    }

    const capacity = parseFloat(capacityInput);
    if (isNaN(capacity) || capacity <= 0) {
      alert("La capacidad debe ser un número mayor que cero.");
      return;
    }

    const data = { id: 0, capacity, location, waterType };
    const response = await fetch(TANK_API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });
    const message = await response.text();
    alert(message || "Tank registrado correctamente.");
    if (response.ok) {
      window.location.reload();
    }
  } catch (error) {
    console.error("Error al registrar tank:", error);
    alert("Ocurrió un error al registrar el tank.");
  }
}

// Función para cargar los datos de un tank en el modal de edición.
async function editTank(id) {
  try {
    const response = await fetch(`${TANK_API_URL}/${id}`);
    if (!response.ok) throw new Error("Error al obtener tank.");
    const tank = await response.json();
    document.getElementById("edit-tank-id").value = tank.id;
    document.getElementById("edit-tank-capacity").value = tank.capacity;
    document.getElementById("edit-tank-location").value = tank.location;
    document.getElementById("edit-tank-waterType").value = tank.waterType;
    openModal();
  } catch (error) {
    console.error("Error al cargar datos del tank:", error);
    alert("Ocurrió un error al cargar los datos del tank.");
  }
}

// Función para guardar los cambios del tank en el modal de edición.
async function saveTank() {
  const id = document.getElementById("edit-tank-id").value;
  const capacityInput = document.getElementById("edit-tank-capacity").value.trim();
  const location = document.getElementById("edit-tank-location").value.trim();
  const waterType = document.getElementById("edit-tank-waterType").value.trim();

  if (!capacityInput || !location || !waterType) {
    alert("Todos los campos (Capacidad, Ubicación y Tipo de Agua) son obligatorios.");
    return;
  }

  const capacity = parseFloat(capacityInput);
  if (isNaN(capacity) || capacity <= 0) {
    alert("La capacidad debe ser un número mayor que cero.");
    return;
  }

  try {
    const data = { id, capacity, location, waterType };
    const response = await fetch(`${TANK_API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });
    if (!response.ok) {
      const errorMsg = await response.text();
      throw new Error(errorMsg || "Error desconocido al actualizar tank.");
    }
    const message = await response.text();
    alert(message || "Tank actualizado correctamente.");
    window.location.reload();
  } catch (error) {
    console.error("Error al actualizar tank:", error);
    alert("Ocurrió un error al actualizar el tank: " + (error.message || "Error desconocido."));
  }
}

// Función para eliminar un tank con validación de mantenimientos asociados.
async function deleteTank(id) {
  if (!confirm("¿Estás seguro de que deseas eliminar este tank?")) return;
  try {
    // Consultar mantenimientos para verificar si hay registros relacionados con el tanque
    const maintResp = await fetch(MAINTENANCE_API_URL);
    if (!maintResp.ok) throw new Error("Error al cargar mantenimientos.");
    const maintList = await maintResp.json();
    // Filtrar mantenimientos que tengan este tankId
    const relatedMaintenances = maintList.filter(m => m.tankId === id);

    if (relatedMaintenances.length > 0) {
      const proceed = confirm(
        "Este tank tiene mantenimientos pendientes. Si continúas, se eliminarán también dichos registros. ¿Desea continuar?"
      );
      if (!proceed) return;

      // Eliminar cada mantenimiento relacionado
      await Promise.all(relatedMaintenances.map(async (m) => {
        const delResp = await fetch(`${MAINTENANCE_API_URL}/${m.id}`, {
          method: "DELETE",
          headers: { "Accept": "text/plain" }
        });
        if (!delResp.ok) {
          const errText = await delResp.text();
          throw new Error("Error al eliminar mantenimiento id " + m.id + ": " + errText);
        }
      }));
    }

    // Proceder a eliminar el tank
    const tankDelResp = await fetch(`${TANK_API_URL}/${id}`, { method: "DELETE" });
    if (!tankDelResp.ok) {
      const errText = await tankDelResp.text();
      throw new Error("Error al eliminar tank: " + errText);
    }
    const result = await tankDelResp.text();
    alert(result || "Tank eliminado exitosamente.");
    window.location.reload();
  } catch (error) {
    console.error("Error al eliminar tank:", error);
    alert("Ocurrió un error al eliminar el tank: " + error.message);
  }
}

// Función para abrir el modal de edición.
function openModal() {
  const modal = document.getElementById("edit-modal");
  if (modal) {
    modal.style.display = "flex";
  }
}

// Función para cerrar el modal de edición.
function closeModal() {
  const modal = document.getElementById("edit-modal");
  if (modal) {
    modal.style.display = "none";
  }
}

// Función para vaciar la tabla cuando no existan resultados o se produzca un error.
function clearTable() {
  const tableBody = document.querySelector("#crud-table tbody");
  tableBody.innerHTML = "<tr><td colspan='4'>No hay tanks registrados.</td></tr>";
}

// Configurar eventos para que se active la acción al presionar la tecla "Enter" en los formularios.
function setupEnterKeyEvents() {
  const crudForm = document.getElementById("crud-form");
  if (crudForm) {
    crudForm.addEventListener("keypress", function (event) {
      if (event.key === "Enter") {
        event.preventDefault();
        registerTank();
      }
    });
  }
  const editForm = document.getElementById("edit-form");
  if (editForm) {
    editForm.addEventListener("keypress", function (event) {
      if (event.key === "Enter") {
        event.preventDefault();
        saveTank();
      }
    });
  }
}
