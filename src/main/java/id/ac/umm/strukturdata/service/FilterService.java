package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.model.Putusan;
import id.ac.umm.strukturdata.struktur.DynamicArray;

/**
 * Layanan filtering dengan logika AND antar kriteria.
 * Menghasilkan DynamicArray baru, tidak mengubah struktur utama.
 */
public class FilterService {

    public static class FilterCriteria {
        public String jenisNarkotika;          // mengandung substring (case-insensitive)
        public Double beratMin;
        public Double beratMax;
        public String pasalTerbukti;           // mengandung substring
        public Integer vonisMin;
        public Integer vonisMax;

        public boolean isEmpty() {
            return jenisNarkotika == null
                    && beratMin == null && beratMax == null
                    && pasalTerbukti == null
                    && vonisMin == null && vonisMax == null;
        }
    }

    private final DynamicArray<Putusan> data;

    public FilterService(DynamicArray<Putusan> data) {
        this.data = data;
    }

    /**
     * Filter dengan logika AND.
     * Semua kriteria non-null harus terpenuhi.
     */
    public DynamicArray<Putusan> filter(FilterCriteria criteria) {
        DynamicArray<Putusan> hasil = new DynamicArray<>();

        for (int i = 0; i < data.size(); i++) {
            Putusan p = data.get(i);
            if (matches(p, criteria)) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    private boolean matches(Putusan p, FilterCriteria c) {
        if (c.jenisNarkotika != null && !c.jenisNarkotika.isBlank()) {
            String jenis = p.getJenisNarkotika() != null ? p.getJenisNarkotika() : "";
            if (!jenis.toLowerCase().contains(c.jenisNarkotika.toLowerCase())) {
                return false;
            }
        }
        if (c.beratMin != null && p.getBeratBarangBuktiGram() < c.beratMin) {
            return false;
        }
        if (c.beratMax != null && p.getBeratBarangBuktiGram() > c.beratMax) {
            return false;
        }
        if (c.pasalTerbukti != null && !c.pasalTerbukti.isBlank()) {
            String pasal = p.getPasalTerbukti() != null ? p.getPasalTerbukti() : "";
            if (!pasal.toLowerCase().contains(c.pasalTerbukti.toLowerCase())) {
                return false;
            }
        }
        if (c.vonisMin != null && p.getVonisBulan() < c.vonisMin) {
            return false;
        }
        if (c.vonisMax != null && p.getVonisBulan() > c.vonisMax) {
            return false;
        }
        return true;
    }
}
