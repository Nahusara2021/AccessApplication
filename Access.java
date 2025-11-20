package Application;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Access extends JFrame {

    JComboBox<Item> cmbLocalidad;
    JComboBox<Item> cmbTipoDisca;
    JComboBox<Item> cmbServicio;
    JComboBox<Item> cmbSubServicio;
    JTable tabla;
    DefaultTableModel modeloTabla;

    public Access() {
        setTitle("Consulta de Servicios Accesibles");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // ------------------ PANEL SUPERIOR ------------------
        JPanel panelFiltros = new JPanel(new GridLayout(5, 2, 10, 10));

        cmbLocalidad = new JComboBox<>();
        cmbTipoDisca = new JComboBox<>();
        cmbServicio = new JComboBox<>();
        cmbSubServicio = new JComboBox<>();

        panelFiltros.add(new JLabel("Localidad:"));
        panelFiltros.add(cmbLocalidad);

        panelFiltros.add(new JLabel("Tipo discapacidad:"));
        panelFiltros.add(cmbTipoDisca);

        panelFiltros.add(new JLabel("Servicio:"));
        panelFiltros.add(cmbServicio);

        panelFiltros.add(new JLabel("Sub-servicio:"));
        panelFiltros.add(cmbSubServicio);

        JButton btnBuscar = new JButton("Buscar");
        panelFiltros.add(btnBuscar);

        add(panelFiltros, BorderLayout.NORTH);

        // ------------------ TABLA ------------------
        modeloTabla = new DefaultTableModel(new String[]{
            "Prestador", "Dirección", "Teléfono", "Email", "Referencia"
        }, 0);

        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // ------------------ EVENTOS ------------------
        cargarLocalidades();
        cargarTiposDisca();
        cargarServicios();

        // Evento: cuando el usuario cambia de servicio → cargar subservicios
        cmbServicio.addActionListener(e -> cargarSubServicios());

        btnBuscar.addActionListener(e -> buscar());

        setVisible(true);
    }

    // -------------------------------------------------------------
    // Cargar combos
    // -------------------------------------------------------------

    private void cargarLocalidades() {
        try (Connection con = dbConector.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT loc_id, loc_nombre FROM localidad ORDER BY loc_nombre");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cmbLocalidad.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarTiposDisca() {
        try (Connection con = dbConector.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT td_id, td_nombre FROM tipo_disca ORDER BY td_nombre");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cmbTipoDisca.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarServicios() {
        try (Connection con = dbConector.conectar();
             PreparedStatement ps = con.prepareStatement("SELECT serv_id, serv_nombre FROM servicios ORDER BY serv_nombre");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cmbServicio.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarSubServicios() {
        cmbSubServicio.removeAllItems();

        Item serv = (Item) cmbServicio.getSelectedItem();
        if (serv == null) return;

        try (Connection con = dbConector.conectar();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT sserv_id, sserv_nombre FROM sub_servicios WHERE serv_id = ? ORDER BY sserv_nombre")) {

            ps.setInt(1, serv.id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    cmbSubServicio.addItem(new Item(rs.getInt(1), rs.getString(2)));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------
    // Búsqueda final
    // -------------------------------------------------------------

    private void buscar() {
        modeloTabla.setRowCount(0);

        Item loc = (Item) cmbLocalidad.getSelectedItem();
        Item tipo = (Item) cmbTipoDisca.getSelectedItem();
        Item servicio = (Item) cmbServicio.getSelectedItem();
        Item sub = (Item) cmbSubServicio.getSelectedItem();

        if (loc == null || tipo == null || servicio == null || sub == null) return;

        String sql =
                "SELECT pr.pre_nombre, ps.direccion, ps.telefono, ps.email, ps.referencias " +
                "FROM pre_serv ps " +
                "JOIN prestadores pr ON pr.pre_id = ps.pre_id " +
                "JOIN sub_servicios ss ON ss.sserv_id = ps.sserv_id " +
                "LEFT JOIN accesibilidad acc ON acc.preserv_id = ps.preserv_id " +
                "WHERE ps.loc_id = ? " +
                "AND acc.tp_id = ? " +
                "AND ss.serv_id = ? " +
                "AND ps.sserv_id = ?";

        try (Connection con = dbConector.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, loc.id);
            ps.setInt(2, tipo.id);
            ps.setInt(3, servicio.id);
            ps.setInt(4, sub.id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                        rs.getString("pre_nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("referencias")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------
    // Clase auxiliar para llenar combos
    // -------------------------------------------------------------

    class Item {
        int id;
        String label;

        Item(int id, String label) {
            this.id = id;
            this.label = label;
        }

        public String toString() {
            return label;
        }
    }

    // -------------------------------------------------------------

    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        new Access();
        });
    }
}
