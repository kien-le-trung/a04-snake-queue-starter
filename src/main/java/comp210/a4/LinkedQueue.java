package comp210.a4;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A first-in, first-out queue built from linked nodes.
 *
 * Items join at the back (the tail) and leave from the front (the head). Every
 * operation except contains runs in O(1): no loops, no walking the chain.
 *
 * Do not use any java.util collection in here (ArrayList, LinkedList,
 * ArrayDeque, and so on). The point is to build the links yourself.
 */
public class LinkedQueue<E> implements Iterable<E> {

    /** One link in the chain. */
    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E> head;   // front: the next item to leave
    private Node<E> tail;   // back: the item that joined most recently
    private int size;

    // ------------------------------------------------------------------
    // Your part: the seven methods below. Keep the three fields above; the
    // provided iterator and toString at the bottom read head and next.
    // ------------------------------------------------------------------

    /**
     * Adds item at the back of the queue.
     *
     * @throws IllegalArgumentException if item is null
     */
    public void enqueue(E item) {
        // TODO
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        // empty case
        if (this.isEmpty()) {
            head = new Node<>(item);
            tail = head;
            this.size = 1;
        } else {
            //other cases
            tail.next = new Node<>(item);
            tail = tail.next;
            this.size++;
        }

    }

    /**
     * Removes and returns the item at the front of the queue.
     *
     * @throws NoSuchElementException if the queue is empty
     */
    public E dequeue() {
        // TODO
        this.checkEmpty();
        Node<E> removed = head;
        head = head.next;
        this.size--;
        if (head == null) {
            tail = null;
        }
        return removed.data;
    }

    /**
     * Returns the item at the front without removing it.
     *
     * @throws NoSuchElementException if the queue is empty
     */
    public E peek() {
        // TODO
        this.checkEmpty();
        Node<E> peeked = head;
        return peeked.data;
    }

    /**
     * Returns the item at the back without removing it.
     *
     * @throws NoSuchElementException if the queue is empty
     */
    public E peekLast() {
        // TODO
        this.checkEmpty();
        Node<E> last = tail;
        return last.data;
    }

    /** Returns how many items are in the queue. */
    public int size() {
        // TODO
        return this.size;
    }

    /** Returns true when the queue holds no items. */
    public boolean isEmpty() {
        // TODO
        if (head != null) {
            return false;
        }
        return true;
    }

    /**
     * Returns true if some item in the queue equals item. Compare with
     * equals, not ==. This is the one method allowed to walk the chain.
     */
    public boolean contains(E item) {
        // TODO
        if (this.isEmpty()) {
            return false;
        }
        Node<E> curr = head;
        while (curr != null) {
            if (curr.data.equals(item)) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    private void checkEmpty() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
    }

    // ------------------------------------------------------------------
    // Provided. These only read head, next, and data, so they work once
    // your links are right.
    // ------------------------------------------------------------------

    /** Walks the queue from front to back. */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                E item = current.data;
                current = current.next;
                return item;
            }
        };
    }

    /** Front to back, like [a, b, c]. */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node<E> n = head; n != null; n = n.next) {
            sb.append(n.data);
            if (n.next != null) {
                sb.append(", ");
            }
        }
        return sb.append("]").toString();
    }
}
