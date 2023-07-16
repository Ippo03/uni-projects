public class HashMapEntry<K, V> {
    private final K key;
    private V value;

    public HashMapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    /**
     * @return the key
     */
    public K getKey() {
        return key;
    }
    
    /**
     * @return the value
     */
    public V getValue() {
        return value;
    }
    
    public void setValue(V value) {
        this.value = value;
    }
    
}
