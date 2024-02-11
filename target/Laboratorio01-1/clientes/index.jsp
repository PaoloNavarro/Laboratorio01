<%@page contentType="text/html" pageEncoding="UTF-8"%>


    <title>Lista de clientes</title>
    <%@ include file="/layout/header.jsp" %>
    <style>
        .table-container {
            overflow-x: auto; /* Añade barra de desplazamiento horizontal */
        }
        /* Estilo para la tabla de productos */
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

    <h1 class="mb-3">Lista de clientes</h1>
    
    <!-- Botón para agregar un nuevo producto -->
    <a href="clientescrear" class="btn btn-primary mb-3">
        <i class="fas fa-plus"></i> Agregar cliente
    </a>

    <!-- Tabla para mostrar la lista de productos -->
 <!-- Contenedor para la tabla con barra de desplazamiento horizontal -->
    <div class="table-container">
        <table id="miTabla" class="table">
            <thead>
                <tr>
                    <th class="d-none">ID</th>
                    <th>Nombre</th>
                    <th>Direccion</th>
                    <th>Telefono</th>
                    <th>Email</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${clientes}" var="cliente">
                    <tr>
                        <td  class="d-none" >${cliente.id}</td>
                        <td>${cliente.nombre}</td>
                        <td>${cliente.direccion}</td>
                        <td>${cliente.telefono}</td>
                        <!-- Agrega más columnas según los atributos de Alumno -->
                        <td>${cliente.email}</td>
                        <td>
                            <a href="clienteseditar?id=${cliente.id}" class="btn btn-primary">
                                <i class="fas fa-pencil-alt"></i> Editar
                            </a>
                            <a href="#" class="btn btn-danger" onclick="eliminarElemento(${cliente.id}, 'clientes')">
                                <i class="fas fa-trash"></i> Eliminar
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    
<%@ include file="/layout/footer.jsp" %>

