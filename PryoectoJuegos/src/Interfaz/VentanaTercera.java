package Interfaz;

import Conexion.Conexion;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class VentanaTercera extends JDialog {
    private JTextField textNumeroTarjeta;
    private JTextField textCVV;
    private JTextField textFechaCaducidadTarjeta;
    private JButton PAGARButton;
    private JLabel LabelCVV;
    private JPanel PanelPrincipalVentana3;
    private JPanel Panel1;
    private JPanel Panel2;
    private JPanel Panel3;
    private JPanel Panel4;
    private JLabel LabelNumero;
    private JLabel LabelFecha;
    private JPanel PanelTituloV2;
    private int idUsuario;
    private String nombreJuego;
    private int idJuego;

    public VentanaTercera(int idUsuario, String nombreJuego, int idJuego) {
        this.idUsuario = idUsuario;
        this.nombreJuego = nombreJuego;
        this.idJuego = idJuego;

        setTitle("Datos bancarios");

        if (PanelPrincipalVentana3 == null) {
            System.out.println("⚠Error");
            JOptionPane.showMessageDialog(null, "Error cargando la interfaz de pago");
            return;
        }

        setContentPane(PanelPrincipalVentana3);
        setMinimumSize(new Dimension(400, 300));
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if (PAGARButton != null) {
            PAGARButton.addActionListener(e -> realizarCompra());
        } else {
            JOptionPane.showMessageDialog(null, "Error");
            return;
        }

        setVisible(true);
    }

    private void realizarCompra() {
        String numeroTarjeta = textNumeroTarjeta.getText().trim();
        String cvv = textCVV.getText().trim();
        String fechaCaducidad = textFechaCaducidadTarjeta.getText().trim();

        if (numeroTarjeta.isEmpty() || cvv.isEmpty() || fechaCaducidad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠Todos los campos son obligatorios.");
            return;
        }

        Connection conexion = Conexion.conectar();
        if (conexion == null) {
            JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos.");
            return;
        }

        String sqlCompra = "INSERT INTO compras (idUsuario, idJuego, nombrejuego, fechaCompra) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sqlCompra)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idJuego);
            ps.setString(3, nombreJuego);
            ps.setDate(4, Date.valueOf(LocalDate.now()));

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Compra realizada con éxito.");
            dispose();  // Cierra la ventana después de la compra
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al realizar la compra.");
            e.printStackTrace();
        }
    }
}