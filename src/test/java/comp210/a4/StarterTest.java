package comp210.a4;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A few starter tests. Right-click this file in IntelliJ and choose Run.
 *
 * These are a sample, not the whole autograder. Gradescope runs many more,
 * including empty queues, long queues, and whole scripted games. Passing
 * everything here is a good sign, not a guarantee. Add your own tests below.
 */
class StarterTest {

    @Test
    void itemsComeOutInTheOrderTheyWentIn() {
        LinkedQueue<String> q = new LinkedQueue<>();
        q.enqueue("apple");
        q.enqueue("bomb");
        q.enqueue("snake");
        assertEquals("apple", q.dequeue());
        assertEquals("bomb", q.dequeue());
        assertEquals("snake", q.dequeue());
    }

    @Test
    void peekAndPeekLastSeeBothEnds() {
        LinkedQueue<Integer> q = new LinkedQueue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        assertEquals(1, q.peek());
        assertEquals(3, q.peekLast());
        assertEquals(3, q.size(), "peeking should not remove anything");
    }

    @Test
    void emptyQueueRefusesToDequeue() {
        LinkedQueue<Integer> q = new LinkedQueue<>();
        assertTrue(q.isEmpty());
        assertThrows(NoSuchElementException.class, q::dequeue);
    }

    @Test
    void containsComparesWithEquals() {
        LinkedQueue<Cell> q = new LinkedQueue<>();
        q.enqueue(new Cell(4, 7));
        assertTrue(q.contains(new Cell(4, 7)), "a different Cell object with the same x and y should count");
        assertFalse(q.contains(new Cell(7, 4)));
    }

    @Test
    void snakeMovesWithoutGrowing() {
        Snake s = new Snake(new Cell(5, 5), 3, Direction.EAST);
        s.advance(new Cell(6, 5), false);
        assertEquals(new Cell(6, 5), s.head());
        assertEquals(3, s.length());
        assertEquals(new Cell(4, 5), s.tail(), "the old tail cell (3, 5) should have left");
    }

    @Test
    void snakeGrowsWhenItEats() {
        Snake s = new Snake(new Cell(5, 5), 3, Direction.EAST);
        s.advance(new Cell(6, 5), true);
        assertEquals(4, s.length());
        assertEquals(new Cell(3, 5), s.tail(), "nothing should leave when growing");
    }
}
