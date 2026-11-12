# Contoh Trace Operasi DynamicArray

**Mahasiswa:** Sasmitha Auliyah Ramadhani (202510370110160)

Trace ini menunjukkan perubahan state internal setelah operasi kritis (sesuai skenario uji wajib).

---

## Kondisi Awal (setelah load 50 record)

```
size     = 50
capacity >= 50
data[0]  = Putusan(caseId=P001, ...)
data[49] = Putusan(caseId=P050, ...)
invariant = true
```

---

## 1. Remove First (posisi 0)

Sebelum:
```
[0]=P001, [1]=P002, [2]=P003, ..., [49]=P050
size=50
```

Operasi: `remove(0)`

Sesudah:
```
[0]=P002, [1]=P003, ..., [48]=P050, [49]=null
size=49
```
Semua elemen digeser ke kiri satu posisi. Invariant tetap terjaga.

---

## 2. Remove Middle (contoh posisi 24 setelah remove first)

Sebelum (size=49):
```
[23]=Px, [24]=Py, [25]=Pz, ...
```

Operasi: `remove(24)`

Sesudah:
```
[23]=Px, [24]=Pz, ... (elemen setelah 24 digeser kiri)
size=48
```

---

## 3. Remove Last

Operasi: `remove(size-1)`

Hanya mengurangi size dan men-null-kan posisi terakhir. Tidak ada pergeseran besar.

---

## 4. Add kembali 3 record

```
add(P001) → size menjadi 49
add(P002) → size menjadi 50
add(P003) → size menjadi 51 (atau sesuai jumlah yang ditambahkan)
```

Jika kapasitas masih cukup, tidak terjadi resize. Jika penuh, terjadi resize (capacity × 2).

---

## 5. Search dengan langkah

Contoh cari `P025` (asumsi ada di index 24):

```
step 1: bandingkan data[0]
step 2: bandingkan data[1]
...
step 25: bandingkan data[24] → ketemu
hasil: index=24, steps=25
```

Jika tidak ketemu (P999):
```
steps = size (seluruh elemen diperiksa)
index = -1
```

---

## 6. Sorting (non-destruktif)

```
salinan = salin seluruh elemen ke DynamicArray baru
urutkan salinan dengan Insertion Sort berdasarkan vonisBulan
struktur utama TIDAK berubah
```

Setelah sorting:
- `service.getData().get(0)` masih P001 (atau record awal)
- salinan sudah terurut

---

## Invariant yang selalu dicek

```java
size >= 0
capacity >= size
data.length == capacity
(setelah remove) data[size] == null
```

Method `validateInvariant()` dipanggil setelah setiap operasi kritis di service dan di unit test.
