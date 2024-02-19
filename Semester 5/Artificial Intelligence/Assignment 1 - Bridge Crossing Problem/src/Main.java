/**
 * The `Main` class serves as the entry point for solving the river crossing game. It reads input from a file,
 * defines the problem, performs A* search to find the optimal solution, and displays the results, including the path taken.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static int totalTime = 0;
    public static int countOfFamilyMembers = 0;
    public static int sumTime = 0;
    public static List<FamilyMember> leftBank = new ArrayList<>();
    public static List<FamilyMember> rightBank = new ArrayList<>();

    public static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();

        try (FileReader fileReader = new FileReader(filePath);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }

    public static void main(String[] args) {
     
        if (args.length == 0) {
            System.out.println("Please provide a file name as an argument!");
            System.exit(0);
        }
        
        //FILE MUST HAVE THE SAME FORMAT AS THE EXAMPLE FILE
        String filePath = "..\\tests\\" + args[0]; 
        // when run in src folder through the terminal -> go to root and then go to tests folder to find the input file.
        String content = null;

        // Get file content
        try {
            content = readFile(filePath);
        } catch (IOException e) {
            System.out.println("File not found!");
            System.exit(0);
        }

        boolean foundTotalTime = false;
        String[] lines = content.split("\n");
        int lineCount = 1;

        for (String line : lines) {
            // Skip comments and empty lines
            if (line.trim().startsWith("#")) continue;
            if (line.trim().isEmpty()) continue;

            // Stop when reaching "END"
            if (line.trim().equals("END")) break;

            // First input line is the total time
            if (!foundTotalTime) {
                totalTime = UserInputValidator.validateInputInt(line.split(" ")[1], "Total time");
                foundTotalTime = true;
                continue;
            }

            // All other lines are family members
            String name = UserInputValidator.validateInputString(line.split(" ")[0], "Name of family member " + lineCount);
            int time = UserInputValidator.validateInputInt(line.split(" ")[1], "Time needed by family member " + lineCount + " to cross the river");

            // Add family member to left bank
            rightBank.add(new FamilyMember(name, time));

            sumTime += time;
            lineCount++;
        }

        // Create initial state
        State initialState = new State(leftBank, rightBank, true, 0);

        // Find solution
        SpaceSearcher searcher = new SpaceSearcher();
        long start = System.currentTimeMillis();
        State finalState = searcher.AStarAlgorithm(initialState);
        long end = System.currentTimeMillis();
        SpaceSearcher.retrievePath(finalState);
        // Total time of searching in seconds.
        System.out.println("Search time: " + (double)(end - start) / 1000 + " sec"); 
    }
}