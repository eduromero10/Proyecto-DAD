package org.example;

import javax.swing.*;

public class VentanaPrincipal {
    private JPanel PanelPrincipal;
    private JLabel Jtitulo;
    private JPanel panelCentral;
    private JLabel JRegistro;
    private JPanel JPanel;
    private JPasswordField passwordField1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("VentanaPrincipal");
        frame.setContentPane(new VentanaPrincipal().PanelPrincipal);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
