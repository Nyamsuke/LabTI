package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import util.Dialog;
import util.Session;
import util.Theme;
import view.component.NavButton;

public abstract class DashboardBase extends JFrame {

    private final CardLayout cards = new CardLayout();
    private final JPanel contentPanel = new JPanel(cards);
    private final JLabel lblPageTitle = new JLabel();
    private final JLabel lblPageSubtitle = new JLabel();
    private final List<NavButton> navButtons = new ArrayList<>();
    private final List<String> navCards = new ArrayList<>();
    private JPanel navContainer;

    protected void initDashboard(String roleText, int lebar, int tinggi) {
        setSize(lebar, tinggi);
        setMinimumSize(new Dimension(1024, 640));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG);
        root.add(buatSidebar(roleText), BorderLayout.WEST);
        root.add(buatAreaUtama(), BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel buatSidebar(String roleText) {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(Theme.SIDEBAR);
        sidebar.setPreferredSize(new Dimension(242, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER));

        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBorder(new EmptyBorder(28, 24, 20, 24));
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        brand.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JLabel logo = new JLabel("<html><font color='#F2F2F4'><b>VALORANT</b></font> "
                + "<font color='#E05A63'><b>JOKI</b></font></html>");
        logo.setFont(Theme.bold(17));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel(roleText.toUpperCase());
        sub.setFont(Theme.bold(10));
        sub.setForeground(Theme.MUTED);
        sub.setBorder(new EmptyBorder(6, 0, 0, 0));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        brand.add(logo);
        brand.add(sub);
        sidebar.add(brand);

        JSeparator separator = new JSeparator();
        separator.setForeground(Theme.BORDER);
        separator.setBackground(Theme.BORDER);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sidebar.add(separator);

        navContainer = new JPanel();
        navContainer.setOpaque(false);
        navContainer.setLayout(new BoxLayout(navContainer, BoxLayout.Y_AXIS));
        navContainer.setBorder(new EmptyBorder(16, 10, 16, 10));
        navContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        navContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        sidebar.add(navContainer);

        sidebar.add(Box.createVerticalGlue());

        JPanel user = new JPanel();
        user.setOpaque(false);
        user.setLayout(new BoxLayout(user, BoxLayout.Y_AXIS));
        user.setBorder(new EmptyBorder(14, 24, 20, 24));
        user.setAlignmentX(Component.LEFT_ALIGNMENT);
        user.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

        JLabel nama = new JLabel(Session.getUser().getNama());
        nama.setFont(Theme.bold(12));
        nama.setForeground(Theme.TEXT);
        nama.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel akun = new JLabel("@" + Session.getUser().getUsername()
                + "  \u2022  " + Session.getUser().getRole());
        akun.setFont(Theme.regular(11));
        akun.setForeground(Theme.MUTED);
        akun.setBorder(new EmptyBorder(4, 0, 12, 0));
        akun.setAlignmentX(Component.LEFT_ALIGNMENT);

        javax.swing.JButton btnLogout = Theme.button("LOGOUT", false);
        btnLogout.setFont(Theme.bold(11));
        btnLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btnLogout.setPreferredSize(new Dimension(190, 36));
        btnLogout.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogout.addActionListener(e -> keluar());

        user.add(nama);
        user.add(akun);
        user.add(btnLogout);
        sidebar.add(user);

        return sidebar;
    }

    private JPanel buatAreaUtama() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Theme.BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG);
        header.setBorder(new EmptyBorder(22, 28, 14, 28));

        JPanel kiri = new JPanel();
        kiri.setOpaque(false);
        kiri.setLayout(new BoxLayout(kiri, BoxLayout.Y_AXIS));

        lblPageTitle.setFont(Theme.bold(21));
        lblPageTitle.setForeground(Theme.TEXT);
        lblPageTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblPageSubtitle.setFont(Theme.regular(12));
        lblPageSubtitle.setForeground(Theme.MUTED);
        lblPageSubtitle.setBorder(new EmptyBorder(5, 0, 0, 0));
        lblPageSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        kiri.add(lblPageTitle);
        kiri.add(lblPageSubtitle);

        JLabel halo = new JLabel("Halo, " + Session.getUser().getNama());
        halo.setFont(Theme.regular(12));
        halo.setForeground(Theme.MUTED);

        JPanel kanan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        kanan.setOpaque(false);
        kanan.add(halo);
        kanan.add(chip(Session.getUser().getRole()));

        header.add(kiri, BorderLayout.WEST);
        header.add(kanan, BorderLayout.EAST);

        contentPanel.setBackground(Theme.BG);

        main.add(header, BorderLayout.NORTH);
        main.add(contentPanel, BorderLayout.CENTER);
        return main;
    }

    private JPanel chip(String text) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.setOpaque(false);

        JLabel label = new JLabel(text);
        label.setFont(Theme.bold(10));
        label.setForeground(Theme.TEXT);
        label.setOpaque(true);
        label.setBackground(Theme.RED);
        label.setBorder(new EmptyBorder(4, 10, 4, 10));
        panel.add(label);
        return panel;
    }

    protected NavButton addNav(String text, String card, JPanel panel, String title, String subtitle) {
        NavButton button = new NavButton(text);
        button.addActionListener(e -> showPage(card, title, subtitle));
        navContainer.add(button);
        navContainer.add(Box.createVerticalStrut(4));
        contentPanel.add(panel, card);
        navButtons.add(button);
        navCards.add(card);
        return button;
    }

    protected void showPage(String card, String title, String subtitle) {
        lblPageTitle.setText(title);
        lblPageSubtitle.setText(subtitle);
        cards.show(contentPanel, card);

        for (int i = 0; i < navCards.size(); i++) {
            navButtons.get(i).setActive(navCards.get(i).equals(card));
        }
    }

    protected void keluar() {
        if (Dialog.confirm(this, "Yakin ingin logout dari akun ini?")) {
            Session.logout();
            dispose();
            new LoginForm().setVisible(true);
        }
    }
}
