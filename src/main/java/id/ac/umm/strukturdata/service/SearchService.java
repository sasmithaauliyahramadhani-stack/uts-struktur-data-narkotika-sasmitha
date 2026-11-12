package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.model.Putusan;
import id.ac.umm.strukturdata.struktur.DynamicArray;

/**
 * Layanan pencarian linear dengan pelaporan jumlah langkah.
 * Tidak merusak struktur utama.
 */
public class SearchService {

    public static class SearchResult {
        public final Putusan putusan;   // null jika tidak ditemukan
        public final int index;         // -1 jika tidak ditemukan
        public final int steps;         // jumlah perbandingan

        public SearchResult(Putusan putusan, int index, int steps) {
            this.putusan = putusan;
            this.index = index;
            this.steps = steps;
        }

        public boolean ditemukan() {
            return putusan != null;
        }
    }

    private final DynamicArray<Putusan> data;

    public SearchService(DynamicArray<Putusan> data) {
        this.data = data;
    }

    public SearchResult cariByCaseId(String caseId) {
        int steps = 0;
        for (int i = 0; i < data.size(); i++) {
            steps++;
            if (data.get(i).getCaseId().equals(caseId)) {
                return new SearchResult(data.get(i), i, steps);
            }
        }
        return new SearchResult(null, -1, steps);
    }

    public SearchResult cariByNomorPerkara(String nomorPerkara) {
        int steps = 0;
        for (int i = 0; i < data.size(); i++) {
            steps++;
            if (data.get(i).getNomorPerkara().equals(nomorPerkara)) {
                return new SearchResult(data.get(i), i, steps);
            }
        }
        return new SearchResult(null, -1, steps);
    }

    /** Mencari pada posisi batas (first / last). */
    public SearchResult cariPadaPosisi(int posisi) {
        if (posisi < 0 || posisi >= data.size()) {
            return new SearchResult(null, -1, 0);
        }
        // akses langsung = 1 langkah
        return new SearchResult(data.get(posisi), posisi, 1);
    }
}
