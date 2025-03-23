package MiVentana;

import javax.swing.*;

public class MiVentana extends JFrame {
    public MiVentana() {
        setTitle("Mi Ventana");
        setSize(1500,1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    public void mostrar() {
        setVisible(true);
    }
}