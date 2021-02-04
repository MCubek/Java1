package hr.fer.oprpp1.custom.collections;

/**
 * Klasa predstavlja općenitu kolekciju objekata.
 *
 * @author matej
 */
public class Collection {

    /**
     * Defaultni konstruktor
     */
    protected Collection() {
    }

    /**
     * Metoda koja provjerava je li kolekcija prazna.
     *
     * @return <code>true</code> ako je prazna, <code>false</code> ako nije
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Metoda koja izračunava veličinu kolekcije.
     *
     * @return velićina kolekcije kao <code>int</code>
     */
    public int size() {
        return 0;
    }

    /**
     * Metoda koja dodaje element u kolekciju.
     *
     * @param value element koji se nadodaje u kolekciju
     */
    public void add(Object value) {
        ;
    }

    /**
     * Metoda koja provjerava je li element u kolekciji i vraća <code>true</code> ako je ili <code>false</code> ako nije.
     *
     * @param value objekt nad kojim se provjerava je li u kolekciji
     * @return <code>true</code> ako je element u kolekciji ili <code>false</code> ako nije
     * @throws NullPointerException ako je value <code>null</code>
     */
    public boolean contains(Object value) {
        if(value==null) throw new NullPointerException();
        //if(this.equals(value)) return true;
        return false;
    }

    /**
     * Metoda koja iz kolekcija izbacuje jednu pojavu tog objekta i vraća je li izbačen.
     *
     * @param value objekt koji se iz kolekcije izbavuje van
     * @return <code>true</code> ako je element bio u kolekciji i ako je izbačen ili <code>false</code> ako nije
     */
    public boolean remove(Object value) {
        //if(!this.equals(value)) return false;
        return false;
    }

    /**
     * Metoda koja stvori novi array veličine kolekcije, napuni ga s svim elementima kolekcije i vrati ga.
     * Metoda nikada neće vratiti null.
     *
     * @return array veličine broja elemenata kolekcije s svim njenim elementima
     */
    public Object[] toArray() {
        throw new UnsupportedOperationException("Operation not supported for this object");
    }

    /**
     * Metoda koja za svaki element u kolekciji poziva <code>Processor.process()</code>
     * Redoslijed kojim se elementi šalju nije definiran.
     *
     * @param processor processor kojem se šalje svaki element u kolekciji
     */
    public void forEach(Processor processor) {
        ;
    }

    /**
     * Metoda koja će u kolekciju dodati sve elemente iz predane kolekcije.
     * Predana kolekcija ostati će nepromijenjena
     * @param other druga kolekcija čiji će se elementi dodati
     */
    public void addAll(Collection other) {
         class MyProcessor extends Processor{
             /**
              * Metoda dodaje element u trenutnu kolekciju
              * @param value objekt koji se dodaju u trenutnu kolekciju
              */
             @Override
             public void process(Object value) {
                 add(value);
             }
         }

         Processor myProcessor = new MyProcessor();
         other.forEach(myProcessor);
    }

    /**
     * Metoda koja briše sve elemente iz kolekcije
     */
    public void clear(){
        ;
    }
}
