package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

import static java.lang.Math.max;

/**
 * Implementacija array-backed kolekcije
 * Duplikati su dozvoljeni, ali spremanje <code>null</code> referenci nije
 *
 * @author matej
 */
public class ArrayIndexedCollection<T> implements List<T> {
    private int size;
    private Object[] elements;
    private long modificationCount = 0;

    private static final int DEFAULT_SIZE = 16;

    /**
     * Defaultni konstruktor koji predefinira polje od 16 elemenata
     */
    public ArrayIndexedCollection() {
        this(DEFAULT_SIZE);
    }

    /**
     * Konstruktor koji predefinira polje s danim brojem elemenata
     *
     * @param initialCapacity broj elemenata polja kojim ce ono alocirati
     * @throws IllegalArgumentException ako je predan broj manji od 1 za velicinu polja
     */
    public ArrayIndexedCollection(int initialCapacity) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException("Initial capacity must be higher then 1 and is " + initialCapacity);
        elements = new Object[initialCapacity];
        size = 0;
    }

    /**
     * Metoda koja vraca velicinu polja alociranog
     *
     * @return velicina alociranog polja
     */
    public int getAllocatedSize() {
        return elements.length;
    }

    /**
     * Konstruktor koji u sebe kopira elemente danog polja bez da ga modificira.
     * Ukoliko ima više od 16 elemenata u danom polju, polje će biti veličine tog broja elemata, inače će imati 16 elemenata
     *
     * @param otherCollection druga kolekcija čiji se elementi pohranjuju i koja se ne mijenja
     * @throws NullPointerException ako je predan null kao druga kolekcija
     */
    public ArrayIndexedCollection(Collection<? extends T> otherCollection) {
        this(otherCollection, DEFAULT_SIZE);
    }

    /**
     * Konstruktor koji u sebe kopira elemente danog polja bez da ga modificira.
     * Ukoliko ima više od danog broja elemenata u danom polju, polje će biti veličine broja elemata kolekcije,
     * inače će imati zadani broj elemenata
     *
     * @param otherCollection druga kolekcija čiji se elementi pohranjuju i koja se ne mijenja
     * @param initialCapacity broj elemenata polja kojim ce ono alocirati
     * @throws NullPointerException     ako je predan null kao druga kolekcija
     * @throws IllegalArgumentException ako je predan broj manji od 1 za velicinu polja
     */
    public ArrayIndexedCollection(Collection<? extends T> otherCollection, int initialCapacity) {
        if (initialCapacity < 1)
            throw new IllegalArgumentException("Initial capacity must be higher then 1 and is " + initialCapacity);
        if (otherCollection == null) throw new NullPointerException("Collection must not be null!");

        int otherSize = otherCollection.size();
        elements = new Object[max(initialCapacity, otherSize)];
        size = 0;

        var otherArray = otherCollection.toArray();
        for (int i = 0; i < otherSize; i++) {
            elements[i] = otherArray[i];
            if (elements[i] != null) size++;
        }

    }

    /**
     * Metoda koja dodaje element u prvo prazno mjesto u kolekciji.
     * Prvo prazno mjesto se podrazumijeva da se nalazi na kaju svih ne-null elemenata.
     * Kompleksnost je O(n)
     *
     * @param value element koji se nadodaje u kolekciju
     * @throws NullPointerException ako je predan null kao element
     */
    @Override
    public void add(T value) {
        if (value == null) throw new NullPointerException("Value can't be null");
        if (size >= elements.length) {
            var oldElements = elements;
            elements = new Object[elements.length * 2];

            if (oldElements.length >= 0) System.arraycopy(oldElements, 0, elements, 0, oldElements.length);
        }
        elements[size++] = value;
        modificationCount++;
    }

    /**
     * Metoda koja vraća element na zadanom indexu.
     * Kompleksnost je O(1)
     *
     * @param index na kojem se nalazi element koji se vrati
     * @return objekt koji se nalazi na danom indexu
     * @throws IndexOutOfBoundsException ako je index izvan raspona [0,size-1]
     */
    public T get(int index) {
        Objects.checkIndex(index, size);
        return elementData(index);
    }

    @SuppressWarnings("unchecked")
    T elementData(int index) {
        return (T) elements[index];
    }

    /**
     * Metoda koja iz kolekcija element na zadanom indexu.
     *
     * @throws IndexOutOfBoundsException ako je index izvan raspona [0,size-1]
     */
    public void remove(int index) {
        Objects.checkIndex(index, size);
        elements[index] = null;

        if (size - index >= 0) System.arraycopy(elements, index + 1, elements, index, size - index);
        size--;
        modificationCount++;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        modificationCount++;
        size=0;
    }

    /**
     * Metoda koja na danu poziciju stavlja dan element bez da se izgubi iti jedan element iz polja.
     * Elementi koji smetaju će se pokaknuti tako da stane.
     * Kompleksnost O(n)
     *
     * @param value    vrijednost koju zelimo upisati u kolekciju
     * @param position pozicija na koju zelimo upisti vrijednost u kolekciju
     * @throws IndexOutOfBoundsException ako je pozicija izvan intervala [0,size]
     * @throws NullPointerException      ako je predan null kao objekt
     */
    public void insert(T value, int position) {
        if (position < 0 || position > size) throw new IndexOutOfBoundsException("Invalid position!");
        Objects.requireNonNull(value);

        System.arraycopy(elements, position, elements, position + 1, size - position);
        //Stvara prazno mjesto na poziciji
        elements[position] = value;
        size++;
        modificationCount++;
    }

    /**
     * Metoda koja vraća prvi pronađeni index predanog elementa u kolekciji.
     * Ukoliko element nije u kolekciji vraća -1.
     * Kompleksnost O(n)
     *
     * @param value element za koji se pretražuje index
     * @return index ako je elemnt pronađen, inače -1
     */
    public int indexOf(Object value) {
        if (value == null) return - 1;
        for (int i = 0; i < size; i++) {
            if (elements[i] != null && elements[i].equals(value))
                return i;
        }
        return - 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(Object value) {
        return indexOf(value) >= 0;
    }

    @Override
    public boolean remove(Object value) {
        if (! contains(value)) return false;
        remove(indexOf(value));
        return true;
    }

    @Override
    public Object[] toArray() {
        Object[] newArray = new Object[size];
        System.arraycopy(elements, 0, newArray, 0, size);
        return newArray;
    }

    @Override
    public ElementsGetter<T> createElementsGetter() {
        return new ArrayIndexedCollectionElementsGetter<>(this);
    }

    /**
     * Klasa ElekementsGettera za ArrayIndexedCollection
     * Podrazumijeva se da se polje neće mijenjati za vrijeme korištenja.
     */
    private static class ArrayIndexedCollectionElementsGetter<V> implements ElementsGetter<V> {
        private int position = 0;
        private final ArrayIndexedCollection<V> collection;
        private final long savedModificationCount;

        /**
         * Konstruktor ElemetsGettera Array kolekcije
         *
         * @param collection referenca na kolekciju
         * @throws NullPointerException ako je kao polje predan null
         */
        private ArrayIndexedCollectionElementsGetter(ArrayIndexedCollection<V> collection) {
            this.collection = Objects.requireNonNull(collection);
            this.savedModificationCount = collection.modificationCount;
        }

        @Override
        public boolean hasNextElement() {

            if (savedModificationCount != collection.modificationCount)
                throw new ConcurrentModificationException("Collection has been modified!");
            return position < collection.size;
        }

        @Override
        public V getNextElement() {
            if (savedModificationCount != collection.modificationCount)
                throw new ConcurrentModificationException("Collection has been modified!");
            if (! hasNextElement()) throw new NoSuchElementException("There are no elements!");

            return collection.get(position++);
        }
    }
}
