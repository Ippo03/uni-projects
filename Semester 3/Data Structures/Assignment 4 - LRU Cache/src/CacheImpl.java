public class CacheImpl<K, V> implements  Cache<K, V>{
    private final int capacity;
    private int lookups = 0;
    private int hits = 0;
    DoubleLinkedListImpl<K, V> list;
    HashMapImpl<K, V> map;

    CacheImpl(int capacity){
        this.capacity = capacity;
        this.list = new DoubleLinkedListImpl<>();
        this.map = new HashMapImpl<>(capacity);
    }

    @Override
    public V lookUp(K key) {
        lookups++;
        //key in hash map->hit
        if (map.contains(key)) {
            //update node in list
            DoublyNode<K, V> temp = list.getNode(key);
            list.removeNode(temp);
            list.insertAtBack(temp.getData());
            hits++;
            //returns value V of K key
            return map.get(key); 
        }
        return null;
    }

    @Override
    public void store(K key, V value) {
        //if map or list is full(map size == list size)
        if (map.size() == capacity && list.size() == capacity) { 
            //remove oldest (head) entry from list
            DoublyNode<K, V> lastNode = list.removeNode(list.head);
            //remove same entry from map
            map.remove(lastNode.getData().getKey());
        }
        //new entry
        HashMapEntry<K, V> entry = new HashMapEntry<>(key, value);
        //insert new entry at the back of the list(back=tail -> most recent)
        list.insertAtBack(entry); 
        //add new entry to map
        map.add(entry); 
    }

    @Override
    public double getHitRatio() {
        return hits / (double) lookups;
    }

    @Override
    public long getHits() {
        return hits;
    }

    @Override
    public long getMisses() {
        return lookups - hits;
    }

    @Override
    public long getNumberOfLookUps() {
        return lookups;
    }  
}
