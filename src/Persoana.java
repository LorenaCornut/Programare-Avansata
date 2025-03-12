public class Persoana {
    private String nume;
    private String data_nastere;

    /**
     * Constructor pentru persoana
     * @param nume
     * @param data_nastere
     */
    public Persoana(String nume, String data_nastere) {
        this.nume = nume;
        this.data_nastere = data_nastere;
    }
    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
    public String getData_nastere() {
        return data_nastere;
    }
    public void setData_nastere(String data_nastere) {
        this.data_nastere = data_nastere;
    }

    /**
     * Suprascriem metoda equals pentru a verifica daca doua persoane sunt egale
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Persoana person = (Persoana) obj;
        return nume.equals(person.nume) && data_nastere.equals(person.data_nastere);
    }
}
