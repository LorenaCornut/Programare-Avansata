public class Student extends Persoana{
    private int an;
    private int nr_matricol;
    private Proiect proiect; //proiectul care i-a fost dat
    private Proiect[] preferinte;
    private int nrPref;

    /**
     * constructor pentru Student care primeste ca parametru nrmaxpref care reprezinta numarul maxim de elemente pe care le poate contine lista de preferinte a studentului
     * @param nume
     * @param nr_matricol
     * @param nrmaxpref
     */
    public Student(String nume,int nr_matricol, int nrmaxpref) {
        super(nume);
        this.nr_matricol = nr_matricol;
        this.preferinte = new Proiect[nrmaxpref];
        this.nrPref = 0; //initial nu are nimic in lista de pref, e vida
    }

    /**
     * Metoda care adauga un element de tip proiect la lista de preferinte a studentului
     * @param proiect
     */
    public void adaugaPreferinta(Proiect proiect) {
        if (nrPref < preferinte.length) {
            for (int i = 0; i < nrPref; i++) {
                if (preferinte[i] == proiect) { //daca proiectul exista deja in lista de pref nu il mai ad inca o data
                    return;
                }
            }
            preferinte[nrPref++] = proiect;
        }
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
