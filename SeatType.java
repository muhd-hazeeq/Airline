/*
MINI PROJECT
SEMESTER II 2025/2026
SUBJECT CODE : SECJ2154
SUBJECT NAME : OBJECT ORIENTED PROGRAMMING
GROUP : FLYING SPUR
SECTION : 06
*/

public enum SeatType{
    ECONOMY(1.0),
    BUSINESS(2.0),
    FIRST_CLASS(3.0);

    private final double priceMultiplier;

    SeatType(double priceMultiplier){
        this.priceMultiplier = priceMultiplier;
    }

    public double applyToFare(double baseFare){
        return baseFare * priceMultiplier;
    }

    public static SeatType forRow(int rowNumber){
        if (rowNumber <= 2){
            return FIRST_CLASS;
        }
        if (rowNumber <= 5){
            return BUSINESS;
        }
        return ECONOMY;
    }
}