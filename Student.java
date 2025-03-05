public class Student {
    private String nume;
    private int an;
    private Proiect proiect; //proiectul care i-a fost dat
    private Proiect[] preferinte;
    public Student(String nume, int an, Proiect proiect, Proiect[] preferinte) {
        this.nume = nume;
        this.an = an;
        this.proiect = proiect;
        this.preferinte = preferinte;
    }
    public String getNume() {
        return nume;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
    public int getAn() {
        return an;
    }
    public void setAn(int an) {
        this.an = an;
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

    @Override
    public String toString() {
        return "Student{" +
                "nume='" + nume + '\'' +
                ", an=" + an +
                ", proiect=" + proiect +
                ", preferinte=" + java.util.Arrays.toString(preferinte) +
                '}';
    }
}
