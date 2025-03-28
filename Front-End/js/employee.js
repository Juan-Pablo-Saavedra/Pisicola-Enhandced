function registerEmployee() {
    return new Promise(async (resolve) => {
        // alert("hola");
        let headersList = {
            "Accept": "*/*",
            "User-Agent": "web",
            "Content-Type": "application/json"
        }

        let bodyContent = JSON.stringify({
            "id": 0,
            "name": document.getElementById("nombre").value,
            "position": document.getElementById("cargo").value,
            "phone": document.getElementById("telefono").value,
            "password": document.getElementById("clave").value,
            "email": document.getElementById("email").value
        });

        let response = await fetch("http://localhost:8085/api/v1/employee", {
            method: "POST",
            body: bodyContent,
            headers: headersList
        });

        let data = await response.text();
        console.log(data);

    });
}