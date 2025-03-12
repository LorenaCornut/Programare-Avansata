public class Airliner extends Aircraft implements PassengerCapable, CargoCapable{
    private int seatCount;
    private double maximumPayload;

    /**
     * Constructor pentru Airliner
     * @param name
     * @param tail_number
     * @param seatCount
     * @param maximumPayload
     */
    public Airliner(String name, String tail_number,int seatCount, double maximumPayload) {
        super(name, tail_number); ///folosim constructorul clasei parinte
        this.seatCount = seatCount;
        this.maximumPayload = maximumPayload;
    }

    /**
     * Implementarea metodei din interfata PassengerCapable
     * @return
     */
    @Override
    public int getSeatCount() {
        return seatCount;
    }
    public void setSeatCount(int seatCount) {
        this.seatCount = seatCount;
    }

    /**
     * Implementarea metodei din interfata CargoCapable
     * @return
     */
    @Override
    public double getMaximumPayload() {
        return maximumPayload;
    }
    public void setMaximumPayload(double maximumPayload) {
        this.maximumPayload = maximumPayload;
    }
}
