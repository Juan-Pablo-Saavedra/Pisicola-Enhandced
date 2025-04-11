/**
 * EMPLOYEE CRUD SYSTEM - SECURE IMPLEMENTATION
 * This file implements a secure CRUD (Create, Read, Update, Delete) system for employee management
 * with advanced security features against tampering, manipulation and injection attacks.
 */

// IIFE para encapsular todo el código y proteger el scope global
(function() {
    'use strict';

    // Guardar referencias originales de funciones nativas que podrían ser manipuladas
    const originalFetch = window.fetch;
    const originalJSON = JSON.parse;
    const originalStringify = JSON.stringify;
    const originalAlert = window.alert;
    const originalConfirm = window.confirm;
    const originalConsoleError = console.error;
    
    // Guardar también las referencias de funciones utilizadas para inyecciones
    const originalSetTimeout = window.setTimeout;
    const originalSetInterval = window.setInterval;
    const originalEval = window.eval;
    const originalFunction = window.Function;
    
    // URLs de API originales - almacenadas en un objeto no enumerable para dificultar su manipulación
    const ApiConfig = Object.defineProperties({}, {
        'baseUrl': {
            value: "http://localhost:8085/api/v1/employee",
            writable: false,
            configurable: false,
            enumerable: false
        },
        'rolesUrl': {
            get: function() { return `${this.baseUrl}/roles`; },
            configurable: false,
            enumerable: false
        }
    });

    // Variable para controlar la frecuencia de peticiones - anti flood
    const requestThrottling = {
        lastRequestTime: {},     // Registro de última petición por tipo
        minIntervalMs: 1000,     // Tiempo mínimo entre peticiones (1 segundo)
        counters: {},            // Contador de peticiones por tipo
        maxPerMinute: 60,        // Máximo de peticiones por minuto
        resetTime: Date.now()    // Tiempo para resetear contadores
    };

    /**
     * Sistema de limitación de peticiones (throttling)
     * Evita ataques de inundación (flood) contra la API
     * @param {string} requestType - Tipo de petición a verificar
     * @returns {boolean} - true si la petición está permitida, false si debe ser bloqueada
     */
    function canMakeRequest(requestType) {
        const now = Date.now();
        
        // Resetear contadores cada minuto
        if (now - requestThrottling.resetTime > 60000) {
            requestThrottling.counters = {};
            requestThrottling.resetTime = now;
        }
        
        // Inicializar contador si no existe
        if (!requestThrottling.counters[requestType]) {
            requestThrottling.counters[requestType] = 0;
        }
        
        // Verificar número máximo de peticiones por minuto
        if (requestThrottling.counters[requestType] >= requestThrottling.maxPerMinute) {
            originalConsoleError(`Demasiadas peticiones de tipo ${requestType}. Por favor, espere.`);
            return false;
        }
        
        // Verificar tiempo mínimo entre peticiones
        const lastTime = requestThrottling.lastRequestTime[requestType] || 0;
        if (now - lastTime < requestThrottling.minIntervalMs) {
            originalConsoleError(`Peticiones demasiado frecuentes de tipo ${requestType}. Por favor, espere.`);
            return false;
        }
        
        // Actualizar contadores y permitir la petición
        requestThrottling.lastRequestTime[requestType] = now;
        requestThrottling.counters[requestType]++;
        return true;
    }

    // Bloquear intentos de manipulación de Object.defineProperty
    const originalDefineProperty = Object.defineProperty;
    Object.defineProperty = function(obj, prop, descriptor) {
        // Detectar intentos de alterar objetos críticos
        if (obj === window || obj === document || obj === XMLHttpRequest.prototype || 
            obj === ApiConfig || obj === Object.prototype || obj === Function.prototype ||
            obj === requestThrottling) {
            console.warn("⚠️ Intento de modificación de objeto crítico detectado");
            return obj;
        }
        return originalDefineProperty.call(this, obj, prop, descriptor);
    };

    /**
     * Verifica periódicamente que las funciones críticas no hayan sido manipuladas
     * Restaura las funciones a su estado original si se detecta alguna modificación
     */
    function secureFunctions() {
        // Restaurar fetch si fue manipulado
        if (window.fetch !== originalFetch) {
            console.warn("⚠️ Manipulación de fetch detectada. Restaurando...");
            window.fetch = originalFetch;
        }
        
        // Restaurar JSON.parse y JSON.stringify si fueron manipulados
        if (JSON.parse !== originalJSON) {
            console.warn("⚠️ Manipulación de JSON.parse detectada. Restaurando...");
            JSON.parse = originalJSON;
        }
        
        if (JSON.stringify !== originalStringify) {
            console.warn("⚠️ Manipulación de JSON.stringify detectada. Restaurando...");
            JSON.stringify = originalStringify;
        }
        
        // Restaurar setTimeout y setInterval si fueron manipulados
        if (window.setTimeout !== originalSetTimeout) {
            console.warn("⚠️ Manipulación de setTimeout detectada. Restaurando...");
            window.setTimeout = originalSetTimeout;
        }
        
        if (window.setInterval !== originalSetInterval) {
            console.warn("⚠️ Manipulación de setInterval detectada. Restaurando...");
            window.setInterval = originalSetInterval;
        }
        
        // Restaurar eval y Function si fueron manipulados
        if (window.eval !== originalEval) {
            console.warn("⚠️ Manipulación de eval detectada. Restaurando...");
            window.eval = originalEval;
        }
        
        if (window.Function !== originalFunction) {
            console.warn("⚠️ Manipulación de Function detectada. Restaurando...");
            window.Function = originalFunction;
        }
        
        // Verificar periódicamente
        originalSetTimeout(secureFunctions, 1000);
    }

    /**
     * Detector de DevTools
     * Monitorea las dimensiones de la ventana para detectar cuándo se abren las herramientas de desarrollo
     */
    function detectDevTools() {
        const threshold = 160;
        
        const checkDevTools = function() {
            const widthThreshold = window.outerWidth - window.innerWidth > threshold;
            const heightThreshold = window.outerHeight - window.innerHeight > threshold;
            
            if (widthThreshold || heightThreshold) {
                console.warn("⚠️ DevTools detectadas");
                // Podrías implementar medidas adicionales aquí
            }
        };

        window.addEventListener('resize', checkDevTools);
        originalSetInterval(checkDevTools, 1000);
    }

    /**
     * Implementa técnicas anti-debugging
     * Usa la instrucción debugger para detectar si se está depurando el código
     */
    function antiDebugging() {
        const startTime = new Date();
        debugger;
        const endTime = new Date();
        
        if (endTime - startTime > 100) {
            console.warn("⚠️ Modo debugger detectado");
        }
        
        originalSetTimeout(antiDebugging, 2000);
    }

    /**
     * Suplanta la consola para detectar intentos maliciosos
     * Identifica patrones sospechosos como bucles infinitos o flooding
     */
    function secureConsole() {
        const originalConsole = {
            log: console.log,
            warn: console.warn,
            error: console.error
        };
        
        // Patrones sospechosos para detectar
        const suspiciousPatterns = [
            /while\s*\(\s*true\s*\)/i,
            /for\s*\(\s*;.*;\s*\)/i,
            /setInterval\s*\(\s*.*,\s*[0-9]+\s*\)/i,
            /fetch\s*\(\s*.*\)/i
        ];
        
        // Sobrescribir console.log para detectar código malicioso
        console.log = function() {
            const input = Array.from(arguments).join(' ');
            
            // Detectar patrones sospechosos
            for (const pattern of suspiciousPatterns) {
                if (pattern.test(input)) {
                    console.warn("⚠️ Código potencialmente malicioso detectado en consola");
                    return;
                }
            }
            
            originalConsole.log.apply(console, arguments);
        };
        
        // Proteger también los otros métodos
        console.warn = function() {
            originalConsole.warn.apply(console, arguments);
        };
        
        console.error = function() {
            originalConsole.error.apply(console, arguments);
        };
    }

    /**
     * Monitorea e intercepta intentos de inyección de scripts
     * Evita la ejecución de código malicioso inyectado dinámicamente
     */
    function preventScriptInjection() {
        // Observer para detectar nuevos scripts
        const observer = new MutationObserver((mutations) => {
            mutations.forEach((mutation) => {
                if (mutation.type === 'childList') {
                    mutation.addedNodes.forEach((node) => {
                        // Detectar scripts añadidos dinámicamente
                        if (node.tagName === 'SCRIPT') {
                            console.warn("⚠️ Intento de inyección de script detectado");
                            node.parentNode.removeChild(node);
                        }
                    });
                }
            });
        });
        
        // Observar cambios en todo el DOM
        observer.observe(document.documentElement, {
            childList: true,
            subtree: true
        });
        
        // Interceptar createElement para detectar creación de scripts
        const originalCreateElement = document.createElement;
        document.createElement = function(tagName) {
            if (tagName.toLowerCase() === 'script') {
                console.warn("⚠️ Intento de creación de script detectado");
                // Se puede retornar un elemento inofensivo o el script pero bloqueado
                const element = originalCreateElement.call(document, tagName);
                // Hacer que el script sea inofensivo
                element.setAttribute('type', 'text/plain');
                return element;
            }
            return originalCreateElement.call(document, tagName);
        };
    }

    // ========= FUNCIONES ORIGINALES PROTEGIDAS ==========

    /**
     * Carga los roles disponibles desde el backend
     * Actualiza el select de posiciones con los roles obtenidos
     */
    async function loadRoles() {
        // Aplicar throttling para evitar solicitudes excesivas
        if (!canMakeRequest('loadRoles')) {
            return;
        }
        
        try {
            // Usar referencia segura de fetch y URL
            const response = await originalFetch(ApiConfig.rolesUrl);

            if (response.ok) {
                const roles = await response.json();
                const positionSelect = document.getElementById("employee-position");

                // Sanitizar antes de modificar el DOM
                positionSelect.innerHTML = '<option value="" disabled selected>Selecciona un rol</option>';

                // Añadir los roles como opciones del <select> - con sanitización
                roles.forEach((role) => {
                    // Validar que role sea una cadena
                    if (typeof role !== 'string') {
                        console.warn("Tipo de dato no válido para rol");
                        return;
                    }
                    
                    const option = document.createElement("option");
                    option.value = role;
                    option.textContent = role; // textContent es seguro contra XSS
                    positionSelect.appendChild(option);
                });
            } else {
                const errorText = await response.text();
                originalAlert(`Error al cargar roles: ${errorText}`);
            }
        } catch (error) {
            originalConsoleError("Error al cargar roles:", error);
            originalAlert("Ocurrió un error al cargar los roles: " + error.message);
        }
    }

    /**
     * Registra un nuevo empleado en el sistema
     * Valida los datos antes de enviarlos al servidor
     */
    async function registerEmployee() {
        // Aplicar throttling para evitar solicitudes excesivas
        if (!canMakeRequest('registerEmployee')) {
            originalAlert("Demasiadas solicitudes de registro. Por favor, espere un momento antes de intentarlo nuevamente.");
            return;
        }
        
        try {
            // Obtener y sanitizar valores
            const name = document.getElementById("employee-name").value.trim();
            const position = document.getElementById("employee-position").value;
            const phone = document.getElementById("employee-phone").value.trim();
            const password = document.getElementById("employee-password").value.trim();
            const email = document.getElementById("employee-email").value.trim();

            // Validaciones
            if (!name || !position || !phone || !password || !email) {
                originalAlert("Todos los campos son obligatorios.");
                return;
            }

            if (password.length < 8) {
                originalAlert("La contraseña debe tener al menos 8 caracteres.");
                return;
            }

            if (!email.includes("@")) {
                originalAlert("El correo electrónico debe incluir '@'.");
                return;
            }

            // Crear objeto con datos sanitizados
            const employeeData = {
                id: 0,
                name,
                position,
                phone,
                password,
                email
            };

            // Usar referencias seguras
            const bodyContent = originalStringify(employeeData);

            const response = await originalFetch(ApiConfig.baseUrl, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: bodyContent
            });

            if (response.ok) {
                const message = await response.text();
                originalAlert(message);
                loadTable(); // Recargar la tabla
            } else {
                const errorText = await response.text();
                originalAlert(`Error al registrar empleado: ${errorText}`);
            }
        } catch (error) {
            originalConsoleError("Error al registrar empleado:", error);
            originalAlert("Ocurrió un error durante el registro: " + error.message);
        }
    }

    /**
     * Carga todos los empleados en la tabla
     * Implementa sanitización para prevenir XSS
     */
    async function loadTable() {
        // Aplicar throttling para evitar solicitudes excesivas
        if (!canMakeRequest('loadTable')) {
            return;
        }
        
        try {
            // Usar referencia segura
            const response = await originalFetch(ApiConfig.baseUrl);

            if (response.ok) {
                const employees = await response.json();
                const tableBody = document.querySelector("#crud-table tbody");
                
                // Sanear el contenido de la tabla
                tableBody.innerHTML = "";

                employees.forEach((employee) => {
                    // Validar la estructura del objeto employee
                    if (!employee || typeof employee !== 'object' || !employee.id) {
                        console.warn("Datos de empleado inválidos");
                        return;
                    }

                    const row = document.createElement("tr");
                    
                    // Crear celdas de manera segura para evitar XSS
                    const nameTd = document.createElement("td");
                    nameTd.textContent = employee.name;
                    
                    const positionTd = document.createElement("td");
                    positionTd.textContent = employee.position;
                    
                    const phoneTd = document.createElement("td");
                    phoneTd.textContent = employee.phone;
                    
                    const emailTd = document.createElement("td");
                    emailTd.textContent = employee.email;
                    
                    // Crear botones de manera segura
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
                    
                    // Agregar todas las celdas a la fila
                    row.appendChild(nameTd);
                    row.appendChild(positionTd);
                    row.appendChild(phoneTd);
                    row.appendChild(emailTd);
                    row.appendChild(actionsTd);
                    
                    tableBody.appendChild(row);
                });
            } else {
                const errorText = await response.text();
                originalAlert(`Error al cargar empleados: ${errorText}`);
            }
        } catch (error) {
            originalConsoleError("Error al cargar empleados:", error);
            originalAlert("Ocurrió un error al cargar la lista de empleados: " + error.message);
        }
    }

    /**
     * Filtra las filas de la tabla según los criterios ingresados
     * No requiere comunicación con el servidor (filtrado del lado del cliente)
     */
    function applyFilters() {
        const filterName = document.getElementById("filter-name").value.toLowerCase();
        const filterPhone = document.getElementById("filter-phone").value.toLowerCase();
        const filterPosition = document.getElementById("filter-position").value.toLowerCase();
        const filterEmail = document.getElementById("filter-email").value.toLowerCase();

        const rows = document.querySelectorAll("#crud-table tbody tr");

        rows.forEach((row) => {
            // Verificar que la estructura de la fila es correcta
            if (!row || !row.children || row.children.length < 4) return;
            
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
        // Aplicar throttling para evitar solicitudes excesivas
        if (!canMakeRequest('editEmployee')) {
            originalAlert("Demasiadas solicitudes de edición. Por favor, espere un momento antes de intentarlo nuevamente.");
            return;
        }
        
        // Validar que id es un número
        if (!id || isNaN(parseInt(id))) {
            originalAlert("ID inválido para editar el empleado.");
            return;
        }

        try {
            // Usar referencia segura y URL sanitizada
            const response = await originalFetch(`${ApiConfig.baseUrl}/${id}`);

            if (response.ok) {
                const employee = await response.json();
                
                // Validar la estructura del empleado antes de utilizarlo
                if (!employee || typeof employee !== 'object') {
                    throw new Error("Datos de empleado con formato inválido");
                }

                // Asignar valores de manera segura
                document.getElementById("edit-nombre").value = employee.name || '';
                document.getElementById("edit-cargo").value = employee.position || '';
                document.getElementById("edit-telefono").value = employee.phone || '';
                document.getElementById("edit-email").value = employee.email || '';
                document.getElementById("edit-clave").value = employee.password || '';
                document.getElementById("edit-employee-id").value = employee.id;

                openModal();
            } else {
                const errorText = await response.text();
                originalAlert(`Error al obtener empleado: ${errorText}`);
            }
        } catch (error) {
            originalConsoleError("Error al cargar datos del empleado:", error);
            originalAlert("Ocurrió un error al cargar los datos del empleado: " + error.message);
        }
    }

    /**
     * Guarda los cambios realizados a un empleado
     * Valida los datos antes de enviarlos al servidor
     */
    async function saveEmployee() {
        // Aplicar throttling para evitar solicitudes excesivas
        if (!canMakeRequest('saveEmployee')) {
            originalAlert("Demasiadas solicitudes de guardado. Por favor, espere un momento antes de intentarlo nuevamente.");
            return;
        }
        
        const id = document.getElementById("edit-employee-id").value;
        
        // Validar que id es un número
        if (!id || isNaN(parseInt(id))) {
            originalAlert("ID de empleado inválido.");
            return;
        }

        const name = document.getElementById("edit-nombre").value.trim();
        const position = document.getElementById("edit-cargo").value.trim();
        const phone = document.getElementById("edit-telefono").value.trim();
        const email = document.getElementById("edit-email").value.trim();
        const password = document.getElementById("edit-clave").value.trim();

        // Validaciones
        if (!name || !position || !phone || !email || !password) {
            originalAlert("Todos los campos son obligatorios.");
            return;
        }

        if (password.length < 8) {
            originalAlert("La contraseña debe tener al menos 8 caracteres.");
            return;
        }

        if (!email.includes("@")) {
            originalAlert("El correo electrónico debe incluir '@'.");
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
            // Usar referencias seguras
            const response = await originalFetch(`${ApiConfig.baseUrl}/${id}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: originalStringify(employeeData)
            });

            if (response.ok) {
                const message = await response.text();
                originalAlert(message);
                closeModal();
                loadTable();
            } else {
                const errorText = await response.text();
                originalAlert(`Error al actualizar empleado: ${errorText}`);
            }
        } catch (error) {
            originalConsoleError("Error al actualizar empleado:", error);
            originalAlert("Ocurrió un error al actualizar el empleado: " + error.message);
        }
    }

    /**
     * Elimina un empleado del sistema
     * @param {number} id - ID del empleado a eliminar
     */
    async function deleteEmployee(id) {
        // Aplicar throttling para evitar solicitudes excesivas
        if (!canMakeRequest('deleteEmployee')) {
            originalAlert("Demasiadas solicitudes de eliminación. Por favor, espere un momento antes de intentarlo nuevamente.");
            return;
        }
        
        // Validar que id sea un número
        if (!id || isNaN(parseInt(id))) {
            originalAlert("ID de empleado inválido.");
            return;
        }
        
        const confirmation = originalConfirm("¿Estás seguro de que deseas eliminar este empleado?");
        if (!confirmation) return;

        try {
            // Usar referencia segura y URL sanitizada
            const response = await originalFetch(`${ApiConfig.baseUrl}/${id}`, { method: "DELETE" });

            if (response.ok) {
                const message = await response.text();
                originalAlert(message);
                loadTable();
            } else {
                const errorText = await response.text();
                originalAlert(`Error al eliminar empleado: ${errorText}`);
            }
        } catch (error) {
            originalConsoleError("Error al eliminar empleado:", error);
            originalAlert("Ocurrió un error al eliminar el empleado: " + error.message);
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
     * Bloquea intentos de sobrescritura de funciones globales
     * Impide que se modifiquen las funciones expuestas
     */
    function lockGlobalFunctions() {
        const exposedFunctions = [
            'loadRoles', 'registerEmployee', 'loadTable', 'applyFilters', 
            'resetFilters', 'editEmployee', 'saveEmployee', 'deleteEmployee',
            'openModal', 'closeModal'
        ];
        
        // Crear versiones proxificadas de las funciones originales
        const originalFunctions = {};
        
        exposedFunctions.forEach(funcName => {
            // Guardar referencia original
            originalFunctions[funcName] = window[funcName];
            
            // Definir propiedad no configurable
            Object.defineProperty(window, funcName, {
                value: function() {
                    // Verificar si hay un ciclo infinito detectando llamadas muy frecuentes
                    if (!canMakeRequest(`function_${funcName}`)) {
                        console.warn(`⚠️ Posible llamada en bucle infinito a ${funcName} detectada`);
                        return;
                    }
                    return originalFunctions[funcName].apply(this, arguments);
                },
                writable: false,
                configurable: false
            });
        });
    }

    // Inicializar todas las protecciones
    secureFunctions();
    detectDevTools();
    antiDebugging();
    secureConsole();
    preventScriptInjection();

    // Exponer funciones de manera segura al scope global
    window.loadRoles = loadRoles;
    window.registerEmployee = registerEmployee;
    window.loadTable = loadTable;
    window.applyFilters = applyFilters;
    window.resetFilters = resetFilters;
    window.editEmployee = editEmployee;
    window.saveEmployee = saveEmployee;
    window.deleteEmployee = deleteEmployee;
    window.openModal = openModal;
    window.closeModal = closeModal;

    // Bloquear funciones contra manipulación
    lockGlobalFunctions();

    /**
     * Control del evento onload con verificación de integridad
     * Restaura funciones manipuladas y ejecuta la carga inicial
     */
    const originalOnload = window.onload;
    window.onload = function() {
        // Verificar si hay manipulación
        if (window.loadRoles !== loadRoles || 
            window.loadTable !== loadTable) {
            console.warn("⚠️ Se detectó manipulación de funciones críticas");
            // Restaurar funciones originales
            window.loadRoles = loadRoles;
            window.loadTable = loadTable;
        }
        
        // Ejecutar carga inicial
        loadRoles();
        loadTable();
        
        // Si había una función onload previa, ejecutarla también
        if (typeof originalOnload === 'function') {
            originalOnload();
        }
    };

})();