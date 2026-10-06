package comp210.a4;

/**
 * The snake's body, stored as a queue of Cells.
 *
 *     front (peek)                          back (peekLast)
 *     tail of the snake  ->  ...  ->  ...   head of the snake
 *
 * The head is the BACK of the queue, because the head is where new cells
 * arrive. The tail end is the FRONT, because that is where cells leave.
 */
public class Snake {

    private final LinkedQueue<Cell> body = new LinkedQueue<>();

    /**
     * Builds a snake of the given length with its head at head, lying in a
     * straight line behind it. Provided.
     */
    public Snake(Cell head, int length, Direction facing) {
        Direction back = facing.opposite();
        // Enqueue the tail end first so the head ends up at the back.
        for (int i = length - 1; i >= 0; i--) {
            body.enqueue(new Cell(head.x() + back.dx * i, head.y() + back.dy * i));
        }
    }

    /** The cell the snake's head is on. Provided. */
    public Cell head() {
        return body.peekLast();
    }

    /** The last cell of the snake's tail. Provided. */
    public Cell tail() {
        return body.peek();
    }

    /** How many cells long the snake is. Provided. */
    public int length() {
        return body.size();
    }

    /** True if any part of the snake is on cell c. Provided. */
    public boolean occupies(Cell c) {
        return body.contains(c);
    }

    /** Every cell of the snake, tail end first. Provided. */
    public Iterable<Cell> cells() {
        return body;
    }

    /**
     * Moves the snake one step so its head lands on newHead.
     *
     * The new head joins the queue. Unless the snake is growing, the tail
     * cell leaves the queue, so the length stays the same. When grow is true,
     * nothing leaves, and the snake is one cell longer.
     */
    public void advance(Cell newHead, boolean grow) {
        // TODO: two queue operations, one of them only when not growing
    }

    /**
     * Loses one cell from the tail end, after a bomb. Returns true if the
     * snake still has at least one cell left, false if it is gone.
     */
    public boolean shrink() {
        // TODO
        return false;
    }
}
