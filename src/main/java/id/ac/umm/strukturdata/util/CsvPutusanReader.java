package id.ac.umm.strukturdata.util;

import id.ac.umm.strukturdata.model.Putusan;
import id.ac.umm.strukturdata.struktur.DynamicArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList; // HANYA untuk menampung pesan error sementara, BUKAN untuk struktur data utama
import java.util.List;

/**
 * Pembaca dan validator CSV putusan.
 * Menolak record tidak valid tanpa menghentikan keseluruhan proses.
 * Tidak menggunakan koleksi java.util sebagai struktur data utama aplikasi.
 */
public class CsvPutusanReader {

    public static class ImportResult {
        public final DynamicArray<Putusan> berhasil;
        public final int jumlahBerhasil;
        public final int jumlahGagal;
        public final List<String> pesanError;

        public ImportResult(DynamicArray<Putusan> berhasil, int jumlahBerhasil, int jumlahGagal, List<String> pesanError) {
            this.berhasil = berhasil;
            this.jumlahBerhasil = jumlahBerhasil;
            this.jumlahGagal = jumlahGagal;
            this.pesanError = pesanError;
        }
    }

    /**
     * Membaca file CSV dan mengembalikan hanya record yang valid.
     */
    public ImportResult baca(Path pathCsv) throws IOException {
        DynamicArray<Putusan> hasil = new DynamicArray<>(64);
        List<String> errors = new ArrayList<>();
        int berhasil = 0;
        int gagal = 0;

        try (BufferedReader reader = Files.newBufferedReader(pathCsv, StandardCharsets.UTF_8)) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new IOException("File CSV kosong");
            }

            String line;
            int baris = 1; // header = baris 1
            while ((line = reader.readLine()) != null) {
                baris++;
                if (line.isBlank()) continue;

                try {
                    Putusan p = parseLine(line, baris);
                    hasil.add(p);
                    berhasil++;
                } catch (Exception e) {
                    gagal++;
                    errors.add("Baris " + baris + ": " + e.getMessage());
                }
            }
        }

        return new ImportResult(hasil, berhasil, gagal, errors);
    }

    private Putusan parseLine(String line, int baris) {
        // Parser CSV sederhana yang menghormati tanda kutip
        String[] fields = splitCsv(line);

        if (fields.length < 16) {
            throw new IllegalArgumentException("Jumlah kolom kurang dari 16 (ditemukan " + fields.length + ")");
        }

        String caseId = emptyToNull(fields[0]);
        String nomorPerkara = emptyToNull(fields[1]);
        String pengadilan = emptyToNull(fields[2]);
        LocalDate tanggal = parseDate(fields[3], "tanggalPutusan");
        String terdakwaKode = emptyToNull(fields[4]);
        String jenisNarkotika = emptyToNull(fields[5]);
        double berat = parseDouble(fields[6], "beratBarangBuktiGram");
        String pasalDakwaan = emptyToNull(fields[7]);
        String pasalTerbukti = emptyToNull(fields[8]);
        int tuntutan = parseInt(fields[9], "tuntutanBulan");
        int vonis = parseInt(fields[10], "vonisBulan");
        long denda = parseLong(fields[11], "dendaRupiah");
        String barangBukti = emptyToNull(fields[12]);
        String keadaanMemberatkan = emptyToNull(fields[13]);
        String keadaanMeringankan = emptyToNull(fields[14]);
        String sourcePages = emptyToNull(fields[15]);
        String urlSumber = fields.length > 16 ? emptyToNull(fields[16]) : null;

        // Validasi minimal wajib
        if (caseId == null || caseId.isBlank()) {
            throw new IllegalArgumentException("caseId wajib diisi");
        }
        if (nomorPerkara == null || nomorPerkara.isBlank()) {
            throw new IllegalArgumentException("nomorPerkara wajib diisi");
        }
        if (tanggal == null) {
            throw new IllegalArgumentException("tanggalPutusan tidak valid");
        }
        if (tuntutan < 0 || vonis < 0) {
            throw new IllegalArgumentException("tuntutan/vonis tidak boleh negatif");
        }
        if (denda < 0) {
            throw new IllegalArgumentException("denda tidak boleh negatif");
        }
        if (berat < 0) {
            throw new IllegalArgumentException("berat tidak boleh negatif");
        }

        return new Putusan(
                caseId, nomorPerkara, pengadilan, tanggal, terdakwaKode,
                jenisNarkotika, berat, pasalDakwaan, pasalTerbukti,
                tuntutan, vonis, denda, barangBukti,
                keadaanMemberatkan, keadaanMeringankan, sourcePages, urlSumber
        );
    }

    /** Parser CSV sederhana yang menangani field ber-tanda-kutip. */
    private String[] splitCsv(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString().trim());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        result.add(current.toString().trim());
        return result.toArray(new String[0]);
    }

    private String emptyToNull(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty() || s.equalsIgnoreCase("null")) return null;
        return s;
    }

    private LocalDate parseDate(String s, String field) {
        s = emptyToNull(s);
        if (s == null) return null;
        try {
            return LocalDate.parse(s);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(field + " format tidak valid: " + s);
        }
    }

    private double parseDouble(String s, String field) {
        s = emptyToNull(s);
        if (s == null) return 0.0;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(field + " bukan angka: " + s);
        }
    }

    private int parseInt(String s, String field) {
        s = emptyToNull(s);
        if (s == null) return 0;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(field + " bukan bilangan bulat: " + s);
        }
    }

    private long parseLong(String s, String field) {
        s = emptyToNull(s);
        if (s == null) return 0L;
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(field + " bukan bilangan long: " + s);
        }
    }
}
