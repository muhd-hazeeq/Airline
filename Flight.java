/*
MINI PROJECT
SEMESTER II 2025/2026
SUBJECT CODE : SECJ2154
SUBJECT NAME : OBJECT ORIENTED PROGRAMMING
GROUP : FLYING SPUR
SECTION : 06
*/

public class Flight{
    private static final int MAX_CREW = 10;

    private String flightNumber;
    private String destination;
    private double baseFare;
    private FlightStatus status;
    private Crew[] flightCrew;
    private int crewCount;
    private Seat[][] seats;

    public Flight(String flightNumber, String destination, double baseFare){
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.baseFare = baseFare;
        status = FlightStatus.SCHEDULED;
        flightCrew = new Crew[MAX_CREW];
        crewCount = 0;
    }

    public String getFlightNumber(){
        return flightNumber;
    }

    public String getDestination(){
        return destination;
    }

    public double getBaseFare(){
        return baseFare;
    }

    public FlightStatus getStatus(){
        return status;
    }

    public void setStatus(FlightStatus status){
        if (status != null){
            this.status = status;
            if (status.releasesSeats()){
                releaseAllSeats();
            }
        }
    }

    public void restoreStatus(FlightStatus status){
        if (status != null){
            this.status = status;
        }
    }

    public Crew[] getCrewMembers(){
        Crew[] members = new Crew[crewCount];
        for (int i = 0; i < crewCount; i++){
            members[i] = flightCrew[i];
        }
        return members;
    }

    public Seat[][] getSeats(){
        return seats;
    }

    public boolean isAvailableForBooking(){
        return status.isAvailableForBooking();
    }

    public boolean isActiveForPassenger(){
        return status.isActiveForPassenger();
    }

    private void releaseAllSeats(){
        if (seats == null){
            return;
        }

        for (int row = 0; row < seats.length; row++){
            for (int col = 0; col < seats[row].length; col++){
                Seat seat = seats[row][col];
                if (seat != null){
                    seat.release();
                }
            }
        }
    }

    public void initializeSeats(){
        seats = new Seat[15][6];
        String[] columns = {"A", "B", "C", "D", "E", "F"};

        for (int row = 0; row < seats.length; row++){
            for (int col = 0; col < seats[row].length; col++){
                int rowNumber = row + 1;
                String seatNumber = rowNumber + columns[col];
                seats[row][col] = new Seat(seatNumber, SeatType.forRow(rowNumber));
            }
        }
    }

    public boolean assignCrew(Crew crewMember){
        if (crewMember == null){
            System.out.println("Failed to assign crew: crew member is null.");
            return false;
        }

        if (crewCount >= flightCrew.length){
            System.out.println("Failed to assign crew: flight crew is at full capacity.");
            return false;
        }

        for (int i = 0; i < crewCount; i++){
            if (flightCrew[i] != null && flightCrew[i].getEmployeeId().equals(crewMember.getEmployeeId())){
                System.out.println("Failed to assign crew: crew member already assigned to this flight.");
                return false;
            }
        }

        flightCrew[crewCount] = crewMember;
        crewCount++;
        return true;
    }

    public Seat findSeat(String seatNumber){
        if (seats == null || seatNumber == null){
            return null;
        }

        for (int row = 0; row < seats.length; row++){
            for (int col = 0; col < seats[row].length; col++){
                Seat seat = seats[row][col];
                if (seat != null && seat.getSeatNumber().equalsIgnoreCase(seatNumber)){
                    return seat;
                }
            }
        }

        return null;
    }
}