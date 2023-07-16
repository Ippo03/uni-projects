/**
 * Defines the interface for the hash map data structure
 * Hash Map contains the elements of the cache divided in buckets
 * */
public interface HashMap<K, V> {
	
	/**
	 * add a pair of key and value in the hash map 
	 * @param entry the pair to add
	 * @return null 
	 */
    void add(HashMapEntry<K, V> entry);

	/**
	 * remove a pair of key and value from the hash map 
	 * @param key key of pair to be removed
	 * @return null 
	 */
	void remove(K key);

	/**
	 * find value using key
	 * @param key key of pair to find value
	 * @return value 
	 */
	public V get(K key);

	/**
	 * check if a key exist in the hash map
	 * @param key key of pair to search for
	 * @return true or false 
	 */
	boolean contains(K key);

	/**
	 * Returns the size of the hash map
	 */
    int size();
}
