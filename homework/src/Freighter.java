public class Freighter extends Aircraft implements CargoCapable{
    private double maxPayload;

    /**
     * Constructor pentru Freighter
     * @param name
     * @param tail_number
     * @param maxPayload
     */
    public Freighter(String name, String tail_number, double maxPayload) {
        super(name, tail_number); ///folosim contructorul din clasa parinte
        this.maxPayload = maxPayload;
    }

    /**
     * Implementam metoda din interfata CargoCapable
     * @return
     */
    @Override
    public double getMaximumPayload() {
        return maxPayload;
    }
}
