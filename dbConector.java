package Application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConector {

    private static final String url = "jdbc:mysql://localhost:3306/AccessApp?useSSL=false&serverTimezone=UTC";
    private static final String user = "root";
    private static final String pass = "accessapp";

    public static Connection conectar() {

        Connection con = null;

        try {
            // Forzar carga del driver (importante en JDBC 9)
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexion exitosa a la base AccessApp");

        } catch (ClassNotFoundException e) {
            System.out.println("No se encontró el Driver JDBC de MySQL");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos");
            e.printStackTrace();
        }

        return con;
    }
}
