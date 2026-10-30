package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

public final class Theme {

    public static final Color BG = new Color(0x16, 0x16, 0x1A);
    public static final Color SIDEBAR = new Color(0x1B, 0x1B, 0x21);
    public static final Color CARD = new Color(0x1F, 0x1F, 0x26);
    public static final Color FIELD = new Color(0x1D, 0x1D, 0x24);
    public static final Color BORDER = new Color(0x3B, 0x3B, 0x45);
    public static final Color TEXT = new Color(0xF2, 0xF2, 0xF4);
    public static final Color MUTED = new Color(0x9A, 0x9A, 0xA6);
    public static final Color RED = new Color(0xA8, 0x1C, 0x25);

    private Theme() {
    }

    public static Font regular(int ukuran) {
        return new Font("Segoe UI", Font.PLAIN, ukuran);
    }

    public static Font bold(int ukuran) {
        return new Font("Segoe UI", Font.BOLD, ukuran);
    }

    public static JLabel label(String teks, Color warna, int ukuran, boolean tebal) {
        JLabel label = new JLabel(teks);
        label.setFont(tebal ? bold(ukuran) : regular(ukuran));
        label.setForeground(warna);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    public static <T extends JTextField> T field(T field) {
        field.setBackground(FIELD);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setFont(regular(13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    public static JButton button(String teks, boolean utama) {
        JButton button = new JButton(teks);
        button.setFont(bold(12));
        button.setForeground(TEXT);
        button.setBackground(utama ? RED : CARD);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(utama ? RED : BORDER),
                BorderFactory.createEmptyBorder(9, 14, 9, 14)));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        return button;
    }
}
