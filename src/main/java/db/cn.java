package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class cn {
    private Connection con;

    public cn() throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/semana04", "root", "");
            System.out.println("¡Conexión exitosa!");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error al intentar conectar: " + e.getMessage());

            if (e instanceof SQLException) {
                SQLException sqlEx = (SQLException) e;
                System.err.println("SQL State: " + sqlEx.getSQLState());
                System.err.println("Error Code: " + sqlEx.getErrorCode());
            }

            e.printStackTrace();
            throw e;
        }
    }

    public Connection getConnection() {
        return con;
    }

    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
