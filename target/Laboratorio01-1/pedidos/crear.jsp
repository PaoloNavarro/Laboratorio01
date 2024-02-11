<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<title>Crear pedido</title>
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
    select,
    textarea {
        width: 100%;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
    }

    input[type="date"] {
        width: calc(100% - 20px); /* Ajusta el ancho para compensar el padding */
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

<form action="pedidoscrear" method="post" onsubmit="return validarFormulario()">
    <h1>Crear pedido</h1>

    <label for="idCliente">Cliente:</label>
    <select id="idCliente" name="idCliente" required>
        <c:forEach items="${clientes}" var="cliente">
            <option value="${cliente.id}">${cliente.nombre}</option>
        </c:forEach>
    </select><br><br>

    <label for="fecha">Fecha:</label>
    <input type="date" id="fecha" name="fecha" required><br><br>

    <label for="total">Total:</label>
    <input type="number" id="total" name="total" required min="0" step="0.01"><br><br>

    <label for="estado">Estado:</label>
    <select id="estado" name="estado" required>
        <option value="1">Activo</option>
        <option value="2">Cancelado</option>
        <option value="3">Finalizado</option>
    </select><br><br>

    <input type="submit" value="Guardar pedido">
</form>

<%@ include file="/layout/footer.jsp" %>

<script>
    function validarFormulario() {
        var idCliente = document.getElementById("idCliente").value;
        var fecha = document.getElementById("fecha").value;
        var total = document.getElementById("total").value;
        var estado = document.getElementById("estado").value;

        if (idCliente.trim() === "") {
            alert("Por favor, seleccione un cliente.");
            return false;
        }

        if (fecha.trim() === "") {
            alert("Por favor, ingrese la fecha.");
            return false;
        }

        if (total.trim() === "") {
            alert("Por favor, ingrese el total.");
            return false;
        }

        if (estado.trim() === "") {
            alert("Por favor, seleccione un estado.");
            return false;
        }

        return true;
    }
</script>
