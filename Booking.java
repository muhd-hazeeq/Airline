/*
MINI PROJECT
SEMESTER II 2025/2026
SUBJECT CODE : SECJ2154
SUBJECT NAME : OBJECT ORIENTED PROGRAMMING
GROUP : FLYING SPUR
SECTION : 06
*/

public class Booking{
    private String bookingId;
    private Passenger passenger;
    private Flight flight;
    private Seat seat;
    private double finalTicketPrice;
    private String restoreFailureReason;

    public Booking(String bookingId, Passenger passenger, Flight flight, Seat seat){
        this.bookingId = bookingId;
        this.passenger = passenger;
        this.flight = flight;
        this.seat = seat;
        finalTicketPrice = 0;
    }

    public String getBookingId(){
        return bookingId;
    }

    public Passenger getPassenger(){
        return passenger;
    }

    public Flight getFlight(){
        return flight;
    }

    public Seat getSeat(){
        return seat;
    }

    public double getFinalTicketPrice(){
        return finalTicketPrice;
    }

    public String getRestoreFailureReason(){
        return restoreFailureReason;
    }

    public double calculateFinalPrice(){
        finalTicketPrice = computePriceForSeat();
        return finalTicketPrice;
    }

    private double computePriceForSeat(){
        return seat.getType().applyToFare(flight.getBaseFare());
    }

    public void attachPersisted(double savedPrice){
        finalTicketPrice = savedPrice;
        passenger.addBooking(this);
    }

    public boolean confirmTransaction(){
        if (seat.isBooked()){
            System.out.println("Booking failed: seat is already booked.");
            return false;
        }

        boolean loungeBefore = passenger.qualifiesForLounge();
        double price = calculateFinalPrice();

        if (!seat.reserve()){
            System.out.println("Booking failed: seat could not be reserved.");
            return false;
        }

        int points = passenger.calculateLoyaltyPoints(price);
        passenger.accumulatePoints(points);
        passenger.addBooking(this);

        System.out.println("Booking confirmed. Earned " + points + " loyalty points.");
        notifyLoungeStatusChange(loungeBefore);
        return true;
    }

    public boolean restoreTransaction(double savedPrice){
        restoreFailureReason = null;

        if (seat.isBooked()){
            restoreFailureReason = "seat already booked";
            return false;
        }

        if (savedPrice < 0){
            restoreFailureReason = "negative price";
            return false;
        }

        if (Math.abs(savedPrice - computePriceForSeat()) > 0.01){
            restoreFailureReason = "saved price does not match expected price";
            return false;
        }

        boolean loungeBefore = passenger.qualifiesForLounge();
        finalTicketPrice = savedPrice;

        if (!seat.reserve()){
            finalTicketPrice = 0;
            restoreFailureReason = "seat could not be reserved";
            return false;
        }

        int points = passenger.calculateLoyaltyPoints(savedPrice);
        passenger.accumulatePoints(points);
        passenger.addBooking(this);
        notifyLoungeStatusChange(loungeBefore);
        return true;
    }

    public boolean applyFlightChange(Flight newFlight, Seat newSeat){
        if (finalTicketPrice <= 0){
            return false;
        }

        if (newFlight == null || newSeat == null || newSeat.isBooked()){
            return false;
        }

        boolean loungeBefore = passenger.qualifiesForLounge();
        int oldPoints = passenger.calculateLoyaltyPoints(finalTicketPrice);

        Flight previousFlight = flight;
        Seat previousSeat = seat;
        double previousPrice = finalTicketPrice;

        previousSeat.release();
        flight = newFlight;
        seat = newSeat;
        finalTicketPrice = 0;
        calculateFinalPrice();

        if (!seat.reserve()){
            seat = previousSeat;
            flight = previousFlight;
            finalTicketPrice = previousPrice;
            if (!previousSeat.reserve()){
                System.out.println("Flight change failed: could not restore previous seat reservation.");
            }
            return false;
        }

        passenger.accumulatePoints(-oldPoints);
        int newPoints = passenger.calculateLoyaltyPoints(finalTicketPrice);
        passenger.accumulatePoints(newPoints);

        int pointDifference = newPoints - oldPoints;
        if (pointDifference > 0){
            System.out.println("Flight change adjusted loyalty points by +" + pointDifference + ".");
        } else if (pointDifference < 0){
            System.out.println("Flight change adjusted loyalty points by " + pointDifference + ".");
        } else {
            System.out.println("Flight change did not affect loyalty points.");
        }

        notifyLoungeStatusChange(loungeBefore);
        return true;
    }

    private void notifyLoungeStatusChange(boolean loungeBefore){
        boolean loungeAfter = passenger.qualifiesForLounge();

        if (!loungeBefore && loungeAfter){
            System.out.println("Congratulations! Lounge access unlocked.");
            System.out.println(passenger.getLoungeEligibilityMessage());
        } else if (loungeBefore && !loungeAfter){
            System.out.println("Warning: Lounge access revoked due to loyalty point adjustment.");
            System.out.println(passenger.getLoungeEligibilityMessage());
        }
    }
}