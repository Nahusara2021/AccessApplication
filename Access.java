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

    private final Font fontLabel = new Font("Segoe UI Variable", Font.BOLD, 14);
    private final Font fontCombo = new Font("Segoe UI Variable", Font.PLAIN, 14);
    private final Font fontButton = new Font("Segoe UI Variable", Font.BOLD, 14);
    private final Color backgroundColor = Color.BLACK;
    private final Color fieldBackground = new Color(30, 30, 30);

    public Access() {
        setTitle("Consulta de Servicios Accesibles");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(backgroundColor);
        setLayout(new BorderLayout(10, 10));

        JPanel panelFiltros = new JPanel(new GridLayout(5, 2, 10, 10));
        panelFiltros.setBackground(backgroundColor);
        panelFiltros.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        cmbLocalidad = createDarkComboBox();
        cmbTipoDisca = createDarkComboBox();
        cmbServicio = createDarkComboBox();
        cmbSubServicio = createDarkComboBox();

        JLabel lblLocalidad = createDarkLabel("Localidad:");
        JLabel lblTipoDisca = createDarkLabel("Tipo discapacidad:");
        JLabel lblServicio = createDarkLabel("Servicio:");
        JLabel lblSubServicio = createDarkLabel("Sub-servicio:");

        panelFiltros.add(lblLocalidad);
        panelFiltros.add(cmbLocalidad);

        panelFiltros.add(lblTipoDisca);
        panelFiltros.add(cmbTipoDisca);

        panelFiltros.add(lblServicio);
        panelFiltros.add(cmbServicio);

        panelFiltros.add(lblSubServicio);
        panelFiltros.add(cmbSubServicio);

        JButton btnBuscar = new JButton("Buscar");
        setupDarkButton(btnBuscar);
        
        panelFiltros.add(new JLabel());
        panelFiltros.add(btnBuscar);

        add(panelFiltros, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{
                "Prestador", "Dirección", "Teléfono", "Email", "Referencia"
        }, 0);

        tabla = new JTable(modeloTabla) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tabla.setBackground(fieldBackground);
        tabla.setForeground(Color.WHITE);
        tabla.setGridColor(new Color(80, 80, 80));
        tabla.setSelectionBackground(new Color(255, 140, 0));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setFont(new Font("Segoe UI Variable", Font.PLAIN, 13));
        tabla.setRowHeight(25);
        
        tabla.getTableHeader().setBackground(new Color(50, 50, 50));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("Segoe UI Variable", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBackground(backgroundColor);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(fieldBackground);
        
        add(scrollPane, BorderLayout.CENTER);

        cargarLocalidades();
        cargarTiposDisca();
        cargarServicios();

        cmbServicio.addActionListener(e -> cargarSubServicios());
        btnBuscar.addActionListener(e -> buscar());

        setVisible(true);
    }

    private JComboBox<Item> createDarkComboBox() {
        JComboBox<Item> combo = new JComboBox<Item>() {
            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        
        combo.setBackground(backgroundColor);
        combo.setForeground(Color.WHITE);
        combo.setFont(fontCombo);
        combo.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setBackground(isSelected ? new Color(255, 140, 0) : backgroundColor);
                c.setForeground(Color.WHITE);
                if (c instanceof JComponent) {
                    ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                }
                return c;
            }
        });
        
        return combo;
    }

    private JLabel createDarkLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(fontLabel);
        return label;
    }

    private void setupDarkButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(new Color(255, 140, 0));
        button.setForeground(Color.WHITE);
        button.setFont(fontButton);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 160, 40));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 140, 0));
            }
        });
    }

    private void cargarLocalidades() {
        try {
            dbConector.conectar();
            Connection con = dbConector.getConexion();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT loc_id, loc_nombre FROM localidad ORDER BY loc_nombre");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                cmbLocalidad.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarTiposDisca() {
        try {
            dbConector.conectar();
            Connection con = dbConector.getConexion();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT td_id, td_nombre FROM tipo_disca ORDER BY td_nombre");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                cmbTipoDisca.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarServicios() {
        try {
            dbConector.conectar();
            Connection con = dbConector.getConexion();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT serv_id, serv_nombre FROM servicios ORDER BY serv_nombre");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                cmbServicio.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarSubServicios() {
        cmbSubServicio.removeAllItems();

        Item serv = (Item) cmbServicio.getSelectedItem();
        if (serv == null) return;

        try {
            dbConector.conectar();
            Connection con = dbConector.getConexion();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT sserv_id, sserv_nombre FROM sub_servicios WHERE serv_id = ? ORDER BY sserv_nombre");

            ps.setInt(1, serv.id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cmbSubServicio.addItem(new Item(rs.getInt(1), rs.getString(2)));
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                        "AND acc.td_id = ? " +
                        "AND ss.serv_id = ? " +
                        "AND ps.sserv_id = ?";

        try {
            dbConector.conectar();
            Connection con = dbConector.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);

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

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Access());
    }
}


