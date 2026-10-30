package util;

import java.awt.Component;
import javax.swing.JOptionPane;

public final class Dialog {

    private Dialog() {
    }

    public static void info(Component parent, String pesan) {
        JOptionPane.showMessageDialog(parent, pesan, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void warning(Component parent, String pesan) {
        JOptionPane.showMessageDialog(parent, pesan, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }

    public static void error(Component parent, String pesan) {
        JOptionPane.showMessageDialog(parent, pesan, "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirm(Component parent, String pesan) {
        return JOptionPane.showConfirmDialog(parent, pesan, "Konfirmasi",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
