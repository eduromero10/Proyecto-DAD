package Conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/videojuegos";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "Mariateresa95";

    public static Connection conectar() {
        try {
            Connection connection = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            if (connection != null) {
                System.out.println("Conexión exitosa a MySQL");
                return connection;
            } else {
                System.out.println("Error al conectar");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error de conexión a la base de datos: " + e.getMessage());
        }
        return null;
    }
}