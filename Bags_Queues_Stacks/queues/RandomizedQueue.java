import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int nItem = 0;
    private int capacity = 2;
    private Item[] array = (Item[]) new Object[capacity]; // Initialise with default space

    // construct an empty randomized queue
    public RandomizedQueue() {
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return nItem == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return nItem;
    }

    private void resizeArray(int size) {
        Item[] copy = (Item[]) new Object[size];
        for (int i = 0; i < nItem; i++) {
            if (array[i] != null) {
                copy[i] = array[i];
            }
        }
        array = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        if (nItem == capacity) {
            resizeArray(capacity * 2);
            capacity *= 2;
        }


        // If empty queue, nItem = 0. Store item to array[0].
        // nItem = 1 => array[1] and so on
        array[nItem] = item;
        nItem++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (nItem == 0) throw new NoSuchElementException();

        int index = StdRandom.uniform(nItem);
        Item item = array[index];
        // Move the last item to the empty slot that just have item removed
        array[index] = array[nItem - 1];
        array[nItem - 1] = null;

        nItem -= 1;
        if (nItem <= capacity / 4) resizeArray(capacity / 2);
        capacity /= 2;
        return item;

    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (nItem == 0) throw new NoSuchElementException();

        int index = StdRandom.uniform(nItem);
        return array[index];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private final Item[] shuffledArray;
        private int current = 0;

        public DequeIterator() {
            // Copy to last object in the array at the moment when the Iterator is made
            shuffledArray = (Item[]) new Object[nItem];
            for (int i = 0; i < nItem; i++) {
                shuffledArray[i] = array[i];
            }

            StdRandom.shuffle(shuffledArray);
        }


        public boolean hasNext() {
            return current < shuffledArray.length;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext() || (current == nItem)) {
                throw new NoSuchElementException();
            }

            return shuffledArray[current++];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        int n = 5;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);

        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }

        assert !queue.isEmpty();
        assert queue.size() == 5;

        System.out.println(queue.sample());
        System.out.println(queue.dequeue());
        assert queue.size() == 4;
        assert !queue.isEmpty();

    }

}
