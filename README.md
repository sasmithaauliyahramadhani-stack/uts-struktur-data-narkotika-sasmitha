# UTS Struktur Data — Putusan Narkotika

**Mahasiswa:** Sasmitha Auliyah Ramadhani  
**NIM:** 202510370110160  
**Mata Kuliah:** Struktur Data  
**Program Studi / Fakultas:** Teknik — Universitas Muhammadiyah Malang  
**Semester:** Ganjil 2026/2027  

---

## 1. Deskripsi Proyek

Aplikasi Java berorientasi objek untuk menyimpan dan mengolah **50 putusan pengadilan narkotika** (Pengadilan Negeri Cibinong) menggunakan **satu struktur data utama yang dibuat dari nol**.

- **Struktur utama:** `DynamicArray<T>` (array dinamis generik)
- **Struktur sekunder:** `SinglyLinkedList<T>` (untuk audit log)

Seluruh implementasi struktur data **tidak menggunakan** `ArrayList`, `LinkedList`, `Stack`, `Queue`, `Deque`, `Vector`, maupun koleksi `java.util` lainnya sebagai penyimpanan inti.

---

## 2. Teknologi

| Item              | Versi / Keterangan          |
|-------------------|-----------------------------|
| Bahasa            | Java 17+                    |
| Build Tool        | Maven 3.8+                  |
| Testing           | JUnit 5                     |
| Encoding          | UTF-8                       |

---

## 3. Struktur Direktori

```
uts-struktur-data-narkotika-sasmitha-auliyah-ramadhani/
├── pom.xml
├── README.md
├── AI_USAGE.md
├── CONTRIBUTIONS.md
├── data/
│   └── putusan-50.csv
├── docs/
│   ├── data-provenance.csv
│   ├── adr/
│   │   ├── 001-pemilihan-struktur-data.md
│   │   └── 002-strategi-pencarian-pengurutan.md
│   ├── demo/
│   │   └── panduan-demo.md
│   └── trace/
│       └── contoh-trace-operasi.md
├── src/
│   ├── main/java/id/ac/umm/strukturdata/
│   │   ├── app/Main.java
│   │   ├── model/
│   │   ├── struktur/
│   │   ├── service/
│   │   └── util/
│   └── test/java/...
└── target/   (hasil build)
```

---

## 4. Cara Build & Test

```bash
# Compile
mvn clean compile

# Jalankan seluruh unit test
mvn test

# Jalankan aplikasi
mvn exec:java
# atau
java -cp target/classes id.ac.umm.strukturdata.app.Main
```

Pastikan file `data/putusan-50.csv` berada di working directory saat menjalankan aplikasi.

---

## 5. Fitur yang Diimplementasikan

| Fitur          | Status | Keterangan |
|----------------|--------|----------|
| Impor CSV      | ✅     | Membaca tepat 50 record, menolak invalid tanpa crash |
| Tampil         | ✅     | Menampilkan ringkasan sesuai urutan struktur utama |
| Tambah / Hapus | ✅     | Operasi asli DynamicArray + maintain invariant |
| Cari           | ✅     | By caseId / nomorPerkara + jumlah langkah |
| Filter         | ✅     | Jenis, rentang berat, pasal, rentang vonis (AND) |
| Agregasi       | ✅     | Jumlah, rata-rata vonis, total denda, distribusi jenis |
| Urutan         | ✅     | Sort by vonis (ASC/DESC) tanpa merusak struktur utama |
| Audit          | ✅     | Tercatat di SinglyLinkedList |
| Ekspor         | ✅     | CSV dan TXT |

---

## 6. Analisis Kompleksitas (DynamicArray)

| Operasi       | Rata-rata     | Terburuk     | Keterangan |
|---------------|---------------|--------------|----------|
| add (akhir)   | O(1) amortized| O(n)         | resize 2× |
| insert        | O(n)          | O(n)         | geser elemen |
| get / set     | O(1)          | O(1)         | akses indeks |
| remove        | O(n)          | O(n)         | geser elemen |
| search / contains | O(n)      | O(n)         | linear |
| filter        | O(n)          | O(n)         | satu kali traversal |
| sort (insertion) | O(n²)      | O(n²)        | n=50 sangat kecil |
| agregasi      | O(n)          | O(n)         | satu traversal |

Ruang tambahan: O(n) untuk array internal + O(n) saat membuat salinan untuk sorting/filter.

---

## 7. Skenario Uji yang Sudah Diverifikasi

1. Load 50 record → size == 50  
2. Cari yang ada, yang tidak ada, posisi batas (first & last)  
3. Hapus first, middle, last  
4. Tambah kembali 3 record + invariant terjaga  
5. Filter gabungan  
6. Sorting tidak mengubah urutan asli struktur utama  
7. Record invalid ditolak, struktur tetap konsisten  

**Hasil terakhir:** 17 tests, 0 failures (BUILD SUCCESS).

---

## 8. Catatan Dataset

- Sumber: putusan3.mahkamahagung.go.id (PN Cibinong)
- Semua 50 record berhasil diimpor.
- `sourcePages` kosong di CSV sumber → ditandai `unknown` / `unverified` di provenance.
- 4 record memiliki berat barang bukti = 0 (dibiarkan sesuai data asli, tidak dikarang).

---

## 9. Lisensi & Tanggung Jawab

Kode ini dikerjakan untuk memenuhi tugas UTS Struktur Data.  
Penggunaan AI dicatat secara transparan di `AI_USAGE.md`.
