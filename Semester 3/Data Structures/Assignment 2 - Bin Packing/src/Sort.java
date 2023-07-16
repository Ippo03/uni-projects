
public class Sort{                                                                  //class Sort
    public static void mergeSort(int[] array) {                                     //method that sorts an array 
        if (array.length <= 1) {                                                    //if we have 1 item,it is sorted
            return;
        }
        int mid = array.length / 2;                                                 //split the array in two
        int[] left = new int[mid];
        int[] right = new int[array.length - mid];
        System.arraycopy(array, 0, left, 0, mid);                   //copy elements from to left to array
        System.arraycopy(array, mid, right, 0, array.length - mid);         //copy elements from to left to array
        mergeSort(left);                                                            //merge using recursion the left side
        mergeSort(right);                                                           //merge using recursion the right side
        merge(array, left, right);                                                  //merge the sorted halves
    }

    private static void merge(int[] array, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;                                        //indexes for array,left and right
        while (i < left.length && j < right.length) {                   //put the smaller item of left and right array in array
            if (left[i] > right[j]) {
                array[k] = left[i];
                i++;
            } else {
                array[k] = right[j];
                j++;
            }
            k++;
        }
        while (i < left.length) {                                       //add the other items of left in the array
            array[k] = left[i];
            i++;
            k++;
        }
        while (j < right.length) {                                      //add the other items of right in the array
            array[k] = right[j];
            j++;
            k++;
        }
    }
}
