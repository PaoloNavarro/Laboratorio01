<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<title>Gráfico de Pedidos</title>
<%@ include file="/layout/header.jsp" %>

<style>
    .table-container {
        overflow-x: auto;
    }

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

    .chart-container {
        padding: 20px;
    }
</style>

<h1 class="mb-3">Gráfico de Pedidos por Cliente</h1>
<div class="row">
    <div class="col-6">
        <h2>Top 10 Clientes con más pedidos</h2>
        <canvas id="myChart"></canvas>
    </div>
    <div class="col-6">
        <div class="table-container">
            <table id="miTabla" class="table">
                <thead>
                    <tr>
                        <th>Cliente</th>
                        <th>Pedidos</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="clientesConMasPedidos" items="${clientesConMasPedidos}">
                        <tr>
                            <td>${clientesConMasPedidos.nombreCliente}</td>
                              <td>${clientesConMasPedidos.totalPedidos}</td>

                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/layout/footer.jsp" %>

<script>
    // Obtiene la lista de clientes desde el atributo de sesión
    var listaClientes = ${sessionScope.clientesConMasPedidosAsJSON};
    // Convierte la cadena JSON a un objeto JavaScript
    var clientes = listaClientes;

    var colores = [
    'rgba(75, 192, 192, 0.7)',
    'rgba(255, 99, 132, 0.7)',
    'rgba(255, 205, 86, 0.7)',
    'rgba(54, 162, 235, 0.7)',
    'rgba(255, 159, 64, 0.7)',
    'rgba(153, 102, 255, 0.7)',
    'rgba(255, 87, 34, 0.7)',
    'rgba(75, 192, 192, 0.7)',
    'rgba(255, 99, 132, 0.7)',
    'rgba(255, 205, 86, 0.7)'
];


    // Ejemplo de uso de Chart.js para crear un gráfico de barras
    var ctx = document.getElementById('myChart').getContext('2d');
    var myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: clientes.map(cliente => cliente.nombreCliente),
            datasets: [{
                label: 'Total de Pedidos',
                data: clientes.map(cliente => cliente.totalPedidos),
                backgroundColor: colores,
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 1
            }]
        },
        options: {
             indexAxis: 'y',
            responsive: true,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                x: {
                    beginAtZero: true
                },
                y: {
                    beginAtZero: true
                }
            }
        }
    });
</script>
