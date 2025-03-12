public class Proiect {
    private String titlu;
    private tipuri tip;
    Profesor profesor;

    /**
     * Constructor pentru Proiect
     * @param titlu
     * @param tip
     * @param profesor
     */
    public Proiect(String titlu, tipuri tip, Profesor profesor) {
        this.titlu = titlu;
        this.tip = tip;
        this.profesor = profesor;
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
    public Profesor getProfesor() {
        return profesor;
    }
    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    /**
     * Suprascriem metoda toString pentru afisare
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
     * Suprascriem metoda equal pentru a verifica daca doua proiecte sunt egale
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