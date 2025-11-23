package Application;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;

public class UIStyle {

    public static void apply() {
        try {
            FlatDarkLaf.setup();

            Font regular = new Font("Segoe UI Variable", Font.PLAIN, 14);
            Font bold = new Font("Segoe UI Variable", Font.BOLD, 15);

            UIManager.put("defaultFont", regular);

            UIManager.put("Label.font", bold);
            UIManager.put("Label.foreground", Color.WHITE);

            UIManager.put("TextField.font", regular);
            UIManager.put("TextField.background", new Color(30, 30, 30));
            UIManager.put("TextField.foreground", Color.WHITE);
            UIManager.put("TextField.caretForeground", Color.WHITE);

            UIManager.put("ComboBox.font", regular);
            UIManager.put("ComboBox.background", new Color(30, 30, 30));
            UIManager.put("ComboBox.foreground", Color.WHITE);

            UIManager.put("Button.font", bold);
            UIManager.put("Button.background", new Color(255, 140, 0));
            UIManager.put("Button.foreground", Color.WHITE);

            UIManager.put("Panel.background", Color.BLACK);

        } catch (Exception ex) {
            System.out.println("No se pudo aplicar FlatLaf");
        }
    }
}
