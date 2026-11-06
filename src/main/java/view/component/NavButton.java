package view.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import util.Theme;

public class NavButton extends JButton {

    private boolean active;

    public NavButton(String text) {
        super(text);
        setHorizontalAlignment(SwingConstants.LEFT);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setRolloverEnabled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setFont(Theme.regular(13));
        setForeground(Theme.MUTED);
        setBorder(new EmptyBorder(12, 24, 12, 16));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public void setActive(boolean active) {
        this.active = active;
        setForeground(active ? Theme.TEXT : Theme.MUTED);
        setFont(active ? Theme.bold(13) : Theme.regular(13));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (active) {
            g2.setColor(new Color(0x2B, 0x2B, 0x34));
            g2.fillRoundRect(10, 3, getWidth() - 20, getHeight() - 6, 8, 8);
            g2.setColor(Theme.RED);
            g2.fillRoundRect(0, 11, 3, getHeight() - 22, 3, 3);
        } else if (getModel().isRollover()) {
            g2.setColor(new Color(0x24, 0x24, 0x2C));
            g2.fillRoundRect(10, 3, getWidth() - 20, getHeight() - 6, 8, 8);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
