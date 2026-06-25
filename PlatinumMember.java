/*
MINI PROJECT
SEMESTER II 2025/2026
SUBJECT CODE : SECJ2154
SUBJECT NAME : OBJECT ORIENTED PROGRAMMING
GROUP : FLYING SPUR
SECTION : 06
*/

public class PlatinumMember extends Passenger{
    public PlatinumMember(String userId, String name, String email, String passportNumber){
        super(userId, name, email, passportNumber);
    }

    @Override
    public String getMembershipLabel(){
        return "Platinum";
    }

    @Override
    public boolean canFlexChange(){
        return true;
    }

    @Override
    public int calculateLoyaltyPoints(double ticketPrice){
        return (int) Math.round(ticketPrice * 2.5);
    }

    @Override
    public boolean qualifiesForLounge(){
        return true;
    }

    @Override
    public String getLoungeEligibilityMessage(){
        return "Lounge Access: Granted (Platinum membership)";
    }

    @Override
    public void displayProfile(){
        super.displayProfile();
        System.out.println("Flex Changes: Unlimited");
        System.out.println("Points contribute to your loyalty record and exclusive offers.");
    }
}