package Interfaz;

import Modelo.Usuario;
import Conexion.Conexion;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class VentanaPrinc extends JDialog {
    private JTextField campoCorreo;
    private JPasswordField campoContraseña;
    private JButton botonIniciarSesion;
    private JPanel panelPrincipal;
    private JButton botonRegistrarse;
    private JLabel ImagenLabel;
    private JLabel CorreoLabel;
    private JPanel Panelcentro;
    private JLabel ContraseñaTexto;
    private JLabel BienvenidosText;
    private JLabel VideojuegosText;
    private JPanel PanelBienvenida;

    private Usuario usuarioAutenticado;

    public VentanaPrinc(JFrame parentFrame) {
        setTitle("Iniciar sesión");
        setContentPane(panelPrincipal);
        setMinimumSize(new Dimension(400, 300));
        setModal(true);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setResizable(true);
        setLocationRelativeTo(null);

        botonIniciarSesion.addActionListener(e -> iniciarSesion());
        botonRegistrarse.addActionListener(e -> registrarUsuario());

        setVisible(true);
    }

    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    private void iniciarSesion() {
        String correo = campoCorreo.getText();
        String contraseña = new String(campoContraseña.getPassword());

        if (correo.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rellene todos los campos.");
            return;
        }

        System.out.println("Intentando iniciar sesión...");

        Connection conexion = Conexion.conectar();
        if (conexion == null) {
            JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos.");
            return;
        }

        String sql = "SELECT idUsuario, contraseña FROM usuarios WHERE correo = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, correo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idUsuario = rs.getInt("idUsuario");
                String hashContraseña = rs.getString("contraseña");

                if (contraseña.equals(hashContraseña)) {
                    usuarioAutenticado = new Usuario(idUsuario, correo, hashContraseña);
                    JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.");
                    System.out.println("Usuario autenticado: " + correo);

                    dispose();
                    new VentanaSegunda(usuarioAutenticado).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Contraseña incorrecta.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos.");
            e.printStackTrace();
        }
    }

    private void registrarUsuario() {
        String correo = campoCorreo.getText().trim();
        String contraseña = new String(campoContraseña.getPassword()).trim();

        if (correo.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rellene todos los campos.");
            return;
        }

        System.out.println("Intentando registrar usuario con correo: " + correo);

        Connection conexion = Conexion.conectar();
        if (conexion == null) {
            JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos.");
            return;
        }

        String sqlVerificar = "SELECT correo FROM usuarios WHERE correo = ?";
        String sqlInsertar = "INSERT INTO usuarios (correo, contraseña) VALUES (?, ?)";
        String sqlObtenerID = "SELECT idUsuario FROM usuarios WHERE correo = ?";

        try (PreparedStatement psVerificar = conexion.prepareStatement(sqlVerificar)) {
            psVerificar.setString(1, correo);
            ResultSet rs = psVerificar.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "El correo ya está registrado.");
                return;
            }

            try (PreparedStatement psInsertar = conexion.prepareStatement(sqlInsertar)) {
                psInsertar.setString(1, correo);
                psInsertar.setString(2, contraseña);
                psInsertar.executeUpdate();
            }


            try (PreparedStatement psObtenerID = conexion.prepareStatement(sqlObtenerID)) {
                psObtenerID.setString(1, correo);
                ResultSet rsID = psObtenerID.executeQuery();

                if (rsID.next()) {
                    int idUsuario = rsID.getInt("idUsuario");
                    JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");

                    dispose();
                    new VentanaSegunda(new Usuario(idUsuario, correo, contraseña)).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al obtener el ID del usuario.");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error en la base de datos.");
            e.printStackTrace();
        }
    }
}