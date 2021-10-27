import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int nItem = 0;

    private class Node {
        private Node nextNode;
        private Node previousNode;
        private final Item content;

        public Node(Item input) {
            content = input;
        }
    }

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return nItem == 0;
    }

    // return the number of items on the deque
    public int size() {
        return nItem;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException(
                    "Null cannot be added to the queue");
        }

        if (nItem == 0) {
            first = new Node(item);
            last = first;
        }
        else {
            Node nextNode = first;
            first = new Node(item);
            nextNode.previousNode = first;
            first.nextNode = nextNode;
            first.previousNode = null;
        }
        nItem++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException(
                    "Null cannot be added to the queue");
        }
        if (nItem == 0) {
            last = new Node(item);
            first = last;
        }
        else {
            Node secondLastNode = last;
            last = new Node(item);
            secondLastNode.nextNode = last;
            last.nextNode = null;
            last.previousNode = secondLastNode;
        }
        nItem++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (nItem == 0) {
            throw new NoSuchElementException("The queue is already empty");
        }

        Node temp = first;
        if (nItem > 1) {
            first = temp.nextNode;
            first.previousNode = null;
        }
        else {
            first = null;
            last = first;
        }
        nItem--;
        return temp.content;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (nItem == 0) {
            throw new NoSuchElementException("The queue is already empty");
        }

        Node temp = last;
        if (nItem > 1) {
            last = temp.previousNode;
            last.nextNode = null;
        }
        else {
            // nItem == 1
            first = null;
            last = first;
        }

        nItem--;
        return temp.content;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (current == null) {
                throw new NoSuchElementException("No more object next");
            }

            Item item = current.content;
            current = current.nextNode;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("Not implemented");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> model = new Deque<String>();

        System.out.println("********");
        model.addFirst("A");
        model.addLast("B");
        model.addLast("C");
        System.out.printf("How many items: %d || Is empty? %s\n", model.size(), model.isEmpty());
        for (String s : model) {
            System.out.println(s);
        }

        System.out.println("********");
        model.removeFirst();
        System.out.printf("How many items: %d || Is empty? %s\n", model.size(), model.isEmpty());
        for (String s : model) {
            System.out.println(s);
        }

        System.out.println("********");
        model.addFirst("D");
        model.removeLast();
        System.out.printf("How many items: %d || Is empty? %s\n", model.size(), model.isEmpty());
        for (String s : model) {
            System.out.println(s);
        }

        try {
            System.out.println("********");
            model.removeFirst();
            model.removeLast();
            model.removeLast();
        }
        catch (NoSuchElementException e) {
            System.out.println(e);
        }

        System.out.println("********");
        model.addLast("A");
        model.addLast("B");
        model.addLast("C");
        System.out.printf("How many items: %d || Is empty? %s\n", model.size(), model.isEmpty());
        for (String s : model) {
            System.out.println(s);
        }
    }

}
