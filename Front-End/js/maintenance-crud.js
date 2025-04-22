// Endpoints base (ajusta según corresponda)
const API_URL = "http://localhost:8085/api/v1/maintenance";
const TANK_API_URL = "http://localhost:8085/api/v1/tank";
const EMPLOYEE_API_URL = "http://localhost:8085/api/v1/employee";

// Al cargar la página: cargar la tabla, los selects (tanques y empleados) y configurar eventos
document.addEventListener("DOMContentLoaded", () => {
  loadMaintenanceTable();
  loadTankOptions();
  loadEmployeeOptions();
  setupEnterKeyEvents();
  setupFilterEvents();
});

/* 
 * Función auxiliar para forzar que el día que se seleccione se convierta
 * en una cadena ISO con la zona horaria local. 
 * Por ejemplo, si se ingresa "2025-04-19" y la zona es GMT-5,
 * se generará "2025-04-19T00:00:00-05:00" para que al interpretarse se
 * mantenga la fecha correcta.
 */
function getLocalISOString(dateStr) {
  if (!dateStr) return "";
  const [year, month, day] = dateStr.split("-");
  // Construye una fecha usando el constructor que interpreta la fecha en horario local
  const localDate = new Date(year, month - 1, day);
  const pad = n => String(n).padStart(2, '0');
  const yyyy = localDate.getFullYear();
  const mm = pad(localDate.getMonth() + 1);
  const dd = pad(localDate.getDate());
  const hh = pad(localDate.getHours());
  const mi = pad(localDate.getMinutes());
  const ss = pad(localDate.getSeconds());
  const tzOffset = -localDate.getTimezoneOffset(); // en minutos
  const sign = tzOffset >= 0 ? "+" : "-";
  const tzHours = pad(Math.floor(Math.abs(tzOffset) / 60));
  const tzMinutes = pad(Math.abs(tzOffset) % 60);
  return `${yyyy}-${mm}-${dd}T${hh}:${mi}:${ss}${sign}${tzHours}:${tzMinutes}`;
}

// ==================================================
// Cargar opciones para el select de tanques
async function loadTankOptions() {
  try {
    const response = await fetch(TANK_API_URL, { headers: { "Accept": "application/json" } });
    if (!response.ok) throw new Error(await response.text());
    const tanks = await response.json();

    // Para el formulario de registro
    const maintenanceTankSelect = document.getElementById("maintenance-tank");
    if (maintenanceTankSelect) {
      maintenanceTankSelect.innerHTML = '<option value="">Selecciona un tanque</option>';
      tanks.forEach(tank => {
        // Usamos getLocation() para el "nombre"
        maintenanceTankSelect.appendChild(new Option(tank.location, tank.id));
      });
    }
    // Para el modal de edición
    const editMaintenanceTankSelect = document.getElementById("edit-maintenance-tank");
    if (editMaintenanceTankSelect) {
      editMaintenanceTankSelect.innerHTML = '<option value="">Selecciona un tanque</option>';
      tanks.forEach(tank => {
        editMaintenanceTankSelect.appendChild(new Option(tank.location, tank.id));
      });
    }
  } catch (error) {
    console.error("Error al cargar tanques:", error);
    alert("Error al cargar tanques: " + error.message);
  }
}

// ==================================================
// Cargar opciones para el select de empleados
async function loadEmployeeOptions() {
  try {
    const response = await fetch(EMPLOYEE_API_URL, { headers: { "Accept": "application/json" } });
    if (!response.ok) throw new Error(await response.text());
    const employees = await response.json();

    // Para el formulario de registro
    const maintenanceEmployeeSelect = document.getElementById("maintenance-employee");
    if (maintenanceEmployeeSelect) {
      maintenanceEmployeeSelect.innerHTML = '<option value="">Selecciona un empleado</option>';
      employees.forEach(emp => {
        // Usamos getName() para el nombre
        maintenanceEmployeeSelect.appendChild(new Option(emp.name, emp.id));
      });
    }
    // Para el modal de edición
    const editMaintenanceEmployeeSelect = document.getElementById("edit-maintenance-employee");
    if (editMaintenanceEmployeeSelect) {
      editMaintenanceEmployeeSelect.innerHTML = '<option value="">Selecciona un empleado</option>';
      employees.forEach(emp => {
        editMaintenanceEmployeeSelect.appendChild(new Option(emp.name, emp.id));
      });
    }
  } catch (error) {
    console.error("Error al cargar empleados:", error);
    alert("Error al cargar empleados: " + error.message);
  }
}

// ==================================================
// Filtrar mantenimientos por fecha
// Se utiliza un único input (id="filter-date"), y se envía el valor como parámetros "start" y "end"
// con el mismo valor (formato "yyyy-MM-dd") para buscar el día exacto.
async function applyMaintenanceFilters() {
  try {
    const filterDate = document.getElementById("filter-date").value; // "yyyy-MM-dd"
    const params = new URLSearchParams();
    if (filterDate) {
      params.append("start", filterDate);
      params.append("end", filterDate);
    }
    const url = API_URL + "/filter?" + params.toString();
    const response = await fetch(url, { headers: { "Accept": "application/json" } });
    if (!response.ok) {
      const errText = await response.text();
      throw new Error("Error al cargar mantenimientos (filtros): " + errText);
    }
    const maintList = await response.json();
    renderMaintenanceTable(maintList);
  } catch (error) {
    console.error("Error aplicando filtros:", error);
    alert(error.message);
    clearTable();
  }
}

// ==================================================
// Cargar todos los mantenimientos (sin filtros)
async function loadMaintenanceTable() {
  try {
    const response = await fetch(API_URL, { headers: { "Accept": "application/json" } });
    if (!response.ok) {
      const errText = await response.text();
      throw new Error("Error al cargar mantenimientos: " + errText);
    }
    const maintList = await response.json();
    renderMaintenanceTable(maintList);
  } catch (error) {
    console.error("Error al cargar mantenimientos:", error);
    alert(error.message);
    clearTable();
  }
}

// ==================================================
// Renderizar la tabla de mantenimientos
// Se muestran: Fecha (formateada), Descripción, Tanque y Empleado, con acciones Editar/Eliminar
function renderMaintenanceTable(maintList) {
  const tableBody = document.querySelector("#crud-table tbody");
  if (maintList.length === 0) {
    tableBody.innerHTML = "<tr><td colspan='5'>No hay mantenimientos registrados.</td></tr>";
    return;
  }
  tableBody.innerHTML = "";
  maintList.forEach(maint => {
    const formattedDate = new Date(maint.date).toLocaleDateString();
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${formattedDate}</td>
      <td>${maint.description}</td>
      <td>${maint.tankName || "Sin tanque"}</td>
      <td>${maint.employeeName || "Sin empleado"}</td>
      <td>
        <button onclick="editMaintenance(${maint.id})">Editar</button>
        <button onclick="deleteMaintenance(${maint.id})">Eliminar</button>
      </td>
    `;
    tableBody.appendChild(row);
  });
}

// ==================================================
// Registrar un nuevo mantenimiento
async function registerMaintenance() {
  try {
    const maintenanceDate = document.getElementById("maintenance-date").value;
    const maintenanceDescription = document.getElementById("maintenance-description").value.trim();
    const maintenanceTank = document.getElementById("maintenance-tank").value;
    const maintenanceEmployee = document.getElementById("maintenance-employee").value;
    
    if (!maintenanceDate || !maintenanceDescription || !maintenanceTank || !maintenanceEmployee) {
      alert("Todos los campos (Fecha, Descripción, Tanque y Empleado) son obligatorios.");
      return;
    }
    
    // Convertir el valor del input en una cadena ISO local con la zona horaria incluida
    const formattedDate = getLocalISOString(maintenanceDate);
    
    const maintData = {
      id: 0,
      date: formattedDate,
      description: maintenanceDescription,
      tankId: parseInt(maintenanceTank),
      employeeId: parseInt(maintenanceEmployee)
    };
    
    const response = await fetch(API_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Accept": "text/plain"
      },
      body: JSON.stringify(maintData)
    });
    const message = await response.text();
    alert(message || "Mantenimiento registrado correctamente.");
    if (response.ok) {
      window.location.reload();
    }
  } catch (error) {
    console.error("Error al registrar mantenimiento:", error);
    alert("Ocurrió un error al registrar el mantenimiento: " + error.message);
  }
}

// ==================================================
// Cargar datos de un mantenimiento en el modal para editar
async function editMaintenance(id) {
  try {
    const response = await fetch(`${API_URL}/${id}`, { headers: { "Accept": "application/json" } });
    if (!response.ok) {
      const errText = await response.text();
      throw new Error("Error al obtener el mantenimiento: " + errText);
    }
    const maint = await response.json();
    document.getElementById("edit-maintenance-id").value = maint.id;
    const dt = new Date(maint.date);
    // Para editar, se recorta la cadena ISO al formato "yyyy-MM-dd"
    document.getElementById("edit-maintenance-date").value = dt.toISOString().substring(0, 10);
    document.getElementById("edit-maintenance-description").value = maint.description;
    document.getElementById("edit-maintenance-tank").value = maint.tankId;
    document.getElementById("edit-maintenance-employee").value = maint.employeeId;
    openModal();
  } catch (error) {
    console.error("Error al cargar mantenimiento:", error);
    alert("Ocurrió un error al cargar los datos del mantenimiento: " + error.message);
  }
}

// ==================================================
// Actualizar mantenimiento desde el modal de edición
async function saveMaintenance() {
  const id = document.getElementById("edit-maintenance-id").value;
  const maintenanceDate = document.getElementById("edit-maintenance-date").value;
  const maintenanceDescription = document.getElementById("edit-maintenance-description").value.trim();
  const maintenanceTank = document.getElementById("edit-maintenance-tank").value;
  const maintenanceEmployee = document.getElementById("edit-maintenance-employee").value;
  
  if (!maintenanceDate || !maintenanceDescription || !maintenanceTank || !maintenanceEmployee) {
    alert("Todos los campos (Fecha, Descripción, Tanque y Empleado) son obligatorios.");
    return;
  }
  
  try {
    const formattedDate = getLocalISOString(maintenanceDate);
    const maintData = {
      id: id,
      date: formattedDate,
      description: maintenanceDescription,
      tankId: parseInt(maintenanceTank),
      employeeId: parseInt(maintenanceEmployee)
    };
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Accept": "text/plain"
      },
      body: JSON.stringify(maintData)
    });
    if (!response.ok) {
      const errMsg = await response.text();
      throw new Error(errMsg || "Error desconocido al actualizar mantenimiento.");
    }
    const message = await response.text();
    alert(message || "Mantenimiento actualizado correctamente.");
    window.location.reload();
  } catch (error) {
    console.error("Error al actualizar mantenimiento:", error);
    alert("Ocurrió un error al actualizar el mantenimiento: " + (error.message || "Error desconocido."));
  }
}

// ==================================================
// Eliminar mantenimiento
async function deleteMaintenance(id) {
  if (!confirm("¿Estás seguro de que deseas eliminar este mantenimiento?")) return;
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "DELETE",
      headers: { "Accept": "text/plain" }
    });
    const result = await response.text();
    alert(result);
    window.location.reload();
  } catch (error) {
    console.error("Error al eliminar mantenimiento:", error);
    alert("Ocurrió un error al eliminar el mantenimiento: " + error.message);
  }
}

// ==================================================
// Abrir y cerrar modal
function openModal() {
  const modal = document.getElementById("edit-modal");
  if (modal) modal.style.display = "flex";
}

function closeModal() {
  const modal = document.getElementById("edit-modal");
  if (modal) modal.style.display = "none";
}

// ==================================================
// Limpiar la tabla en caso de error o sin datos
function clearTable() {
  const tableBody = document.querySelector("#crud-table tbody");
  tableBody.innerHTML = "<tr><td colspan='5'>No hay mantenimientos registrados.</td></tr>";
}

// ==================================================
// Configuración de la acción de la tecla "Enter" en los formularios
function setupEnterKeyEvents() {
  const crudForm = document.getElementById("crud-form");
  if (crudForm) {
    crudForm.addEventListener("keypress", function (event) {
      if (event.key === "Enter") {
        event.preventDefault();
        registerMaintenance();
      }
    });
  }
  const editForm = document.getElementById("edit-form");
  if (editForm) {
    editForm.addEventListener("keypress", function (event) {
      if (event.key === "Enter") {
        event.preventDefault();
        saveMaintenance();
      }
    });
  }
}

// ==================================================
// Configurar que el filtro se active al cambiar el input de fecha
function setupFilterEvents() {
  const filterDate = document.getElementById("filter-date");
  if (filterDate) {
    filterDate.addEventListener("change", applyMaintenanceFilters);
  }
}
