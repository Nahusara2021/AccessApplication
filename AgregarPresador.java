package Application;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    private final Font fontLabel = new Font("Segoe UI Variable", Font.BOLD, 14);
    private final Font fontField = new Font("Segoe UI Variable", Font.PLAIN, 14);

    public AgregarPrestador() {

        setTitle("Agregar Prestador");
        setSize(520, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panel);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        cmbLocalidad = new JComboBox<>();
        cmbDisca = new JComboBox<>();
        cmbServicio = new JComboBox<>();
        cmbSubServicio = new JComboBox<>();

        txtPrestadorNombre = new JTextField();
        txtDireccion = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        txtReferencias = new JTextField();

        int y = 0;

        addField(panel, c, y++, "Localidad:", cmbLocalidad);
        addField(panel, c, y++, "Discapacidad:", cmbDisca);
        addField(panel, c, y++, "Servicio:", cmbServicio);
        addField(panel, c, y++, "Sub Servicio:", cmbSubServicio);

        addField(panel, c, y++, "Nombre:", txtPrestadorNombre);
        addField(panel, c, y++, "Dirección:", txtDireccion);
        addField(panel, c, y++, "Teléfono:", txtTelefono);
        addField(panel, c, y++, "Email:", txtEmail);
        addField(panel, c, y++, "Referencias:", txtReferencias);

        JButton btnGuardar = new JButton("Guardar Prestador");
        btnGuardar.setFocusPainted(false);

        c.gridx = 0;
        c.gridy = y;
        c.gridwidth = 2;
        panel.add(btnGuardar, c);

        cargarCombos();

        btnGuardar.addActionListener(e -> guardar());

        setVisible(true);
    }

    private void addField(JPanel panel, GridBagConstraints c, int y, String label, JComponent field) {
        c.gridx = 0;
        c.gridy = y;
        c.weightx = 0.3;

        JLabel lbl = new JLabel(label);
        lbl.setFont(fontLabel);
        panel.add(lbl, c);

        c.gridx = 1;
        c.weightx = 0.7;

        field.setFont(fontField);
        panel.add(field, c);
    }
    
    private void cargarSubServicios() {
        cmbSubServicio.removeAllItems();

        Servicio serv = (Servicio) cmbServicio.getSelectedItem();
        if (serv == null) return;

        try {
            dbConector.conectar();
            Connection con = dbConector.getConexion();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT sserv_id, sserv_nombre FROM sub_servicios WHERE serv_id = ? ORDER BY sserv_nombre");

            ps.setInt(1, serv.id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cmbSubServicio.addItem(new SubServicio(
                        rs.getInt("sserv_id"),
                        rs.getString("sserv_nombre")
                ));
            }

            rs.close();
            ps.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    

    private void cargarCombos() {
        cmbLocalidad.removeAllItems();
        cmbDisca.removeAllItems();
        cmbServicio.removeAllItems();
        cmbServicio.addActionListener(e -> cargarSubServicios());
        cmbSubServicio.removeAllItems();

        for (Localidad l : dbConector.getLocalidades())
            cmbLocalidad.addItem(l);

        for (Discapacidad d : dbConector.getDiscapacidades())
            cmbDisca.addItem(d);

        for (Servicio s : dbConector.getServicios())
            cmbServicio.addItem(s);

    }

    private void guardar() {

        Localidad loc = (Localidad) cmbLocalidad.getSelectedItem();
        SubServicio sub = (SubServicio) cmbSubServicio.getSelectedItem();
        Discapacidad disca = (Discapacidad) cmbDisca.getSelectedItem();

        Integer preId = dbConector.insertarPrestador(txtPrestadorNombre.getText());

        if (preId == null) {
            JOptionPane.showMessageDialog(this, "Error al guardar prestador");
            return;
        }

        Integer preservId = dbConector.insertarPreServ(
                preId,
                loc.id,
                sub.id,
                txtDireccion.getText(),
                txtTelefono.getText(),
                txtEmail.getText(),
                txtReferencias.getText()
        );

        if (preservId == null) {
            JOptionPane.showMessageDialog(this, "Error guardando relación prestador-servicio");
            return;
        }

        // Insertar en accesibilidad
        boolean okAcc = dbConector.insertarAccesibilidad(preservId, disca.id);

        if (okAcc)
            JOptionPane.showMessageDialog(this, "Datos guardados correctamente");
        else
            JOptionPane.showMessageDialog(this, "Error guardando accesibilidad");
    }
}
