// Animaciones
const botonRegistro = document.getElementById("register");
const botonInicioSesion = document.getElementById("login");
const contenedor = document.getElementById("container");

botonRegistro.addEventListener("click", () => {
  contenedor.classList.add("right-panel-active");
});

botonInicioSesion.addEventListener("click", () => {
  contenedor.classList.remove("right-panel-active");
});

// Verificación de errores en el registro
const formulario = document.querySelector("form");
const nombreUsuario = document.getElementById("nombre");
const cargo = document.getElementById("cargo");
const telefono = document.getElementById("telefono");
const correo = document.getElementById("email");
const contraseña = document.getElementById("clave");

const errorNombreUsuario = document.querySelector("#username-error");
const errorCorreo = document.querySelector("#email-error");
const errorContraseña = document.querySelector("#password-error");

// Validar email con regex
const validarCorreo = (correo) =>
  /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(correo);

// Validar campo y mostrar mensaje de error
const validarCampo = (input, errorElemento, mensaje, condicion) => {
  errorElemento.textContent = condicion ? mensaje : "";
};

// Eventos para validar en tiempo real
nombreUsuario.addEventListener("input", () => {
  validarCampo(
    nombreUsuario,
    errorNombreUsuario,
    "*El nombre debe tener entre 4 y 20 caracteres",
    nombreUsuario.value.length < 4 || nombreUsuario.value.length > 20
  );
});

correo.addEventListener("input", () => {
  validarCampo(correo, errorCorreo, "*El correo no es válido", !validarCorreo(correo.value));
});

contraseña.addEventListener("input", () => {
  validarCampo(
    contraseña,
    errorContraseña,
    "*La contraseña debe tener entre 8 y 20 caracteres",
    contraseña.value.length < 8 || contraseña.value.length > 20
  );
});

// Verificar campos obligatorios
const verificarCamposRequeridos = (inputs) => {
  inputs.forEach((input) => {
    if (input.value.trim() === "") {
      const errorElemento = input.nextElementSibling;
      errorElemento.textContent = "*Campo obligatorio";
    }
  });
};

// Evento para enviar formulario de registro
formulario.addEventListener("submit", (e) => {
  e.preventDefault();
  verificarCamposRequeridos([nombreUsuario, cargo, telefono, correo, contraseña]);
});

// Verificación de errores en el inicio de sesión
const formularioLogin = document.querySelector(".form-lg");
const correoLogin = document.querySelector(".email-2");
const contraseñaLogin = document.querySelector(".password-2");

const errorCorreoLogin = document.querySelector(".email-error-2");
const errorContraseñaLogin = document.querySelector(".password-error-2");

correoLogin.addEventListener("input", () => {
  validarCampo(
    correoLogin,
    errorCorreoLogin,
    "*El correo no es válido",
    !validarCorreo(correoLogin.value)
  );
});

contraseñaLogin.addEventListener("input", () => {
  validarCampo(
    contraseñaLogin,
    errorContraseñaLogin,
    "*La contraseña debe tener entre 8 y 20 caracteres",
    contraseñaLogin.value.length < 8 || contraseñaLogin.value.length > 20
  );
});

// Evento para enviar formulario de inicio de sesión
formularioLogin.addEventListener("submit", (e) => {
  e.preventDefault();
  verificarCamposRequeridos([correoLogin, contraseñaLogin]);
});