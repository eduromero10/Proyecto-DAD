package Main;

import Modelo.Usuario;
import Conexion.Conexion;
import Interfaz.VentanaPrinc;

import java.sql.Connection;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame parentFrame = new JFrame();
            VentanaPrinc ventana = new VentanaPrinc(parentFrame);

            Usuario usuario = ventana.getUsuarioAutenticado(); // Ahora sí funciona

            if (usuario != null) {
                System.out.println("Usuario autenticado: " + usuario.getCorreo());
            } else {
                System.out.println("Inicio de sesión cancelado o fallido.");
            }

            Connection conexion = Conexion.conectar();
            if (conexion != null) {
                System.out.println("Base de datos conectada correctamente desde Main");
            } else {
                System.out.println("No se pudo conectar a la base de datos");
            }
        });
    }
}