import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Greedy {                                                       //class Greedy
    static int [] sizes;                                                    //array that saves files folders
    boolean first = true;                                                   
    public static int [] read(String pathname) throws IOException{          //method that reads a file and returns an array     
        BufferedReader br = new BufferedReader(new FileReader(pathname));
        String line;
        int lines = 0;                                                      //lines of the file
        while ((line = br.readLine()) != null) {                            //counting lines
            lines++;
        }                 
        br.close();
        sizes = new int[lines];                                             //initialize array
        int i = 0;
        br = new BufferedReader(new FileReader(pathname));                  //read the same file again
        while ((line = br.readLine()) != null) {                            
            int intline = Integer.valueOf(line);                            //casting lines into int
            if (intline >= 0 && intline <= 1000000){                        //inserting values in the array if >=0 & <=1000000
                sizes[i] = intline;
            }else{
                System.out.println("The size is not correct");           //else exit program
                System.exit(0);
            } 
            i++;      
        }                                                   
        return sizes;                                                      
    }
   
    public static int Allocation(int[] sizes){                              //method that allocates folders to disks                
    int id = 0;
    Disk disk = new Disk(id);                                               //first disk                                 
    int i = 0;                                                              //variable for the array
    int sum = 0;                                                            //sum of all folders                                                   
    int folderscount = 0;                                                   //folder counter
    MaxPQ<Disk> disks = new MaxPQImpl<>(new DiskComparator());              //priority queue of Diks using DiskComparator
    disks.insert(disk);                                                     //adding first disk in the queue
    while (i < sizes.length){                                               //while we have folders
        sum += sizes[i];                                                    //increase size
        Disk max = disks.peek();                                            //find max disk(max is the disk with most free space)
        if(max.getFreeSpace() >= sizes[i]){                                 //if we have enough free space in max,adding in list the new folders,decreasing capacity and increasing folder counter
            max.folders.put(sizes[i]);
            max.capacity -= sizes[i];
            folderscount++;
        }else{                                                              //create a new disk,add it in the queue & do the same work
            id++;
            disk = new Disk(id);
            disks.insert(disk);
            disk.folders.put(sizes[i]);
            disk.capacity -= sizes[i];
            folderscount++;
        }
        max = disks.getMax();                                               //update max
        disks.insert(max);
        i++;
    }
    Disk maxs;
    System.out.println("Sum of all folders = " + sum + " TB");
    System.out.println("Total number of disks used = " + (id + 1));
    for (int k = 0; k <= id; k++){                                         //print all disks
        if(folderscount >= 0 && folderscount <= 100){                      //deceasing(getting everytime the max disk & print its id,freespace,folders)
            maxs = disks.getMax();
            System.out.print("id " + maxs.id + " " + maxs.getFreeSpace() + ": "  );
            maxs.folders.printQueue(System.out); 
             
            System.out.println(); 
        }
    } 
    System.out.println(); 
    return id + 1; 
    }

    public static void main(String[] args) throws IOException {
        int [] sizes = read(args[0]);
        Allocation(sizes);
    }
}
