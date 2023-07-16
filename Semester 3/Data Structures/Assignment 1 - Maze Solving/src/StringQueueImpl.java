import java.io.PrintStream;
import java.util.NoSuchElementException;

public class StringQueueImpl<T> implements StringQueue<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public StringQueueImpl() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void put(T item) {
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }

    @Override
    public T get() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        T removedItem = head.value;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return removedItem;
    }

    @Override
    public T peek() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return head.value;
    }

    @Override
    public void printQueue(PrintStream stream) {
        if (isEmpty()) {
            stream.println("Queue is empty");
        } else {
            Node<T> currentNode = head;
            StringBuilder sb = new StringBuilder();
            while (currentNode != null) {
                sb.append(currentNode.value);
                if (currentNode.next != null) {
                    sb.append(", ");
                }
                currentNode = currentNode.next;
            }
            stream.println(sb.toString());
        }
    }

    @Override
    public int size() {
        return size;
    }
}
