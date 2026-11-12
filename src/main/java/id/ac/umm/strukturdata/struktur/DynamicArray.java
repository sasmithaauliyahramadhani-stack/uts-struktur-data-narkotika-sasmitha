package id.ac.umm.strukturdata.struktur;

import java.util.Objects;

/**
 * Implementasi Dynamic Array generik dari nol.
 * Tidak menggunakan ArrayList, Vector, atau koleksi java.util lainnya.
 * Array mentah T[] digunakan sebagai penyimpanan inti.
 *
 * Invariant utama:
 * - capacity >= size
 * - elemen aktif berada di indeks [0 .. size-1]
 * - elemen di luar size dianggap tidak valid (boleh null)
 * - size >= 0
 */
@SuppressWarnings("unchecked")
public class DynamicArray<T> implements StrukturData<T> {

    private static final int DEFAULT_CAPACITY = 16;
    private static final double GROWTH_FACTOR = 2.0;

    private T[] data;
    private int size;
    private int capacity;

    public DynamicArray() {
        this(DEFAULT_CAPACITY);
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Kapasitas awal tidak boleh negatif");
        }
        this.capacity = Math.max(initialCapacity, 1);
        this.data = (T[]) new Object[this.capacity];
        this.size = 0;
    }

    // ========== OPERASI UTAMA ==========

    @Override
    public void add(T value) {
        ensureCapacity(size + 1);
        data[size] = value;
        size++;
    }

    /**
     * Menyisipkan elemen pada posisi tertentu.
     * Elemen dari posisi tersebut digeser ke kanan.
     */
    public void insert(int position, T value) {
        checkPositionForInsert(position);
        ensureCapacity(size + 1);
        // geser elemen ke kanan
        for (int i = size; i > position; i--) {
            data[i] = data[i - 1];
        }
        data[position] = value;
        size++;
    }

    @Override
    public T get(int position) {
        checkPosition(position);
        return data[position];
    }

    /**
     * Mengganti nilai pada posisi tertentu.
     * @return nilai lama
     */
    public T set(int position, T value) {
        checkPosition(position);
        T old = data[position];
        data[position] = value;
        return old;
    }

    @Override
    public T remove(int position) {
        checkPosition(position);
        T removed = data[position];
        // geser elemen ke kiri
        for (int i = position; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null; // bantu GC
        size--;
        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(T value) {
        return indexOf(value) >= 0;
    }

    /**
     * Mencari indeks pertama dari value.
     * @return indeks atau -1 jika tidak ditemukan
     */
    public int indexOf(T value) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(data[i], value)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Pencarian linear yang juga mengembalikan jumlah langkah (perbandingan).
     * Berguna untuk memenuhi persyaratan "melaporkan jumlah langkah".
     *
     * @return array [index, steps]. index = -1 jika tidak ditemukan.
     */
    public int[] searchWithSteps(T value) {
        int steps = 0;
        for (int i = 0; i < size; i++) {
            steps++;
            if (Objects.equals(data[i], value)) {
                return new int[]{i, steps};
            }
        }
        return new int[]{-1, steps};
    }

    /**
     * Mencari berdasarkan predikat sederhana (untuk caseId / nomorPerkara).
     * Mengembalikan [index, steps].
     */
    public int[] searchWithSteps(java.util.function.Predicate<T> predicate) {
        int steps = 0;
        for (int i = 0; i < size; i++) {
            steps++;
            if (predicate.test(data[i])) {
                return new int[]{i, steps};
            }
        }
        return new int[]{-1, steps};
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            data[i] = null;
        }
        size = 0;
    }

    // ========== RESIZE ==========

    private void ensureCapacity(int minCapacity) {
        if (minCapacity > capacity) {
            int newCapacity = (int) Math.max(minCapacity, capacity * GROWTH_FACTOR);
            resize(newCapacity);
        }
    }

    /**
     * Mengubah kapasitas array. Hanya memperbesar atau mengecilkan secara eksplisit.
     */
    public void resize(int newCapacity) {
        if (newCapacity < size) {
            throw new IllegalArgumentException(
                    "Kapasitas baru (" + newCapacity + ") lebih kecil dari size (" + size + ")");
        }
        T[] newData = (T[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
        capacity = newCapacity;
    }

    public int capacity() {
        return capacity;
    }

    // ========== INVARIANT ==========

    /**
     * Memeriksa invariant internal.
     * Dipanggil di unit test setelah operasi kritis.
     */
    @Override
    public boolean validateInvariant() {
        if (size < 0) {
            throw new IllegalStateException("Invariant dilanggar: size < 0");
        }
        if (capacity < size) {
            throw new IllegalStateException(
                    "Invariant dilanggar: capacity (" + capacity + ") < size (" + size + ")");
        }
        if (data == null) {
            throw new IllegalStateException("Invariant dilanggar: data array null");
        }
        if (data.length != capacity) {
            throw new IllegalStateException(
                    "Invariant dilanggar: data.length (" + data.length + ") != capacity (" + capacity + ")");
        }
        // Elemen di luar size harus null (setelah remove)
        // Catatan: kita tidak memaksa null di luar size setelah add biasa,
        // tetapi setelah remove kita set null. Pengecekan longgar di sini.
        return true;
    }

    // ========== HELPER BOUNDS ==========

    private void checkPosition(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException(
                    "Posisi " + position + " di luar batas [0 .. " + (size - 1) + "]");
        }
    }

    private void checkPositionForInsert(int position) {
        if (position < 0 || position > size) {
            throw new IndexOutOfBoundsException(
                    "Posisi insert " + position + " di luar batas [0 .. " + size + "]");
        }
    }

    /**
     * Mengembalikan salinan array elemen aktif (untuk keperluan sorting/filter tanpa mutasi).
     * Tidak mengekspos array internal.
     */
    public T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        for (int i = 0; i < size; i++) {
            a[i] = data[i];
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DynamicArray[size=").append(size)
          .append(", capacity=").append(capacity).append("] {");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append(data[i]);
        }
        sb.append("}");
        return sb.toString();
    }
}
