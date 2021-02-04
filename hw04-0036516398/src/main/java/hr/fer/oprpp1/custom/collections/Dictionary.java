package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

/**
 * Klasa predstavlja mapu
 * Sadrzi genericki kljuc i genericku vrijednost
 * U mapi ne postoje vise ulaza za isti kljuc
 *
 * @param <K> tip kljuca
 * @param <V> tip vrijednosti
 * @author matej
 */
public class Dictionary<K, V> {
    private final ArrayIndexedCollection<KeyValuePair<K, V>> list;

    public Dictionary() {
        list = new ArrayIndexedCollection<>();
    }

    /**
     * Metoda javi je li mapa prazna
     *
     * @return je li mapa prazna
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Metoda vrati velicinu mape
     *
     * @return velicina mape
     */
    public int size() {
        return list.size();
    }

    /**
     * Metoda obrise cijelu mapu
     */
    public void clear() {
        list.clear();
    }

    /**
     * Metoda dodaje u mapu danu kljuc i vrijednost
     * Prilikom upisa prebrisuje element s istim kljucem ako takav postoji
     *
     * @param key   kljuc
     * @param value vrijednost
     * @return upisana brijednost
     */
    public V put(K key, V value) {
        int index = getIndexOfKey(Objects.requireNonNull(key));
        if (index >= 0) {
            var pair = list.get(index);
            V oldValue = pair.getValue();
            pair.setValue(value);

            return oldValue;
        } else {
            KeyValuePair<K, V> pair = new KeyValuePair<>(key, value);
            list.add(pair);
            return null;
        }
    }

    /**
     * Metoda trazi i vraca indeks za zadani objekt
     * Ukoliko objekt nije pronaden vrati -1
     */
    private int getIndexOfKey(Object key) {
        for (int i = 0; i < list.size(); i++) {
            KeyValuePair<K, V> pair = list.get(i);
            if (pair.key.equals(key)) {
                return i;
            }
        }
        return - 1;
    }


    /**
     * Metoda izbacuje iz kolekcije par ukoliko postoji s zadanim kljucem
     * Ukoliko je izbacila par vraca staru vrijednost
     *
     * @param key kljuc za koji se isprobava
     * @return stara vrijednost ako je postojala inace null
     */
    public V remove(K key) {
        int index = getIndexOfKey(Objects.requireNonNull(key));
        if (index >= 0) {
            var pair = list.get(index);
            V oldVal = pair.getValue();
            list.remove(index);
            return oldVal;
        }
        return null;
    }

    /**
     * Metoda pretrazuje mapu i vraca vrijednost za zadnani kljuc ako postojit
     * Ako se vrijednost ne pronade metoda vraca null
     *
     * @param key kljuc za koga se trazi vrijednost
     * @return vrijednost za zadani kljuc ili null ako nije pronadena
     */
    public V get(Object key) {
        int index = getIndexOfKey(key);
        if (index >= 0)
            return list.get(index).getValue();

        return null;
    }

    /**
     * Privatni razred koji čuva vrijednost mape
     *
     * @param <K> kluč tip
     * @param <V> vrijednost tip
     */
    private static class KeyValuePair<K, V> {
        private final K key;
        private V value;

        public KeyValuePair(K key, V value) {
            this.key = Objects.requireNonNull(key);
            this.value = value;
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
    }
}
