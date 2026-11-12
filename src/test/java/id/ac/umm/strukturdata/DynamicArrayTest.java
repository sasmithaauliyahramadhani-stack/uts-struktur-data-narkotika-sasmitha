package id.ac.umm.strukturdata;

import id.ac.umm.strukturdata.struktur.DynamicArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {

    private DynamicArray<String> arr;

    @BeforeEach
    void setUp() {
        arr = new DynamicArray<>(4);
    }

    @Test
    void testAddAndSize() {
        assertTrue(arr.isEmpty());
        arr.add("A");
        arr.add("B");
        arr.add("C");
        assertEquals(3, arr.size());
        assertFalse(arr.isEmpty());
        assertTrue(arr.validateInvariant());
    }

    @Test
    void testGetAndSet() {
        arr.add("X");
        arr.add("Y");
        assertEquals("X", arr.get(0));
        assertEquals("Y", arr.get(1));
        String old = arr.set(0, "Z");
        assertEquals("X", old);
        assertEquals("Z", arr.get(0));
        assertTrue(arr.validateInvariant());
    }

    @Test
    void testInsert() {
        arr.add("A");
        arr.add("C");
        arr.insert(1, "B");
        assertEquals(3, arr.size());
        assertEquals("A", arr.get(0));
        assertEquals("B", arr.get(1));
        assertEquals("C", arr.get(2));
        assertTrue(arr.validateInvariant());
    }

    @Test
    void testRemoveFirstMiddleLast() {
        arr.add("A");
        arr.add("B");
        arr.add("C");
        arr.add("D");

        // remove first
        assertEquals("A", arr.remove(0));
        assertEquals(3, arr.size());
        assertEquals("B", arr.get(0));

        // remove middle
        assertEquals("C", arr.remove(1));
        assertEquals(2, arr.size());
        assertEquals("B", arr.get(0));
        assertEquals("D", arr.get(1));

        // remove last
        assertEquals("D", arr.remove(1));
        assertEquals(1, arr.size());
        assertEquals("B", arr.get(0));

        assertTrue(arr.validateInvariant());
    }

    @Test
    void testContainsAndIndexOf() {
        arr.add("apple");
        arr.add("banana");
        assertTrue(arr.contains("banana"));
        assertFalse(arr.contains("cherry"));
        assertEquals(1, arr.indexOf("banana"));
        assertEquals(-1, arr.indexOf("cherry"));
    }

    @Test
    void testSearchWithSteps() {
        arr.add("A");
        arr.add("B");
        arr.add("C");
        int[] found = arr.searchWithSteps("B");
        assertEquals(1, found[0]);
        assertEquals(2, found[1]); // 2 langkah

        int[] notFound = arr.searchWithSteps("Z");
        assertEquals(-1, notFound[0]);
        assertEquals(3, notFound[1]);
    }

    @Test
    void testClear() {
        arr.add("A");
        arr.add("B");
        arr.clear();
        assertEquals(0, arr.size());
        assertTrue(arr.isEmpty());
        assertTrue(arr.validateInvariant());
    }

    @Test
    void testBounds() {
        arr.add("A");
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> arr.remove(5));
    }

    @Test
    void testResize() {
        for (int i = 0; i < 20; i++) {
            arr.add("item-" + i);
        }
        assertEquals(20, arr.size());
        assertTrue(arr.capacity() >= 20);
        assertTrue(arr.validateInvariant());
    }
}
