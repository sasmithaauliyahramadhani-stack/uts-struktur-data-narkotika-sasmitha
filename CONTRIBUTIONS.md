# CONTRIBUTIONS.md

**Mahasiswa:** Sasmitha Auliyah Ramadhani  
**NIM:** 202510370110160  

Karena pengerjaan dilakukan secara individu, seluruh kontribusi tercatat atas nama saya (mahasiswa di atas).

---

## Ringkasan Kontribusi

| Area                    | Kontribusi yang dilakukan                                                                 | Bukti |
|-------------------------|-------------------------------------------------------------------------------------------|-------|
| Ekstraksi / Dataset     | Menyediakan & memvalidasi 50 record CSV, membuat data-provenance.csv                      | `data/putusan-50.csv`, `docs/data-provenance.csv` |
| Implementasi Struktur   | DynamicArray generik + SinglyLinkedList generik + interface StrukturData                  | `src/main/java/.../struktur/` |
| Model & Util            | Class Putusan, AuditEntry, CsvPutusanReader dengan validasi ketat                         | `model/`, `util/` |
| Service Layer           | PutusanService, Search, Filter, Aggregation, Sorting, Export                              | `service/` |
| Aplikasi                | Main menu konsol interaktif                                                               | `app/Main.java` |
| Pengujian               | 17 unit test mencakup semua skenario wajib PDF                                            | `src/test/java/` (BUILD SUCCESS) |
| Dokumentasi             | README, AI_USAGE, ADR (2), trace, demo, CONTRIBUTIONS                                     | root & `docs/` |
| Analisis Kompleksitas   | Tabel Big-O berdasarkan implementasi aktual                                               | README.md |

---

## Catatan GitHub (untuk diisi setelah diunggah)

Setelah repository di-push ke GitHub, lengkapi bagian berikut:

- Issue yang dibuat: (isi nomor & judul)
- Pull Request: (isi nomor & judul)
- Review yang dilakukan: (jika ada kolaborasi tambahan)
- Tag final: `uts-v1.0`

Contoh format setelah diunggah:

```
- Issue #1 : Setup project structure
- Issue #2 : Implement DynamicArray
- PR #1    : Feature/dynamic-array (reviewed & merged)
- Tag      : uts-v1.0
```

---

## Pernyataan

Seluruh kode, pengujian, dan dokumentasi dalam repository ini dikerjakan dan dipertanggungjawabkan oleh:

**Sasmitha Auliyah Ramadhani (202510370110160)**
