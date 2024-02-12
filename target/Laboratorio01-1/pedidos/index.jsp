<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file="/layout/header.jsp" %>

<title>Lista de pedidos</title>

<style>
    .table-container {
        overflow-x: auto; /* Añade barra de desplazamiento horizontal */
    }

    /* Estilo para la tabla de pedidos */
    .table {
        width: 100%;
        border-collapse: collapse;
    }

    .table th, .table td {
        border: 1px solid #ccc;
        padding: 8px;
        text-align: left;
    }

    .table th {
        background-color: #f2f2f2;
    }

    .btn {
        padding: 5px 10px;
        text-decoration: none;
        border: none;
        cursor: pointer;
    }

    .btn-primary {
        background-color: #007bff;
        color: #fff;
    }

    .btn-primary:hover {
        background-color: #0056b3;
    }

    .btn-danger {
        background-color: #dc3545;
        color: #fff;
    }

    .btn-danger:hover {
        background-color: #c82333;
    }
</style>

<h1 class="mb-3">Lista de pedidos</h1>

<!-- Botón para agregar un nuevo pedido -->
<a href="pedidoscrear" class="btn btn-primary mb-3">
    <i class="fas fa-plus"></i> Agregar pedido
</a>

<!-- Contenedor para la tabla con barra de desplazamiento horizontal -->
<div class="table-container">
    <table id="miTabla" class="table">
        <thead>
            <tr>
                <th class="d-none">ID</th>
                <th>Nombre</th>
                <th>Fecha</th>
                <th>Total</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${pedidos}" var="pedido">
                <tr>
                    <td class="d-none">${pedido.id}</td>
                    <td>${pedido.nombreCliente}</td>
                    <td>${pedido.fecha}</td>
                    <td>${pedido.total} $</td>
                    <td>
                        <c:choose>
                            <c:when test="${pedido.estado eq 1}">Activo</c:when>
                            <c:when test="${pedido.estado eq 2}">Cancelado</c:when>
                            <c:when test="${pedido.estado eq 3}">Finalizado</c:when>
                            <c:otherwise>Desconocido</c:otherwise>
                        </c:choose>
                    </td>                    <td>
                        <a href="pedidoseditar?id=${pedido.id}" class="btn btn-primary">
                            <i class="fas fa-pencil-alt"></i> Editar
                        </a>
                        <a href="#" class="btn btn-danger" onclick="eliminarElemento(${pedido.id}, 'pedidos')">
                            <i class="fas fa-trash"></i> Eliminar
                        </a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="/layout/footer.jsp" %>
