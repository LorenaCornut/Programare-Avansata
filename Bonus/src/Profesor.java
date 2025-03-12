public class Profesor extends Persoana{
    private Proiect[] propuneri;

    /**
     * Constructor pentru profesor
     * @param nume
     * @param propuneri
     */
    public Profesor(String nume,Proiect[] propuneri) {
        super(nume);
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
     * Suprascriem metoda equals pentru a compara doi profesori
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
