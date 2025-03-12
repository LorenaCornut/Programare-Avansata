/**
 * Clasa abstracta care implementeaza comparatia
 */
public abstract class Aircraft implements Comparable<Aircraft> {
    private String name;
    private String  tail_number;

    /**
     * Constructor pentru Aircraft
     * @param name
     * @param tail_number
     */
    public Aircraft(String name, String tail_number) {
        this.name = name;
        this.tail_number = tail_number;
    }

    /**
     * Gettere si settere pentru name si tail_number
     * @return
     */
    public String getName() {
        return name;
    }
    public String getTail_number() {
        return tail_number;
    }
    public void setTail_number(String tail_number) {
        this.tail_number = tail_number;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Suprascriem metoda toString pentru afisare
     * @return
     */
    @Override
    public String toString() {
        return name + " (" + tail_number + ")";
    }

    /**
     * Metoda de comparatie
     * @param other the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Aircraft other) {
        if (this.name == null) return (other.name == null) ? 0 : -1; ///daca ambele nume sunt null -> 0 (null=null) iar daca doar prima e null e clar ca e mai mica
        if (other.name == null) return 1; ///daca prima nu e null iar a doua e null -> prima e mai mare
        return this.name.compareTo(other.name); ///compar numele alfabetic
    }
}
