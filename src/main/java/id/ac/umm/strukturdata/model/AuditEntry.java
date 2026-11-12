package id.ac.umm.strukturdata.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Entri audit untuk mencatat operasi pengguna.
 * Disimpan di struktur sekunder (SinglyLinkedList).
 */
public final class AuditEntry {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LocalDateTime timestamp;
    private final String operasi;
    private final String detail;
    private final String aktor;

    public AuditEntry(String operasi, String detail, String aktor) {
        this.timestamp = LocalDateTime.now();
        this.operasi = operasi;
        this.detail = detail;
        this.aktor = aktor != null ? aktor : "system";
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getOperasi() { return operasi; }
    public String getDetail() { return detail; }
    public String getAktor() { return aktor; }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | oleh: %s",
                timestamp.format(FMT), operasi, detail, aktor);
    }
}
