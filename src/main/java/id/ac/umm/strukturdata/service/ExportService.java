package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.model.Putusan;
import id.ac.umm.strukturdata.struktur.DynamicArray;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Layanan ekspor hasil pencarian / laporan ke TXT dan CSV.
 */
public class ExportService {

    public void eksporCsv(DynamicArray<Putusan> data, Path tujuan) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(tujuan, StandardCharsets.UTF_8)) {
            // header
            w.write("caseId,nomorPerkara,pengadilan,tanggalPutusan,terdakwaKode,jenisNarkotika,"
                    + "beratBarangBuktiGram,pasalDakwaan,pasalTerbukti,tuntutanBulan,vonisBulan,"
                    + "dendaRupiah,barangBukti,keadaanMemberatkan,keadaanMeringankan,sourcePages,urlSumber");
            w.newLine();

            for (int i = 0; i < data.size(); i++) {
                Putusan p = data.get(i);
                w.write(csvEscape(p.getCaseId()));
                w.write(",");
                w.write(csvEscape(p.getNomorPerkara()));
                w.write(",");
                w.write(csvEscape(p.getPengadilan()));
                w.write(",");
                w.write(p.getTanggalPutusan() != null ? p.getTanggalPutusan().toString() : "");
                w.write(",");
                w.write(csvEscape(p.getTerdakwaKode()));
                w.write(",");
                w.write(csvEscape(p.getJenisNarkotika()));
                w.write(",");
                w.write(String.valueOf(p.getBeratBarangBuktiGram()));
                w.write(",");
                w.write(csvEscape(p.getPasalDakwaan()));
                w.write(",");
                w.write(csvEscape(p.getPasalTerbukti()));
                w.write(",");
                w.write(String.valueOf(p.getTuntutanBulan()));
                w.write(",");
                w.write(String.valueOf(p.getVonisBulan()));
                w.write(",");
                w.write(String.valueOf(p.getDendaRupiah()));
                w.write(",");
                w.write(csvEscape(p.getBarangBukti()));
                w.write(",");
                w.write(csvEscape(p.getKeadaanMemberatkan()));
                w.write(",");
                w.write(csvEscape(p.getKeadaanMeringankan()));
                w.write(",");
                w.write(csvEscape(p.getSourcePages()));
                w.write(",");
                w.write(csvEscape(p.getUrlSumber()));
                w.newLine();
            }
        }
    }

    public void eksporTxt(DynamicArray<Putusan> data, Path tujuan, String judul) throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(tujuan, StandardCharsets.UTF_8)) {
            w.write(judul != null ? judul : "Laporan Putusan Narkotika");
            w.newLine();
            w.write("Jumlah record: " + data.size());
            w.newLine();
            w.write("------------------------------------------------------------");
            w.newLine();

            for (int i = 0; i < data.size(); i++) {
                Putusan p = data.get(i);
                w.write(String.format("%d. %s", i + 1, p.toSummary()));
                w.newLine();
                w.write("   Pasal terbukti : " + nullSafe(p.getPasalTerbukti()));
                w.newLine();
                w.write("   Tuntutan       : " + p.getTuntutanBulan() + " bulan");
                w.newLine();
                w.write("   Denda          : " + p.getDendaRupiah());
                w.newLine();
                w.write("------------------------------------------------------------");
                w.newLine();
            }
        }
    }

    private String csvEscape(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }

    private String nullSafe(String s) {
        return s == null ? "-" : s;
    }
}
