package id.ac.umm.strukturdata.struktur;

/**
 * Kontrak umum struktur data generik sesuai panduan UTS.
 * Nama method dapat disesuaikan untuk Stack/Queue, tetapi pemetaan harus dijelaskan di ADR.
 */
public interface StrukturData<T> {

    /**
     * Menambahkan elemen di akhir struktur.
     */
    void add(T value);

    /**
     * Mengambil elemen pada posisi tertentu (0-based).
     * @throws IndexOutOfBoundsException jika posisi tidak valid
     */
    T get(int position);

    /**
     * Menghapus elemen pada posisi tertentu dan mengembalikannya.
     * @throws IndexOutOfBoundsException jika posisi tidak valid
     */
    T remove(int position);

    /**
     * Jumlah elemen aktif saat ini.
     */
    int size();

    /**
     * True jika tidak ada elemen.
     */
    boolean isEmpty();

    /**
     * True jika value ditemukan (menggunakan equals).
     */
    boolean contains(T value);

    /**
     * Menghapus semua elemen.
     */
    void clear();

    /**
     * Memeriksa invariant internal struktur.
     * Harus dipanggil di unit test setelah operasi kritis.
     * @return true jika invariant terjaga
     * @throws IllegalStateException jika invariant dilanggar (opsional, tergantung implementasi)
     */
    boolean validateInvariant();
}
