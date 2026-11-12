package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.model.AuditEntry;
import id.ac.umm.strukturdata.model.Putusan;
import id.ac.umm.strukturdata.struktur.DynamicArray;
import id.ac.umm.strukturdata.struktur.SinglyLinkedList;
import id.ac.umm.strukturdata.struktur.StrukturData;
import id.ac.umm.strukturdata.util.CsvPutusanReader;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Layanan utama pengelolaan putusan.
 * Semua data disimpan di DynamicArray (struktur utama).
 * Audit disimpan di SinglyLinkedList (struktur sekunder).
 */
public class PutusanService {

    private final DynamicArray<Putusan> data;
    private final SinglyLinkedList<AuditEntry> auditLog;
    private final String aktor;

    public PutusanService() {
        this("system");
    }

    public PutusanService(String aktor) {
        this.data = new DynamicArray<>(64);
        this.auditLog = new SinglyLinkedList<>();
        this.aktor = aktor;
    }

    public DynamicArray<Putusan> getData() {
        return data;
    }

    public SinglyLinkedList<AuditEntry> getAuditLog() {
        return auditLog;
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    /** Impor dari CSV. */
    public CsvPutusanReader.ImportResult imporDariCsv(Path path) throws IOException {
        CsvPutusanReader reader = new CsvPutusanReader();
        CsvPutusanReader.ImportResult result = reader.baca(path);

        // pindahkan hasil ke struktur utama
        data.clear();
        for (int i = 0; i < result.berhasil.size(); i++) {
            data.add(result.berhasil.get(i));
        }

        catat("IMPORT", "Berhasil: " + result.jumlahBerhasil + ", Gagal: " + result.jumlahGagal);
        data.validateInvariant();
        return result;
    }

    /** Menambah putusan baru. */
    public void tambah(Putusan p) {
        if (p == null) throw new IllegalArgumentException("Putusan tidak boleh null");
        data.add(p);
        catat("TAMBAH", "caseId=" + p.getCaseId());
        data.validateInvariant();
    }

    /** Menghapus berdasarkan posisi (0-based). */
    public Putusan hapus(int posisi) {
        Putusan removed = data.remove(posisi);
        catat("HAPUS", "posisi=" + posisi + ", caseId=" + removed.getCaseId());
        data.validateInvariant();
        return removed;
    }

    /** Menghapus berdasarkan caseId. */
    public Putusan hapusByCaseId(String caseId) {
        int idx = cariIndexByCaseId(caseId);
        if (idx < 0) {
            throw new IllegalArgumentException("caseId tidak ditemukan: " + caseId);
        }
        return hapus(idx);
    }

    public Putusan get(int posisi) {
        return data.get(posisi);
    }

    public int cariIndexByCaseId(String caseId) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getCaseId().equals(caseId)) {
                return i;
            }
        }
        return -1;
    }

    public int cariIndexByNomorPerkara(String nomor) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getNomorPerkara().equals(nomor)) {
                return i;
            }
        }
        return -1;
    }

    /** Menampilkan semua putusan (ringkasan). */
    public void tampilkanSemua() {
        System.out.println("=== DAFTAR PUTUSAN (ukuran: " + data.size() + ") ===");
        for (int i = 0; i < data.size(); i++) {
            System.out.printf("%3d. %s%n", i, data.get(i).toSummary());
        }
    }

    private void catat(String operasi, String detail) {
        auditLog.add(new AuditEntry(operasi, detail, aktor));
    }

    public void tampilkanAudit() {
        System.out.println("=== RIWAYAT AUDIT (ukuran: " + auditLog.size() + ") ===");
        for (int i = 0; i < auditLog.size(); i++) {
            System.out.println(auditLog.get(i));
        }
    }
}
