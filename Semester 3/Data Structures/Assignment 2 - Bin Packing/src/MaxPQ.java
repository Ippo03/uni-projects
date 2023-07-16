public interface MaxPQ<T>{           //Priority Queue Interface
    /**
     * Inserts the specified element into this priority queue.
     *
     * @param item
     */
    void insert(T item);

    /**
     * Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
     *
     * @return the head of the queue
     */
    T peek();

    /**
     * Retrieves and removes the head of this queue, or returns null if this queue is empty.
     *
     * @return the head of the queue
     */
    T getMax();
}