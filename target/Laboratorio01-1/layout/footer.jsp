<%-- 
    Document   : footer
    Created on : 26 sep. 2023, 08:41:05
    Author     : X1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
    </div>

</body>
</html>
    <%-- Mostrar mensaje de éxito si está presente en la sesión --%>
    <% String successMessage = (String) session.getAttribute("successMessage"); %>
    <% if (successMessage != null) { %>
        <script>
            Swal.fire({
                title: 'Éxito',
                text: '<%= successMessage %>',
                icon: 'success',
                toast: true,
                position: 'top-end',
                showConfirmButton: false,
                timer: 3000
            });
        </script>
        <% session.removeAttribute("successMessage"); %>
    <% } %>

    <%-- Mostrar mensaje de error si está presente en la sesión --%>
    <% String errorMessage = (String) session.getAttribute("errorMessage"); %>
    <% if (errorMessage != null) { %>
        <script>
            Swal.fire({
                title: 'Error',
                text: '<%= errorMessage %>',
                icon: 'error',
                toast: true,
                position: 'top-end',
                showConfirmButton: false,
                timer: 3000
            });
        </script>
        <% session.removeAttribute("errorMessage"); %>
    <% } %>

<script>
    
    function eliminarElemento(idElemento, elemento) {
        console.log("ID del " + elemento + " a eliminar:", idElemento);

        Swal.fire({
            title: '¿Estás seguro?',
            text: `Esta acción eliminará el ${elemento}. ¿Deseas continuar?`,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Sí, eliminar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                // Si el usuario confirmó, redirige a la URL de eliminación
                window.location.href = elemento+`eliminar?id=` + idElemento;
            }
        });
    }
     function comprar() {
    // Verificar si la sesión contiene un cliente (puedes ajustar esta lógica según tu aplicación)
    var clienteEnSesion = <%= session.getAttribute("cliente") != null %> ;

    if (clienteEnSesion) {
        // Si la sesión contiene un cliente, muestra una confirmación adicional antes de comprar
        Swal.fire({
            title: 'Confirmar compra',
            text: '¿Estás seguro de realizar la compra?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Sí, comprar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                // El usuario confirmó la compra, aquí puedes redirigir a la página de compra
                window.location.href = "comprar";
                
            }
        });
    } else {
        // Si la sesión no contiene un cliente, muestra el mensaje de inicio de sesión como antes
        Swal.fire({
            title: 'Iniciar sesión',
            text: 'Para realizar la compra, debes iniciar sesión y tener un cliente válido.',
            icon: 'info',
            showCancelButton: true,
            confirmButtonText: 'Iniciar sesión',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                // Redirige a la página de inicio de sesión
                window.location.href = "login";
            }
        });
    }
}

$(document).ready(function() {
    $('#miTabla').DataTable({
        "language": {
            "sProcessing": "Procesando...",
            "sLengthMenu": "Mostrar _MENU_ registros por página",
            "sZeroRecords": "No se encontraron resultados",
            "sEmptyTable": "Ningún dato disponible en esta tabla",
            "sInfo": "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
            "sInfoEmpty": "Mostrando registros del 0 al 0 de un total de 0 registros",
            "sInfoFiltered": "(filtrado de un total de _MAX_ registros)",
            "sInfoPostFix": "",
            "sSearch": "Buscar:",
            "sUrl": "",
            "sInfoThousands": ",",
            "sLoadingRecords": "Cargando...",
            "oPaginate": {
                "sFirst": "Primero",
                "sLast": "Último",
                "sNext": "Siguiente",
                "sPrevious": "Anterior"
            }
        },
        "lengthMenu": [5, 10, 25], // Mostrar opciones de 5, 10 y 25 registros por página
        "pageLength": 5 // Establecer 5 como valor por defecto
    });
});


</script>