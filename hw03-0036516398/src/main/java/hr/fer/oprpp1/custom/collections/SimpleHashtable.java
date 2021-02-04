package hr.fer.oprpp1.custom.collections;

import java.util.*;

/**
 * Razred implementacije mape rasprešenog adresiranja.
 * Omogućava pohranu uređenih parova (ključ,vrijednost).
 * Ova mapa onemogućava {@code null} kao ključ, ali dopušta kao vrijednost.
 *
 * @param <K> tip kluč
 * @param <V> tip vrijednost
 * @author matej
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K, V>> {
    private int size;
    private TableEntry<K, V>[] table;
    private static final int DEFAULT_SIZE = 16;
    private int modificationCount;

    /**
     * Stvara mapu s defaultnom velicinom hash tablice {@code DEFAULT_SIZE = 16}.
     */
    public SimpleHashtable() {
        this(DEFAULT_SIZE);
    }

    /**
     * Stvara mapu s velicinom hash tablice zadanom u argumentu.
     * Vrijednost ce se postaviti na najblizi veci ili jednak broj koji je potencija broja 2.
     *
     * @param capacity velicina hash tablice
     */
    @SuppressWarnings("unchecked")
    public SimpleHashtable(int capacity) {
        if (capacity < 1) throw new IllegalArgumentException("Size has to be >=1 .");
        table = (TableEntry<K, V>[]) new TableEntry[nearestFactorOf2LargerOrEqual(capacity)];
        modificationCount = 0;
    }

    /**
     * Metoda put dodaje element u mapu. Ukoliko već postoji element s istim
     * ključem vrijednost se zamijenjuje s novim. Ukolko ključ ne postoji u mapi
     * dodaje se nova vrijednost.
     * Povratna je vrijednost stara vrijednost uz ključ ukoliko ona postoji.
     *
     * @param key   ključ
     * @param value vrijednost
     * @return stara vrijednost ako postoji, inače {@code null}
     * @throws NullPointerException ako je predan key {@code null}
     */
    public V put(K key, V value) {
        var entry = findEntryForKey(Objects.requireNonNull(key));
        V oldValue = null;
        if (entry != null) {
            //Postoji već
            oldValue = entry.value;
            entry.value = value;
        } else {
            //Ne postoji
            modificationCount++;

            if (flowWarningCheck())
                doubleHashTable();

            var slot = calculateSlot(key);
            entry = table[slot];

            //Rubni slucaj
            if (table[slot] == null) {
                table[slot] = new TableEntry<>(key, value);
                size++;
                return null;
            }

            while (entry.next != null)
                entry = entry.next;

            entry.next = new TableEntry<>(key, value);
            size++;
        }
        return oldValue;
    }

    /**
     * Metoda dohvaća vrijednost iz mape za zadanu vrijednost ako postoji.
     * Ukoliko nema vrijednosti za zadani ključ vrati {@code null}
     *
     * @param key kljuc za koju se traži vrijednost
     * @return vrijednost za zadani ključ
     */
    public V get(Object key) {
        var entry = findEntryForKey(key);

        return entry != null ? entry.value : null;
    }

    private boolean flowWarningCheck() {
        return size * 1.0 / getHashTableCapacity() >= 0.75;
    }

    @SuppressWarnings("unchecked")
    private void doubleHashTable() {
        var array = toArray();
        var currentSize = getHashTableCapacity();

        table = (TableEntry<K, V>[]) new TableEntry[2 * currentSize];
        size = 0;
        modificationCount++;

        Arrays.stream(array).forEach(v -> put(v.key, v.value));
    }

    /**
     * Metoda koja briše sve uređene parove iz tablice
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        modificationCount++;
        table = (TableEntry<K, V>[]) new TableEntry[getHashTableCapacity()];
    }

    private int calculateSlot(Object o) {
        return (Math.abs(o.hashCode()) % getHashTableCapacity());
    }

    /**
     * Metoda dohvaća broj elemenata u mapi.
     *
     * @return broj elemenata u mapi
     */
    public int size() {
        return size;
    }

    private int getHashTableCapacity() {
        return table.length;
    }

    private TableEntry<K, V> findEntryForKey(Object key) {
        if (key == null) return null;

        var slot = calculateSlot(key);
        TableEntry<K, V> tableEntry = table[slot];
        //Rubni slucaj

        if (tableEntry == null)
            return null;

        do {
            if (tableEntry.key.equals(key))
                return tableEntry;

            tableEntry = tableEntry.next;
        } while (tableEntry != null);

        return null;
    }

    /**
     * Metoda pregledava mapu i vraća nalazi li se element s zadanim ključem
     * u njoj.
     * Kompleksnost je O(1)
     *
     * @param key ključ za koji se pregledava
     * @return {@code true} ako je ključ u mapi, inače {@code false}
     */
    public boolean containsKey(Object key) {
        return findEntryForKey(key) != null;
    }

    /**
     * Metoda pregledava mapu i vraća nalazi li se element s zadanom vrijednošću.
     * Kompleksnost je O(n)
     *
     * @param value vrijednost koja se pretražuje u mapi.
     * @return {@code true} ako je vrijednost u mapi, inače {@code false}
     */
    public boolean containsValue(Object value) {
        for (var tableEntry : table) {
            if (tableEntry == null) continue;
            var entry = tableEntry;
            while (entry != null) {
                if (entry.value.equals(value)) {
                    return true;
                }
                entry = entry.next;
            }
        }
        return false;
    }

    /**
     * Metoda koja iz kolekcije briše element s zadanom vrijednosti ako on postoji u mapi.
     * Ukoliko je element obrisan vraća se njegova vrijednost, inače se vraća {@code null}.
     *
     * @param key ključ vrijednosti koja se briše.
     * @return stara vrijednost ako je element obrisan iz mape.
     */
    public V remove(Object key) {
        if (key == null) return null;

        var slot = calculateSlot(key);
        var entry = table[slot];

        //rubni slucaj
        if (entry.key.equals(key)) {
            table[slot] = entry.next;
            size--;
            modificationCount++;
            return entry.value;
        }

        while (entry.next != null && ! entry.next.key.equals(key)) {
            entry = entry.next;
        }

        if (entry.next == null)
            return null;

        if (entry.next.key.equals(key)) {
            V oldValue = entry.next.value;
            entry.next = entry.next.next;
            modificationCount++;
            size--;
            return oldValue;
        }

        return null;
    }

    /**
     * Metoda sve elemente koje mapa sadrži stavlja u jedan novi array
     * tipa {@code TableEntry<K,V>}. Array je veličine broja elemenata u mapi.
     *
     * @return novi array s svim vrijednostima mape
     */
    @SuppressWarnings("unchecked")
    public TableEntry<K, V>[] toArray() {
        TableEntry<K, V>[] array = (TableEntry<K, V>[]) new TableEntry[size];

        int i = 0;
        for (var slotElement : table) {
            if (slotElement != null) {
                array[i++] = slotElement;

                var sameSlotElement = slotElement;
                while (sameSlotElement.next != null) {
                    sameSlotElement = sameSlotElement.next;
                    array[i++] = sameSlotElement;
                }
            }
        }

        return array;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        Arrays.stream(toArray()).forEach(v -> sb.append(v).append(", "));

        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);

        sb.append("]");
        return sb.toString();
    }

    private static int nearestFactorOf2LargerOrEqual(int size) {
        int number = 1;
        while (number < size) {
            number *= 2;
        }
        return number;
    }

    @Override
    public Iterator<TableEntry<K, V>> iterator() {
        return new IteratorImpl(modificationCount);
    }

    /**
     * Statička klasa predstavlja čvor u mapi. Sadrži ključ i vrijednost.
     * Klasa se može generizirati.
     * Ključ ne smije biti null.
     *
     * @param <K> tip kljuca
     * @param <V> tip vrijednosti
     */
    public static class TableEntry<K, V> {
        private final K key;
        private V value;
        private TableEntry<K, V> next;

        public TableEntry(K key, V value, TableEntry<K, V> next) {
            this.key = Objects.requireNonNull(key);
            this.value = value;
            this.next = next;
        }

        public TableEntry(K key, V value) {
            this(key, value, null);
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return key.toString() + "=" + value.toString();
        }
    }

    /**
     * Implementacije iteratora za SimpleHashtable
     */
    private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K, V>> {
        private int savedModificationCount;
        private int currentSlotIndex;
        private TableEntry<K, V> currentEntry;
        private boolean removeAllowed;

        public IteratorImpl(int modificationCount) {
            this.savedModificationCount = modificationCount;
            currentEntry = null;
            currentSlotIndex = 0;
            removeAllowed = false;
        }

        @Override
        public boolean hasNext() {
            if (modificationCount != savedModificationCount) throw new ConcurrentModificationException();

            if (currentEntry != null && currentEntry.next != null)
                return true;

            for (var tempIndex = currentSlotIndex; tempIndex < getHashTableCapacity(); tempIndex++) {
                if (table[tempIndex] != null)
                    return true;

            }
            return false;
        }

        @Override
        public TableEntry<K, V> next() {
            if (modificationCount != savedModificationCount) throw new ConcurrentModificationException();

            if (currentEntry != null && currentEntry.next != null) {
                currentEntry = currentEntry.next;
                removeAllowed = true;
                return currentEntry;
            }
            for (; currentSlotIndex < getHashTableCapacity(); currentSlotIndex++) {
                if (table[currentSlotIndex] != null) {
                    currentEntry = table[currentSlotIndex++];
                    removeAllowed = true;
                    return currentEntry;
                }
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            if (modificationCount != savedModificationCount) throw new ConcurrentModificationException();
            if (! removeAllowed) throw new IllegalStateException("Removed called when not allowed.");

            var oldEntry = currentEntry;
            if (currentEntry.next != null)
                currentEntry = currentEntry.next;
            else currentEntry = null;

            removeAllowed = false;
            savedModificationCount++;
            SimpleHashtable.this.remove(oldEntry.key);
        }
    }

}
