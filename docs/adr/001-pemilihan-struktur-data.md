# ADR-001: Pemilihan Struktur Data Utama dan Sekunder

**Status:** Accepted  
**Tanggal:** 9 November 2026  
**Decider:** Sasmitha Auliyah Ramadhani  

---

## Konteks

Aplikasi harus menyimpan 50 objek `Putusan` dan mendukung operasi:
- akses posisi (get/set)
- tambah & hapus di sembarang posisi (first, middle, last)
- pencarian dengan hitungan langkah
- filter & sorting tanpa merusak urutan asli
- audit riwayat operasi

Empat pilihan yang diizinkan: Dynamic Array, Singly Linked List, Stack, Queue.

---

## Keputusan

**Struktur utama:** `DynamicArray<T>` (array dinamis generik yang diimplementasikan dari nol).  
**Struktur sekunder:** `SinglyLinkedList<T>` untuk menyimpan `AuditEntry`.

---

## Alasan

1. **Kebutuhan akses indeks dan operasi posisi**  
   Fitur hapus first/middle/last, pencarian posisi batas, dan tampilan terurut membutuhkan akses O(1) by index. Dynamic Array memenuhi ini secara alami.

2. **Transparansi invariant**  
   Invariant `capacity >= size` dan elemen aktif di `[0..size-1]` mudah dicek dan didemonstrasikan di unit test serta trace.

3. **Sorting & Filter non-destruktif**  
   Mudah membuat salinan array untuk diurutkan/difilter tanpa mengubah struktur utama. Stack/Queue memerlukan mekanisme restore yang lebih kompleks dan rawan error.

4. **Kesesuaian dengan rubrik**  
   Rubrik menekankan “boundary aman” dan “invariant terjaga”. Array memudahkan pembuktian tersebut dibanding pointer-based structure untuk kasus n=50.

5. **Stack/Queue kurang cocok sebagai utama**  
   Operasi non-destruktif (searchPreserve / rotateSearch) menambah kompleksitas signifikan, sementara fitur filter + sort menjadi lebih sulit dijaga urutannya.

---

## Konsekuensi

**Positif**
- Operasi get/set O(1)
- Implementasi resize dan shift mudah ditrace
- Unit test invariant sederhana dan kuat

**Negatif**
- Remove/insert di tengah O(n) (diterima karena n=50)
- Memerlukan perhatian ekstra pada resize dan nulling elemen setelah remove

---

## Alternatif yang dipertimbangkan

| Alternatif            | Alasan ditolak |
|-----------------------|----------------|
| Singly Linked List    | get by index menjadi O(n); trace middle remove lebih rumit |
| Stack                 | Tidak natural untuk akses posisi & filter multi-kriteria |
| Queue                 | Sama seperti Stack; FIFO tidak sesuai pola akses utama |
