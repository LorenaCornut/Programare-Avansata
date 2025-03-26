public class Runway {
    private int id;

    /**
     * Constructoe pentru Runway
     * @param id
     */
    public Runway(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Suprascriem metoda toString pentru afisare
     * @return
     */
    @Override
    public String toString() {
        return "Runway " + id;
    }
}
