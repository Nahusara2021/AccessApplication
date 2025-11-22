package Application;

import javax.swing.*;
import java.awt.*;

public class Ventana extends JFrame {

    public Ventana() {
        setTitle("AccesApp - Panel Principal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new GridLayout(3, 1, 10, 10));

        JButton btnConsultar = new JButton("Consultar Prestadores");
        JButton btnAgregar = new JButton("Agregar Nuevo Prestador");
        JButton btnSalir = new JButton("Salir");

        add(btnConsultar);
        add(btnAgregar);
        add(btnSalir);

        btnConsultar.addActionListener(e -> new Access().setVisible(true));
        btnAgregar.addActionListener(e -> new AgregarPrestador().setVisible(true));
        btnSalir.addActionListener(e -> System.exit(0));
    }
}
