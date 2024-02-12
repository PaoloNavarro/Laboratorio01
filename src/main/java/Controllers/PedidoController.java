package Controllers;

import Modelos.Cliente;
import Modelos.Pedido;
import ModelosDAO.PedidoDAO;
import ModelosDAO.ClienteDAO;
import com.google.protobuf.TextFormat.ParseException;


import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "PedidoController", urlPatterns = {"/pedidos", "/pedidoscrear", "/pedidoseditar", "/pedidoseliminar"})
public class PedidoController extends HttpServlet {

    private PedidoDAO pedidoDAO;
    private ClienteDAO clienteDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            pedidoDAO = new PedidoDAO();
            clienteDAO = new ClienteDAO();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ServletException("Error al inicializar PedidoDAO", ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        HttpSession session = request.getSession();

        switch (action) {
            case "/pedidos":
                mostrarPedidos(request, response);
                break;
            case "/pedidoscrear":
                mostrarFormularioCrearPedido(request, response);
                break;
            case "/pedidoseditar":
                mostrarFormularioEditarPedido(request, response);
                break;
            case "/pedidoseliminar":
                eliminarPedido(request, response);
                break;
            default:
                // Lógica para otras rutas si es necesario
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {

        String action = request.getServletPath();

        switch (action) {
            case "/pedidoscrear": {
                try {
                    crearPedido(request, response);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    throw new ServletException("Error al crear pedido", ex);
                } catch (java.text.ParseException ex) {
                Logger.getLogger(PedidoController.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            break;

            case "/pedidoseditar":
            {
                editarPedido(request, response);
            }
                break;


            default:
                // Lógica para otras rutas si es necesario
                break;
        }
    }

    private void mostrarPedidos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            HttpSession session = request.getSession();

            // Obtener la lista de pedidos desde la base de datos
            List<Pedido> listaPedidos = pedidoDAO.consultaGeneral();
            session.setAttribute("pedidos", listaPedidos);

            // Redirigir a la página de visualización de pedidos
            RequestDispatcher dispatcher = request.getRequestDispatcher("pedidos/index.jsp");
            dispatcher.forward(request, response);
    }

    private void mostrarFormularioCrearPedido(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            HttpSession session = request.getSession();

            List<Cliente> listaClientes = clienteDAO.consultaGeneral();
            session.setAttribute("clientes", listaClientes);


        RequestDispatcher dispatcher = request.getRequestDispatcher("pedidos/crear.jsp");
        dispatcher.forward(request, response);
    }

   private void mostrarFormularioEditarPedido(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    HttpSession session = request.getSession();
    String idPedidoStr = request.getParameter("id");

    // Verificar si el ID es válido (puedes agregar validaciones adicionales)
    if (idPedidoStr != null && !idPedidoStr.isEmpty()) {
        try {
            int idPedido = Integer.parseInt(idPedidoStr);

            // Lógica para obtener los datos del pedido desde la base de datos
            Pedido pedido = pedidoDAO.buscarPedidoPorId(idPedido);

            if (pedido != null) {
                // Pasar los datos del pedido a la vista de edición
                request.setAttribute("pedido", pedido);
                
                //datos del cliente
                List<Cliente> listaClientes = clienteDAO.consultaGeneral();
                session.setAttribute("clientes", listaClientes);
                
                // Convertir la fecha a un formato adecuado antes de enviarla al formulario
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String fechaFormateada = dateFormat.format(pedido.getFecha());
                request.setAttribute("fechaFormateada", fechaFormateada);

                RequestDispatcher dispatcher = request.getRequestDispatcher("pedidos/editar.jsp");
                dispatcher.forward(request, response);
            } else {
                // Manejo de error si el pedido no existe
                session.setAttribute("errorMessage", "Error al cargar editar pedido");
                response.sendRedirect("pedidos?error=true");
            }
        } catch (NumberFormatException e) {
            // Manejo de error si el ID no es un número válido
            session.setAttribute("errorMessage", "Error al cargar editar pedido");
            response.sendRedirect("pedidos?error=true");
        }
    } else {
        // Manejo de error si no se proporciona un ID válido en la ruta
        session.setAttribute("errorMessage", "Error al cargar editar pedido");
        response.sendRedirect("pedidos?error=true");
    }
}


    private void crearPedido(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, SQLException, ParseException, java.text.ParseException {
    HttpSession session = request.getSession();

    // Obtener los datos del formulario HTML
    int idCliente = Integer.parseInt(request.getParameter("idCliente"));

    try {
        // Utiliza SimpleDateFormat para parsear la fecha desde el formato del formulario
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date fechaUtil = dateFormat.parse(request.getParameter("fecha"));

        // Convertir a java.sql.Date
        java.sql.Date fechaSql = new java.sql.Date(fechaUtil.getTime());

        double total = Double.parseDouble(request.getParameter("total"));
        int estado = Integer.parseInt(request.getParameter("estado"));

        // Crear un objeto Pedido con los datos del formulario
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setIdCliente(idCliente);
        nuevoPedido.setFecha(fechaSql); // Asigna la fecha convertida a java.sql.Date
        nuevoPedido.setTotal(total);
        nuevoPedido.setEstado(estado);

        // Llamar al método agregarPedido de tu PedidoDAO
        boolean exito = pedidoDAO.agregar(nuevoPedido);

        if (exito) {
            // La inserción fue exitosa, redirige a la página de pedidos
            session.setAttribute("successMessage", "Pedido creado con éxito");
            response.sendRedirect("pedidos");
        } else {
            // La inserción falló, puedes mostrar un mensaje de error o redirigir a una página de error
            session.setAttribute("errorMessage", "Error al crear el pedido");
            response.sendRedirect("pedidos");
        }
    } catch (ParseException e) {
        e.printStackTrace();
        // Manejar el caso en el que la fecha no se pueda parsear correctamente
        session.setAttribute("errorMessage", "Error al parsear la fecha");
        response.sendRedirect("pedidos");
    }
}



   private void editarPedido(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    HttpSession session = request.getSession();

    // Obtener los datos del formulario HTML
    int idPedido = Integer.parseInt(request.getParameter("idPedido"));
    int idCliente = Integer.parseInt(request.getParameter("idCliente"));

    // Ajusta la obtención y conversión de la fecha según tu formulario
    String fechaStr = request.getParameter("fecha");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    java.util.Date fechaUtil = null;

    try {
        fechaUtil = dateFormat.parse(fechaStr);
    }catch (java.text.ParseException ex) {
        Logger.getLogger(PedidoController.class.getName()).log(Level.SEVERE, null, ex);
    }
        // Manejo de error si la fecha no se puede parsear
        // O maneja el error según tu lógica

    // Convertir a java.sql.Date
    java.sql.Date fechaSql = new java.sql.Date(fechaUtil.getTime());

    // Ajusta según los campos de tu formulario y modelo de Pedido
    double total = Double.parseDouble(request.getParameter("total"));
    int estado = Integer.parseInt(request.getParameter("estado"));

    // Crear un objeto Pedido con los datos del formulario
    Pedido pedido = new Pedido();
    pedido.setId(idPedido);
    pedido.setIdCliente(idCliente);
    pedido.setFecha(fechaSql);
    pedido.setTotal(total);
    pedido.setEstado(estado);

    // Llamar al método editarPedido de tu PedidoDAO
    boolean exito = pedidoDAO.actualizarPedido(pedido); // Manejo de la excepción de SQL si es necesario
    if (exito) {
        // La actualización fue exitosa, redirige a la página de pedidos
        session.setAttribute("successMessage", "Pedido actualizado con éxito");
        response.sendRedirect("pedidos");
    } else {
        // La actualización falló, puedes mostrar un mensaje de error o redirigir a una página de error
        session.setAttribute("errorMessage", "Error al actualizar el pedido");
        response.sendRedirect("pedidos");
    }
}

    private void eliminarPedido(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        // Lógica para verificar el rol del usuario si es necesario

        // Obtener el ID del pedido a eliminar desde la solicitud
        String idPedidoStr = request.getParameter("id");

        if (idPedidoStr != null && !idPedidoStr.isEmpty()) {
            try {
                int idPedido = Integer.parseInt(idPedidoStr);

                // Llamar al método en el DAO para eliminar el pedido
                boolean exito = pedidoDAO.eliminarPedido(idPedido);

                if (exito) {
                    // Redirigir a la página de pedidos si la eliminación fue exitosa con un mensaje de éxito
                    session.setAttribute("successMessage", "Pedido eliminado con éxito");
                    response.sendRedirect("pedidos");
                } else {
                    // Manejar el caso en el que la eliminación falla, tal vez mostrar un mensaje de error
                    session.setAttribute("errorMessage", "Error al eliminar el pedido");
                    response.sendRedirect("pedidos");
                }
            } catch (NumberFormatException e) {
                // Manejar el caso en el que el parámetro de ID no es un número válido con un mensaje de error
                session.setAttribute("errorMessage", "Error al eliminar el pedido");
                response.sendRedirect("pedidos");
            }
        } else {
            // Manejar el caso en el que no se proporcionó un ID válido con un mensaje de error
            session.setAttribute("errorMessage", "Error al eliminar el pedido");
            response.sendRedirect("pedidos");
        }
    }
}
