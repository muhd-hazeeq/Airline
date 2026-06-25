/*
MINI PROJECT
SEMESTER II 2025/2026
SUBJECT CODE : SECJ2154
SUBJECT NAME : OBJECT ORIENTED PROGRAMMING
GROUP : FLYING SPUR
SECTION : 06
*/

public class Seat{
    private String seatNumber;
    private SeatType type;
    private boolean isBooked;

    public Seat(String seatNumber, SeatType type){
        this.seatNumber = seatNumber;
        this.type = type;
        isBooked = false;
    }

    public String getSeatNumber(){
        return seatNumber;
    }

    public SeatType getType(){
        return type;
    }

    public boolean isBooked(){
        return isBooked;
    }

    public boolean reserve(){
        if (isBooked){
            return false;
        }
        isBooked = true;
        return true;
    }

    public void release(){
        isBooked = false;
    }

    public void restoreBooked(boolean booked){
        isBooked = booked;
    }
}