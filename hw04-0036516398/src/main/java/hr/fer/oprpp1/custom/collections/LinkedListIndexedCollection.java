package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Implementacija linked-list-backed kolekcije
 * Duplikati su dozvoljeni, ali spremanje <code>null</code> referenci nije
 *
 * @author matej
 */
public class LinkedListIndexedCollection<T> implements List<T> {
    private int size;
    private ListNode<T> first;
    private ListNode<T> last;
    private long modificationCount = 0;

    /**
     * Defaultni konstuktor koji ne stvara niti jedan cvor
     */
    public LinkedListIndexedCollection() {
        this.size = 0;
        this.first = null;
        this.last = null;
    }

    /**
     * Konstruktor koji će spremiti u kolekciju sve elemente iz dane bez da mijenja danu kolekciju
     *
     * @param collection kolekcija čiji će se članovi kopirati
     * @throws NullPointerException ako je predan null kao kolekcija
     */
    public LinkedListIndexedCollection(Collection<? extends T> collection) {
        addAll(Objects.requireNonNull(collection));
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Metoda koja dodaje element na kraj u kolekciju.
     * Kompleksnost O(1)
     *
     * @param value element koji se nadodaje u kolekciju
     * @throws NullPointerException ako je element <code>null</code>
     */
    @Override
    public void add(T value) {
        if (value == null) throw new NullPointerException("Value can't be null!");
        ListNode<T> node = new ListNode<>(value);
        if (size == 0) {
            first = node;
        } else {
            last.next = node;
            node.previous = last;
        }
        last = node;
        size++;
        modificationCount++;
    }

    @Override
    public boolean contains(Object value) {
        ListNode<T> node = first;
        while (node != null) {
            if (node.element.equals(value)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public boolean remove(Object value) {
        ListNode<T> node = first;
        while (node != null) {
            if (node.element.equals(value)) {
                node.next.previous = node.previous;
                node.previous.next = node.next;
                size--;
                modificationCount++;
                return true;
            }
            node = node.next;
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        var array = new Object[size];
        ListNode<T> node = first;
        for (int i = 0; i < size; i++, node = node.next) {
            array[i] = node.element;
        }
        return array;
    }

    @Override
    public void clear() {
        size = 0;
        first = null;
        last = null;
        modificationCount++;
    }

    /**
     * Metoda koja vraca objekt koji se nalazi na zadanom indexu u kolekciji
     * Validni indexi su [0,size-1]
     *
     * @param index objekta koji se trazi
     * @return objekt na zadanom indeksu
     * @throws IndexOutOfBoundsException ako je index izvan intervala [0,size-1]
     */
    public T get(int index) {
        Objects.checkIndex(index, size);
        ListNode<T> node = findNodeInPosition(index);
        return node.element;
    }

    /**
     * Metoda koja na danu poziciju stavlja dan element bez da se izgubi iti jedan element iz polja.
     * Elementi koji smetaju će se pokaknuti tako da stane.
     * Kompleksnost O(n)
     *
     * @param value    vrijednost koju zelimo upisati u kolekciju
     * @param position pozicija na koju zelimo upisti vrijednost u kolekciju
     * @throws IndexOutOfBoundsException ako je pozicija izvan intervala [0,size]
     * @throws NullPointerException      ako je predan element <code>null</code>
     */
    public void insert(T value, int position) {
        if (value == null) throw new NullPointerException("Value can't be null!");
        if (position < 0 || position >= size) throw new IndexOutOfBoundsException("Index is invalid!");
        ListNode<T> node = findNodeInPosition(position);
        ListNode<T> newNode = new ListNode<>(value, node.previous, node);
        if (node.previous != null)
            node.previous.next = newNode;
        if (node.next != null)
            node.next.previous = newNode;
        if (position == 0)
            first = newNode;
        if (position == size - 1)
            last = newNode;

        size++;
        modificationCount++;
    }

    /**
     * Privatna metoda koja pronalazi traženi čvor na zadanom indeksu u linked-listi i vrati ga
     *
     * @param position indeks čvora
     * @return ListNode na poziciji indeksa
     */
    private ListNode<T> findNodeInPosition(int position) {
        ListNode<T> node;
        if (position < size / 2) {
            node = first;
            for (int i = 0; i < size / 2; i++, node = node.next) {
                if (i == position) {
                    break;
                }
            }
        } else {
            node = last;
            for (int i = size - 1; i >= size / 2; i--, node = node.previous) {
                if (i == position) {
                    break;
                }
            }
        }
        return node;
    }

    /**
     * Metoda koja vraća prvi pronađeni index predanog elementa u kolekciji.
     * Ukoliko element nije u kolekciji vraća -1.
     * <p>
     * O(n) kompleksnost
     *
     * @param value element za koji se pretražuje index
     * @return index ako je elemnt pronađen, inače -1
     */
    public int indexOf(Object value) {
        ListNode<T> node = first;
        int i = 0;
        while (node != null) {
            if (node.element.equals(value)) {
                return i;
            }
            i++;
            node = node.next;
        }
        return - 1;
    }

    /**
     * Metoda koja iz kolekcija element na zadanom indexu.
     *
     * @throws IndexOutOfBoundsException ako je index izvan raspona [0,size-1]
     */
    public void remove(int index) {
        Objects.checkIndex(index, size);
        ListNode<T> node = findNodeInPosition(index);
        node.previous.next = node.next;
        node.next.previous = node.previous;
        size--;
        modificationCount++;
    }

    @Override
    public ElementsGetter<T> createElementsGetter() {
        return new LinkedListIndexedCollectionElementsGetter<>(this);
    }

    /**
     * Statička klasa koja predstavlja čvor linked-liste
     * Sadrži  prijašnji i sljedeći pokazivač te element koji sadrži vrijednost
     */
    private static class ListNode<E> {
        ListNode<E> previous;
        ListNode<E> next;
        final E element;

        /**
         * Konstruktor koji prima element i sprema ga u cvor
         *
         * @param element element koji se sprema
         * @throws NullPointerException ako je poslan null kao element
         */
        private ListNode(E element) {
            this(element, null, null);
        }

        /**
         * Konstrukor koji prima element i pokazivace na susjedne cvorove
         *
         * @param element  element koji se sprema
         * @param previous pokazivac na prijasnji element
         * @param next     pokazivac na iduci element
         * @throws NullPointerException ako je poslan null kao element
         */
        private ListNode(E element, ListNode<E> previous, ListNode<E> next) {
            this.element = Objects.requireNonNull(element);
            this.previous = previous;
            this.next = next;
        }
    }

    /**
     * Klasa ElekementsGettera za LinkedListIndexedCollection
     * Podrazumijeva se da se polje neće mijenjati za vrijeme korištenja.
     */
    private static class LinkedListIndexedCollectionElementsGetter<V> implements ElementsGetter<V> {
        private final LinkedListIndexedCollection<V> collection;
        private final long savedModificationCount;
        private ListNode<V> positionNode;

        /**
         * Konstruktor ElementsGettera LinkedListe koji prima referencu na listu
         *
         * @param collection referenca na linkedListu
         * @throws NullPointerException ako je predan null kao referenca
         */
        private LinkedListIndexedCollectionElementsGetter(LinkedListIndexedCollection<V> collection) {
            this.collection = Objects.requireNonNull(collection);
            positionNode = collection.first;
            savedModificationCount = collection.modificationCount;
        }

        @Override
        public boolean hasNextElement() {
            if (savedModificationCount != collection.modificationCount)
                throw new ConcurrentModificationException("Collection has been modified!");
            return positionNode != null;
        }

        @Override
        public V getNextElement() {
            if (savedModificationCount != collection.modificationCount)
                throw new ConcurrentModificationException("Collection has been modified!");
            if (! hasNextElement()) throw new NoSuchElementException("There are no elements!");

            ListNode<V> currentNode = positionNode;
            positionNode = positionNode.next;
            return currentNode.element;
        }
    }
}

