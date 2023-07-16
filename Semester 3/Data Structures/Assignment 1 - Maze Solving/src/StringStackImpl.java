import java.io.PrintStream;
import java.util.NoSuchElementException;

public class StringStackImpl<T> implements StringStack<T> {

    private Node<T> top;
    private int size;

    public StringStackImpl() {
        top = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return top == null;
    }

    @Override
    public void push(T item) {
        System.out.println("Storing value (" + item + ") in stack.");
        Node<T> node = new Node<>(item);
        node.next = top;
        top = node;
        size++;
    }

    @Override
    public T pop() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }
        T removedItem = top.value;
        top = top.next;
        size--;
        System.out.println("Removing value (" + removedItem + ") from stack.");
        return removedItem;
    }

    @Override
    public T peek() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }
        return top.value;
    }

    @Override
    public void printStack(PrintStream stream) {
        if (isEmpty()) {
            stream.println("Stack is empty");
        } else {
            Node<T> current = top;
            while (current != null) {
                stream.println(current.value);
                current = current.next;
            }
        }
    }

    @Override
    public int size() {
        return size;
    }
}
