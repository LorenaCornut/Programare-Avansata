public class Runway {
    private String id;

    /**
     * Constructoe pentru Runway
     * @param id
     */
    public Runway(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
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
