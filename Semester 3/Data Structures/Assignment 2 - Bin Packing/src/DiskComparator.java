import java.util.Comparator;

public class DiskComparator implements Comparator<Disk> {        //This class is used to compare Disk based on available capacity
    @Override
    public int compare(Disk t1, Disk t2) {
        return t1.compareTo(t2);
    }
}
