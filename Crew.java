/*
MINI PROJECT
SEMESTER II 2025/2026
SUBJECT CODE : SECJ2154
SUBJECT NAME : OBJECT ORIENTED PROGRAMMING
GROUP : FLYING SPUR
SECTION : 06
*/

public class Crew extends User{
    private String employeeId;
    private String rank;

    public Crew(String userId, String name, String email, String employeeId, String rank){
        super(userId, name, email);
        this.employeeId = employeeId;
        this.rank = rank;
    }

    public String getEmployeeId(){
        return employeeId;
    }

    public String getRank(){
        return rank;
    }

    @Override
    public void displayProfile(){
        System.out.println("Crew ID: " + getUserId());
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Rank: " + rank);
    }
}