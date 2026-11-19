package view;

import dao.LayananDAO;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import main.Main;
import util.Session;
import util.Theme;
import view.component.NavButton;
import view.page.LayananPanel;

public class DashboardUser extends DashboardBase {

    public DashboardUser() {
        setTitle("VALORANT JOKI - Dashboard User");
        initDashboard("User Panel", 1180, 720);
        addNav("Beranda", "beranda", buatBeranda(),
                "Dashboard", "Selamat datang di panel user");

        LayananDAO layananDAO = Main.getContext().getBean(LayananDAO.class);
        LayananPanel layananPanel = new LayananPanel(layananDAO, false);
        NavButton navLayanan = addNav("Daftar Layanan", "layanan", layananPanel,
                "Daftar Layanan", "Lihat layanan joki yang tersedia");
        navLayanan.addActionListener(e -> layananPanel.refresh());

        showPage("beranda", "Dashboard", "Selamat datang di panel user");
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
