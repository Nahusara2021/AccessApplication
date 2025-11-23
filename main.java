package Application;

import javax.swing.*;

public class main {

    public static void main(String[] args) {

        UIStyle.apply();
        
        try {
            dbConector.conectar();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error conectando DB: " + e.getMessage());
            return;
        }

        SwingUtilities.invokeLater(() -> new Ventana().setVisible(true));
    }
}
