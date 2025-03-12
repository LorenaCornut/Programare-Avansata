public class Profesor extends Persoana{
    private Proiect[] propuneri;

    /**
     * Constructor pentru profesor
     * @param nume
     * @param data_nastere
     * @param propuneri
     */
    public Profesor(String nume, String data_nastere,Proiect[] propuneri) {
        super(nume, data_nastere);
        this.propuneri = propuneri;
    }
    public Proiect[] getPropuneri() {
        return propuneri;
    }
    public void setPropuneri(Proiect[] propuneri) {
        this.propuneri = propuneri;
    }

    /**
     * Suprascriem metoda toString pentru afisare
     * @return
     */
    @Override
    public String toString() {
        return "Student{" +
                "nume='" + getNume() + '\'' +
                ",data_nastere='" + getData_nastere() + '\'' +
                ", preferinte=" + java.util.Arrays.toString(propuneri) +
                '}';
    }

    /**
     * Suprascriem metoda equals pentru a verifica daca doi studenti sunt egali
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        Profesor profesor = (Profesor) obj;
        return java.util.Arrays.equals(propuneri, profesor.propuneri);
    }
}
