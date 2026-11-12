# ADR-002: Strategi Pencarian dan Pengurutan

**Status:** Accepted  
**Tanggal:** 9 November 2026  
**Decider:** Sasmitha Auliyah Ramadhani  

---

## Konteks

Aplikasi wajib mendukung:
- Pencarian berdasarkan `caseId` dan `nomorPerkara` dengan **pelaporan jumlah langkah**.
- Pengurutan berdasarkan `vonisBulan` (ascending & descending) **tanpa merusak invariant** maupun urutan asli struktur utama.

---

## Keputusan

### Pencarian
- Menggunakan **linear search** langsung di `DynamicArray`.
- Method `searchWithSteps` / `SearchService` mengembalikan pasangan `(index, steps)`.
- Tidak menggunakan binary search karena data tidak dijamin terurut berdasarkan kunci pencarian, dan n=50 membuat linear search sudah sangat cepat serta mudah dihitung langkahnya.

### Pengurutan
- Algoritma: **Insertion Sort**.
- Strategi: **membuat salinan** (`DynamicArray` baru) lalu mengurutkan salinan tersebut.
- Struktur utama **tidak pernah dimutasi** oleh operasi sorting.
- Alasan pemilihan Insertion Sort: sederhana, stabil, mudah diimplementasikan tanpa library, dan sangat memadai untuk n=50 (O(n²) ≈ 2500 operasi).

---

## Alasan Teknis

1. Linear search menjamin jumlah langkah dapat dilaporkan secara akurat dan transparan (satu perbandingan = satu langkah).
2. Membuat salinan memastikan invariant dan urutan asli struktur utama tetap utuh → memenuhi persyaratan “tanpa merusak invariant”.
3. Insertion Sort tidak memerlukan ruang tambahan yang besar dan mudah diverifikasi kebenarannya di unit test.

---

## Konsekuensi

- Setiap kali sorting dipanggil, terbentuk objek `DynamicArray` baru (O(n) ruang tambahan sementara).
- Pencarian selalu O(n) — diterima karena n kecil dan kebutuhan hitung langkah.

---

## Verifikasi

Unit test `sortingDoesNotMutateOriginal` membuktikan:
- Hasil sorting terurut benar.
- `service.get(0)` masih berisi record awal (P001) setelah sorting dipanggil.
