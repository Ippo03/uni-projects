/*
	Name: IPPOKRATIS PANTELIDIS
	Student Number:p3210150
*/
import java.util.Arrays;

public class App2 {
    public static String IntToWord(int num){
        if(num == 0){return "A";}
        if(num == 1){return "B";}
        return "C";
    }
    public static void main(String[] args) {
        final int NUM_REGIONS = 5;
        final int NUM_CANDIDATES = 3;

        // Initialize the votes array
        int[][] votes = {
                {182, 41, 202},
                {145, 85, 325},
                {195, 15, 115},
                {110, 24, 407},
                {255, 11, 357}
        };

        // Print the votes array
        System.out.println("_________________________________________________________");
        System.out.println("| Constituency | Candidate A | Candidate B | Candidate C |");
        for (int i = 0; i < NUM_REGIONS; i++) {
            System.out.println("_________________________________________________________");
            System.out.print("|      " + i + "       |");
            for (int j = 0; j < NUM_CANDIDATES; j++) {
                if(votes[i][j] < 100){
                    System.out.print("     " + votes[i][j] + "      |");
                }else{
                    System.out.print("     " + votes[i][j] + "     |");
                }
            }
            System.out.println();
        }
        System.out.println("_________________________________________________________");
        System.out.println();

        // Calculate and print the total votes for each candidate
        int[] totalVotes = new int[NUM_CANDIDATES];
        int total = 0;
        for (int i = 0; i < NUM_REGIONS; i++) {
            for (int j = 0; j < NUM_CANDIDATES; j++) {
                totalVotes[j] += votes[i][j];
                total += votes[i][j];
            }
        }
        System.out.println("Total votes per candidate:");
        for (int j = 0; j < NUM_CANDIDATES; j++) {
            System.out.println("Candidate " + IntToWord(j)  + ": " + totalVotes[j]);
        }
        System.out.println();

        // Calculate and print the percentage of votes for each candidate
        System.out.println("Candidate's percentage");
        double[] percentages = new double[NUM_CANDIDATES];
        for (int j = 0; j < NUM_CANDIDATES; j++) {
            percentages[j] = totalVotes[j] / (double)total * 100;
			System.out.println("Candidate " + IntToWord(j) + ": " + String.format("%.2f", percentages[j]) + "%");
        }
        System.out.println();

        // Check for a winner
        boolean winner = false;
        for (int j = 0; j < NUM_CANDIDATES; j++) {
            if (percentages[j] > 50.0) {
                System.out.println("Winner: Candidate " + IntToWord(j));
                winner = true;
            }
        }
        if (!winner) {
            Arrays.sort(percentages);
            int max = 2;
            if(percentages[3] > percentages[max]){ max = 3;}
            System.out.println("Winner: Candidate " + IntToWord(max));
        }
    }
}

