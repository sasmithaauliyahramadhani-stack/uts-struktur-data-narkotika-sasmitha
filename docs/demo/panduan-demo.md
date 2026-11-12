# Panduan Demonstrasi Aplikasi

**Mahasiswa:** Sasmitha Auliyah Ramadhani (202510370110160)

---

## Persiapan

```bash
cd uts-struktur-data-narkotika-sasmitha-auliyah-ramadhani
mvn clean compile
mvn exec:java
```

Pastikan `data/putusan-50.csv` ada di working directory.

---

## Alur Demonstrasi yang Disarankan

### 1. Cek Load & Size
- Menu `11` → pastikan size = 50 dan invariant = true
- Atau menu `2` untuk melihat daftar ringkasan

### 2. Pencarian
- Menu `5` → cari caseId `P001` (harus ditemukan)
- Cari caseId `P999` (tidak ditemukan, steps = 50)
- Cari posisi `0` dan `49` (batas)

### 3. Hapus & Tambah
- Menu `4` → hapus posisi 0 (first)
- Hapus posisi tengah (misalnya 20)
- Hapus posisi terakhir
- Tunjukkan size berubah dan invariant tetap true (menu 11)

### 4. Filter Gabungan
- Menu `6`
  - Jenis: `Sabu`
  - Vonis min: `100`
  - Vonis max: `150`
- Tunjukkan hasil dan pastikan struktur utama size tetap 50

### 5. Agregasi
- Menu `7` → tampilkan jumlah, rata-rata vonis, total denda, distribusi jenis

### 6. Pengurutan
- Menu `8` → pilih Ascending
- Tunjukkan beberapa record terurut
- Cek kembali menu `2` atau `11` bahwa urutan asli tidak berubah

### 7. Audit
- Menu `9` → tampilkan riwayat operasi yang sudah dilakukan

### 8. Ekspor
- Menu `10` → ekspor ke CSV atau TXT

---

## Perintah Cepat untuk Video / Screenshot

```text
Pilihan: 11          → size & invariant
Pilihan: 5           → search P001 dan P999
Pilihan: 6           → filter Sabu + vonis 100-150
Pilihan: 7           → agregasi
Pilihan: 8           → sort
Pilihan: 9           → audit log
```
