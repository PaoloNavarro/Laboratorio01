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

<h1 class="mb-3">Gráfico de Pedidos por Mes</h1>
<div class="row">
       <div class="col-6">
           <h2>Top 3 meses con mas pedidos</h2>
                <canvas id="myChart"></canvas>
    </div>
    <div class="col-6">
        <div class="table-container">
            <table id="miTabla" class="table">
                <thead>
                    <tr>
                        <th>Cliente</th>
                        <th>Fecha</th>
                        <th>Total</th>
                        <th>Estado</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="pedido" items="${pedidos}">
                        <tr>
                            <td>${pedido.nombreCliente}</td>
                            <td>
                                <fmt:formatDate value="${pedido.fecha}" pattern="MMMM"/>
                            </td>                            
                            <td>${pedido.total}</td>
                           <td>
                            <c:choose>
                                <c:when test="${pedido.estado eq 1}">Activo</c:when>
                                <c:when test="${pedido.estado eq 2}">Cancelado</c:when>
                                <c:when test="${pedido.estado eq 3}">Finalizado</c:when>
                                <c:otherwise>Desconocido</c:otherwise>
                            </c:choose>
                            </tr>
                            </td>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
 
</div>

<%@ include file="/layout/footer.jsp" %>


<script>
    // Obtiene la lista de pedidos desde el atributo de sesión
    var listaPedidos = ${sessionScope.pedidosAsJSON};

    // Convierte la cadena JSON a un objeto JavaScript
    var pedidos = listaPedidos;

    // Agrupa los pedidos por mes
    var gruposMeses = {};

    pedidos.forEach(function (pedido) {
        // Formatear la fecha en un formato estándar (YYYY-MM-DD) antes de crear el objeto Date
        var fechaParts = pedido.fecha.match(/([a-z]+)\. (\d+), (\d+)/i);
        var formattedDate = fechaParts[3] + '-' + obtenerNumeroMes(fechaParts[1]) + '-' + fechaParts[2];
        var fecha = new Date(formattedDate);
        
        console.log(fecha);
        var nombreMes = obtenerNombreMes(fecha.getMonth() + 1); // Sumamos 1 porque los meses son indexados desde 0

        // Inicializar el contador para el mes si aún no existe
        if (!gruposMeses[nombreMes]) {
            gruposMeses[nombreMes] = 0;
        }

        // Incrementar el contador para el mes
        gruposMeses[nombreMes]++;
    });

    // Ordenar el objeto gruposMeses por la cantidad de pedidos de mayor a menor
    var sortedMeses = Object.keys(gruposMeses).sort(function(a, b) {
        return gruposMeses[b] - gruposMeses[a];
    });

    // Tomar solo los tres primeros elementos
    var tresPrimerosMeses = sortedMeses.slice(0, 3);

    // Crear un nuevo objeto con solo los tres primeros meses y sus colores
    var tresMesesConMasPedidos = {};
    var colores = ['rgba(75, 192, 192, 0.7)', 'rgba(255, 99, 132, 0.7)', 'rgba(255, 205, 86, 0.7)']; // Puedes agregar más colores según sea necesario

    tresPrimerosMeses.forEach(function(mes, index) {
        tresMesesConMasPedidos[mes] = {
            cantidad: gruposMeses[mes],
            color: colores[index]
        };
    });

    // Ejemplo de uso de Chart.js para crear un gráfico de barras
    var ctx = document.getElementById('myChart').getContext('2d');
    var myChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: Object.keys(tresMesesConMasPedidos),
            datasets: [{
                label: 'Cantidad de Pedidos',
                data: tresPrimerosMeses.map(mes => tresMesesConMasPedidos[mes].cantidad),
                backgroundColor: tresPrimerosMeses.map(mes => tresMesesConMasPedidos[mes].color),
                borderColor: 'rgba(0, 0, 0, 1)',
                borderWidth: 1
            }]
        },
        options: {
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

    // Función para obtener el nombre del mes a partir de su número
    function obtenerNombreMes(numeroMes) {
        var meses = [
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        ];
        return meses[numeroMes - 1] || ""; // Agregamos el operador de coalescencia nula para evitar valores undefined
    };

    // Función para obtener el número del mes a partir de su nombre en español
    function obtenerNumeroMes(nombreMes) {
        var meses = {
            'ene': '01', 'feb': '02', 'mar': '03', 'abr': '04', 'may': '05', 'jun': '06',
            'jul': '07', 'ago': '08', 'sep': '09', 'oct': '10', 'nov': '11', 'dic': '12'
        };
        return meses[nombreMes.toLowerCase()] || "";
    };
</script>


