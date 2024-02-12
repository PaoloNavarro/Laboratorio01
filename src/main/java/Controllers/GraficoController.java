package Controllers;

import Modelos.ClienteConPedidos;
import Modelos.Pedido;  // Ajusta la importación según tu modelo
import ModelosDAO.PedidoDAO;  // Ajusta la importación según tu modelo DAO
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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

@WebServlet(name = "GraficoController", urlPatterns = {"/graficoMeses", "/graficoClientes"})
public class GraficoController extends HttpServlet {
    private PedidoDAO pedidoDAO;
    private List<Pedido> listaPedidos;
    private List<ClienteConPedidos> listaClientes;
    private Gson gson;
    private String jsonPedidos;
    private String jsonClientes;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            pedidoDAO = new PedidoDAO();  // Ajusta según tu DAO
            // Lista de la consulta con los datos de pedidos
            listaPedidos = pedidoDAO.consultaGeneral();  //metodo top 10
           listaClientes = pedidoDAO.obtenerClientesConMasPedidos();
            // Librería de JSON
            gson = new Gson();
            // Convertir la lista de pedidos a formato JSON
            jsonPedidos = gson.toJson(listaPedidos);
            jsonClientes = gson.toJson(listaClientes);

        } catch (SQLException ex) {
            Logger.getLogger(GraficoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        switch (action) {
            case "/graficoMeses":
                mostrarGraficoMeses(request, response);
                break;
            case "/graficoClientes":
                mostrarGraficoClientes(request, response);
                break;
            default:
                break;
        }
    }

    private void mostrarGraficoMeses(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lógica para obtener los datos de los tres meses con más pedidos por año
        // y enviarlos a la vista
        HttpSession session = request.getSession();
        // Mandamos a la sesión los datos de los pedidos
        session.setAttribute("pedidos", listaPedidos);
        // Envío en forma de JSON los datos
        session.setAttribute("pedidosAsJSON", jsonPedidos);
        // Redirigir a la página de visualización de gráficos de meses
        RequestDispatcher dispatcher = request.getRequestDispatcher("graficos/meses.jsp");
        dispatcher.forward(request, response);
    }

    private void mostrarGraficoClientes(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
           HttpSession session = request.getSession();

       // Mandamos a la sesión los datos de los pedidos
        session.setAttribute("clientesConMasPedidos", listaClientes);
        // Envío en forma de JSON los datos
        session.setAttribute("clientesConMasPedidosAsJSON", jsonClientes);

       
       // También puedes enviar la lista de clientes como JSON si lo necesitas

       // Redirigir a la página de visualización de gráficos de clientes
       RequestDispatcher dispatcher = request.getRequestDispatcher("graficos/clientes.jsp");
       dispatcher.forward(request, response);
}


    // Resto del código, como el método processRequest y doPost, se pueden mantener como están o ajustar según necesites
}
