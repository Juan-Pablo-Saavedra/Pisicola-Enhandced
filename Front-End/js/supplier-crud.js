const API_URL = "http://localhost:8085/api/v1/supplier";
const CATEGORY_API_URL = "http://localhost:8085/api/v1/category";
const FOOD_API_URL = "http://localhost:8085/api/v1/food";

// Al cargar la página, se cargan la tabla, las categorías, los alimentos y se configuran los eventos.
document.addEventListener("DOMContentLoaded", function () {
  loadSupplierTable();
  loadCategoryOptions();
  loadFoodOptions();
  setupEnterKeyEvents();
});

// Función para validar que el email tenga formato correcto: debe tener un "@" y un dominio.
function isValidEmail(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

// Función para validar que el contacto contenga únicamente el símbolo "+" (opcional) y dígitos.
function isValidContact(contact) {
  const contactRegex = /^\+?\d+$/;
  return contactRegex.test(contact);
}

// Cargar categorías para registro y edición desde la API.
async function loadCategoryOptions() {
  try {
    const response = await fetch(CATEGORY_API_URL);
    if (!response.ok) throw new Error("Error al cargar categorías.");
    const categories = await response.json();
    const categorySelectAdd = document.getElementById("supplier-category");
    const categorySelectEdit = document.getElementById("edit-category");
    if (categorySelectAdd) {
      categorySelectAdd.innerHTML = '<option value="">Selecciona una categoría</option>';
      categories.forEach(category => {
        categorySelectAdd.appendChild(new Option(category.name, category.name));
      });
    }
    if (categorySelectEdit) {
      categorySelectEdit.innerHTML = '<option value="">Selecciona una categoría</option>';
      categories.forEach(category => {
        categorySelectEdit.appendChild(new Option(category.name, category.name));
      });
    }
  } catch (error) {
    console.error("Error al cargar categorías:", error);
  }
}

// Cargar alimentos para registro y edición desde la API.
async function loadFoodOptions() {
  try {
    const response = await fetch(FOOD_API_URL);
    if (!response.ok) throw new Error("Error al cargar alimentos.");
    const foods = await response.json();
    const foodSelectAdd = document.getElementById("supplier-food");
    const foodSelectEdit = document.getElementById("edit-food");
    if (foodSelectAdd) {
      foodSelectAdd.innerHTML = '<option value="">Selecciona un alimento</option>';
      foods.forEach(food => {
        foodSelectAdd.appendChild(new Option(`${food.type} - ${food.brand}`, food.id));
      });
    }
    if (foodSelectEdit) {
      foodSelectEdit.innerHTML = '<option value="">Selecciona un alimento</option>';
      foods.forEach(food => {
        foodSelectEdit.appendChild(new Option(`${food.type} - ${food.brand}`, food.id));
      });
    }
  } catch (error) {
    console.error("Error al cargar alimentos:", error);
  }
}

// Función que aplica los filtros (por nombre y por correo) y vuelve a renderizar la tabla.
async function applySupplierFilters() {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("Error al cargar proveedores.");
    let suppliers = await response.json();
    const filterName = document.getElementById("filter-name").value.trim().toLowerCase();
    const filterEmail = document.getElementById("filter-category").value.trim().toLowerCase();
    if (filterName || filterEmail) {
      // Se requiere que ambos campos coincidan (puedes cambiar a OR si lo prefieres)
      suppliers = suppliers.filter(supplier => {
        return supplier.name.toLowerCase().includes(filterName) &&
               supplier.email.toLowerCase().includes(filterEmail);
      });
    }
    renderSupplierTable(suppliers);
  } catch (error) {
    console.error("Error aplicando filtros:", error);
    clearTable();
  }
}

// Registrar un nuevo proveedor.
async function registerSupplier() {
  try {
    const name = document.getElementById("supplier-name").value.trim();
    const category = document.getElementById("supplier-category").value;
    const contact = document.getElementById("supplier-contact").value.trim();
    const email = document.getElementById("supplier-email").value.trim();
    const foodId = document.getElementById("supplier-food").value;
    
    if (!name || !category || !contact || !email) {
      alert("Todos los campos (Nombre, Categoría, Contacto y Email) son obligatorios.");
      return;
    }
    if (!isValidEmail(email)) {
      alert("El correo debe tener un formato válido (ej. usuario@dominio.com).");
      return;
    }
    if (!isValidContact(contact)) {
      alert("El contacto solo puede contener el símbolo '+' y números, sin letras.");
      return;
    }
    
    const foodAssociations = foodId ? [{ id: foodId }] : [];
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ id: 0, name, category, contact, email, foodAssociations })
    });
    const message = await response.text();
    alert(message || "Proveedor registrado correctamente.");
    if (response.ok) {
      window.location.reload();
    }
  } catch (error) {
    console.error("Error al registrar proveedor:", error);
    alert("Ocurrió un error al registrar el proveedor.");
  }
}

// Limpiar campos del formulario de registro.
function resetSupplierForm() {
  document.getElementById("supplier-name").value = "";
  if (document.getElementById("supplier-category"))
    document.getElementById("supplier-category").selectedIndex = 0;
  document.getElementById("supplier-contact").value = "";
  document.getElementById("supplier-email").value = "";
  if (document.getElementById("supplier-food"))
    document.getElementById("supplier-food").selectedIndex = 0;
}

// Cargar la tabla de proveedores desde la API.
async function loadSupplierTable() {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error("Error al cargar proveedores.");
    const suppliers = await response.json();
    renderSupplierTable(suppliers);
  } catch (error) {
    console.error("Error al cargar proveedores:", error);
    clearTable();
  }
}

// Renderizar la tabla con proveedores y sus alimentos asociados.
// Se asume que la tabla tiene 6 columnas: Nombre, Categoría, Contacto, Email, Alimento‑Marca y Acciones.
function renderSupplierTable(suppliers) {
  const tableBody = document.querySelector("#crud-table tbody");
  if (suppliers.length === 0) {
    tableBody.innerHTML = "<tr><td colspan='6'>No hay proveedores registrados.</td></tr>";
    return;
  }
  tableBody.innerHTML = "";
  suppliers.forEach(supplier => {
    const foodList =
      supplier.foodAssociations && supplier.foodAssociations.length > 0
        ? supplier.foodAssociations.map(food => `<p>${food.type} - ${food.brand}</p>`).join("")
        : "<p>Sin alimento asociado</p>";
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${supplier.name}</td>
      <td>${supplier.category}</td>
      <td>${supplier.contact}</td>
      <td>${supplier.email}</td>
      <td>${foodList}</td>
      <td>
        <button onclick="editSupplier(${supplier.id})">Editar</button>
        <button onclick="deleteSupplier(${supplier.id})">Eliminar</button>
      </td>
    `;
    tableBody.appendChild(row);
  });
}

// Cargar los datos de un proveedor en el modal de edición.
async function editSupplier(id) {
  try {
    await loadCategoryOptions();
    await loadFoodOptions();
    const response = await fetch(`${API_URL}/${id}`);
    if (!response.ok) throw new Error("Error al obtener proveedor.");
    const supplier = await response.json();
    document.getElementById("edit-supplier-id").value = supplier.id;
    document.getElementById("edit-name").value = supplier.name;
    document.getElementById("edit-category").value = supplier.category;
    document.getElementById("edit-contact").value = supplier.contact;
    document.getElementById("edit-email").value = supplier.email;
    if (supplier.foodAssociations && supplier.foodAssociations.length > 0) {
      document.getElementById("edit-food").value = supplier.foodAssociations[0].id;
    } else {
      document.getElementById("edit-food").selectedIndex = 0;
    }
    openModal();
  } catch (error) {
    console.error("Error al cargar datos del proveedor:", error);
    alert("Ocurrió un error al cargar los datos del proveedor.");
  }
}

// Guardar cambios del proveedor editado, incluyendo alimento.
async function saveSupplier() {
  const id = document.getElementById("edit-supplier-id").value;
  const name = document.getElementById("edit-name").value.trim();
  const category = document.getElementById("edit-category").value;
  const contact = document.getElementById("edit-contact").value.trim();
  const email = document.getElementById("edit-email").value.trim();
  const foodId = document.getElementById("edit-food").value;
  
  if (!name || !category || !contact || !email) {
    alert("Todos los campos (Nombre, Categoría, Contacto y Email) son obligatorios.");
    return;
  }
  if (!isValidEmail(email)) {
    alert("El correo debe tener un formato válido (ej. usuario@dominio.com).");
    return;
  }
  if (!isValidContact(contact)) {
    alert("El contacto solo puede contener el símbolo '+' y números, sin letras.");
    return;
  }
  
  try {
    const foodAssociations = foodId ? [{ id: foodId }] : [];
    const response = await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ id, name, category, contact, email, foodAssociations })
    });
    if (!response.ok) {
      const errorMsg = await response.text();
      throw new Error(errorMsg || "Error desconocido al actualizar proveedor.");
    }
    const message = await response.text();
    alert(message || "Proveedor actualizado correctamente.");
    window.location.reload();
  } catch (error) {
    console.error("Error al actualizar proveedor:", error);
    alert("Ocurrió un error al actualizar el proveedor: " + (error.message || "Error desconocido."));
  }
}

// Eliminar un proveedor.
async function deleteSupplier(id) {
  if (!confirm("¿Estás seguro de que deseas eliminar este proveedor?")) return;
  try {
    const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    const result = await response.text();
    alert(result);
    window.location.reload();
  } catch (error) {
    console.error("Error al eliminar proveedor:", error);
    alert("Ocurrió un error al eliminar el proveedor.");
  }
}

// Mostrar alimentos asociados cuando un proveedor no pueda eliminarse.
function showAssociatedFoods(foods) {
  const section = document.getElementById("associated-foods-section");
  const tableBody = document.querySelector("#associated-foods tbody");
  tableBody.innerHTML = foods.map(food => `
    <tr>
      <td>${food.type}</td>
      <td>${food.brand}</td>
      <td>${food.supplier ? food.supplier.name : "Sin proveedor"}</td>
    </tr>
  `).join("");
  section.style.display = "block";
}

// Configurar eventos para la tecla "Enter" en formularios.
function setupEnterKeyEvents() {
  const crudForm = document.getElementById("crud-form");
  if (crudForm) {
    crudForm.addEventListener("keypress", function (event) {
      if (event.key === "Enter") {
        event.preventDefault();
        registerSupplier();
      }
    });
  }
  const editForm = document.getElementById("edit-form");
  if (editForm) {
    editForm.addEventListener("keypress", function (event) {
      if (event.key === "Enter") {
        event.preventDefault();
        saveSupplier();
      }
    });
  }
}

// Abrir y cerrar el modal de edición.
function openModal() {
  const modal = document.getElementById("edit-modal");
  if (modal) {
    modal.style.display = "flex";
  }
}

function closeModal() {
  const modal = document.getElementById("edit-modal");
  if (modal) {
    modal.style.display = "none";
  }
}

// Vaciar la tabla cuando no existan resultados o se produzca un error.
function clearTable() {
  const tableBody = document.querySelector("#crud-table tbody");
  tableBody.innerHTML = "<tr><td colspan='6'>No hay proveedores que coincidan con el filtro.</td></tr>";
}
