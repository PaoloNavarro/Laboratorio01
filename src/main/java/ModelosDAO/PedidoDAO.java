package ModelosDAO;

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
}
