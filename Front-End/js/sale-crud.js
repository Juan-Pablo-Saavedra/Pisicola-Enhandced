// Endpoints base (ajusta según corresponda):
const API_URL = "http://localhost:8085/api/v1/sale";
const CUSTOMER_API_URL = "http://localhost:8085/api/v1/customer";

// Al cargar la página se inicializa la tabla, se llenan los selects de clientes y se configuran eventos.
document.addEventListener("DOMContentLoaded", function () {
  loadSaleTable();
  loadCustomerOptions();
  setupEnterKeyEvents();
  setupInputValidations();
  setupFilterEvents();
  setupDateValidations();
});

// ==================================================
// Función para cargar clientes en los selects (registro, edición y filtro)
async function loadCustomerOptions() {
  try {
    const response = await fetch(CUSTOMER_API_URL, {
      headers: { "Accept": "application/json" }
    });
    if (!response.ok) throw new Error(await response.text());
    const customers = await response.json();

    // Formulario de registro:
    const saleCustomerSelect = document.getElementById("sale-customer");
    if (saleCustomerSelect) {
      saleCustomerSelect.innerHTML = '<option value="">Selecciona un cliente</option>';
      customers.forEach(customer => {
        saleCustomerSelect.appendChild(new Option(customer.name, customer.id));
      });
    }
    // Modal de edición:
    const editSaleCustomerSelect = document.getElementById("edit-sale-customer");
    if (editSaleCustomerSelect) {
      editSaleCustomerSelect.innerHTML = '<option value="">Selecciona un cliente</option>';
      customers.forEach(customer => {
        editSaleCustomerSelect.appendChild(new Option(customer.name, customer.id));
      });
    }
    // Filtro:
    const filterCustomerSelect = document.getElementById("filter-customer");
    if (filterCustomerSelect) {
      filterCustomerSelect.innerHTML = '<option value="">Todos los clientes</option>';
      customers.forEach(customer => {
        filterCustomerSelect.appendChild(new Option(customer.name, customer.id));
      });
    }
  } catch (error) {
    console.error("Error al cargar clientes:", error);
    alert("Error al cargar clientes: " + error.message);
  }
}

// ==================================================
// Configurar eventos para los filtros
function setupFilterEvents() {
  // Aplicar filtros al hacer clic en botón de filtrado (si existe)
  const filterButton = document.getElementById("apply-filters");
  if (filterButton) {
    filterButton.addEventListener("click", applySaleFilters);
  }
  
  // También aplicar filtros al cambiar valores de los campos
  const filterDate = document.getElementById("filter-date");
  const filterTotal = document.getElementById("filter-total");
  const filterCustomer = document.getElementById("filter-customer");
  
  if (filterDate) filterDate.addEventListener("change", applySaleFilters);
  // Cambiamos a filtrado en tiempo real cuando se escribe
  if (filterTotal) filterTotal.addEventListener("input", debounce(applyTotalFilter, 300));
  if (filterCustomer) filterCustomer.addEventListener("change", applySaleFilters);
}

// Función para aplicar filtrado por coincidencias parciales en el total
function applyTotalFilter() {
  const filterTotal = document.getElementById("filter-total").value.trim().replace('$', '');
  
  if (!filterTotal) {
    // Si el filtro está vacío, mostrar todas las filas
    loadSaleTable();
    return;
  }
  
  // Obtener todas las filas de la tabla
  const rows = document.querySelectorAll("#crud-table tbody tr");
  let foundMatch = false;
  
  rows.forEach(row => {
    // Verificar si es una fila de mensaje (no datos)
    const isMessageRow = row.cells.length === 1 && row.cells[0].colSpan === 4;
    if (isMessageRow) return;
    
    // Obtener el total de la segunda columna (índice 1)
    const totalCell = row.cells[1].textContent.replace('$', '').trim();
    
    // Comprobar si el total contiene el filtro (coincidencia parcial)
    if (totalCell.includes(filterTotal)) {
      row.style.display = "";
      foundMatch = true;
    } else {
      row.style.display = "none";
    }
  });
  
  // Si no hay coincidencias, mostrar mensaje
  const tableBody = document.querySelector("#crud-table tbody");
  const noMatchesRow = document.getElementById("no-matches-row");
  
  if (!foundMatch) {
    if (!noMatchesRow) {
      const newRow = document.createElement("tr");
      newRow.id = "no-matches-row";
      newRow.innerHTML = `<td colspan="4">No hay coincidencias con "${filterTotal}"</td>`;
      tableBody.appendChild(newRow);
    } else {
      noMatchesRow.innerHTML = `<td colspan="4">No hay coincidencias con "${filterTotal}"</td>`;
      noMatchesRow.style.display = "";
    }
  } else if (noMatchesRow) {
    noMatchesRow.style.display = "none";
  }
}

// Función de debounce para evitar demasiadas peticiones
function debounce(func, wait) {
  let timeout;
  return function(...args) {
    clearTimeout(timeout);
    timeout = setTimeout(() => func.apply(this, args), wait);
  };
}

// ==================================================
// Configurar validaciones para fechas (prevenir fechas futuras)
function setupDateValidations() {
  // Obtener la fecha actual en formato YYYY-MM-DD
  const today = new Date();
  today.setDate(today.getDate() + 1); // Permitir hasta mañana para evitar problemas con zonas horarias
  const formattedToday = today.toISOString().split('T')[0];
  
  // Aplicar límite máximo a los campos de fecha
  const dateInputs = [
    document.getElementById("sale-date"),
    document.getElementById("edit-sale-date")
  ];
  
  dateInputs.forEach(input => {
    if (input) {
      // Establecer la fecha máxima como hoy + 1 día para flexibilidad
      input.setAttribute("max", formattedToday);
    }
  });
}

// Validación adicional antes de enviar formularios
function validateDateNotFuture(dateValue) {
  if (!dateValue) return true; // Si no hay fecha, otra validación se encargará
  
  const selectedDate = new Date(dateValue);
  const today = new Date();
  
  // Permitir registrar con la fecha de mañana para mayor flexibilidad
  today.setDate(today.getDate() + 1);
  today.setHours(23, 59, 59, 999); // Final del día de mañana
  
  if (selectedDate > today) {
    alert("Error: No se pueden registrar ventas con fechas futuras (más allá de mañana).");
    return false;
  }
  return true;
}

// ==================================================
// Función para aplicar filtros a las ventas
async function applySaleFilters() {
  try {
    const filterDate = document.getElementById("filter-date").value; // esperado "yyyy-MM-dd"
    const filterTotal = document.getElementById("filter-total").value.trim().replace('$', '').replace(',', '.');
    const filterCustomer = document.getElementById("filter-customer").value; // opcional

    // Si no hay filtros activos, cargar todas las ventas
    if (!filterDate && !filterTotal && !filterCustomer) {
      return loadSaleTable();
    }

    // Construir la URL con los parámetros
    const params = new URLSearchParams();
    
    if (filterDate) {
      // Formato para enviar al backend: yyyy-MM-dd
      params.append("date", filterDate);
    }
    
    if (filterTotal && filterTotal.length > 0) {
      params.append("total", filterTotal);
    }
    
    if (filterCustomer) {
      params.append("customerId", filterCustomer);
    }

    const url = API_URL + "/filter?" + params.toString();
    console.log("URL de filtrado:", url);
    
    const response = await fetch(url, {
      headers: { "Accept": "application/json" }
    });
    
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error("Error al cargar ventas (filtros): " + errorText);
    }
    
    const sales = await response.json();
    renderSaleTable(sales);
  } catch (error) {
    console.error("Error aplicando filtros:", error);
    alert(error.message);
    // No limpiar la tabla para mantener los datos anteriores
  }
}

// ==================================================
// Configurar validaciones para campos numéricos
function setupInputValidations() {
  // Permitir solo números y punto decimal en campos de total
  const totalInputs = [
    document.getElementById("sale-total"), 
    document.getElementById("edit-sale-total"),
    document.getElementById("filter-total")
  ];
  
  totalInputs.forEach(input => {
    if (input) {
      // Validar entradas numéricas
      input.addEventListener("input", function(e) {
        // Permitir solo números, punto decimal y borrar entrada anterior
        let value = e.target.value.replace(/[^\d.]/g, '');
        
        // Asegurar que solo haya un punto decimal
        const decimalCount = (value.match(/\./g) || []).length;
        if (decimalCount > 1) {
          value = value.substr(0, value.lastIndexOf('.'));
        }
        
        e.target.value = value;
      });
      
      // Formatear al perder el foco (agregar signo de $)
      input.addEventListener("blur", function(e) {
        if (e.target.value) {
          // Formatear con signo de $ para todos los campos, incluyendo filtro
          const formattedValue = `$${parseFloat(e.target.value).toFixed(2)}`;
          e.target.value = formattedValue;
        }
      });
    }
  });
}

// ==================================================
// Función para cargar todas las ventas (sin filtros)
async function loadSaleTable() {
  try {
    const response = await fetch(API_URL, {
      headers: { "Accept": "application/json" }
    });
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error("Error al cargar ventas: " + errorText);
    }
    const sales = await response.json();
    renderSaleTable(sales);
  } catch (error) {
    console.error("Error al cargar ventas:", error);
    alert(error.message);
    clearTable();
  }
}

// ==================================================
// Función para renderizar la tabla de ventas
function renderSaleTable(sales) {
  const tableBody = document.querySelector("#crud-table tbody");
  if (!sales || sales.length === 0) {
    tableBody.innerHTML = "<tr><td colspan='4'>No hay ventas registradas.</td></tr>";
    return;
  }
  tableBody.innerHTML = "";
  sales.forEach(sale => {
    // Se intenta formatear la fecha; si sale.date es un timestamp o una cadena ISO, esto debería funcionar.
    let formattedDate;
    try {
      formattedDate = new Date(sale.date).toLocaleDateString();
    } catch (e) {
      formattedDate = "Fecha inválida";
    }
    
    // Formatear total con signo de pesos
    const formattedTotal = `$${parseFloat(sale.total).toFixed(0)}`;
    
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${formattedDate}</td>
      <td>${formattedTotal}</td>
      <td>${sale.customerName || "Sin cliente"}</td>
      <td>
        <button onclick="editSale(${sale.id})">Editar</button>
        <button onclick="deleteSale(${sale.id})">Eliminar</button>
      </td>
    `;
    tableBody.appendChild(row);
  });
}

// ==================================================
// Función para registrar una nueva venta
async function registerSale() {
  try {
    const saleDate = document.getElementById("sale-date").value;
    const saleTotal = document.getElementById("sale-total").value.trim().replace('$', '').replace(',', '.');
    const saleCustomer = document.getElementById("sale-customer").value;

    if (!saleDate || !saleTotal || !saleCustomer) {
      alert("Todos los campos (Fecha, Total y Cliente) son obligatorios.");
      return;
    }

    const totalNumber = parseFloat(saleTotal);
    if (isNaN(totalNumber) || totalNumber <= 0) {
      alert("El total debe ser un número mayor que cero.");
      return;
    }
    
    // Validar que la fecha no sea demasiado futura
    if (!validateDateNotFuture(saleDate)) {
      return;
    }

    // Para el registro, enviamos la fecha tal cual, asumiendo que Jackson convertirá "yyyy-MM-dd" a Date.
    const saleData = {
      id: 0,
      date: saleDate,
      total: totalNumber,
      customerId: parseInt(saleCustomer)
    };

    const response = await fetch(API_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Accept": "text/plain"
      },
      body: JSON.stringify(saleData)
    });
    const message = await response.text();
    alert(message || "Venta registrada correctamente.");
    if (response.ok) {
      window.location.reload();
    }
  } catch (error) {
    console.error("Error al registrar venta:", error);
    alert("Ocurrió un error al registrar la venta: " + error.message);
  }
}

// ==================================================
// Función para cargar los datos de una venta en el modal para editarla
async function editSale(id) {
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      headers: { "Accept": "application/json" }
    });
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error("Error al obtener la venta: " + errorText);
    }
    const sale = await response.json();
    document.getElementById("edit-sale-id").value = sale.id;
    
    // Formato de fecha para el campo input date (yyyy-MM-dd)
    let dateStr;
    try {
      const dt = new Date(sale.date);
      dateStr = dt.toISOString().substring(0, 10);
    } catch (e) {
      dateStr = "";
      console.error("Error al formatear fecha:", e);
    }
    
    document.getElementById("edit-sale-date").value = dateStr;
    document.getElementById("edit-sale-total").value = sale.total;
    document.getElementById("edit-sale-customer").value = sale.customerId;
    openModal();
  } catch (error) {
    console.error("Error al cargar datos de la venta:", error);
    alert("Ocurrió un error al cargar los datos de la venta: " + error.message);
  }
}

// ==================================================
// Función para actualizar los datos de la venta desde el modal
async function saveSale() {
  const id = document.getElementById("edit-sale-id").value;
  const saleDate = document.getElementById("edit-sale-date").value;
  const saleTotal = document.getElementById("edit-sale-total").value.trim().replace('$', '').replace(',', '.');
  const saleCustomer = document.getElementById("edit-sale-customer").value;

  if (!saleDate || !saleTotal || !saleCustomer) {
    alert("Todos los campos (Fecha, Total y Cliente) son obligatorios.");
    return;
  }

  const totalNumber = parseFloat(saleTotal);
  if (isNaN(totalNumber) || totalNumber <= 0) {
    alert("El total debe ser un número mayor que cero.");
    return;
  }
  
  // Validar que la fecha no sea demasiado futura al editar
  if (!validateDateNotFuture(saleDate)) {
    return;
  }

  try {
    const saleData = {
      id: id,
      date: saleDate,
      total: totalNumber,
      customerId: parseInt(saleCustomer)
    };
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Accept": "text/plain"
      },
      body: JSON.stringify(saleData)
    });
    if (!response.ok) {
      const errorMsg = await response.text();
      throw new Error(errorMsg || "Error desconocido al actualizar la venta.");
    }
    const message = await response.text();
    alert(message || "Venta actualizada correctamente.");
    window.location.reload();
  } catch (error) {
    console.error("Error al actualizar venta:", error);
    alert("Ocurrió un error al actualizar la venta: " + (error.message || "Error desconocido."));
  }
}

// ==================================================
// Función para eliminar una venta
async function deleteSale(id) {
  if (!confirm("¿Estás seguro de que deseas eliminar esta venta?")) return;
  try {
    const response = await fetch(`${API_URL}/${id}`, {
      method: "DELETE",
      headers: { "Accept": "text/plain" }
    });
    const result = await response.text();
    alert(result);
    window.location.reload();
  } catch (error) {
    console.error("Error al eliminar venta:", error);
    alert("Ocurrió un error al eliminar la venta: " + error.message);
  }
}

// ==================================================
// Funciones para abrir y cerrar el modal de edición
function openModal() {
  const modal = document.getElementById("edit-modal");
  if (modal) modal.style.display = "flex";
}

function closeModal() {
  const modal = document.getElementById("edit-modal");
  if (modal) modal.style.display = "none";
}

// ==================================================
// Función para limpiar la tabla (en caso de error o sin resultados)
function clearTable() {
  const tableBody = document.querySelector("#crud-table tbody");
  tableBody.innerHTML = "<tr><td colspan='4'>No hay ventas registradas.</td></tr>";
}

// ==================================================
// Configuración de eventos para que la tecla "Enter" active las acciones en los formularios.
function setupEnterKeyEvents() {
  const crudForm = document.getElementById("crud-form");
  if (crudForm) {
    crudForm.addEventListener("keypress", function (event) {
      if (event.key === "Enter") {
        event.preventDefault();
        registerSale();
      }
    });
  }
  const editForm = document.getElementById("edit-form");
  if (editForm) {
    editForm.addEventListener("keypress", function (event) {
      if (event.key === "Enter") {
        event.preventDefault();
        saveSale();
      }
    });
  }
  
  // Agregar evento para filtros
  const filterForm = document.getElementById("filter-form");
  if (filterForm) {
    filterForm.addEventListener("keypress", function (event) {
      if (event.key === "Enter") {
        event.preventDefault();
        applySaleFilters();
      }
    });
  }
}