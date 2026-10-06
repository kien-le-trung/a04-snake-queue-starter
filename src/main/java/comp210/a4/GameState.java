package comp210.a4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The rules of the game, with no BRIDGES and no drawing. You do not need to
 * edit this file. SnakeGame draws whatever this class says is true.
 *
 * Each tick:
 *   1. Work out the cell in front of the head, wrapping at the edges.
 *   2. If that cell is part of the snake, the snake bit itself: game over.
 *      (The tail cell does not count, since it moves out of the way, unless
 *      the snake is about to grow.)
 *   3. snake.advance(next, ateApple). Your queue does the moving.
 *   4. Apple eaten: plant a new apple.
 *   5. Bomb hit: depends on the BombRule.
 *        SHRINK     snake.shrink(), and plant that bomb somewhere else. A
 *                   snake that shrinks to nothing is game over.
 *        GAME_OVER  the game ends on the spot.
 */
public class GameState {

    public enum Status { RUNNING, BIT_ITSELF, BLOWN_UP }

    /** What a bomb does. Pick one at the top of SnakeGame. */
    public enum BombRule {
        /** A bomb costs one cell. The game ends when the snake runs out. */
        SHRINK,
        /** Any bomb ends the game immediately, like classic Snake. */
        GAME_OVER
    }

    public static final int START_LENGTH = 3;
    public static final int BOMBS = 5;

    private final int columns;
    private final int rows;
    private final Random random;
    private final BombRule bombRule;

    private Snake snake;
    private Direction heading;
    private Direction lastMoved;
    private Cell apple;
    private final Cell[] bombs = new Cell[BOMBS];
    private Status status;
    private int applesEaten;

    /** A game where each bomb costs one cell (BombRule.SHRINK). */
    public GameState(int columns, int rows, Random random) {
        this(columns, rows, random, BombRule.SHRINK);
    }

    public GameState(int columns, int rows, Random random, BombRule bombRule) {
        this.columns = columns;
        this.rows = rows;
        this.random = random;
        this.bombRule = bombRule;
        reset();
    }

    /** Starts a fresh game: a short snake heading east, one apple, five bombs. */
    public void reset() {
        snake = new Snake(new Cell(columns / 3, rows / 2), START_LENGTH, Direction.EAST);
        heading = Direction.EAST;
        lastMoved = Direction.EAST;
        apple = null;
        for (int i = 0; i < BOMBS; i++) {
            bombs[i] = null;
        }
        apple = freeCell();
        for (int i = 0; i < BOMBS; i++) {
            bombs[i] = freeCell();
        }
        status = Status.RUNNING;
        applesEaten = 0;
    }

    /**
     * Asks the snake to turn. Reversing straight into its own neck is ignored.
     * The check is against the last direction actually moved, so two quick
     * key presses between ticks cannot sneak a reversal through.
     */
    public void turn(Direction d) {
        if (d != lastMoved.opposite()) {
            heading = d;
        }
    }

    /** Runs one tick of the game and returns the status afterward. */
    public Status tick() {
        if (status != Status.RUNNING) {
            return status;
        }
        Cell next = snake.head().step(heading, columns, rows);
        boolean ateApple = next.equals(apple);

        boolean tailMovesAway = !ateApple && next.equals(snake.tail());
        if (snake.occupies(next) && !tailMovesAway) {
            status = Status.BIT_ITSELF;
            return status;
        }

        snake.advance(next, ateApple);
        lastMoved = heading;

        if (ateApple) {
            applesEaten++;
            apple = freeCell();
        }
        for (int i = 0; i < BOMBS; i++) {
            if (next.equals(bombs[i])) {
                if (bombRule == BombRule.GAME_OVER) {
                    status = Status.BLOWN_UP;
                    return status;
                }
                if (!snake.shrink()) {
                    status = Status.BLOWN_UP;
                    return status;
                }
                bombs[i] = freeCell();
            }
        }
        return status;
    }

    /**
     * A random cell with nothing on it: no snake, no apple, no bomb. Collects
     * every open cell first, so it always finishes, even on a crowded board.
     */
    Cell freeCell() {
        List<Cell> open = new ArrayList<>();
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                Cell c = new Cell(x, y);
                if (!snake.occupies(c) && !c.equals(apple) && !isBomb(c)) {
                    open.add(c);
                }
            }
        }
        if (open.isEmpty()) {
            return null;   // board is full; nothing gets planted
        }
        return open.get(random.nextInt(open.size()));
    }

    private boolean isBomb(Cell c) {
        for (Cell b : bombs) {
            if (c.equals(b)) {
                return true;
            }
        }
        return false;
    }

    public Snake getSnake()      { return snake; }
    public Cell getApple()       { return apple; }
    public Cell[] getBombs()     { return bombs.clone(); }
    public Status getStatus()    { return status; }
    public int getApplesEaten()  { return applesEaten; }
    public int getColumns()      { return columns; }
    public int getRows()         { return rows; }
    public BombRule getBombRule() { return bombRule; }

    /** For tests: put the apple exactly where you want it. */
    void placeApple(Cell c)             { apple = c; }

    /** For tests: put one bomb exactly where you want it. */
    void placeBomb(int i, Cell c)       { bombs[i] = c; }
}
