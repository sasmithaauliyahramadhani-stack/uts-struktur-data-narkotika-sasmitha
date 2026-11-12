package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.model.Putusan;
import id.ac.umm.strukturdata.struktur.DynamicArray;

/**
 * Layanan pengurutan berdasarkan vonis.
 * Menghasilkan DynamicArray baru (salinan terurut).
 * Struktur utama TIDAK diubah → invariant tetap terjaga.
 *
 * Algoritma: Insertion Sort (sederhana, stabil, cocok untuk n=50).
 */
public class SortingService {

    public enum Order {
        ASCENDING, DESCENDING
    }

    private final DynamicArray<Putusan> data;

    public SortingService(DynamicArray<Putusan> data) {
        this.data = data;
    }

    /**
     * Menghasilkan salinan terurut berdasarkan vonisBulan.
     * Tidak memodifikasi array asli.
     */
    public DynamicArray<Putusan> sortByVonis(Order order) {
        // salin dulu
        DynamicArray<Putusan> copy = new DynamicArray<>(data.size());
        for (int i = 0; i < data.size(); i++) {
            copy.add(data.get(i));
        }

        // Insertion Sort
        for (int i = 1; i < copy.size(); i++) {
            Putusan key = copy.get(i);
            int j = i - 1;

            while (j >= 0 && shouldSwap(copy.get(j), key, order)) {
                // geser
                // karena DynamicArray tidak punya set langsung di loop ini,
                // kita gunakan set
                copy.set(j + 1, copy.get(j));
                j--;
            }
            copy.set(j + 1, key);
        }

        copy.validateInvariant();
        return copy;
    }

    private boolean shouldSwap(Putusan a, Putusan b, Order order) {
        if (order == Order.ASCENDING) {
            return a.getVonisBulan() > b.getVonisBulan();
        } else {
            return a.getVonisBulan() < b.getVonisBulan();
        }
    }
}
