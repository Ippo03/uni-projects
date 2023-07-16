/**
 * Defines the interface for the doubly linked list data structure
 * Doubly Linked List stores nodes in order(head->oldest and tail->newest) 
 **/
public interface DoubleLinkedList<K, V> {
    /**
	 * insert an item at the back of the list. 
	 * @param item the item to be inserted
	 * @return null
	 */
    public void insertAtBack(HashMapEntry<K, V> item);

    /**
	 * delete a node from the list. 
	 * @param node the node to be deleted
	 * @return deleted node
	 */
    public DoublyNode<K, V> removeNode(DoublyNode<K, V> node);

    /**
	 * find node with key given. 
	 * @param key key to search for in list
	 * @return node that has this key or null
	 */
    public DoublyNode<K, V> getNode(K key);

    /**
	 * Returns the size of the doubly linked list
	 */
    public int size();

    /**
	 * Returns whether the doubly linked list is empty or not
	 */
    public boolean isEmpty();
}
