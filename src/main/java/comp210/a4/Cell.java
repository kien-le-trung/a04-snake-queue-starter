package comp210.a4;

/**
 * One square on the board. Column x runs left to right, row y runs top to
 * bottom, and (0, 0) is the top-left corner.
 *
 * A record gets equals and hashCode for free, so two Cells with the same x and
 * y are equal even when they are different objects. LinkedQueue.contains
 * depends on that.
 */
public record Cell(int x, int y) {

    /** The neighboring cell one step in direction d, wrapping at the edges. */
    public Cell step(Direction d, int columns, int rows) {
        return new Cell(Math.floorMod(x + d.dx, columns), Math.floorMod(y + d.dy, rows));
    }
}
