package Interfaz;

import Conexion.Conexion;
import Modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class VentanaSegunda extends JDialog {
    private JTable tableJuegos;
    private JTextField queJuegoVasATextField;
    private JButton COMPRARButton;
    private JComboBox<String> comboBox1;
    private JPanel PanelPrincipalV2;
    private JPanel PanelExtremo4;
    private JPanel PanelExtremo3;
    private JPanel PanelExtremo2;
    private JLabel LabelMenu;
    private JPanel Panel1V2;
    private JPanel PanelExtremo1;
    private Usuario usuario;

    public VentanaSegunda(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Bienvenido, " + usuario.getCorreo());
        setContentPane(PanelPrincipalV2);
        setMinimumSize(new Dimension(600, 400));
        setModal(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nombre");
        tableModel.addColumn("Precio (€)");
        tableModel.addColumn("Tipo");
        tableModel.addColumn("Año de salida");
        tableModel.addColumn("Descripción");

        tableJuegos.setModel(tableModel);
        tableJuegos.removeColumn(tableJuegos.getColumnModel().getColumn(0));

        try {
            cargarJuegos(""); // Cargar juegos por defecto (sin filtro)
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando los juegos.");
            ex.printStackTrace();
        }

        comboBox1.addActionListener(e -> {
            String tipoSeleccionado = (String) comboBox1.getSelectedItem();
            cargarJuegos(tipoSeleccionado.equals("Todos") ? "" : tipoSeleccionado);
        });

        tableJuegos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = tableJuegos.getSelectedRow();
                if (filaSeleccionada != -1) {
                    String nombreJuego = tableJuegos.getModel().getValueAt(filaSeleccionada, 1).toString();
                    String precioJuego = tableJuegos.getModel().getValueAt(filaSeleccionada, 2).toString();
                    queJuegoVasATextField.setText(nombreJuego + " - " + precioJuego + "€");
                }
            }
        });

        COMPRARButton.addActionListener(e -> comprarJuego());

        setVisible(true);
    }

    private void cargarJuegos(String tipo) {
        DefaultTableModel model = (DefaultTableModel) tableJuegos.getModel();
        model.setRowCount(0); // Limpiar la tabla antes de cargar los datos

        Connection conexion = Conexion.conectar();
        if (conexion == null) {
            JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos.");
            return;
        }

        String sql = "SELECT * FROM videojuegos";
        if (!tipo.isEmpty()) {
            sql += " WHERE tipoJuego = ?";
        }

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            if (!tipo.isEmpty()) {
                ps.setString(1, tipo);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idJuego = rs.getInt("idJuego");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                String tipoJuego = rs.getString("tipoJuego");
                int anioSalida = rs.getInt("añoSalida");
                String descripcion = rs.getString("descripcion");

                model.addRow(new Object[]{idJuego, nombre, precio, tipoJuego, anioSalida, descripcion});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los juegos.");
            e.printStackTrace();
        }
    }

    private void comprarJuego() {
        int filaSeleccionada = tableJuegos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un juego antes de comprar.");
            return;
        }

        int idJuego = (int) tableJuegos.getModel().getValueAt(filaSeleccionada, 0);
        String nombreJuego = tableJuegos.getModel().getValueAt(filaSeleccionada, 1).toString();

        System.out.println("Intentando abrir VentanaTercera con el juego: " + nombreJuego);

        try {
            VentanaTercera ventanaTercera = new VentanaTercera(usuario.getIdUsuario(), nombreJuego, idJuego);
            ventanaTercera.setVisible(true);
            dispose();  // Cierra la ventana actual (VentanaSegunda) al abrir la de pago
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir la interfaz de pago.");
            ex.printStackTrace();
        }
    }
}