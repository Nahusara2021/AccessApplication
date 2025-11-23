package Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class dbConector {

    private static Connection conexion;

    public static void conectar() throws SQLException {
        if (conexion != null && !conexion.isClosed()) return;

        String url = "jdbc:mysql://localhost:3306/accessapp?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String usuario = "root";
        String contrasena = "accessapp";

        conexion = DriverManager.getConnection(url, usuario, contrasena);
        System.out.println("Conexión exitosa a la base TPAccess");
    }

    public static Connection getConexion() {
        return conexion;
    }


    
    public static List<SubServicio> getSubServiciosPorServicio(int servId) {
        ArrayList<SubServicio> lista = new ArrayList<>();

        String sql = "SELECT sserv_id, sserv_nombre FROM sub_servicios WHERE serv_id = ? ORDER BY sserv_nombre";

        try {
            conectar();
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, servId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new SubServicio(
                        rs.getInt("sserv_id"),
                        rs.getString("sserv_nombre")
                ));
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

     return lista;
    }


    public static List<Servicio> getServicios() {
        ArrayList<Servicio> lista = new ArrayList<>();

        String sql = "SELECT serv_id, serv_nombre FROM servicios ORDER BY serv_nombre";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Servicio(rs.getInt("serv_id"), rs.getString("serv_nombre")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static List<Localidad> getLocalidades() {
        ArrayList<Localidad> lista = new ArrayList<>();

        String sql = "SELECT loc_id, loc_nombre FROM localidad ORDER BY loc_nombre";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Localidad(rs.getInt("loc_id"), rs.getString("loc_nombre")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static List<Discapacidad> getDiscapacidades() {
        ArrayList<Discapacidad> lista = new ArrayList<>();

        String sql = "SELECT td_id, td_nombre FROM tipo_disca ORDER BY td_nombre";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Discapacidad(rs.getInt("td_id"), rs.getString("td_nombre")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public static List<Object[]> getFilas(int loc_id, int discapacidad_id, int servicio_id) {

        ArrayList<Object[]> list = new ArrayList<>();

        String sql =
                "SELECT pr.pre_nombre, sp.direccion, sp.telefono, sp.email, sp.referencias " +
                "FROM pre_servicios sp " +
                "JOIN prestadores pr ON pr.id = sp.pre_id " +
                "WHERE sp.serv_id = ? " +
                "AND sp.loc_id = ? " +
                "AND sp.td_id = ?";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, servicio_id);
            ps.setInt(2, loc_id);
            ps.setInt(3, discapacidad_id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Object[]{
                        rs.getString("nombre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("referencias")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public static Integer insertarPrestador(String pre_nombre) {
        String sql = 
            "INSERT INTO prestadores (pre_nombre) VALUES (?)";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pre_nombre);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    return idGenerado;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public static Integer insertarPreServ(
            int pre_id, int loc_id, int sserv_id,
            String direccion, String telefono,
            String email, String referencias) {

        String sql =
                "INSERT INTO pre_serv " +
                "(pre_id, sserv_id, loc_id, direccion, telefono, email, referencias) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, pre_id);
            ps.setInt(2, sserv_id);
            ps.setInt(3, loc_id);
            ps.setString(4, direccion);
            ps.setString(5, telefono);
            ps.setString(6, email);
            ps.setString(7, referencias);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public static boolean insertarAccesibilidad(int preserv_id, int td_id) {

        String sql = "INSERT INTO accesibilidad (preserv_id, td_id) VALUES (?, ?)";

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, preserv_id);
            ps.setInt(2, td_id);

            ps.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
