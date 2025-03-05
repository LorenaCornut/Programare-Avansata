public class Proiect {
    private String titlu;
    private tipuri tip;
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

    @Override
    public String toString() {
        return "Proiect{" +
                "titlu='" + titlu + '\'' +
                ", tip=" + tip +
                '}';
    }
}