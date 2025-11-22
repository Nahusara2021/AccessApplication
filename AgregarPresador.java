package Application;

import javax.swing.*;
import java.awt.*;

public class AgregarPrestador extends JFrame {

    JComboBox<SubServicio> cmbSubServicio;
    JComboBox<Servicio> cmbServicio;
    JComboBox<Localidad> cmbLocalidad;
    JComboBox<Discapacidad> cmbDisca;

    JTextField txtPrestadorNombre;
    JTextField txtDireccion;
    JTextField txtTelefono;
    JTextField txtEmail;
    JTextField txtReferencias;

    public AgregarPrestador() {

        setTitle("Agregar Prestador");
        setSize(500, 550);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(9, 2, 10, 10));

        cmbLocalidad = new JComboBox<>();
        cmbDisca = new JComboBox<>();
        cmbServicio = new JComboBox<>();
        cmbSubServicio = new JComboBox<>();

        
        txtPrestadorNombre   = new JTextField();
        txtDireccion         = new JTextField();
        txtTelefono          = new JTextField();
        txtEmail             = new JTextField();
        txtReferencias       = new JTextField();

        
        add(new JLabel("Localidad:"));   add(cmbLocalidad);
        add(new JLabel("Discapacidad:")); add(cmbDisca);
        add(new JLabel("Servicio:"));   add(cmbServicio);
        add(new JLabel("Sub Servicio:"));    add(cmbSubServicio);

        add(new JLabel("Nombre:"));      add(txtPrestadorNombre);
        add(new JLabel("Dirección:"));   add(txtDireccion);
        add(new JLabel("Teléfono:"));    add(txtTelefono);
        add(new JLabel("Email:"));       add(txtEmail);
        add(new JLabel("Referencias:")); add(txtReferencias);

        JButton btnGuardar = new JButton("Guardar Prestador");
        add(btnGuardar);

        cargarCombos();

        btnGuardar.addActionListener(e -> guardar());

        setVisible(true);
    }

    private void cargarCombos() {

        
        cmbLocalidad.removeAllItems();
        cmbDisca.removeAllItems();
        cmbServicio.removeAllItems();
        cmbSubServicio.removeAllItems();


        for (Localidad l : dbConector.getLocalidades())
            cmbLocalidad.addItem(l);

        for (Discapacidad d : dbConector.getDiscapacidades())
            cmbDisca.addItem(d);
        
        for (Servicio s : dbConector.getServicios())
            cmbServicio.addItem(s);
        
        for (SubServicio p : dbConector.getSubServicio())
            cmbSubServicio.addItem(p);
    }

    private void guardar() {

        Localidad loc = (Localidad) cmbLocalidad.getSelectedItem();
        SubServicio sserv = (SubServicio) cmbSubServicio.getSelectedItem();
        
        Integer preId = dbConector.insertarPrestador(txtPrestadorNombre.getText());

        if (preId == null) {
            JOptionPane.showMessageDialog(null, "Error al guardar prestador");
            return;
        }

        boolean ok = dbConector.insertarPreServ(
                preId,               
                loc.id,
                sserv.id,
                txtDireccion.getText(),
                txtTelefono.getText(),
                txtEmail.getText(),
                txtReferencias.getText()
        );

        if (ok) {
            JOptionPane.showMessageDialog(null, "Datos guardados correctamente");
        } else {
            JOptionPane.showMessageDialog(null, "Error guardando relación prestador-servicio");
        }
    }
}
