package view;

import dao.LayananDAO;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import main.Main;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.springframework.context.ApplicationContext;
import util.Dialog;
import util.Session;
import util.Theme;
import view.component.NavButton;
import view.page.LayananPanel;

public class DashboardAdmin extends DashboardBase {

    private static final String NAMA_LAPORAN = "Simple_Blue.jrxml";
    private static final String PATH_LAPORAN = "src/main/java/reports/" + NAMA_LAPORAN;

    public DashboardAdmin() {
        setTitle("VALORANT JOKI - Dashboard Admin");
        initDashboard("Admin Panel", 1280, 760);
        addNav("Beranda", "beranda", buatBeranda(),
                "Dashboard", "Selamat datang di panel admin");

        LayananDAO layananDAO = Main.getContext().getBean(LayananDAO.class);
        LayananPanel layananPanel = new LayananPanel(layananDAO, true);
        NavButton navLayanan = addNav("Kelola Layanan", "layanan", layananPanel,
                "Kelola Layanan", "Tambah, ubah, hapus, dan atur status layanan");
        navLayanan.addActionListener(e -> layananPanel.refresh());

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

        JLabel keterangan = Theme.label(
                "Cetak laporan data layanan menggunakan JasperReports.",
                Theme.MUTED, 12, false);
        keterangan.setBorder(new EmptyBorder(8, 0, 16, 0));
        kartu.add(keterangan);

        JPanel tombol = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        tombol.setOpaque(false);
        tombol.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnLihat = tombol("LIHAT LAPORAN", false);
        btnLihat.addActionListener(e -> reportActionPerformed(null));

        JButton btnCetak = tombol("CETAK LAPORAN", true);
        btnCetak.addActionListener(e -> cetakLaporan());

        tombol.add(btnLihat);
        tombol.add(btnCetak);
        kartu.add(tombol);

        panel.add(kartu, BorderLayout.NORTH);
        return panel;
    }

    private JButton tombol(String teks, boolean utama) {
        JButton button = Theme.button(teks, utama);
        button.setMaximumSize(new Dimension(210, 40));
        return button;
    }

    /** Handler laporan dari kode yang diberikan: menampilkan preview JasperViewer. */
    private void reportActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            JasperPrint jp = buatJasperPrint();
            JasperViewer.viewReport(jp, false);
        } catch (Exception ex) {
            Dialog.error(this, "Gagal menampilkan laporan.\n\n" + ex.getMessage());
        }
    }

    /** Membuka dialog printer untuk mencetak laporan langsung. */
    private void cetakLaporan() {
        try {
            JasperPrint jp = buatJasperPrint();
            boolean berhasil = JasperPrintManager.printReport(jp, true);
            if (!berhasil) {
                Dialog.info(this, "Pencetakan dibatalkan.");
            }
        } catch (Exception ex) {
            Dialog.error(this, "Gagal mencetak laporan.\n\n" + ex.getMessage());
        }
    }

    /** Menyusun JasperPrint: compile .jrxml -> isi dengan koneksi Spring DataSource. */
    private JasperPrint buatJasperPrint() throws Exception {
        JasperReport jr = kompilLaporan();

        // Koneksi database dari Spring DataSource
        ApplicationContext context = Main.getContext();
        DataSource ds = context.getBean(DataSource.class);
        try (Connection conn = ds.getConnection()) {
            Map<String, Object> param = new HashMap<>();
            return JasperFillManager.fillReport(jr, param, conn);
        }
    }

    private JasperReport kompilLaporan() throws Exception {
        File file = new File(PATH_LAPORAN);
        if (file.exists()) {
            return JasperCompileManager.compileReport(file.getAbsolutePath());
        }

        // Fallback saat aplikasi dijalankan dari JAR: baca dari classpath
        try (InputStream in = getClass().getResourceAsStream("/reports/" + NAMA_LAPORAN)) {
            if (in == null) {
                throw new FileNotFoundException("File laporan " + NAMA_LAPORAN
                        + " tidak ditemukan di " + PATH_LAPORAN);
            }
            return JasperCompileManager.compileReport(in);
        }
    }
}
