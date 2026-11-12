package id.ac.umm.strukturdata.service;

import id.ac.umm.strukturdata.model.Putusan;
import id.ac.umm.strukturdata.struktur.DynamicArray;

import java.util.LinkedHashMap; // Hanya untuk menampung distribusi sementara di method, bukan struktur utama
import java.util.Map;

/**
 * Layanan agregasi.
 * Memperhatikan nilai kosong/nol dan kemungkinan overflow.
 */
public class AggregationService {

    public static class AggregationResult {
        public final int jumlahPerkara;
        public final double rataRataVonis;
        public final long totalDenda;
        public final Map<String, Integer> distribusiJenis;

        public AggregationResult(int jumlahPerkara, double rataRataVonis, long totalDenda,
                                 Map<String, Integer> distribusiJenis) {
            this.jumlahPerkara = jumlahPerkara;
            this.rataRataVonis = rataRataVonis;
            this.totalDenda = totalDenda;
            this.distribusiJenis = distribusiJenis;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== HASIL AGREGASI ===\n");
            sb.append("Jumlah perkara      : ").append(jumlahPerkara).append("\n");
            sb.append(String.format("Rata-rata vonis     : %.2f bulan\n", rataRataVonis));
            sb.append("Total denda         : ").append(totalDenda).append(" Rupiah\n");
            sb.append("Distribusi jenis    :\n");
            for (Map.Entry<String, Integer> e : distribusiJenis.entrySet()) {
                sb.append("  - ").append(e.getKey()).append(": ").append(e.getValue()).append("\n");
            }
            return sb.toString();
        }
    }

    private final DynamicArray<Putusan> data;

    public AggregationService(DynamicArray<Putusan> data) {
        this.data = data;
    }

    public AggregationResult agregasi() {
        int n = data.size();
        if (n == 0) {
            return new AggregationResult(0, 0.0, 0L, new LinkedHashMap<>());
        }

        long totalVonis = 0;
        long totalDenda = 0;
        Map<String, Integer> distribusi = new LinkedHashMap<>();

        for (int i = 0; i < n; i++) {
            Putusan p = data.get(i);
            totalVonis += p.getVonisBulan();
            // cegah overflow sederhana
            long denda = p.getDendaRupiah();
            if (totalDenda > Long.MAX_VALUE - denda) {
                // overflow protection (sangat jarang terjadi pada data ini)
                totalDenda = Long.MAX_VALUE;
            } else {
                totalDenda += denda;
            }

            // distribusi jenis (bisa multi dengan ;)
            String jenis = p.getJenisNarkotika();
            if (jenis == null || jenis.isBlank()) {
                distribusi.merge("UNKNOWN", 1, Integer::sum);
            } else {
                String[] parts = jenis.split(";");
                for (String part : parts) {
                    String key = part.trim();
                    if (!key.isEmpty()) {
                        distribusi.merge(key, 1, Integer::sum);
                    }
                }
            }
        }

        double rataVonis = (double) totalVonis / n;
        return new AggregationResult(n, rataVonis, totalDenda, distribusi);
    }
}
