/**
 * Represents a state in the river-crossing game, including family members' positions, lantern location, and cost metrics.
 * Provides methods for state initialization, heuristic estimation, and generating valid child states.
 */

import java.util.ArrayList;
import java.util.List;

public class State implements Comparable<State> {
    private List<FamilyMember> leftBank;
    private List<FamilyMember> rightBank;
    
    private boolean lanternOnRightBank;
    
    // Cost up to this state: g(n)
    private int cost;
    // Heuristic value of this state: h(n)
    private int heuristic;
    // Total cost of this state: f(n) = g(n) + h(n)
    private int totalCost;
    // Parent state
    private State father = null;
    
    // Default Constructor
    public State() {
        this.leftBank = new ArrayList<>();
        this.rightBank = new ArrayList<>();
        this.lanternOnRightBank = true;
        this.cost = 0;
        this.heuristic = 0;
        this.totalCost = 0;

    }
    
    // Overloaded Constructor
    public State(List<FamilyMember> leftBank, List<FamilyMember> rightBank, boolean lanternOnRightBank, int cost) {
        this.leftBank = leftBank;
        this.rightBank = rightBank;
        this.lanternOnRightBank = lanternOnRightBank;
        this.cost = cost;
        this.heuristic = calculateHeuristic2();
        this.totalCost = this.cost + this.heuristic;
    }
    
    // Copy Constructor
    public State(State currentState) {
        this.leftBank = new ArrayList<>(currentState.getLeftBank());
        this.rightBank = new ArrayList<>(currentState.getRightBank());
        this.lanternOnRightBank = currentState.isLanternOnRightBank();
        this.cost = currentState.getCost();
        this.heuristic = currentState.getHeuristic();
        this.totalCost = currentState.getTotalCost();
    }
    
    // Overloaded toString Function
    @Override
    public String toString() {
        return "State{\n" +
                "  leftBank=" + leftBank +
                ",  rightBank=" + rightBank +
                ",  lanternOnRightBank=" + lanternOnRightBank +
                ",  cost=" + cost +
                ",  heuristic=" + heuristic +
                ",  totalCost=" + totalCost +
                "\n}";
    }
    
    // Print Function
    void print() {
        
        System.out.println("Left Bank:\n[");
        for (FamilyMember famMem : this.leftBank) {System.out.println("  " + famMem + ",");}
        System.out.println("]");

        System.out.println("Right Bank:\n[");
        for (FamilyMember famMem : this.rightBank) {System.out.println("  " + famMem + ",");}
        System.out.println("]");
        
        System.out.println("Lantern on Right Bank: " + this.lanternOnRightBank);
        System.out.println("Cost: " + this.cost);
        System.out.println("Heuristic: " + this.heuristic);
        System.out.println("Total Cost: " + this.totalCost);
        System.out.println();
    }
    
    // Overloaded Hashcode Function
    @Override
    public int hashCode(){
    // Prime number * hashCode of leftBank + Another Prime number * hashCode of rightBank 
        return leftBank.hashCode() * 5 + rightBank.hashCode() * 31 + (lanternOnRightBank ? 1 : 0); 
    }
    
    // Overloaded equals Function
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        
        State otherState = (State) o;
        
        return leftBank.equals(otherState.getLeftBank()) && 
        rightBank.equals(otherState.getRightBank()) &&
        lanternOnRightBank == otherState.isLanternOnRightBank();
    }
    
    // Overloaded compareTo Function
    @Override
    public int compareTo(State otherState) {
        // Returns 0 if the two states are equal.
        // Returns 1 if this state is 'greater' than the other state.
        // Returns -1 if this state is 'less' than the other state.
        return Integer.compare(this.getTotalCost(), otherState.getTotalCost());
    }

    /**
        * Generates child states by simulating possible moves from the current state, considering individual and paired crossings. 
        * Returns a list of these child states, each associated with its parent state.
    */
    ArrayList<State> getChildren() {
        ArrayList<State> children = new ArrayList<>();
        
        // Get the bank that the lantern is on.
        List<FamilyMember> currentBank = new ArrayList<>();
        currentBank = isLanternOnRightBank() ? this.rightBank : this.leftBank; 

        // Iterate over all possible individuals on the current bank.
        for (int i = 0; i < currentBank.size(); i++) {
            // Create a copy of current state before each move
            State child = new State(this);
            // Check whether crossing can happen.
            boolean flag = child.crossRiver(currentBank.get(i), null);
            // Include valid child states and set their parent.
            if (flag) { 
                child.setFather(this); 
                children.add(child); 
            }
        }

        // Iterate over all possible pairs of family members on the current bank.
        for (int i = 0; i < currentBank.size(); i++) {
            for (int j = i + 1; j < currentBank.size(); j++) {
                // Create a copy of current state before each move
                State child = new State(this);
                // Check whether crossing can happen.
                boolean flag = child.crossRiver(currentBank.get(i), currentBank.get(j));
                // Include valid child states and set their parent.
                if (flag) { 
                    child.setFather(this); 
                    children.add(child); 
                }
            }
        }

        return children; 
    }

    // Crossing function
    public boolean crossRiver(FamilyMember fm1, FamilyMember fm2){
        // Create deep copies of the current left and right banks
        List<FamilyMember> newLeftBank = new ArrayList<>(this.leftBank);
        List<FamilyMember> newRightBank = new ArrayList<>(this.rightBank);
        
        int crossingCost = 0;
        // flag for pair or not
        boolean pair;     

        // No family member can cross.
        if( fm1 == null && fm2 == null ) { 
            // No crossing can happen
            return false;
        }
        // Only one family member can cross.
        else if (fm1 == null || fm2 == null) { 
            // Check which family member can cross.
            fm1 = (fm1 == null) ? fm2 : fm1; 
            crossingCost = fm1.getTime();
            pair = false;
        }
        // Both family members can cross.
        else { 
            // Check which family member is the slowest.
            crossingCost= Math.max(fm1.getTime(), fm2.getTime());
            pair = true;
        }
        
        // Calculate the new cost of the state.
        int newCost = this.getCost() + crossingCost;
        
        // Pair or not
        if (pair){
            if(isLanternOnRightBank()) {
                return this.pairToLeftCross(fm1, fm2, newLeftBank, newRightBank, newCost);
            } else {
                return this.pairToRightCross(fm1, fm2, newLeftBank, newRightBank, newCost);
            }
        } else{
            if(isLanternOnRightBank()) {
                return this.singleToLeftCross(fm1, newLeftBank, newRightBank, newCost);
            } else {
                return this.singleToRightCross(fm1, newLeftBank, newRightBank, newCost);
            }
        }
    } 

    // Crossing helper functions
    // Right -> Left for single family member
    private boolean singleToLeftCross(FamilyMember fm, List<FamilyMember> newLeftBank, List<FamilyMember> newRightBank, int newCost) {
        // Family member can cross
        if(newRightBank.contains(fm) == true) {
            // Remove family member from the right bank.
            newRightBank.remove(fm);
            // Add family member to the left bank.
            newLeftBank.add(fm);
            // Update the child state with the new banks, lantern position, and cost.
            this.updateState(newLeftBank, newRightBank, newCost);
            return true;
        } 
        return false;
    }

    // Right -> Left for pair of family members
    private boolean pairToLeftCross(FamilyMember fm1, FamilyMember fm2, List<FamilyMember> newLeftBank, List<FamilyMember> newRightBank, int newCost) {
        // Family members can cross
        if(newRightBank.contains(fm1) == true && newRightBank.contains(fm2) == true) {
            // Remove family members from the right bank.
            newRightBank.remove(fm1);
            newRightBank.remove(fm2);
            // Add family members to the left bank.
            newLeftBank.add(fm1);
            newLeftBank.add(fm2);
            // Update the child state with the new banks, lantern position, and cost.
            this.updateState(newLeftBank, newRightBank, newCost);
            return true;
        } 
        return false;
    }

    // Left -> Right for single family member
    private boolean singleToRightCross(FamilyMember fm, List<FamilyMember> newLeftBank, List<FamilyMember> newRightBank, int newCost) {
        // Family member can cross
        if(newLeftBank.contains(fm) == true) {
            // Remove family member from the left bank.
            newLeftBank.remove(fm);
            // Add family member to the right bank.
            newRightBank.add(fm);
            // Update the child state with the new banks, lantern position, and cost.
            this.updateState(newLeftBank, newRightBank, newCost);
            return true;
        }
        return false;
    }

    // Left -> Right for pair of family members
    private boolean pairToRightCross(FamilyMember fm1, FamilyMember fm2, List<FamilyMember> newLeftBank, List<FamilyMember> newRightBank, int newCost) {
        // Family members can cross
        if(newLeftBank.contains(fm1) == true && newLeftBank.contains(fm2) == true) {
            // Remove family members from the left bank.
            newLeftBank.remove(fm1);
            newLeftBank.remove(fm2);
            // Add family members to the right bank.
            newRightBank.add(fm1);
            newRightBank.add(fm2);
            // Update the child state with the new banks, lantern position, and cost.
            this.updateState(newLeftBank, newRightBank, newCost);
            return true;
        } 
        return false;
    }
    
    // Helper function to update the child state.
    private void updateState(List<FamilyMember> newLeftBank, List<FamilyMember> newRightBank, int newCost) {
            this.setLeftBank(newLeftBank);
            this.setRightBank(newRightBank);
            this.setLanternOnRightBank(!this.lanternOnRightBank); //toggle lantern position
            this.setCost(newCost);
            this.setHeuristic(this.calculateHeuristic2());
            this.setTotalCost(this.getCost() + this.getHeuristic());
    }
    
    // First Heuristic Function
    /* 
    This heuristic function calculates the maximum time taken by the remaining family members to cross the river (from right to left).
    It is an admissible heuristic because it always finds an underestimate of the cost.   
    Constraints that get Overlooked are:
    - The cost of the return trips to get the lantern back to the right bank.
    - The pairing of the slowest members to cross the river together to optimize the total crossing time.
    */
    private int calculateHeuristic() {
    // If there are no family members on the right bank, then the heuristic is 0.[ FINAL STATE ]
        if (isFinalState()) {
            return 0;
        }
        int maxTime = 0;
        int temp;
        for (FamilyMember fm : this.rightBank) {
            temp = fm.getTime();
            if (temp > maxTime) {
                maxTime = temp;
            }
        }
        heuristic = maxTime;
        return heuristic;
    }
    
    // Second Heuristic Function
    private int calculateHeuristic2(){
        int heuristic;
        
        // start state -> heuristic value = sum of all family members' times minus the time of the fastest family member times the number of pairs of family members
        if (this.isStartState()) {
            int minTime = Integer.MAX_VALUE;
            for (FamilyMember fm : this.rightBank) {
                int temp = fm.getTime();
                if (temp < minTime) {
                    minTime = temp;
                }
            }
            heuristic = Main.sumTime - ( minTime* Math.floorDiv(this.rightBank.size(), 2));
            return heuristic;
        }
        
        // final state -> heuristic value = 0
        if (this.isFinalState()) {
            heuristic = 0;
            return heuristic;
        }

        int maxTimeRight = 0;
        // Find the maximum time taken by the remaining family members to cross the river (from right to left).
        for (FamilyMember fm : this.rightBank) {
            int temp = fm.getTime();
            if (temp > maxTimeRight) {
               maxTimeRight = temp;
            }
        }
        
        // Calculate the heuristic as the maximum time multiplied by the number of pairs of family members.
        int numOfPairs = Math.floorDiv(this.rightBank.size(), 2);
        heuristic = numOfPairs * maxTimeRight;
        int numReturnTrips = Math.floorMod(this.rightBank.size(), 2);
        numReturnTrips = (this.rightBank.size()%2 == 1)  ? numReturnTrips + 1 : numReturnTrips; // +1 if odd number of Family Members
        heuristic = heuristic + numReturnTrips; 
        
        // Calculate the minimum time on the left bank only if the lantern is there.
        if (!this.lanternOnRightBank) {
            int minTimeLeft = Integer.MAX_VALUE;
            for (FamilyMember fm : this.leftBank) {
                int temp = fm.getTime();
                if (temp < minTimeLeft) {
                    minTimeLeft = temp;
                }
            }
            // In case there are no family members on the left bank, set minTimeLeft to 0
            minTimeLeft = (this.leftBank.isEmpty()) ? 0 : minTimeLeft;
            heuristic += minTimeLeft;
        }  
        return heuristic;
    }  
    
    // Check if every family member is on the left bank. Final/Goal State
    public boolean isFinalState() {
        return this.rightBank.size() == 0;
    }
    
    // Check if every family member is on the right bank. Start State
    public boolean isStartState() {
        return this.leftBank.size() == 0;
    }
    
    // Getters and Setters
    public List<FamilyMember> getLeftBank() {
        return leftBank;
    }

    public void setLeftBank(List<FamilyMember> leftBank) {
        this.leftBank = leftBank;
    }

    public List<FamilyMember> getRightBank() {
        return rightBank;
    }

    public void setRightBank(List<FamilyMember> rightBank) {
        this.rightBank = rightBank;
    }

    public boolean isLanternOnRightBank() {
        return lanternOnRightBank;
    }

    public void setLanternOnRightBank(boolean lanternOnRightBank) {
        this.lanternOnRightBank = lanternOnRightBank;
    }

    State getFather() {
        return this.father;
    }

    void setFather(State father) {
        this.father = father;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }
}
    
