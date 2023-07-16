import java.util.Comparator;

class MaxPQImpl<T> implements MaxPQ<T>{                            //Priority Queue Implementation
    private T[] heap;                                              //heap array
    int size;                                                      //size of the array
    private Comparator<T> comparator;                              //the comparator to use between the objects
    
    private static final int DEFAULT_CAPACITY = 10;                //default capacity
    private static final int AUTOGROW_SIZE = 10;                   //default auto grow

    public MaxPQImpl(Comparator<T> comparator) {                   //Constructor
        this.heap = (T[]) new Object[DEFAULT_CAPACITY + 1];
        this.size = 0;
        this.comparator = comparator;
    }

    @Override
    public void insert(T item) {                                   //method that inserts an item to the priority queue
        if (size == heap.length - 1) {                             //if it is full,increase its capacity using method grow
            grow();
        }
        heap[++size] = item;                                       //increase size & add the new item at the end of the heap                                                        //increase index
        swim(size);                                                //fix the heap property using method swim
    }

    @Override
    public T peek() {                                              //method that returns without removing the root,the biggest element
        if (size == 0)                                             // Ensure not empty
            return null;
            
        return heap[1];                                            //root is in the first cell of heap
    }

    @Override
    public T getMax() {                                             //method that deletes and returns the largest item
        if (size == 0)                                              //Ensure heap not empty
            return null;

        T root = heap[1];                                           //hold root item,biggest item
        heap[1] = heap[size];                                       //Replace root item with the one at rightmost leaf
        size--;                                                     //decrease size of heap
        sink(1);                                                 //fix the heap property using method sink
        return root;
    }

    private void swim(int i) {                                      
        if (i == 1)                                                 //if i is root (i==1) return
            return;

        int parent = i / 2;                                         //find parent
        
        while (i != 1 && comparator.compare(heap[i], heap[parent]) == 1) {   //compare parent with child i
            swap(i, parent);
            i = parent;
            parent = i / 2;
        }
    }

    private void sink(int i) {
        int left = 2 * i;                                           //determine left, right child
        int right = left + 1;

        if (left > size)                                            //if 2*i > size, node i is a leaf return
            return;

        while (left <= size) {                                      //Determine the largest child of node i
            int max = left;
            if (right <= size) {
                if (comparator.compare(heap[left], heap[right]) == -1)
                    max = right;
            }
            if (comparator.compare(heap[i], heap[max]) == 0 || comparator.compare(heap[i], heap[max]) == 1)           //If the heap condition holds, stop. Else swap and go on. //child smaller than parent
                return;
            else {
                swap(i, max);
                i = max;
                left = i * 2;
                right = left + 1;
            }
        }
    }

    private void grow() {                                           //method that increases the size of heap
       T[] newHeap = (T[]) new Object[heap.length + AUTOGROW_SIZE];
       for (int i = 0; i <= size; i++) {                            //copying elemenents of heap to a new larger array
           newHeap[i] = heap[i];
       }
        heap = newHeap;                                             //change name to heap again
    }

    private void swap(int i, int j) {                               //method that swaps two items of an array
    	T tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }
}
