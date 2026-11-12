package id.ac.umm.strukturdata.app;

import id.ac.umm.strukturdata.model.Putusan;
import id.ac.umm.strukturdata.service.*;
import id.ac.umm.strukturdata.struktur.DynamicArray;
import id.ac.umm.strukturdata.util.CsvPutusanReader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Entry point aplikasi konsol.
 */
public class Main {

    private static final Path DEFAULT_CSV = Paths.get("data/putusan-50.csv");

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("  UTS STRUKTUR DATA - PUTUSAN NARKOTIKA");
        System.out.println("  Struktur Utama : Dynamic Array (generic)");
        System.out.println("  Struktur Sekunder : Singly Linked List (Audit)");
        System.out.println("====================================================");

        PutusanService service = new PutusanService("user");
        Scanner sc = new Scanner(System.in);

        // Auto-load jika file ada
        try {
            if (DEFAULT_CSV.toFile().exists()) {
                CsvPutusanReader.ImportResult res = service.imporDariCsv(DEFAULT_CSV);
                System.out.printf("Auto-import: %d berhasil, %d gagal%n", res.jumlahBerhasil, res.jumlahGagal);
                if (res.jumlahGagal > 0) {
                    res.pesanError.forEach(System.out::println);
                }
            } else {
                System.out.println("File data/putusan-50.csv tidak ditemukan. Silakan impor manual.");
            }
        } catch (Exception e) {
            System.err.println("Gagal auto-import: " + e.getMessage());
        }

        boolean running = true;
        while (running) {
            tampilMenu();
            System.out.print("Pilihan: ");
            String pilihan = sc.nextLine().trim();

            try {
                switch (pilihan) {
                    case "1" -> doImport(service, sc);
                    case "2" -> service.tampilkanSemua();
                    case "3" -> doTambah(service, sc);
                    case "4" -> doHapus(service, sc);
                    case "5" -> doCari(service, sc);
                    case "6" -> doFilter(service, sc);
                    case "7" -> doAgregasi(service);
                    case "8" -> doUrutkan(service, sc);
                    case "9" -> service.tampilkanAudit();
                    case "10" -> doEkspor(service, sc);
                    case "11" -> {
                        System.out.println("Size struktur utama : " + service.size());
                        System.out.println("Invariant OK        : " + service.getData().validateInvariant());
                    }
                    case "0" -> {
                        running = false;
                        System.out.println("Keluar. Terima kasih.");
                    }
                    default -> System.out.println("Pilihan tidak dikenal.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
        sc.close();
    }

    private static void tampilMenu() {
        System.out.println();
        System.out.println("---------- MENU ----------");
        System.out.println("1.  Impor CSV");
        System.out.println("2.  Tampilkan semua putusan");
        System.out.println("3.  Tambah putusan");
        System.out.println("4.  Hapus putusan (by posisi / caseId)");
        System.out.println("5.  Cari (caseId / nomor perkara / posisi batas)");
        System.out.println("6.  Filter");
        System.out.println("7.  Agregasi");
        System.out.println("8.  Urutkan by vonis");
        System.out.println("9.  Tampilkan Audit Log");
        System.out.println("10. Ekspor (CSV / TXT)");
        System.out.println("11. Cek size & invariant");
        System.out.println("0.  Keluar");
    }

    private static void doImport(PutusanService service, Scanner sc) throws Exception {
        System.out.print("Path CSV [data/putusan-50.csv]: ");
        String path = sc.nextLine().trim();
        if (path.isEmpty()) path = "data/putusan-50.csv";
        CsvPutusanReader.ImportResult res = service.imporDariCsv(Paths.get(path));
        System.out.printf("Import selesai. Berhasil: %d, Gagal: %d%n", res.jumlahBerhasil, res.jumlahGagal);
        res.pesanError.forEach(System.out::println);
    }

    private static void doTambah(PutusanService service, Scanner sc) {
        System.out.println("Fitur tambah interaktif penuh belum diimplementasi di menu ini.");
        System.out.println("Gunakan unit test atau tambah programatik untuk demonstrasi.");
        System.out.println("(Untuk UTS, operasi add sudah tersedia di PutusanService.tambah())");
    }

    private static void doHapus(PutusanService service, Scanner sc) {
        System.out.print("Hapus by (1) posisi  atau (2) caseId ? ");
        String mode = sc.nextLine().trim();
        if ("1".equals(mode)) {
            System.out.print("Posisi (0-based): ");
            int pos = Integer.parseInt(sc.nextLine().trim());
            Putusan removed = service.hapus(pos);
            System.out.println("Dihapus: " + removed.toSummary());
        } else {
            System.out.print("caseId: ");
            String id = sc.nextLine().trim();
            Putusan removed = service.hapusByCaseId(id);
            System.out.println("Dihapus: " + removed.toSummary());
        }
    }

    private static void doCari(PutusanService service, Scanner sc) {
        SearchService search = new SearchService(service.getData());
        System.out.print("Cari by (1) caseId  (2) nomorPerkara  (3) posisi batas: ");
        String mode = sc.nextLine().trim();

        SearchService.SearchResult result;
        switch (mode) {
            case "1" -> {
                System.out.print("caseId: ");
                result = search.cariByCaseId(sc.nextLine().trim());
            }
            case "2" -> {
                System.out.print("nomorPerkara: ");
                result = search.cariByNomorPerkara(sc.nextLine().trim());
            }
            case "3" -> {
                System.out.print("Posisi (0=first, size-1=last): ");
                int pos = Integer.parseInt(sc.nextLine().trim());
                result = search.cariPadaPosisi(pos);
            }
            default -> {
                System.out.println("Mode tidak dikenal");
                return;
            }
        }

        if (result.ditemukan()) {
            System.out.println("DITEMUKAN di index " + result.index + " setelah " + result.steps + " langkah");
            System.out.println(result.putusan.toSummary());
        } else {
            System.out.println("TIDAK DITEMUKAN setelah " + result.steps + " langkah");
        }
    }

    private static void doFilter(PutusanService service, Scanner sc) {
        FilterService.FilterCriteria c = new FilterService.FilterCriteria();
        System.out.print("Jenis narkotika (kosong=skip): ");
        String j = sc.nextLine().trim();
        if (!j.isEmpty()) c.jenisNarkotika = j;

        System.out.print("Berat min (kosong=skip): ");
        String bm = sc.nextLine().trim();
        if (!bm.isEmpty()) c.beratMin = Double.parseDouble(bm);

        System.out.print("Berat max (kosong=skip): ");
        String bx = sc.nextLine().trim();
        if (!bx.isEmpty()) c.beratMax = Double.parseDouble(bx);

        System.out.print("Pasal terbukti mengandung (kosong=skip): ");
        String p = sc.nextLine().trim();
        if (!p.isEmpty()) c.pasalTerbukti = p;

        System.out.print("Vonis min bulan (kosong=skip): ");
        String vm = sc.nextLine().trim();
        if (!vm.isEmpty()) c.vonisMin = Integer.parseInt(vm);

        System.out.print("Vonis max bulan (kosong=skip): ");
        String vx = sc.nextLine().trim();
        if (!vx.isEmpty()) c.vonisMax = Integer.parseInt(vx);

        FilterService filter = new FilterService(service.getData());
        DynamicArray<Putusan> hasil = filter.filter(c);
        System.out.println("Hasil filter: " + hasil.size() + " record");
        for (int i = 0; i < hasil.size(); i++) {
            System.out.println("  " + hasil.get(i).toSummary());
        }
    }

    private static void doAgregasi(PutusanService service) {
        AggregationService agg = new AggregationService(service.getData());
        System.out.println(agg.agregasi());
    }

    private static void doUrutkan(PutusanService service, Scanner sc) {
        System.out.print("Urutan (1) Ascending  (2) Descending: ");
        String o = sc.nextLine().trim();
        SortingService.Order order = "2".equals(o)
                ? SortingService.Order.DESCENDING
                : SortingService.Order.ASCENDING;

        SortingService sorter = new SortingService(service.getData());
        DynamicArray<Putusan> sorted = sorter.sortByVonis(order);

        System.out.println("Hasil pengurutan (struktur utama TIDAK berubah):");
        for (int i = 0; i < Math.min(10, sorted.size()); i++) {
            System.out.println("  " + sorted.get(i).toSummary());
        }
        if (sorted.size() > 10) {
            System.out.println("  ... (" + (sorted.size() - 10) + " lagi)");
        }
        System.out.println("Size struktur utama tetap: " + service.size());
    }

    private static void doEkspor(PutusanService service, Scanner sc) throws Exception {
        System.out.print("Format (1) CSV  (2) TXT: ");
        String fmt = sc.nextLine().trim();
        System.out.print("Path output: ");
        String path = sc.nextLine().trim();
        if (path.isEmpty()) {
            path = fmt.equals("1") ? "output/hasil.csv" : "output/hasil.txt";
        }

        ExportService exporter = new ExportService();
        if ("1".equals(fmt)) {
            exporter.eksporCsv(service.getData(), Paths.get(path));
        } else {
            exporter.eksporTxt(service.getData(), Paths.get(path), "Laporan Semua Putusan");
        }
        System.out.println("Berhasil diekspor ke " + path);
    }
}
