import java.io.PrintStream;
import java.util.NoSuchElementException;

public class StringQueueWithOnePointer<T> implements StringQueue<T> {

    private Node<T> tail;
    private int size;

    public StringQueueWithOnePointer() {
        tail = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return tail == null;
    }

    @Override
    public void put(T item) {
        System.out.println("Storing value (" + item + ") in queue.");
        size++;
        Node<T> node = new Node<>(item);
        if (isEmpty()) {
            tail = node;
            tail.next = node;
        } else {
            node.next = tail.next;
            tail.next = node;
            tail = node;
        }
    }

    @Override
    public T get() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        size--;
        T removedValue = tail.next.value;
        if (tail.next == tail) {
            tail = null;
        } else {
            tail.next = tail.next.next;
        }
        System.out.println("Removing value (" + removedValue + ") from queue");
        return removedValue;
    }

    @Override
    public T peek() throws NoSuchElementException {
        if (!isEmpty()) {
            System.out.println("The first item of the queue is: " + tail.next.value);
            return tail.next.value;
        } else {
            throw new NoSuchElementException("Queue is empty");
        }
    }

    @Override
    public void printQueue(PrintStream stream) {
        if (isEmpty()) {
            stream.println("Queue is empty");
        } else {
            Node<T> current = tail.next;
            do {
                stream.println(current.value);
                current = current.next;
            } while (current != tail.next);
        }
    }

    @Override
    public int size() {
        System.out.println("The size of the queue is: " + size);
        return size;
    }
}
