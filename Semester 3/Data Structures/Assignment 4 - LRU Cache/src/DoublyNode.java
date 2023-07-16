public class DoublyNode<K, V> {
    //value variable  
    HashMapEntry<K, V> data;  
    //pointers                                                                                  
    DoublyNode<K, V> next;
    DoublyNode<K, V> prev;    
                                             
    //Constructor 
    public DoublyNode(HashMapEntry<K, V> entry) {                                         
        this.data = entry;
        next = null;
        prev = null;
    }

    public HashMapEntry<K, V> getData() {return data;}
}
