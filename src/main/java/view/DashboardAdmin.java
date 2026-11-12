package view;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import util.Session;
import util.Theme;

public class DashboardAdmin extends DashboardBase {

    public DashboardAdmin() {
        setTitle("VALORANT JOKI - Dashboard Admin");
        initDashboard("Admin Panel", 1280, 760);
        addNav("Beranda", "beranda", buatBeranda(),
                "Dashboard", "Selamat datang di panel admin");
        showPage("beranda", "Dashboard", "Selamat datang di panel admin");
    }

    private JPanel buatBeranda() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BG);
        panel.setBorder(new EmptyBorder(4, 28, 22, 28));

        JPanel kartu = new JPanel();
        kartu.setOpaque(true);
        kartu.setBackground(Theme.CARD);
        kartu.setLayout(new BoxLayout(kartu, BoxLayout.Y_AXIS));
        kartu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER),
                new EmptyBorder(24, 26, 24, 26)));

        JLabel judul = new JLabel("Halo, " + Session.getUser().getNama() + "!");
        judul.setFont(Theme.bold(20));
        judul.setForeground(Theme.TEXT);
        judul.setAlignmentX(Component.LEFT_ALIGNMENT);

        kartu.add(judul);
        panel.add(kartu, BorderLayout.NORTH);
        return panel;
    }
}
