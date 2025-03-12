public interface PassengerCapable {
    int getSeatCount();
    default boolean hasBusinessClassSeats() {
        return true;
    }
}
