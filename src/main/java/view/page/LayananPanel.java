package view.page;

import dao.LayananDAO;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Layanan;
import util.Dialog;
import util.Theme;

/**
 * Panel daftar layanan. Dipakai admin (CRUD) dan user (read-only).
 */
public class LayananPanel extends JPanel {

    private static final String[] KOLOM = {
        "ID", "Nama Layanan", "Rank Awal", "Rank Tujuan", "Harga", "Status", "Deskripsi"
    };

    private final LayananDAO dao;
    private final boolean admin;
    private final DefaultTableModel tableModel = new DefaultTableModel(KOLOM, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tabel = new JTable(tableModel);
    private final JButton btnEdit;
    private final JButton btnHapus;
    private final JButton btnStatus;

    public LayananPanel(LayananDAO dao, boolean admin) {
        this.dao = dao;
        this.admin = admin;

        setBackground(Theme.BG);
        setBorder(new EmptyBorder(4, 28, 22, 28));
        setLayout(new BorderLayout(0, 12));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel judul = Theme.label(admin ? "KELOLA LAYANAN" : "DAFTAR LAYANAN",
                Theme.TEXT, 16, true);
        JLabel keterangan = Theme.label(
                admin ? "Tambah, ubah, hapus, dan atur status layanan joki."
                        : "Pilihan layanan joki yang tersedia saat ini.",
                Theme.MUTED, 11, false);
        keterangan.setBorder(new EmptyBorder(4, 0, 0, 0));

        JPanel teks = new JPanel();
        teks.setOpaque(false);
        teks.setLayout(new javax.swing.BoxLayout(teks, javax.swing.BoxLayout.Y_AXIS));
        teks.add(judul);
        teks.add(keterangan);
        header.add(teks, BorderLayout.WEST);

        JPanel aksi = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        aksi.setOpaque(false);

        if (admin) {
            JButton btnTambah = tombol("TAMBAH", true);
            btnTambah.addActionListener(e -> tambah());
            aksi.add(btnTambah);

            btnEdit = tombol("EDIT", false);
            btnEdit.addActionListener(e -> edit());
            aksi.add(btnEdit);

            btnHapus = tombol("HAPUS", false);
            btnHapus.addActionListener(e -> hapus());
            aksi.add(btnHapus);

            btnStatus = tombol("AKTIF/NONAKTIF", false);
            btnStatus.addActionListener(e -> ubahStatus());
            aksi.add(btnStatus);
        } else {
            btnEdit = null;
            btnHapus = null;
            btnStatus = null;
        }

        JButton btnRefresh = tombol("REFRESH", false);
        btnRefresh.addActionListener(e -> refresh());
        aksi.add(btnRefresh);
        header.add(aksi, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        tabel.setRowHeight(30);
        tabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel.setBackground(Theme.CARD);
        tabel.setForeground(Theme.TEXT);
        tabel.setGridColor(Theme.BORDER);
        tabel.setSelectionBackground(Theme.RED);
        tabel.setSelectionForeground(Theme.TEXT);
        tabel.setFont(Theme.regular(12));
        tabel.getTableHeader().setFont(Theme.bold(11));
        tabel.getTableHeader().setBackground(Theme.SIDEBAR);
        tabel.getTableHeader().setForeground(Theme.TEXT);
        tabel.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabel.getColumnModel().getColumn(1).setPreferredWidth(180);
        tabel.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabel.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabel.getColumnModel().getColumn(4).setPreferredWidth(120);
        tabel.getColumnModel().getColumn(5).setPreferredWidth(90);
        tabel.getColumnModel().getColumn(6).setPreferredWidth(240);

        if (admin) {
            tabel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() == 2 && tabel.getSelectedRow() >= 0) {
                        edit();
                    }
                }
            });
            tabel.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    perbaruiTeksTombolStatus();
                }
            });
        }

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBackground(Theme.CARD);
        scroll.getViewport().setBackground(Theme.CARD);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        add(scroll, BorderLayout.CENTER);

        refresh();
    }

    private JButton tombol(String teks, boolean utama) {
        JButton button = Theme.button(teks, utama);
        button.setMaximumSize(new Dimension(160, 40));
        return button;
    }

    public final void refresh() {
        tableModel.setRowCount(0);
        try {
            List<Layanan> daftar = admin ? dao.findAll() : dao.findAktif();
            for (Layanan l : daftar) {
                tableModel.addRow(new Object[]{
                    l.getId(),
                    l.getNamaLayanan(),
                    l.getRankAwal(),
                    l.getRankTujuan(),
                    l.getHargaFormat(),
                    l.getStatus(),
                    l.getDeskripsi() == null ? "" : l.getDeskripsi()
                });
            }
            perbaruiTeksTombolStatus();
        } catch (RuntimeException ex) {
            Dialog.error(this, "Gagal memuat data layanan.\n\n" + ex.getMessage());
        }
    }

    private Layanan terpilih() {
        int baris = tabel.getSelectedRow();
        if (baris < 0) {
            Dialog.warning(this, "Pilih layanan terlebih dahulu.");
            return null;
        }
        int id = (int) tableModel.getValueAt(baris, 0);
        Layanan layanan = dao.findById(id);
        if (layanan == null) {
            Dialog.warning(this, "Layanan tidak ditemukan. Silakan refresh.");
            refresh();
        }
        return layanan;
    }

    private void perbaruiTeksTombolStatus() {
        if (btnStatus == null) {
            return;
        }
        int baris = tabel.getSelectedRow();
        boolean aktif = baris >= 0
                && "Aktif".equalsIgnoreCase(String.valueOf(tableModel.getValueAt(baris, 5)));
        btnStatus.setText(aktif ? "NONAKTIFKAN" : "AKTIFKAN");
    }

    private void tambah() {
        Layanan hasil = tampilForm(new Layanan(), "Tambah Layanan");
        if (hasil == null) {
            return;
        }
        try {
            if (dao.namaExists(hasil.getNamaLayanan(), 0)) {
                Dialog.warning(this, "Nama layanan sudah digunakan.");
                return;
            }
            dao.save(hasil);
            Dialog.info(this, "Layanan berhasil ditambahkan.");
            refresh();
        } catch (RuntimeException ex) {
            Dialog.error(this, "Gagal menyimpan layanan.\n\n" + ex.getMessage());
        }
    }

    private void edit() {
        Layanan dipilih = terpilih();
        if (dipilih == null) {
            return;
        }
        Layanan hasil = tampilForm(dipilih, "Ubah Layanan");
        if (hasil == null) {
            return;
        }
        try {
            if (dao.namaExists(hasil.getNamaLayanan(), hasil.getId())) {
                Dialog.warning(this, "Nama layanan sudah digunakan.");
                return;
            }
            dao.save(hasil);
            Dialog.info(this, "Layanan berhasil diubah.");
            refresh();
        } catch (RuntimeException ex) {
            Dialog.error(this, "Gagal mengubah layanan.\n\n" + ex.getMessage());
        }
    }

    private void hapus() {
        Layanan dipilih = terpilih();
        if (dipilih == null) {
            return;
        }
        if (!Dialog.confirm(this, "Hapus layanan \"" + dipilih.getNamaLayanan() + "\"?")) {
            return;
        }
        try {
            dao.delete(dipilih.getId());
            Dialog.info(this, "Layanan berhasil dihapus.");
            refresh();
        } catch (RuntimeException ex) {
            Dialog.error(this, "Gagal menghapus layanan.\n\n"
                    + "Pastikan layanan belum dipakai oleh pesanan.\n\n" + ex.getMessage());
        }
    }

    private void ubahStatus() {
        Layanan dipilih = terpilih();
        if (dipilih == null) {
            return;
        }
        String statusBaru = dipilih.isAktif() ? "Nonaktif" : "Aktif";
        try {
            dipilih.setStatus(statusBaru);
            dao.save(dipilih);
            Dialog.info(this, "Status layanan diubah menjadi " + statusBaru + ".");
            refresh();
        } catch (RuntimeException ex) {
            Dialog.error(this, "Gagal mengubah status.\n\n" + ex.getMessage());
        }
    }

    private Layanan tampilForm(Layanan sumber, String judul) {
        JTextField txtNama = new JTextField(sumber.getNamaLayanan() == null ? "" : sumber.getNamaLayanan(), 20);
        JTextField txtAwal = new JTextField(sumber.getRankAwal() == null ? "" : sumber.getRankAwal(), 20);
        JTextField txtTujuan = new JTextField(sumber.getRankTujuan() == null ? "" : sumber.getRankTujuan(), 20);
        JTextField txtHarga = new JTextField(
                sumber.getHarga() == null ? "" : sumber.getHarga().toPlainString(), 20);
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Aktif", "Nonaktif"});
        cmbStatus.setSelectedItem(sumber.getStatus() == null ? "Aktif" : sumber.getStatus());
        JTextArea txtDeskripsi = new JTextArea(
                sumber.getDeskripsi() == null ? "" : sumber.getDeskripsi(), 3, 20);
        txtDeskripsi.setLineWrap(true);
        txtDeskripsi.setWrapStyleWord(true);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);

        tambahBaris(form, gbc, "Nama Layanan", Theme.field(txtNama));
        tambahBaris(form, gbc, "Rank Awal", Theme.field(txtAwal));
        tambahBaris(form, gbc, "Rank Tujuan", Theme.field(txtTujuan));
        tambahBaris(form, gbc, "Harga (Rp)", Theme.field(txtHarga));
        tambahBaris(form, gbc, "Status", cmbStatus);
        tambahBaris(form, gbc, "Deskripsi", txtDeskripsi);

        while (true) {
            int pilihan = JOptionPane.showConfirmDialog(this, form, judul,
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (pilihan != JOptionPane.OK_OPTION) {
                return null;
            }

            String nama = txtNama.getText().trim();
            String awal = txtAwal.getText().trim();
            String tujuan = txtTujuan.getText().trim();
            String hargaTeks = txtHarga.getText().trim();

            if (nama.isEmpty() || awal.isEmpty() || tujuan.isEmpty() || hargaTeks.isEmpty()) {
                Dialog.warning(this, "Nama, rank awal, rank tujuan, dan harga wajib diisi.");
                continue;
            }

            BigDecimal harga;
            try {
                harga = new BigDecimal(hargaTeks.replace(".", "").replace(",", "."));
                if (harga.signum() <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                Dialog.warning(this, "Harga harus berupa angka positif. Contoh: 40000");
                continue;
            }

            sumber.setNamaLayanan(nama);
            sumber.setRankAwal(awal);
            sumber.setRankTujuan(tujuan);
            sumber.setHarga(harga);
            sumber.setDeskripsi(txtDeskripsi.getText().trim());
            sumber.setStatus(String.valueOf(cmbStatus.getSelectedItem()));
            return sumber;
        }
    }

    private void tambahBaris(JPanel panel, GridBagConstraints gbc, String label, java.awt.Component field) {
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(Theme.label(label, Theme.MUTED, 11, true), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
        gbc.gridy++;
    }
}
