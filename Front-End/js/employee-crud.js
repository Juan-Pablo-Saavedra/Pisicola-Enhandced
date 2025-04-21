// Versión actualizada con select de roles en el modal de edición
(function() {
    'use strict';
    
    // URLs de API
    const ApiConfig = {
        baseUrl: "http://localhost:8085/api/v1/employee",
        get rolesUrl() { return `${this.baseUrl}/roles`; }
    };

    /**
     * Carga los roles disponibles desde el backend para el modal de edición
     */
    async function loadRoles() {
        try {
            const response = await fetch(ApiConfig.rolesUrl);

            if (response.ok) {
                const roles = await response.json();
                const positionSelect = document.getElementById("edit-cargo");

                // Guardar la opción seleccionada actual si existe
                const currentSelected = positionSelect.value;
                
                // Limpiar el select manteniendo solo la opción por defecto
                positionSelect.innerHTML = '<option value="" disabled selected>Selecciona un rol</option>';

                // Agregar las opciones de roles
                roles.forEach((role) => {
                    const option = document.createElement("option");
                    option.value = role;
                    option.textContent = role;
                    positionSelect.appendChild(option);
                });

                // Si había un valor seleccionado, restaurarlo
                if (currentSelected) {
                    positionSelect.value = currentSelected;
                }
                
                return roles;
            } else {
                const errorText = await response.text();
                alert(`Error al cargar roles: ${errorText}`);
                return [];
            }
        } catch (error) {
            console.error("Error al cargar roles:", error);
            alert("Ocurrió un error al cargar los roles: " + error.message);
            return [];
        }
    }

    /**
     * Carga todos los empleados en la tabla
     */
    async function loadTable() {
        try {
            const response = await fetch(ApiConfig.baseUrl);

            if (response.ok) {
                const employees = await response.json();
                const tableBody = document.querySelector("#crud-table tbody");
                
                tableBody.innerHTML = "";

                employees.forEach((employee) => {
                    const row = document.createElement("tr");
                    
                    const nameTd = document.createElement("td");
                    nameTd.textContent = employee.name;
                    
                    const positionTd = document.createElement("td");
                    positionTd.textContent = employee.position;
                    
                    const phoneTd = document.createElement("td");
                    phoneTd.textContent = employee.phone;
                    
                    const emailTd = document.createElement("td");
                    emailTd.textContent = employee.email;
                    
                    const actionsTd = document.createElement("td");
                    
                    const editButton = document.createElement("button");
                    editButton.textContent = "Editar";
                    editButton.addEventListener('click', () => editEmployee(employee.id));
                    
                    const deleteButton = document.createElement("button");
                    deleteButton.textContent = "Eliminar";
                    deleteButton.addEventListener('click', () => deleteEmployee(employee.id));
                    
                    actionsTd.appendChild(editButton);
                    actionsTd.appendChild(document.createTextNode(" "));
                    actionsTd.appendChild(deleteButton);
                    
                    row.appendChild(nameTd);
                    row.appendChild(positionTd);
                    row.appendChild(phoneTd);
                    row.appendChild(emailTd);
                    row.appendChild(actionsTd);
                    
                    tableBody.appendChild(row);
                });
            } else {
                const errorText = await response.text();
                alert(`Error al cargar empleados: ${errorText}`);
            }
        } catch (error) {
            console.error("Error al cargar empleados:", error);
            alert("Ocurrió un error al cargar la lista de empleados: " + error.message);
        }
    }

    /**
     * Filtra las filas de la tabla según los criterios ingresados
     */
    function applyFilters() {
        const filterName = document.getElementById("filter-name").value.toLowerCase();
        const filterPhone = document.getElementById("filter-phone").value.toLowerCase();
        const filterPosition = document.getElementById("filter-position").value.toLowerCase();
        const filterEmail = document.getElementById("filter-email").value.toLowerCase();

        const rows = document.querySelectorAll("#crud-table tbody tr");

        rows.forEach((row) => {
            const name = row.children[0].textContent.toLowerCase();
            const position = row.children[1].textContent.toLowerCase();
            const phone = row.children[2].textContent.toLowerCase();
            const email = row.children[3].textContent.toLowerCase();

            const matches =
                name.includes(filterName) &&
                phone.includes(filterPhone) &&
                position.includes(filterPosition) &&
                email.includes(filterEmail);

            row.style.display = matches ? "" : "none";
        });
    }

    /**
     * Restablece los filtros de la tabla y muestra todas las filas
     */
    function resetFilters() {
        const filterForm = document.getElementById("filter-form");
        if (filterForm) {
            filterForm.reset();
        }

        const rows = document.querySelectorAll("#crud-table tbody tr");
        rows.forEach((row) => {
            row.style.display = "";
        });
    }

    /**
     * Carga los datos de un empleado para su edición
     * @param {number} id - ID del empleado a editar
     */
    async function editEmployee(id) {
        try {
            // Primero cargar los roles para el select
            await loadRoles();
            
            const response = await fetch(`${ApiConfig.baseUrl}/${id}`);

            if (response.ok) {
                const employee = await response.json();
                
                document.getElementById("edit-nombre").value = employee.name;
                document.getElementById("edit-telefono").value = employee.phone;
                document.getElementById("edit-email").value = employee.email;
                document.getElementById("edit-clave").value = employee.password;
                document.getElementById("edit-employee-id").value = employee.id;
                
                // Seleccionar el rol actual del empleado en el select
                const cargoSelect = document.getElementById("edit-cargo");
                cargoSelect.value = employee.position;

                openModal();
            } else {
                const errorText = await response.text();
                alert(`Error al obtener empleado: ${errorText}`);
            }
        } catch (error) {
            console.error("Error al cargar datos del empleado:", error);
            alert("Ocurrió un error al cargar los datos del empleado: " + error.message);
        }
    }

    /**
     * Guarda los cambios realizados a un empleado
     */
    async function saveEmployee() {
        const id = document.getElementById("edit-employee-id").value;
        const name = document.getElementById("edit-nombre").value.trim();
        const position = document.getElementById("edit-cargo").value;
        const phone = document.getElementById("edit-telefono").value.trim();
        const email = document.getElementById("edit-email").value.trim();
        const password = document.getElementById("edit-clave").value.trim();

        // Validaciones
        if (!name || !position || !phone || !email || !password) {
            alert("Todos los campos son obligatorios.");
            return;
        }

        if (password.length < 8) {
            alert("La contraseña debe tener al menos 8 caracteres.");
            return;
        }

        if (!email.includes("@")) {
            alert("El correo electrónico debe incluir '@'.");
            return;
        }

        const employeeData = { 
            id: parseInt(id), 
            name, 
            position, 
            phone, 
            email, 
            password 
        };

        try {
            const response = await fetch(`${ApiConfig.baseUrl}/${id}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(employeeData)
            });

            if (response.ok) {
                const message = await response.text();
                alert(message);
                closeModal();
                loadTable();
            } else {
                const errorText = await response.text();
                alert(`Error al actualizar empleado: ${errorText}`);
            }
        } catch (error) {
            console.error("Error al actualizar empleado:", error);
            alert("Ocurrió un error al actualizar el empleado: " + error.message);
        }
    }

    /**
     * Elimina un empleado del sistema
     * @param {number} id - ID del empleado a eliminar
     */
    async function deleteEmployee(id) {
        const confirmation = confirm("¿Estás seguro de que deseas eliminar este empleado?");
        if (!confirmation) return;

        try {
            const response = await fetch(`${ApiConfig.baseUrl}/${id}`, { method: "DELETE" });

            if (response.ok) {
                const message = await response.text();
                alert(message);
                loadTable();
            } else {
                const errorText = await response.text();
                alert(`Error al eliminar empleado: ${errorText}`);
            }
        } catch (error) {
            console.error("Error al eliminar empleado:", error);
            alert("Ocurrió un error al eliminar el empleado: " + error.message);
        }
    }

    /**
     * Muestra el modal de edición
     */
    function openModal() {
        const modal = document.getElementById("edit-modal");
        if (modal) modal.style.display = "flex";
    }

    /**
     * Cierra el modal de edición y restablece el formulario
     */
    function closeModal() {
        const modal = document.getElementById("edit-modal");
        if (modal) modal.style.display = "none";
        
        const form = document.getElementById("edit-form");
        if (form) form.reset();
    }

    /**
     * Funciones adicionales para el menú de navegación
     */
    function toggleDropdown() {
        const dropdownMenu = document.querySelector('.dropdown-menu');
        dropdownMenu.classList.toggle('show');
    }

    function toggleMenu() {
        const navMenu = document.querySelector('.nav-menu');
        const hamburger = document.querySelector('.hamburger');
        navMenu.classList.toggle('active');
        hamburger.classList.toggle('active');
    }

    // Exponer funciones al scope global
    window.loadTable = loadTable;
    window.applyFilters = applyFilters;
    window.resetFilters = resetFilters;
    window.editEmployee = editEmployee;
    window.saveEmployee = saveEmployee;
    window.deleteEmployee = deleteEmployee;
    window.openModal = openModal;
    window.closeModal = closeModal;
    window.toggleDropdown = toggleDropdown;
    window.toggleMenu = toggleMenu;

    // Evento onload para iniciar la aplicación
    window.onload = function() {
        loadTable();
    };
})();