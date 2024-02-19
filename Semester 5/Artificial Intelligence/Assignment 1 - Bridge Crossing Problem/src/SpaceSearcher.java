/**
 * SpaceSearcher is a utility class designed to perform A* search and path retrieval in a state space.
 * It provides methods for finding optimal solutions, managing explored states, and retrieving paths.
 */
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class SpaceSearcher {
    public static int timePassed = 0;
    private PriorityQueue<State> frontier;
    private HashSet<State> closedSet;

    // Constructor
    SpaceSearcher() {
        // Initialize the frontier to hold unexplored states, prioritized by their total cost.
        this.frontier = new PriorityQueue<>(Comparator.comparing(State::getTotalCost)); 
        // Initialize the closed set to store explored states.
        this.closedSet = new HashSet<>();
    }
    
    State AStarAlgorithm(State initialState) {
        // Step 0 -> if initial state final, return.
        if (initialState.isFinalState()) return initialState;

        // Step 1 -> put initial state in the frontier.
        this.frontier.add(initialState);
        
        // Step 2 -> check for empty frontier.
        while(this.frontier.size() > 0) { 
            //Step 3 ->  get the first node out of the frontier.
            State currentState = this.frontier.remove(); 
            // update time passed.
            timePassed = currentState.getCost();
            
            // if Total Search Time was exceeded stop searching
            if (timePassed > Main.totalTime){
                return null;            
            }
            
            // Step 4 -> if final state, return.
            if (currentState.isFinalState()) { 
                return currentState; 
            }

            // Step 5 -> add valid states to the frontier and only add new states to the closed set.
            if (!this.closedSet.contains(currentState)) { 
                this.closedSet.add(currentState); 
                this.frontier.addAll(currentState.getChildren()); 
            }
        }

        // Step 6 -> retrieve and return final state.
        State finalState = frontier.peek();
        return finalState;
    }

    static void retrievePath(State finalState) {
        // Time to find solution exceeded
        if (finalState ==null) {
            System.out.println("The maximum time for finding the solution has been exceeded !");
        }
        // Solution found in time
        else if (finalState.isFinalState()){
            System.out.println("Optimal solution found !");
            System.out.println("Path to the solution: ");

            // Retrieve path from final state to initial state reverse it and print it
            ArrayList<State> path = new ArrayList<>();
            for (State state = finalState; state!=null; state = state.getFather()) {
                path.add(state);
            }
            Collections.reverse(path);
            SpaceSearcher.printPath(path);
       }
    }
    
    private static void printPath(ArrayList<State> path) {
        int stateCount = 0;
        // Print each state in the path
        for (State state : path) {
            System.out.println("********************************************************"); 
            System.out.println("Remaining Time = " + (Main.totalTime - state.getCost()));
            System.out.println("State 0" + stateCount + ": ");
            state.print();
            stateCount++;
        }
        System.out.println("********************************************************");
    }
}
