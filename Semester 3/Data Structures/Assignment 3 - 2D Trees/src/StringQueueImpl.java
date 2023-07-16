import java.io.PrintStream;
import java.util.NoSuchElementException; 

public class StringQueueImpl<T> implements StringQueue<T> {          //class Queue implements methods of interface stack(we use generics)

    public class myNode<T> {                                         //class node,has two parts,a value and a pointer 
        T value;                                                     //value variable                                 
        myNode<T> nextNode;                                             //pointer

        public myNode(T v) {                                         //Constructor for myNode
            this.value = v;
            nextNode = null;
        }
    }
    myNode<T> head;                                                  //pointer that points at the start of the queue
    myNode<T> tail;                                                  //pointer that points at the end of the queue
    int size;                                                        //int variable that counts the size of the queue
    
    public StringQueueImpl(){                                        //Constructor for StrinQueueImpl
        head = null;
        tail = null;
    }

    @Override
    public boolean isEmpty() {                                       //method that checks if the queue is empty
        return head == null;                                         //if empty then head points to null
    }   

    @Override
    public void put(T item) {                                       //method that puts an item at the end of the queue
        myNode<T> node = new myNode<>(item);                        //creating a new node with value = item
        if (isEmpty()) {                                            //if is empty we make head and tail point at the new node(1 node in the queue) 
            head = tail = node;                   
        }else{                                                      //if is not empty
            tail.nextNode = node;                                   //make pointer next of tail point at the new node
            tail = node;                                            //make tail point to the new node
        }
        size++;                                                     //increasing  size
    }

    @Override
    public T get() throws NoSuchElementException {                  //method that removes the node at the start of the queue       
        if (isEmpty()){                                             //throws Exception if the queue is empty
            throw new NoSuchElementException();
        }
        T removedNode = head.value;                                 //taking the value of the node we remove
        head = head.nextNode;                                       //we move pointer head to the next node
        if(head == null){                                           //if the queue is empty we make tail to point at null            
            tail = null;                                
        }
        System.out.println("Removing value (" + removedNode + ") from queue");
        size--;                                                     //decreasing size
        return removedNode;
    }

    @Override
    public T peek() throws NoSuchElementException {                 //method that returns the value of the first node of the queue
        if(!isEmpty()){                                             //if the queue is not empty returns the value of pointer head
            System.out.println("The first item is: " + head.value);
            return head.value;
        }else{                                                      //throws Exception if the queue is empty
            throw new NoSuchElementException(); 
        }
    }

    @Override
    public void printQueue(PrintStream stream) {                    //method tha prints the elements of the queue from the first to the lats node of the queue
        myNode<T> temp = head;                                      //holding head in a variable
        while (head!= tail){                                        //while we have more than one node in the queue
            stream.print(head.value + " ");
            if(head.nextNode != null)
                head = head.nextNode;                                   //move head to the next node 
        }
        if(tail != null)
            stream.print(tail.value);                                //prints the value of the last node of the queue 
        head = temp;                                               //bringing back head back at the start of the queue
    }

    @Override
    public int size() {                                             //method that counts the size of the queue
        if (isEmpty()){                                             //if the queue is empty returns 0 else returns the counter size
            System.out.println("The does not have any items");
            return 0;
        }
        System.out.println("The size of the queue is: " + size);
        return size;
    }
}

    

