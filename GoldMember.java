/*
MINI PROJECT
SEMESTER II 2025/2026
SUBJECT CODE : SECJ2154
SUBJECT NAME : OBJECT ORIENTED PROGRAMMING
GROUP : FLYING SPUR
SECTION : 06
*/

public class GoldMember extends Passenger{
    private static final int LOUNGE_THRESHOLD = 5000;

    private int maxFlexChanges;

    public GoldMember(String userId, String name, String email, String passportNumber, int maxFlexChanges){
        super(userId, name, email, passportNumber);
        this.maxFlexChanges = maxFlexChanges;
    }

    @Override
    public String getMembershipLabel(){
        return "Gold";
    }

    @Override
    public boolean canFlexChange(){
        return maxFlexChanges > 0;
    }

    @Override
    public int getMaxFlexChanges(){
        return maxFlexChanges;
    }

    @Override
    public void decrementFlexChanges(){
        if (maxFlexChanges > 0){
            maxFlexChanges--;
        }
    }

    @Override
    public void setMaxFlexChanges(int maxFlexChanges){
        this.maxFlexChanges = Math.max(0, maxFlexChanges);
    }

    @Override
    public int calculateLoyaltyPoints(double ticketPrice){
        return (int) Math.round(ticketPrice * 1.5);
    }

    @Override
    public boolean qualifiesForLounge(){
        return getTotalLoyaltyPoints() >= LOUNGE_THRESHOLD;
    }

    @Override
    public String getLoungeEligibilityMessage(){
        if (qualifiesForLounge()){
            return "Lounge Access: Granted (Gold membership status)";
        }
        int remaining = LOUNGE_THRESHOLD - getTotalLoyaltyPoints();
        return "Lounge Access: Not eligible - " + remaining + " more points needed";
    }

    @Override
    public void displayProfile(){
        super.displayProfile();
        System.out.println("Flex Changes Remaining: " + maxFlexChanges);
    }
}