package comp210.a4;

import bridges.base.NamedColor;
import bridges.base.NamedSymbol;
import bridges.games.NonBlockingGame;

import java.util.Random;

/**
 * Draws the game in the BRIDGES web view and turns key presses into turns.
 * The only thing you might change here is BOMB_RULE, just below. The rules
 * live in GameState, and the snake's body lives in your LinkedQueue.
 *
 * Controls: Space starts. Arrow keys steer. After a game over, Space starts a
 * new game.
 */
public class SnakeGame extends NonBlockingGame {

    // ------------------------------------------------------------------
    // YOUR CHOICE: what happens when the snake runs into a bomb.
    //
    //   GameState.BombRule.SHRINK     the snake loses one cell from its tail
    //                                 end and keeps going. The game ends only
    //                                 when it has no cells left. (Default.)
    //   GameState.BombRule.GAME_OVER  any bomb ends the game on the spot,
    //                                 like classic Snake.
    //
    // Either one is fine. Your shrink() is graded the same way under both.
    // ------------------------------------------------------------------
    static final GameState.BombRule BOMB_RULE = GameState.BombRule.SHRINK;

    static final int COLUMNS = 30;
    static final int ROWS = 30;

    // BRIDGES calls gameLoop 30 times a second. Moving on every call would
    // send the snake across the board in one second, so it moves on every
    // fourth call instead: about 7 cells a second.
    static final int FRAMES_PER_MOVE = 4;

    private static final NamedColor GRASS_DARK = NamedColor.forestgreen;
    private static final NamedColor GRASS_LIGHT = NamedColor.green;
    private static final NamedColor BODY = NamedColor.silver;
    private static final NamedColor HEAD = NamedColor.white;
    private static final NamedColor DEAD = NamedColor.darkred;

    private GameState state;
    private boolean started = false;
    private int frame = 0;

    public SnakeGame(BridgesConfig config) {
        super(config.getAssignmentNumber(), config.getUsername(), config.getApiKey(), COLUMNS, ROWS);
    }

    @Override
    protected void initialize() {
        state = new GameState(COLUMNS, ROWS, new Random(), BOMB_RULE);
        paint();
    }

    @Override
    protected void gameLoop() {
        if (!started) {
            started = keySpace();
            return;
        }

        if (state.getStatus() != GameState.Status.RUNNING) {
            if (keySpaceJustPressed()) {
                state.reset();
                frame = 0;
            }
            paint();
            return;
        }

        if (keyLeft()) {
            state.turn(Direction.WEST);
        } else if (keyRight()) {
            state.turn(Direction.EAST);
        } else if (keyUp()) {
            state.turn(Direction.NORTH);
        } else if (keyDown()) {
            state.turn(Direction.SOUTH);
        }

        frame++;
        if (frame % FRAMES_PER_MOVE == 0) {
            state.tick();
        }
        paint();
    }

    /** Redraws the whole board from the current GameState. */
    private void paint() {
        for (int y = 0; y < ROWS; y++) {
            for (int x = 0; x < COLUMNS; x++) {
                setBGColor(y, x, (x + y) % 2 == 0 ? GRASS_DARK : GRASS_LIGHT);
                drawSymbol(y, x, NamedSymbol.none, NamedColor.white);
            }
        }

        boolean alive = state.getStatus() == GameState.Status.RUNNING;
        for (Cell c : state.getSnake().cells()) {
            setBGColor(c.y(), c.x(), alive ? BODY : DEAD);
        }
        if (state.getSnake().length() > 0 && alive) {
            Cell head = state.getSnake().head();
            setBGColor(head.y(), head.x(), HEAD);
        }

        Cell apple = state.getApple();
        if (apple != null) {
            drawSymbol(apple.y(), apple.x(), NamedSymbol.apple, NamedColor.red);
        }
        for (Cell bomb : state.getBombs()) {
            if (bomb != null) {
                drawSymbol(bomb.y(), bomb.x(), NamedSymbol.bomb, NamedColor.black);
            }
        }
    }

    public static void main(String[] args) {
        // Same as A0: the key comes from .env, never from this file.
        BridgesConfig config = BridgesConfig.load();
        System.out.println("Loaded credentials: " + config.describe());

        SnakeGame game = new SnakeGame(config);
        game.setTitle("COMP210 A4: " + config.getUsername() + "'s snake");
        String bombs = BOMB_RULE == GameState.BombRule.SHRINK
                ? "Each bomb costs you one cell; run out and the game is over."
                : "Any bomb ends the game.";
        game.setDescription("Eat apples to grow. " + bombs + " Press Space to start.");
        game.start();
    }
}
