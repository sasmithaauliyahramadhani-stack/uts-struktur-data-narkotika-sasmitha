package id.ac.umm.strukturdata.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Model data putusan pengadilan narkotika.
 * Semua 16 atribut wajib dari panduan UTS + urlSumber sebagai field pendukung provenance.
 * Immutable setelah konstruksi (tidak ada setter publik).
 */
public final class Putusan {

    private final String caseId;
    private final String nomorPerkara;
    private final String pengadilan;
    private final LocalDate tanggalPutusan;
    private final String terdakwaKode;
    private final String jenisNarkotika;
    private final double beratBarangBuktiGram;
    private final String pasalDakwaan;
    private final String pasalTerbukti;
    private final int tuntutanBulan;
    private final int vonisBulan;
    private final long dendaRupiah;
    private final String barangBukti;
    private final String keadaanMemberatkan;
    private final String keadaanMeringankan;
    private final String sourcePages;
    private final String urlSumber; // field pendukung, bukan bagian skema minimum

    public Putusan(
            String caseId,
            String nomorPerkara,
            String pengadilan,
            LocalDate tanggalPutusan,
            String terdakwaKode,
            String jenisNarkotika,
            double beratBarangBuktiGram,
            String pasalDakwaan,
            String pasalTerbukti,
            int tuntutanBulan,
            int vonisBulan,
            long dendaRupiah,
            String barangBukti,
            String keadaanMemberatkan,
            String keadaanMeringankan,
            String sourcePages,
            String urlSumber) {

        this.caseId = Objects.requireNonNull(caseId, "caseId tidak boleh null");
        this.nomorPerkara = Objects.requireNonNull(nomorPerkara, "nomorPerkara tidak boleh null");
        this.pengadilan = pengadilan;
        this.tanggalPutusan = tanggalPutusan;
        this.terdakwaKode = terdakwaKode;
        this.jenisNarkotika = jenisNarkotika;
        this.beratBarangBuktiGram = beratBarangBuktiGram;
        this.pasalDakwaan = pasalDakwaan;
        this.pasalTerbukti = pasalTerbukti;
        this.tuntutanBulan = tuntutanBulan;
        this.vonisBulan = vonisBulan;
        this.dendaRupiah = dendaRupiah;
        this.barangBukti = barangBukti;
        this.keadaanMemberatkan = keadaanMemberatkan;
        this.keadaanMeringankan = keadaanMeringankan;
        this.sourcePages = sourcePages;
        this.urlSumber = urlSumber;
    }

    // ========== GETTERS ==========

    public String getCaseId() { return caseId; }
    public String getNomorPerkara() { return nomorPerkara; }
    public String getPengadilan() { return pengadilan; }
    public LocalDate getTanggalPutusan() { return tanggalPutusan; }
    public String getTerdakwaKode() { return terdakwaKode; }
    public String getJenisNarkotika() { return jenisNarkotika; }
    public double getBeratBarangBuktiGram() { return beratBarangBuktiGram; }
    public String getPasalDakwaan() { return pasalDakwaan; }
    public String getPasalTerbukti() { return pasalTerbukti; }
    public int getTuntutanBulan() { return tuntutanBulan; }
    public int getVonisBulan() { return vonisBulan; }
    public long getDendaRupiah() { return dendaRupiah; }
    public String getBarangBukti() { return barangBukti; }
    public String getKeadaanMemberatkan() { return keadaanMemberatkan; }
    public String getKeadaanMeringankan() { return keadaanMeringankan; }
    public String getSourcePages() { return sourcePages; }
    public String getUrlSumber() { return urlSumber; }

    /**
     * Ringkasan singkat untuk tampilan daftar.
     */
    public String toSummary() {
        return String.format(
                "[%s] %s | %s | %s | Berat: %.4f g | Vonis: %d bln | Denda: %d",
                caseId,
                nomorPerkara,
                jenisNarkotika != null ? jenisNarkotika : "-",
                tanggalPutusan != null ? tanggalPutusan.toString() : "-",
                beratBarangBuktiGram,
                vonisBulan,
                dendaRupiah
        );
    }

    @Override
    public String toString() {
        return "Putusan{" +
                "caseId='" + caseId + '\'' +
                ", nomorPerkara='" + nomorPerkara + '\'' +
                ", pengadilan='" + pengadilan + '\'' +
                ", tanggalPutusan=" + tanggalPutusan +
                ", terdakwaKode='" + terdakwaKode + '\'' +
                ", jenisNarkotika='" + jenisNarkotika + '\'' +
                ", beratBarangBuktiGram=" + beratBarangBuktiGram +
                ", pasalDakwaan='" + pasalDakwaan + '\'' +
                ", pasalTerbukti='" + pasalTerbukti + '\'' +
                ", tuntutanBulan=" + tuntutanBulan +
                ", vonisBulan=" + vonisBulan +
                ", dendaRupiah=" + dendaRupiah +
                ", barangBukti='" + barangBukti + '\'' +
                ", keadaanMemberatkan='" + keadaanMemberatkan + '\'' +
                ", keadaanMeringankan='" + keadaanMeringankan + '\'' +
                ", sourcePages='" + sourcePages + '\'' +
                ", urlSumber='" + urlSumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Putusan)) return false;
        Putusan putusan = (Putusan) o;
        return Objects.equals(caseId, putusan.caseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caseId);
    }
}
