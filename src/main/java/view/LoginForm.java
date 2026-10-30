package view;

import dao.UserDAO;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import model.User;
import util.Dialog;
import util.Session;
import util.Theme;

public class LoginForm extends JFrame {

    private final JTextField txtUsername = Theme.field(new JTextField(20));
    private final JPasswordField txtPassword = Theme.field(new JPasswordField(20));
    private final UserDAO dao = new UserDAO();
    private int row;

    public LoginForm() {
        setTitle("Joki Valorant");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(380, 400);
        setLocationRelativeTo(null);
        setContentPane(build());
    }

    private JPanel build() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Theme.CARD);
        card.setBorder(new EmptyBorder(24, 26, 26, 26));

        JButton btnLogin = Theme.button("LOGIN", true);
        JButton btnDaftar = Theme.button("Daftar Akun", false);

        JLabel judul = Theme.label("VALORANT JOKI", Theme.TEXT, 25, true);
        judul.setHorizontalAlignment(JLabel.CENTER);
        JLabel sub = Theme.label("Silakan login ke akun kamu", Theme.MUTED, 12, false);
        sub.setHorizontalAlignment(JLabel.CENTER);

        add(card, judul, 4);
        add(card, sub, 18);
        add(card, Theme.label("Username", Theme.MUTED, 11, true), 4);
        add(card, txtUsername, 12);
        add(card, Theme.label("Password", Theme.MUTED, 11, true), 4);
        add(card, txtPassword, 20);
        add(card, btnLogin, 8);
        add(card, btnDaftar, 0);

        btnLogin.addActionListener(e -> login());
        btnDaftar.addActionListener(e -> Dialog.info(this, "-"));
        getRootPane().setDefaultButton(btnLogin);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG);
        root.setBorder(new EmptyBorder(18, 18, 18, 18));
        root.add(card, BorderLayout.CENTER);
        return root;
    }

    private void add(JPanel panel, java.awt.Component comp, int gap) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, gap, 0);
        panel.add(comp, gbc);
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            Dialog.warning(this, "Username dan password wajib diisi.");
            return;
        }

        try {
            User user = dao.login(username, password);
            if (user == null) {
                Dialog.error(this, "Username atau password salah.");
                txtPassword.setText("");
                txtPassword.requestFocus();
                return;
            }

            Session.login(user);
            Dialog.info(this, "Login berhasil. Selamat datang, " + user.getNama()
                    + " (" + user.getRole() + ").");
            txtPassword.setText("");
        } catch (RuntimeException ex) {
            Dialog.error(this, "Gagal terhubung ke database.\n\n" + ex.getMessage());
        }
    }
}
