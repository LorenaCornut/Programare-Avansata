public class Proiect {
    private String titlu;
    private tipuri tip;
    Profesor profesor;

    /**
     * Constructor pentru proiect
     * @param titlu
     * @param tip
     */
    public Proiect(String titlu, tipuri tip) {
        this.titlu = titlu;
        this.tip = tip;
    }
    public String getTitlu() {
        return titlu;
    }
    public void setTitlu(String titlu) {
        this.titlu = titlu;
    }
    public tipuri getTip() {
        return tip;
    }
    public void setTip(tipuri tip) {
        this.tip = tip;
    }

    /**
     * Suprascrierea metodei toString pentru afisare
     * @return
     */
    @Override
    public String toString() {
        return "Proiect{" +
                "titlu='" + titlu + '\'' +
                ", tip=" + tip +
                '}';
    }

    /**
     * Suprascrierea metodei equals pentru a verifica daca doua obiecte de tip Proiect sunt egale
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Proiect project = (Proiect) obj;
        return titlu.equals(project.titlu) && tip == project.tip;
    }
}