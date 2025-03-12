public class Drone extends Aircraft implements CargoCapable{
    private double batteryLife;
    private double maxPayload;

    /**
     * Constructor pentru Drone
     * @param name
     * @param tail_number
     * @param batteryLife
     * @param maxPayload
     */
    public Drone(String name, String tail_number, double batteryLife, double maxPayload) {
        super(name, tail_number); ///folosim constructorul din parinte
        this.batteryLife = batteryLife;
        this.maxPayload = maxPayload;
    }

    /**
     * Implementarea metodei din interfata CargoCapable
     * @return
     */
    @Override
    public double getMaximumPayload() {
        return maxPayload;
    }
    public void setMaximumPayload(double maximumPayload) {
        this.maxPayload = maximumPayload;
    }
    public double getBatteryLife() {
        return batteryLife;
    }
    public void setBatteryLife(double batteryLife) {
        this.batteryLife = batteryLife;
    }
}
