/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelosDAO;

import Modelos.Cliente;
import db.cn;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author X1
 */
public class ClienteDAO {
    
    private cn CN;
    private PreparedStatement ps;
    private ResultSet rs;
    private String sql;

    public ClienteDAO() throws SQLException {
        try {
            CN = new cn();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

   public List<Cliente> consultaGeneral() {
    List<Cliente> listaCliente = new ArrayList<>();
    String sql = "SELECT * FROM clientes"; // Ajusta el nombre de la tabla si es necesario

    try (PreparedStatement ps = CN.getConnection().prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setTelefono(rs.getString("telefono")); // Ajusta según tus columnas
                cliente.setEmail(rs.getString("email"));
    
                // Agrega más atributos según tu esquema de base de datos
                listaCliente.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaCliente;
    }


    public boolean agregar(Cliente cliente) {
        this.sql = "INSERT INTO clientes (nombre, direccion, telefono, email) VALUES (?,?,?,?)";
        try {
            ps = CN.getConnection().prepareStatement(sql);
            // Configure the PreparedStatement parameters with the data from the alumno.
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDireccion());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getEmail());
          
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
     public Cliente buscarClientePorId(int idCliente) {
        Cliente cliente = null;

        String sql = "SELECT * FROM clientes WHERE id = ?";

        try  {
            ps = CN.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setId(rs.getInt("id"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setDireccion(rs.getString("direccion"));
                    cliente.setTelefono(rs.getString("telefono"));
                    cliente.setEmail(rs.getString("email"));
                  
                    return cliente;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("error en pedido"+e.getMessage());
        }

        return null; // Devuelve null si no se encuentra un pedido con el ID especificado
    }

    public boolean actualizarCliente(Cliente cliente) {
    String sql = "UPDATE clientes SET nombre=?, direccion=?, telefono=?, email=? WHERE id=?";

        try (PreparedStatement ps = CN.getConnection().prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDireccion());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getEmail());
            ps.setInt(5, cliente.getId()); // Agrega esta línea para establecer el valor del ID

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean eliminarCliente(int idCliente) {
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (PreparedStatement ps = CN.getConnection().prepareStatement(sql)) {
            ps.setInt(1, idCliente);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
