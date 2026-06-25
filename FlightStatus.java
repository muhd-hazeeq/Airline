/*
MINI PROJECT
SEMESTER II 2025/2026
SUBJECT CODE : SECJ2154
SUBJECT NAME : OBJECT ORIENTED PROGRAMMING
GROUP : FLYING SPUR
SECTION : 06
*/

public enum FlightStatus{
    SCHEDULED(true, true, false),
    BOARDING(false, true, false),
    DEPARTED(false, false, false),
    ARRIVED(false, false, false),
    DELAYED(true, true, false),
    CANCELLED(false, false, true);

    private final boolean availableForBooking;
    private final boolean activeForPassenger;
    private final boolean releasesSeats;

    FlightStatus(boolean availableForBooking, boolean activeForPassenger, boolean releasesSeats){
        this.availableForBooking = availableForBooking;
        this.activeForPassenger = activeForPassenger;
        this.releasesSeats = releasesSeats;
    }

    public boolean isAvailableForBooking(){
        return availableForBooking;
    }

    public boolean isActiveForPassenger(){
        return activeForPassenger;
    }

    public boolean releasesSeats(){
        return releasesSeats;
    }
}