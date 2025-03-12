public class Student extends Persoana{
    private int an;
    private int nr_matricol;
    private Proiect proiect; //proiectul care i-a fost dat
    private Proiect[] preferinte;

    /**
     * Constructor pentru student
     * @param nume
     * @param data_nastere
     * @param nr_matricol
     * @param an
     * @param preferinte
     */
    public Student(String nume,String data_nastere,int nr_matricol, int an, Proiect[] preferinte) {
        super(nume, data_nastere);
        this.an = an;
        this.nr_matricol = nr_matricol;
        this.preferinte = preferinte;
    }
    public int getAn() {
        return an;
    }
    public void setAn(int an) {
        this.an = an;
    }
    public int getNr_matricol() {
        return nr_matricol;
    }
    public void setNr_matricol(int nr_matricol) {
        this.nr_matricol = nr_matricol;
    }
    public Proiect getProiect() {
        return proiect;
    }
    public void setProiect(Proiect proiect) {
        this.proiect = proiect;
    }
    public Proiect[] getPreferinte() {
        return preferinte;
    }
    public void setPreferinte(Proiect[] preferinte) {
        this.preferinte = preferinte;
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
                ",nr_matricol=" + nr_matricol +
                ", an=" + an +
                ", proiect=" + proiect +
                ", preferinte=" + java.util.Arrays.toString(preferinte) +
                '}';
    }

    /**
     * Suprascriem metoda equals pentru a verifica daca doi studenti sunt egali
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false; //folosesc fct equals din clasa Persoana
        Student student = (Student) obj;
        return an == student.an && nr_matricol == student.nr_matricol;
    }
}
