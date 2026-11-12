package id.ac.umm.strukturdata.struktur;

import java.util.Objects;

/**
 * Implementasi Singly Linked List generik dari nol.
 * Digunakan sebagai struktur sekunder untuk Audit Log.
 * Tidak menggunakan java.util.LinkedList.
 *
 * Invariant:
 * - head == null  <=>  size == 0
 * - tail.next selalu null (jika tail != null)
 * - size sesuai jumlah node yang dapat ditelusuri dari head
 */
public class SinglyLinkedList<T> implements StrukturData<T> {

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public SinglyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public void add(T value) {
        addLast(value);
    }

    public void addFirst(T value) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = tail = node;
        } else {
            node.next = head;
            head = node;
        }
        size++;
    }

    public void addLast(T value) {
        Node<T> node = new Node<>(value);
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    public void insert(int position, T value) {
        if (position < 0 || position > size) {
            throw new IndexOutOfBoundsException("Posisi insert " + position + " tidak valid");
        }
        if (position == 0) {
            addFirst(value);
            return;
        }
        if (position == size) {
            addLast(value);
            return;
        }
        Node<T> prev = nodeAt(position - 1);
        Node<T> node = new Node<>(value);
        node.next = prev.next;
        prev.next = node;
        size++;
    }

    @Override
    public T get(int position) {
        return nodeAt(position).data;
    }

    @Override
    public T remove(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Posisi " + position + " tidak valid");
        }
        T removed;
        if (position == 0) {
            removed = head.data;
            head = head.next;
            if (head == null) {
                tail = null;
            }
        } else {
            Node<T> prev = nodeAt(position - 1);
            removed = prev.next.data;
            prev.next = prev.next.next;
            if (prev.next == null) {
                tail = prev;
            }
        }
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
        Node<T> current = head;
        while (current != null) {
            if (Objects.equals(current.data, value)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    private Node<T> nodeAt(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Posisi " + position + " tidak valid");
        }
        Node<T> current = head;
        for (int i = 0; i < position; i++) {
            current = current.next;
        }
        return current;
    }

    /**
     * Mengembalikan elemen terakhir tanpa menghapus (berguna untuk audit).
     */
    public T peekLast() {
        if (tail == null) {
            throw new IllegalStateException("List kosong");
        }
        return tail.data;
    }

    @Override
    public boolean validateInvariant() {
        if (size < 0) {
            throw new IllegalStateException("size < 0");
        }
        if (size == 0) {
            if (head != null || tail != null) {
                throw new IllegalStateException("size==0 tetapi head/tail tidak null");
            }
            return true;
        }
        if (head == null || tail == null) {
            throw new IllegalStateException("size>0 tetapi head atau tail null");
        }
        // hitung jumlah node
        int count = 0;
        Node<T> current = head;
        Node<T> last = null;
        while (current != null) {
            count++;
            last = current;
            current = current.next;
        }
        if (count != size) {
            throw new IllegalStateException("Jumlah node (" + count + ") != size (" + size + ")");
        }
        if (last != tail) {
            throw new IllegalStateException("Node terakhir tidak sama dengan tail");
        }
        if (tail.next != null) {
            throw new IllegalStateException("tail.next harus null");
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SinglyLinkedList[size=" + size + "] {");
        Node<T> current = head;
        int i = 0;
        while (current != null) {
            if (i > 0) sb.append(" -> ");
            sb.append(current.data);
            current = current.next;
            i++;
        }
        sb.append("}");
        return sb.toString();
    }
}
