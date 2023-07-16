import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class EvaluateAlgorithms{

    private static void writeToFile(String fileName, String content) {                  //method that writes in a file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(content);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    
    public static void RandomIn(){                                                      //method that creates and initializes files with random folders
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {                                                  //create 10 files for both algorithms with random folders and N = 100
            StringBuilder sb1 = new StringBuilder();                                    //first algorithm
            for (int j = 0; j < 100; j++) {
                sb1.append(rand.nextInt(1000000)).append(System.lineSeparator());
            }
            writeToFile("data/N_100_" + i + ".txt", sb1.toString());
        }

        for (int i = 0; i < 10; i++) {                                                  //create 10 files for both algorithms with random folders and N = 500
            StringBuilder sb1 = new StringBuilder();                                    //first algorithm
            for (int j = 0; j < 500; j++) {
                sb1.append(rand.nextInt(1000000)).append(System.lineSeparator());
            }
            writeToFile("data/N_500_" + i + ".txt", sb1.toString());
        }

        for (int i = 0; i < 10; i++) {                                                  //create 10 files for both algorithms with random folders and N = 1000
            StringBuilder sb1 = new StringBuilder();                                    //first algorithm
            for (int j = 0; j < 1000; j++) {
                sb1.append(rand.nextInt(1000000)).append(System.lineSeparator());
            }
            writeToFile("data/N_1000_" + i + ".txt", sb1.toString());
        }
    }
    
    public static void main(String[] args) throws IOException {
        RandomIn();
        int N1_100 = 0;                                             //disks for algorithm 1 and N=100
        int N2_100 = 0;                                             //disks for algorithm 2 and N=100
        int N1_500 = 0;                                             //disks for algorithm 1 and N=500
        int N2_500 = 0;                                             //disks for algorithm 2 and N=500
        int N1_1000 = 0;                                            //disks for algorithm 1 and N=1000
        int N2_1000 = 0;                                            //disks for algorithm 2 and N=1000
        for (int i = 0; i < 10; i++) {                              //reading and running algorithms
            int [] size1 = Greedy.read("data/N_100_" + i + ".txt");     
            N1_100 += Greedy.Allocation(size1);
            Sort.mergeSort(size1);
            N2_100 += Greedy.Allocation(size1);

            int [] size11 = Greedy.read("data/N_500_" + i + ".txt");
            N1_500 += Greedy.Allocation(size11);
            Sort.mergeSort(size11);
            N2_500 += Greedy.Allocation(size11);

            int [] size12 = Greedy.read("data/N_1000_" + i + ".txt");
            N1_1000 += Greedy.Allocation(size12);
            Sort.mergeSort(size12);
            N2_1000 += Greedy.Allocation(size12);
        }
        int count1 = 0;
        int count2 = 0;                                 
        float Average_N1_100 = N1_100 / 10;                               //finding and printing average for each N
        float Average_N2_100 = N2_100 / 10;
        float Average_N1_500 = N1_500 / 10;
        float Average_N2_500 = N2_500 / 10;
        float Average_N1_1000 = N1_1000 / 10;
        float Average_N2_1000 = N2_1000 / 10;
        System.out.println("for N=100:");
        System.out.println("algorithm 1: " + Average_N1_100);
        System.out.println("algorithm 2: " + Average_N2_100);
        System.out.println();
        System.out.println("for N=500:");
        System.out.println("algorithm 1: " + Average_N1_500);
        System.out.println("algorithm 2: " + Average_N2_500);
        System.out.println();
        System.out.println("for N=1000:");
        System.out.println("algorithm 1: " + Average_N1_1000);
        System.out.println("algorithm 2: " + Average_N2_1000);
        System.out.println();
        
        if(Average_N1_100 < Average_N2_100){                            //compare averages and finds which algorithm is more efficient
            System.out.println("For N=100 1 is more efficient");
            count1++;
        }else if(Average_N1_100 > Average_N2_100){
            System.out.println("For N=100 2 is more efficient");
            count2++;
        }
        if(Average_N1_500 < Average_N2_500){
            System.out.println("For N=500 1 is more efficient");
            count1++;
        }else if(Average_N1_500 > Average_N2_500){
            System.out.println("For N=500 2 is more efficient");
            count2++;
        }
        if(Average_N1_1000 < Average_N2_1000){
            System.out.println("For N=1000 1 is more efficient");
            count1++;
        }else if(Average_N1_1000 > Average_N2_1000){
            System.out.println("For N=1000 2 is more efficient");
            count2++;
        }
        if (count1 > count2){
            System.out.println("1 is more efficient in general");
        }else{
            System.out.println("2 is more efficient in general");
        }
    }
}