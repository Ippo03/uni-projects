//singly linked list from 1st assignment
//help us with seperate chaining->every bucket has a singly linked list
public class SinglyLinkedList <E> { 

    protected static class Node <E> {
        protected E element;        
        protected Node <E> next;    
        
        public Node (E e, Node<E> n) { 
            element = e; 
            next = n;    
        }                
        
        public E getElement() {return element;}    
        public Node <E> getNext() {return next;}   
        public void setNext(Node<E> n) {next = n;} 
    }   
   
    protected Node<E> head = null; 
    protected Node<E> tail = null; 
    protected int size = 0; 
    
    public SinglyLinkedList() { 
        
    }

    public int size() {return size;} 
    
    public boolean isEmpty() {return size == 0;} 
    
    public void addFirst(E e) { 
        head = new Node<E>(e, head); 
        if (isEmpty()) {
            tail = head;}           
    }
}