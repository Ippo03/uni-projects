 
public class DoubleLinkedListImpl<K, V> implements DoubleLinkedList<K, V> {
    DoublyNode<K, V> head;
    DoublyNode<K, V> tail;
    int size;

    DoubleLinkedListImpl(){
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public void insertAtBack(HashMapEntry<K, V> item) {
        DoublyNode<K, V> node = new DoublyNode<>(item);
        if (tail == null) { 
            head = tail = node;
        } else { 
            tail.next = node;
            node.prev = tail;
            tail = node;
            if (head == null) { 
                head = node.prev;
            }
        }
        this.size++;
    }

    @Override
    public DoublyNode<K, V> removeNode(DoublyNode<K , V> node) {
        if (node == null) {
            return null;
        }
        DoublyNode<K, V> prev = node.prev;
        DoublyNode<K, V> next = node.next;
        if (prev != null) {
            prev.next = next;
        } else {
            head = next;
        }
        if (next != null) {
            next.prev = prev;
        } else {
            tail = prev;
        }
        node.prev = null;
        node.next = null;
        size--;
        return node;
    }
    
    @Override
    public DoublyNode<K, V> getNode(K key) {
        if (this.isEmpty()) {
            return null;
        }
        DoublyNode<K, V> current = head;
        while (current!= null) {
            if (current.getData().getKey().equals(key)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public boolean isEmpty() {
        return head == null;
    }

    @Override
    public int size() {
        return size;
    }

}
