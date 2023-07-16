public class HashMapImpl<K, V> implements HashMap<K, V>  {
    private final int capacity;
    private int size;
    private Bucket<K, V>[] array;
    
    //helping class that its methods modify bucket's singly linked lists
    protected class Bucket<K1, V1>{
        
        private SinglyLinkedList<HashMapEntry<K1, V1>> list;
        
        public Bucket() {
            list = new SinglyLinkedList<>();
        }
        
        public void addEntry(HashMapEntry<K1, V1> entry) {
            list.addFirst(entry);
        }
        
        public void removeEntry(K key) {
            SinglyLinkedList.Node<HashMapEntry<K1, V1>> temp = list.head;
            SinglyLinkedList.Node<HashMapEntry<K1, V1>> prev = null;
            while (temp != null && !temp.getElement().getKey().equals(key)) {
                prev = temp;
                temp = temp.next;
            }
            if (temp == null) {
                return;
            }
            if (prev == null) {
                list.head = temp.next;
            } else {
                prev.next = temp.next;
            }
        }
        public HashMapEntry<K1, V1> getEntry(K1 key) {
            SinglyLinkedList.Node<HashMapEntry<K1, V1>> temphead = list.head;
            while (temphead != null) {
                if (temphead.getElement().getKey().equals(key)) {
                    return temphead.getElement();
                }
                temphead = temphead.next;
            }
            return null;
        }
        
        public boolean contains(K1 key){
            SinglyLinkedList.Node<HashMapEntry<K1, V1>> temphead = list.head;
            while (temphead != null) {
                if (temphead.getElement().getKey().equals(key)) { 
                    return true;
                }
                temphead = temphead.next;
            }
            return false;
        }
        
        public boolean isEmpty() {
            return list.isEmpty();
        }

    }
    @SuppressWarnings("unchecked")
    HashMapImpl(int capacity){
        this.capacity = capacity;
        this.size = 0;
        this.array = new Bucket[capacity];
        for (int i = 0; i < capacity; i++) {
            array[i] = (new Bucket<K, V>());
        }
    }
    
    @Override
    public void add(HashMapEntry<K, V> entry) {
        if (contains(entry.getKey())) {
            return ;
        }
        int hash = entry.getKey().hashCode();
        int index = hash % capacity;
        array[index].addEntry(entry);
        this.size++;
        return ;

    }

    @Override
    public void remove(K key) {
        if (!contains(key)) {
            return ;
        }
        int hash = key.hashCode();
        int index = hash % capacity;
        array[index].removeEntry(key);
        this.size--;
        return ;
        
    }
    
    @Override
    public V get(K key) {
        if (!contains(key)) {
            return null;
        }
        int hash = key.hashCode();
        int index = hash % capacity;
        return array[index].getEntry(key).getValue();
    }

    @Override
    public boolean contains(K key) {
        int i = 0;
        boolean found = false;
        if (array.length == 0) {
            return false;
        }
        while(i < capacity && found == false) {
            found = array[i].contains(key);
            i++; 
        }
        return found; 
    }

    @Override
    public int size() {
        return size;
    }
}
