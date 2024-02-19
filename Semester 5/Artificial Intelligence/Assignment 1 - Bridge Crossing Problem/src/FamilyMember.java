/**
 * Represents a family member with a name and associated time. 
 */
public class FamilyMember {
    private String name;
    private int time;

    // Constructor
    FamilyMember(String name, int time) {
        this.name = name;
        this.time = time;
    }

    // Getters
    public String getName() {
        return this.name;
    }
    
    public int getTime() {
        return this.time;
    }

    @Override
    public String toString(){
        return "Name: " + this.getName() + " - Time: " + this.getTime();
    }
}


