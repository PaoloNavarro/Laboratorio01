package ModelosDAO;

import Modelos.ClienteConPedidos;
import Modelos.Pedido;
import db.cn;
import java.sql.Date;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private cn CN;
    private PreparedStatement ps;
    private ResultSet rs;
    private String sql;

    public PedidoDAO() throws SQLException {
        try {
            CN = new cn();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Pedido> consultaGeneral() {
    List<Pedido> listaPedidos = new ArrayList<>();
    String sql = "SELECT p.*, c.nombre AS nombre_cliente FROM pedidos p " +
                 "INNER JOIN clientes c ON p.id_cliente = c.id";

    try (PreparedStatement ps = CN.getConnection().prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            Pedido pedido = new Pedido();
            pedido.setId(rs.getInt("id"));
            pedido.setNombreCliente(rs.getString("nombre_cliente"));
            pedido.setFecha(rs.getDate("fecha"));
            pedido.setTotal(rs.getDouble("total"));
            pedido.setEstado(rs.getInt("estado"));

            listaPedidos.add(pedido);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return listaPedidos;
}

    public boolean agregar(Pedido pedido) {
        this.sql = "INSERT INTO pedidos (id_cliente, fecha, total, estado) VALUES (?,?,?,?)";
        try {
            ps = CN.getConnection().prepareStatement(sql);
            ps.setInt(1, pedido.getIdCliente());
            ps.setDate(2, (Date) pedido.getFecha());
            ps.setDouble(3, pedido.getTotal());
            ps.setInt(4, pedido.getEstado());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Pedido buscarPedidoPorId(int idPedido) {
        Pedido pedido = null;

        String sql = "SELECT * FROM pedidos WHERE id = ?";

        try {
            ps = CN.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pedido = new Pedido();
                    pedido.setId(rs.getInt("id"));
                    pedido.setIdCliente(rs.getInt("id_cliente"));
                    pedido.setFecha(rs.getDate("fecha"));
                    pedido.setTotal(rs.getDouble("total"));
                    pedido.setEstado(rs.getInt("estado"));

                    return pedido;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("error en pedido" + e.getMessage());
        }

        return null;
    }

    public boolean actualizarPedido(Pedido pedido) {
        String sql = "UPDATE pedidos SET id_cliente=?, fecha=?, total=?, estado=? WHERE id=?";

        try (PreparedStatement ps = CN.getConnection().prepareStatement(sql)) {
            ps.setInt(1, pedido.getIdCliente());
            ps.setDate(2, (Date) pedido.getFecha());
            ps.setDouble(3, pedido.getTotal());
            ps.setInt(4, pedido.getEstado());
            ps.setInt(5, pedido.getId());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarPedido(int idPedido) {
        String sql = "DELETE FROM pedidos WHERE id = ?";

        try (PreparedStatement ps = CN.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idPedido);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
        public List<ClienteConPedidos> obtenerClientesConMasPedidos() {
            List<ClienteConPedidos> listaClientes = new ArrayList<>();
            String sql = "SELECT c.id, c.nombre AS nombre_cliente, COUNT(p.id) AS total_pedidos " +
                         "FROM clientes c " +
                         "LEFT JOIN pedidos p ON c.id = p.id_cliente " +
                         "GROUP BY c.id, c.nombre " +
                         "ORDER BY total_pedidos DESC " +
                         "LIMIT 10";

            try (PreparedStatement ps = CN.getConnection().prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ClienteConPedidos cliente = new ClienteConPedidos();
                    cliente.setId(rs.getInt("id"));
                    cliente.setNombreCliente(rs.getString("nombre_cliente"));
                    cliente.setTotalPedidos(rs.getInt("total_pedidos"));

                    listaClientes.add(cliente);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return listaClientes;
        }


    public List<Pedido> obtenerTop3MesesConMasPedidos() {
        List<Pedido> listaTopMeses = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre AS nombre_cliente FROM pedidos p " +
                     "INNER JOIN clientes c ON p.id_cliente = c.id " +
                     "GROUP BY YEAR(p.fecha), MONTH(p.fecha) " +
                     "ORDER BY COUNT(p.id) DESC LIMIT 3";

        try (PreparedStatement ps = CN.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id"));
                pedido.setNombreCliente(rs.getString("nombre_cliente"));
                pedido.setFecha(rs.getDate("fecha"));
                pedido.setTotal(rs.getDouble("total"));
                pedido.setEstado(rs.getInt("estado"));

                listaTopMeses.add(pedido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaTopMeses;
    }
}
