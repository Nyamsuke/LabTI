package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Entity
@Table(name = "layanan")
public class Layanan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nama_layanan", nullable = false, length = 100)
    private String namaLayanan;

    @Column(name = "rank_awal", nullable = false, length = 50)
    private String rankAwal;

    @Column(name = "rank_tujuan", nullable = false, length = 50)
    private String rankTujuan;

    @Column(name = "harga", nullable = false)
    private BigDecimal harga;

    @Column(name = "deskripsi")
    private String deskripsi;

    @Column(name = "status", length = 20)
    private String status = "Aktif";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaLayanan() {
        return namaLayanan;
    }

    public void setNamaLayanan(String namaLayanan) {
        this.namaLayanan = namaLayanan;
    }

    public String getRankAwal() {
        return rankAwal;
    }

    public void setRankAwal(String rankAwal) {
        this.rankAwal = rankAwal;
    }

    public String getRankTujuan() {
        return rankTujuan;
    }

    public void setRankTujuan(String rankTujuan) {
        this.rankTujuan = rankTujuan;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    public void setHarga(BigDecimal harga) {
        this.harga = harga;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAktif() {
        return "Aktif".equalsIgnoreCase(status);
    }

    public String getHargaFormat() {
        if (harga == null) {
            return "-";
        }
        return NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID")).format(harga);
    }
}
