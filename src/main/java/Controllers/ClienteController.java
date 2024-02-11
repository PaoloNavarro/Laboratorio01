package Controllers;

import Modelos.Cliente;
import ModelosDAO.ClienteDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ClienteController", urlPatterns = {"/clientes", "/clientescrear", "/clienteseditar", "/clienteseliminar"})
public class ClienteController extends HttpServlet {

    private ClienteDAO clienteDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            clienteDAO = new ClienteDAO();
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new ServletException("Error al inicializar ClienteDAO", ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        HttpSession session = request.getSession();

        switch (action) {
            case "/clientes":
                mostrarClientes(request, response);
                break;
            case "/clientescrear":
                mostrarFormularioCrearCliente(request, response);
                break;
            case "/clienteseditar":
                mostrarFormularioEditarCliente(request, response);
                break;
            case "/clienteseliminar":
                eliminarCliente(request, response);
                break;
            default:
                // Lógica para otras rutas si es necesario
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getServletPath();

        switch (action) {
            case "/clientescrear": {
                try {
                    crearCliente(request, response);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    throw new ServletException("Error al crear cliente", ex);
                }
            }
            break;

            case "/clienteseditar":
                editarCliente(request, response);
                break;
            default:
                // Lógica para otras rutas si es necesario
                break;
        }
    }

    private void mostrarClientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Obtener la lista de clientes desde la base de datos
        List<Cliente> listaClientes = clienteDAO.consultaGeneral();
        session.setAttribute("clientes", listaClientes);

        // Redirigir a la página de visualización de clientes
        RequestDispatcher dispatcher = request.getRequestDispatcher("clientes/index.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioCrearCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lógica para obtener datos adicionales si es necesario

        RequestDispatcher dispatcher = request.getRequestDispatcher("clientes/crear.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarFormularioEditarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String idClienteStr = request.getParameter("id");

        // Verificar si el ID es válido (puedes agregar validaciones adicionales)
        if (idClienteStr != null && !idClienteStr.isEmpty()) {
            try {
                int idCliente = Integer.parseInt(idClienteStr);

                // Lógica para obtener los datos del cliente desde la base de datos
                Cliente cliente = clienteDAO.buscarClientePorId(idCliente);

                if (cliente != null) {
                    // Pasar los datos del cliente a la vista de edición
                    request.setAttribute("cliente", cliente);

                    RequestDispatcher dispatcher = request.getRequestDispatcher("clientes/editar.jsp");
                    dispatcher.forward(request, response);
                } else {
                    // Manejo de error si el cliente no existe
                    session.setAttribute("errorMessage", "Error al cargar editar cliente");
                    response.sendRedirect("clientes?error=true");
                }
            } catch (NumberFormatException e) {
                // Manejo de error si el ID no es un número válido
                session.setAttribute("errorMessage", "Error al cargar editar cliente");
                response.sendRedirect("clientes?error=true");
            }
        } else {
            // Manejo de error si no se proporciona un ID válido en la ruta
            session.setAttribute("errorMessage", "Error al cargar editar cliente");
            response.sendRedirect("clientes?error=true");
        }
    }

    private void crearCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();

        // Obtener los datos del formulario HTML
        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");

        // Crear un objeto Cliente con los datos del formulario
        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setNombre(nombre);
        nuevoCliente.setDireccion(direccion);
        nuevoCliente.setTelefono(telefono);
        nuevoCliente.setEmail(email);

        // Llamar al método agregarCliente de tu ClienteDAO
        boolean exito = clienteDAO.agregar(nuevoCliente);

        if (exito) {
            // La inserción fue exitosa, redirige a la página de clientes
            session.setAttribute("successMessage", "Cliente creado con éxito");
            response.sendRedirect("clientes");
        } else {
            // La inserción falló, puedes mostrar un mensaje de error o redirigir a una página de error
            session.setAttribute("errorMessage", "Error al crear al cliente");
            response.sendRedirect("clientes");
        }
    }

    private void editarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Obtener los datos del formulario HTML
        int idCliente = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");

        // Crear un objeto Cliente con los datos del formulario
        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        cliente.setNombre(nombre);
        cliente.setDireccion(direccion);
        cliente.setTelefono(telefono);
        cliente.setEmail(email);

        // Llamar al método editarCliente de tu ClienteDAO
        boolean exito = clienteDAO.actualizarCliente(cliente); // Manejo de la excepción de SQL si es necesario
        if (exito) {
            // La actualización fue exitosa, redirige a la página de clientes
            session.setAttribute("successMessage", "Cliente actualizado con éxito");
            response.sendRedirect("clientes");
        } else {
            // La actualización falló, puedes mostrar un mensaje de error o redirigir a una página de error
            session.setAttribute("errorMessage", "Error al actualizar al cliente");
            response.sendRedirect("clientes");
        }
    }

    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        // Lógica para verificar el rol del usuario si es necesario

        // Obtener el ID del cliente a eliminar desde la solicitud
        String idClienteStr = request.getParameter("id");

        if (idClienteStr != null && !idClienteStr.isEmpty()) {
            try {
                int idCliente = Integer.parseInt(idClienteStr);

                // Llamar al método en el DAO para eliminar el cliente
                boolean exito = clienteDAO.eliminarCliente(idCliente);

                if (exito) {
                    // Redirigir a la página de clientes si la eliminación fue exitosa con un mensaje de éxito
                    session.setAttribute("successMessage", "Cliente eliminado con éxito");
                    response.sendRedirect("clientes");
                } else {
                    // Manejar el caso en el que la eliminación falla, tal vez mostrar un mensaje de error
                    session.setAttribute("errorMessage", "Error al eliminar el cliente");
                    response.sendRedirect("clientes");
                }
            } catch (NumberFormatException e) {
                // Manejar el caso en el que el parámetro de ID no es un número válido con un mensaje de error
                session.setAttribute("errorMessage", "Error al eliminar el cliente");
                response.sendRedirect("clientes");
            }
        } else {
            // Manejar el caso en el que no se proporcionó un ID válido con un mensaje de error
            session.setAttribute("errorMessage", "Error al eliminar el cliente");
            response.sendRedirect("clientes");
        }
    }

}
