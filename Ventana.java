package Application;

import javax.swing.*;
import java.awt.*;

public class Ventana extends JFrame {

    public Ventana() {
        setTitle("AccesApp - Panel Principal");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);


        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout(0, 20));

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(Color.BLACK);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        
        JLabel lblTitulo = new JLabel("Bienvenido a AccessApp");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI Variable", Font.BOLD, 20));
        panelTitulo.add(lblTitulo);

        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 10, 10));
        panelBotones.setBackground(Color.BLACK);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));

        JButton btnConsultar = new JButton("Consultar Prestadores");
        JButton btnAgregar = new JButton("Agregar Nuevo Prestador");
        JButton btnSalir = new JButton("Salir");
        
        setupDarkButton(btnConsultar);
        setupDarkButton(btnAgregar);
        setupDarkButton(btnSalir);

        panelBotones.add(btnConsultar);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnSalir);

        add(panelTitulo, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);

        btnConsultar.addActionListener(e -> new Access().setVisible(true));
        btnAgregar.addActionListener(e -> new AgregarPrestador().setVisible(true));
        btnSalir.addActionListener(e -> System.exit(0));
    }

    private void setupDarkButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(255, 140, 0));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI Variable", Font.BOLD, 13));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 160, 40));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 140, 0));
            }
        });
    }
}
