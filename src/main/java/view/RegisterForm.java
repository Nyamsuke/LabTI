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
import util.Theme;

public class RegisterForm extends JFrame {

    private final JTextField txtNama = Theme.field(new JTextField(20));
    private final JTextField txtUsername = Theme.field(new JTextField(20));
    private final JPasswordField txtPassword = Theme.field(new JPasswordField(20));
    private final JPasswordField txtKonfirmasi = Theme.field(new JPasswordField(20));
    private final UserDAO dao = new UserDAO();
    private int row;

    public RegisterForm() {
        setTitle("Daftar Akun");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(400, 520);
        setLocationRelativeTo(null);
        setContentPane(build());
    }

    private JPanel build() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Theme.CARD);
        card.setBorder(new EmptyBorder(24, 26, 26, 26));

        JButton btnDaftar = Theme.button("DAFTAR", true);
        JButton btnKembali = Theme.button("Kembali ke Login", false);

        JLabel judul = Theme.label("DAFTAR AKUN", Theme.TEXT, 22, true);
        judul.setHorizontalAlignment(JLabel.CENTER);
        JLabel sub = Theme.label("Buat akun baru untuk memesan jasa joki", Theme.MUTED, 12, false);
        sub.setHorizontalAlignment(JLabel.CENTER);

        add(card, judul, 4);
        add(card, sub, 18);
        add(card, Theme.label("Nama Lengkap", Theme.MUTED, 11, true), 4);
        add(card, txtNama, 12);
        add(card, Theme.label("Username", Theme.MUTED, 11, true), 4);
        add(card, txtUsername, 12);
        add(card, Theme.label("Password (min. 6 karakter)", Theme.MUTED, 11, true), 4);
        add(card, txtPassword, 12);
        add(card, Theme.label("Konfirmasi Password", Theme.MUTED, 11, true), 4);
        add(card, txtKonfirmasi, 18);
        add(card, btnDaftar, 8);
        add(card, btnKembali, 0);

        btnDaftar.addActionListener(e -> daftar());
        btnKembali.addActionListener(e -> kembali());
        getRootPane().setDefaultButton(btnDaftar);

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

    private void daftar() {
        String nama = txtNama.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String konfirmasi = new String(txtKonfirmasi.getPassword());

        if (nama.isEmpty() || username.isEmpty() || password.isEmpty() || konfirmasi.isEmpty()) {
            Dialog.warning(this, "Semua field wajib diisi.");
            return;
        }

        if (password.length() < 6) {
            Dialog.warning(this, "Password minimal 6 karakter.");
            txtPassword.requestFocus();
            return;
        }

        if (!password.equals(konfirmasi)) {
            Dialog.warning(this, "Konfirmasi password tidak cocok.");
            txtKonfirmasi.setText("");
            txtKonfirmasi.requestFocus();
            return;
        }

        try {
            if (dao.usernameExists(username)) {
                Dialog.warning(this, "Username sudah digunakan. Pilih username lain.");
                txtUsername.requestFocus();
                return;
            }

            User user = new User();
            user.setNama(nama);
            user.setUsername(username);
            user.setPassword(password);
            user.setRole("user");
            dao.insert(user);

            Dialog.info(this, "Akun berhasil dibuat. Silakan login.");
            dispose();
            new LoginForm().setVisible(true);
        } catch (RuntimeException ex) {
            Dialog.error(this, "Gagal menyimpan akun.\n\n" + ex.getMessage());
        }
    }

    private void kembali() {
        dispose();
        new LoginForm().setVisible(true);
    }
}
