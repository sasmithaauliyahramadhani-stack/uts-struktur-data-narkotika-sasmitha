package id.ac.umm.strukturdata;

import id.ac.umm.strukturdata.model.Putusan;
import id.ac.umm.strukturdata.service.*;
import id.ac.umm.strukturdata.struktur.DynamicArray;
import id.ac.umm.strukturdata.util.CsvPutusanReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Skenario pengujian wajib sesuai panduan UTS.
 */
class ImportAndScenarioTest {

    private PutusanService service;
    private static final Path CSV = Paths.get("data/putusan-50.csv");

    @BeforeEach
    void setUp() throws Exception {
        service = new PutusanService("tester");
        assumeCsvExists();
        CsvPutusanReader.ImportResult res = service.imporDariCsv(CSV);
        assertEquals(50, res.jumlahBerhasil, "Harus berhasil impor 50 record");
        assertEquals(0, res.jumlahGagal);
    }

    private void assumeCsvExists() {
        if (!CSV.toFile().exists()) {
            // coba relative dari root project
            Path alt = Paths.get("uts-struktur-data-narkotika-kelompok-XX/data/putusan-50.csv");
            // biarkan fail nanti jika benar-benar tidak ada
        }
    }

    @Test
    void scenario1_load50_sizeIs50() {
        assertEquals(50, service.size());
        assertTrue(service.getData().validateInvariant());
    }

    @Test
    void scenario2_searchExistingAndNonExistingAndBoundary() {
        SearchService search = new SearchService(service.getData());

        // yang ada
        SearchService.SearchResult ada = search.cariByCaseId("P001");
        assertTrue(ada.ditemukan());
        assertEquals("P001", ada.putusan.getCaseId());
        assertTrue(ada.steps > 0);

        // yang tidak ada
        SearchService.SearchResult tidakAda = search.cariByCaseId("P999");
        assertFalse(tidakAda.ditemukan());
        assertEquals(50, tidakAda.steps); // harus scan seluruhnya

        // posisi batas first
        SearchService.SearchResult first = search.cariPadaPosisi(0);
        assertTrue(first.ditemukan());
        assertEquals(0, first.index);

        // posisi batas last
        SearchService.SearchResult last = search.cariPadaPosisi(49);
        assertTrue(last.ditemukan());
        assertEquals(49, last.index);
    }

    @Test
    void scenario3_removeFirstMiddleLast() {
        Putusan first = service.get(0);
        Putusan middle = service.get(25);
        Putusan last = service.get(49);

        // hapus last dulu supaya index middle tidak bergeser parah
        Putusan removedLast = service.hapus(49);
        assertEquals(last.getCaseId(), removedLast.getCaseId());
        assertEquals(49, service.size());

        Putusan removedFirst = service.hapus(0);
        assertEquals(first.getCaseId(), removedFirst.getCaseId());
        assertEquals(48, service.size());

        // middle sekarang di posisi sekitar 24
        int midIdx = service.cariIndexByCaseId(middle.getCaseId());
        assertTrue(midIdx >= 0);
        Putusan removedMid = service.hapus(midIdx);
        assertEquals(middle.getCaseId(), removedMid.getCaseId());
        assertEquals(47, service.size());

        assertTrue(service.getData().validateInvariant());
    }

    @Test
    void scenario4_addBackThreeRecords() {
        // simpan 3 record dulu
        Putusan p0 = service.get(0);
        Putusan p1 = service.get(1);
        Putusan p2 = service.get(2);

        service.hapus(0);
        service.hapus(0);
        service.hapus(0);
        assertEquals(47, service.size());

        service.tambah(p0);
        service.tambah(p1);
        service.tambah(p2);
        assertEquals(50, service.size());
        assertTrue(service.getData().validateInvariant());
    }

    @Test
    void scenario5_combinedFilter() {
        FilterService.FilterCriteria c = new FilterService.FilterCriteria();
        c.jenisNarkotika = "Sabu";
        c.vonisMin = 100;
        c.vonisMax = 150;

        FilterService filter = new FilterService(service.getData());
        DynamicArray<Putusan> hasil = filter.filter(c);

        assertTrue(hasil.size() > 0);
        for (int i = 0; i < hasil.size(); i++) {
            Putusan p = hasil.get(i);
            assertTrue(p.getJenisNarkotika().toLowerCase().contains("sabu"));
            assertTrue(p.getVonisBulan() >= 100 && p.getVonisBulan() <= 150);
        }
        // struktur utama tidak berubah
        assertEquals(50, service.size());
    }

    @Test
    void scenario7_invalidRecordDoesNotBreakStructure() throws Exception {
        // struktur sudah terisi 50
        int sizeSebelum = service.size();
        assertTrue(service.getData().validateInvariant());

        // mencoba menambah putusan null harus ditolak
        assertThrows(IllegalArgumentException.class, () -> service.tambah(null));
        assertEquals(sizeSebelum, service.size());
        assertTrue(service.getData().validateInvariant());
    }

    @Test
    void aggregationWorks() {
        AggregationService agg = new AggregationService(service.getData());
        AggregationService.AggregationResult r = agg.agregasi();
        assertEquals(50, r.jumlahPerkara);
        assertTrue(r.rataRataVonis > 0);
        assertFalse(r.distribusiJenis.isEmpty());
    }

    @Test
    void sortingDoesNotMutateOriginal() {
        SortingService sorter = new SortingService(service.getData());
        DynamicArray<Putusan> sorted = sorter.sortByVonis(SortingService.Order.ASCENDING);

        assertEquals(50, sorted.size());
        // cek terurut ascending
        for (int i = 1; i < sorted.size(); i++) {
            assertTrue(sorted.get(i - 1).getVonisBulan() <= sorted.get(i).getVonisBulan());
        }
        // original tetap 50 dan tidak berubah urutan awal (P001 masih di index 0)
        assertEquals(50, service.size());
        assertEquals("P001", service.get(0).getCaseId());
    }
}
