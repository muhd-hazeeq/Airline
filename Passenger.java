/*
MINI PROJECT
SEMESTER II 2025/2026
SUBJECT CODE : SECJ2154
SUBJECT NAME : OBJECT ORIENTED PROGRAMMING
GROUP : FLYING SPUR
SECTION : 06
*/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Passenger extends User implements LoyaltyProgram{
    private String passportNumber;
    private int totalLoyaltyPoints;
    private ArrayList<Booking> bookingHistory;

    public Passenger(String userId, String name, String email, String passportNumber){
        super(userId, name, email);
        this.passportNumber = passportNumber;
        totalLoyaltyPoints = 0;
        bookingHistory = new ArrayList<>();
    }

    public String getPassportNumber(){
        return passportNumber;
    }

    public int getTotalLoyaltyPoints(){
        return totalLoyaltyPoints;
    }

    public Booking findBooking(String bookingId){
        if (bookingId == null){
            return null;
        }
        for (Booking booking : bookingHistory){
            if (bookingId.equals(booking.getBookingId())){
                return booking;
            }
        }
        return null;
    }

    public List<Booking> getBookingHistory(){
        return Collections.unmodifiableList(bookingHistory);
    }

    public void addBooking(Booking booking){
        bookingHistory.add(booking);
    }

    public void accumulatePoints(int points){
        totalLoyaltyPoints = Math.max(0, totalLoyaltyPoints + points);
    }

    public void restoreLoyaltyPoints(int points){
        totalLoyaltyPoints = Math.max(0, points);
    }

    public boolean canFlexChange(){
        return false;
    }

    public int getMaxFlexChanges(){
        return 0;
    }

    public void decrementFlexChanges(){
    }

    public void setMaxFlexChanges(int maxFlexChanges){
    }

    public abstract String getMembershipLabel();

    public abstract String getLoungeEligibilityMessage();

    public boolean hasActiveBooking(){
        for (Booking booking : bookingHistory){
            if (booking.getFlight().isActiveForPassenger()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void displayProfile(){
        System.out.println("Passenger ID: " + getUserId());
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Passport: " + passportNumber);
        System.out.println("Membership: " + getMembershipLabel());
        System.out.println("Loyalty Points: " + totalLoyaltyPoints);
        System.out.println("Total Bookings: " + bookingHistory.size());
        System.out.println(getLoungeEligibilityMessage());
    }

    @Override
    public abstract int calculateLoyaltyPoints(double ticketPrice);

    @Override
    public abstract boolean qualifiesForLounge();
}