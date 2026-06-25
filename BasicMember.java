/*
MINI PROJECT
SEMESTER II 2025/2026
SUBJECT CODE : SECJ2154
SUBJECT NAME : OBJECT ORIENTED PROGRAMMING
GROUP : FLYING SPUR
SECTION : 06
*/

public class BasicMember extends Passenger{
    private static final int ELITE_LOUNGE_THRESHOLD = 10000;

    public BasicMember(String userId, String name, String email, String passportNumber){
        super(userId, name, email, passportNumber);
    }

    @Override
    public String getMembershipLabel(){
        return "Basic";
    }

    @Override
    public int calculateLoyaltyPoints(double ticketPrice){
        return (int) Math.round(ticketPrice);
    }

    @Override
    public boolean qualifiesForLounge(){
        return getTotalLoyaltyPoints() >= ELITE_LOUNGE_THRESHOLD;
    }

    @Override
    public String getLoungeEligibilityMessage(){
        if (qualifiesForLounge()){
            return "Lounge Access: Granted (Elite loyalty status)";
        }
        int remaining = ELITE_LOUNGE_THRESHOLD - getTotalLoyaltyPoints();
        return "Lounge Access: Not eligible - " + remaining + " more points needed for elite access";
    }
}