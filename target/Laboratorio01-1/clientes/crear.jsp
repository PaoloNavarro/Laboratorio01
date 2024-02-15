<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<title>Crear cliente</title>
<%@ include file="/layout/header.jsp" %>
<style>
    /* Agrega estilos CSS aquí */
    form {
        max-width: 900px;
        margin: 0 auto;
        padding: 20px;
        border: 1px solid #ccc;
        border-radius: 5px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    label {
        font-weight: bold;
    }

    input[type="text"],
    input[type="number"],
    input[type="url"],
    select,
    textarea {
        width: 100%;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
    }

    input[type="checkbox"] {
        margin-top: 5px;
    }

    input[type="submit"] {
        display: block;
        width: 100%;
        padding: 10px;
        background-color: #007bff;
        color: #fff;
        border: none;
        border-radius: 5px;
        text-align: center;
        text-decoration: none;
        font-weight: bold;
    }
</style>

<form action="clientescrear" method="post" onsubmit="return validarFormulario()">
    <h1>Crear cliente</h1>

    <label for="nombre">Nombre:</label>
    <input type="text" id="nombre" name="nombre" required pattern="[A-Za-zÁÉÍÓÚáéíóúñÑ\s]+" title="Ingrese solo letras y espacios"><br><br>

    <label for="direccion">Direccion:</label>
    <input type="text" id="direccion" name="direccion" required pattern="[A-Za-zÁÉÍÓÚáéíóúñÑ\s]+" title="Ingrese solo letras y espacios"><br><br>


    <label for="telefono">Teléfono:</label>
    <input type="text" id="telefono" name="telefono" required pattern="\d{8}" title="Ingrese un teléfono válido de 8 dígitos"><br><br>

    <label for="email">Email:</label>
    <input type="text" id="email" name="email" required pattern="[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}" title="Ingrese un correo electrónico válido (ejemplo@dominio.com)">
    <br><br>



    <input type="submit" value="Guardar cliente">
</form>

<%@ include file="/layout/footer.jsp" %>

<script>
    function validarFormulario() {
    var nombre = document.getElementById("nombre").value;
    var direccion = document.getElementById("direccion").value;
    var telefono = document.getElementById("telefono").value;
    var email = document.getElementById("email").value;

    if (nombre.trim() === "") {
        alert("Por favor, ingrese el nombre.");
        return false;
    }

    if (direccion.trim() === "") {
        alert("Por favor, ingrese la dirección.");
        return false;
    }

    if (telefono.trim() === "") {
        alert("Por favor, ingrese el teléfono.");
        return false;
    }

    if (email.trim() === "") {
        alert("Por favor, ingrese el email.");
        return false;
    }

    if (!/^[A-Za-zÁÉÍÓÚáéíóúñÑ\s]+$/.test(nombre)) {
        alert("El nombre solo debe contener letras y espacios.");
        return false;
    }

    if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email)) {
        alert("El email debe tener el formato algo@algo.algo");
        return false;
      }

    if (!/^[A-Za-z0-9\s\-_.,!@#$%^&*()\/]+$/.test(direccion)) {
        alert("El campo de dirección puede contener letras, números, espacios y símbolos como -, _, ., !, @, #, $, %, ^, &, *, / y ().");
        return false;
    }

    if (!/^\d{8}$/.test(telefono)) {
        alert("El teléfono debe contener exactamente 8 dígitos.");
        return false;
    }


    return true;
}

</script>
