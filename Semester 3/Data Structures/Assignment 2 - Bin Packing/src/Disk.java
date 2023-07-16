public class Disk implements Comparable<Disk>{                      //class Disk
    int id;                                                         //unique id
    int capacity;                                                   //capacity

    Disk(int id) {                                                  //Constructor
        this.id = id;           
        capacity = 1000000;
    }                                                       
    StringQueueImpl<Integer> folders = new StringQueueImpl<>();     //list of folders using list from project 1

    public int getFreeSpace(){                                      //method thats returns the free space of a disk
        return capacity;
    }

    @Override
    public int compareTo(Disk disk) {                               //method that compares the capacity of two disks
        if(this.capacity > disk.capacity){
            return 1;
        }else if (this.capacity < disk.capacity){
            return -1;
        }
        return 0;
    }
}
