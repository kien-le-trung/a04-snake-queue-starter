# A4: Snake, Queued Up

**COMP210, Fall 2026**

| | |
|---|---|
| Released | Thursday, October 1 |
| Due | Friday, October 9, 11:59pm |
| Points | 100 |
| Collaboration | Follows the syllabus policy. Never share your API key. |

You have played Snake. Watch it closely and you will see what the snake does
on each step: a new square appears in front of the head, and the last square of
the tail disappears. Squares join at one end and leave from the other, in the
order they arrived. That is a queue.

In A4 you build a queue out of linked nodes, and the snake runs on it. Every
frame of the game is your `enqueue` and your `dequeue`. If your links are
wrong, you will see it: the snake stretches, vanishes, or leaves bits of
itself behind.

## The snake is a queue

The snake's body is a `LinkedQueue<Cell>`, one `Cell` per square:

```
 front (peek)                                     back (peekLast)
 [tail end] -> [    ] -> [    ] -> [    ] -> [head]
  leaves here                                  joins here
```

On each tick of the game:

1. The cell in front of the head gets enqueued. It becomes the new head.
2. The cell at the tail end gets dequeued. The snake keeps its length.

Eating an apple skips the dequeue, so the snake ends up one cell longer.
Running the head into your own body ends the game.

## Bombs: pick your rule

There are five bombs on the board, and you choose what they do. The setting is
`BOMB_RULE`, the first thing in `SnakeGame.java`.

**`SHRINK` (the default): a bomb does not end the game.** It costs the snake
one cell from its tail end, which is one extra dequeue, and the bomb moves
somewhere else. The game ends only when the snake has no cells left. The snake
starts 3 cells long, so with no apples eaten it survives two bombs and the
third one ends the game. Each apple you eat buys you one more bomb.

**`GAME_OVER`: any bomb ends the game on the spot**, like classic Snake.

To switch, change that one line:

```java
static final GameState.BombRule BOMB_RULE = GameState.BombRule.GAME_OVER;
```

Pick whichever you like playing. It changes nothing about your grade: the
autograder tests your `shrink()` directly and plays its scripted games with
`SHRINK`, whatever your copy of `SnakeGame.java` says. Note your choice at the
top of your write-up.

## Part 1: `LinkedQueue<E>` (50 points)

Open `src/main/java/comp210/a4/LinkedQueue.java`. The `Node` class, the three
fields, and the iterator at the bottom are written. You write seven methods:

| Method | What it does | On an empty queue |
|---|---|---|
| `enqueue(E item)` | adds `item` at the back | works like any other time |
| `dequeue()` | removes and returns the front item | throws `NoSuchElementException` |
| `peek()` | returns the front item, removes nothing | throws `NoSuchElementException` |
| `peekLast()` | returns the back item, removes nothing | throws `NoSuchElementException` |
| `size()` | how many items are in the queue | returns 0 |
| `isEmpty()` | true when there are no items | returns true |
| `contains(E item)` | true if some item `.equals(item)` | returns false |

The rules:

- Every method except `contains` runs in O(1). No loops, no walking the chain.
  The autograder times a few hundred thousand operations to check.
- `enqueue(null)` throws `IllegalArgumentException`.
- `contains` compares with `equals`, not `==`. The game makes a brand new
  `Cell` every time it asks "is the snake here?", so `==` would never match.
- Build it from your own `Node` links. No `ArrayList`, `LinkedList`,
  `ArrayDeque`, or arrays inside `LinkedQueue`. The autograder flags these for
  your instructor to review by hand.

The bug that catches the most people is the last `dequeue`. When you remove
the only item, `head` becomes `null`, and `tail` has to become `null` too.
Otherwise `tail` still points at the node you removed, and the next `enqueue`
attaches to a node nobody can reach.

## Part 2: `Snake` (12 points)

Open `Snake.java`. The constructor and the helper methods are written, and
all of them call your queue. You write two methods, a few lines each:

- `advance(Cell newHead, boolean grow)` moves the snake one step. Enqueue the
  new head, then dequeue the tail cell unless the snake is growing.
- `shrink()` loses one cell from the tail end after a bomb. It returns `true`
  if the snake has any cells left, `false` if it is gone. Write it even if you
  play with `GAME_OVER` bombs; the autograder tests it either way.

`GameState.java` holds the rules (when to grow, when to shrink, what counts as
biting yourself) and `SnakeGame.java` does the drawing. You do not need to edit
`GameState`, and the only line of `SnakeGame` you might change is `BOMB_RULE`.
Reading `GameState.tick()` shows you exactly when your two methods get called.

## Part 3: Play it (10 points)

1. Follow the Setup section below if you have not already.
2. Open `SnakeGame.java` and click the green arrow next to `main`.
3. Open the URL that BRIDGES prints. Press **Space** to start, and steer with
   the arrow keys. After a game over, **Space** starts a new game.

The game needs a working queue. Until Part 1 is done, pressing Space ends with
a `NullPointerException`, because the snake's head comes back `null`.

Your game lives at `https://bridges-cs.herokuapp.com/assignments/4/yourusername`.
Put that URL at the top of your write-up.

## Part 4: Write-up (20 points)

Answer the three questions in `writeup.md`. A few sentences each, with
numbers where the question asks for them.

## Setup

Same setup as A0: a Maven project you open in IntelliJ, with BRIDGES pulled
from JitPack and your key read from `.env` by `BridgesConfig`.

1. **File > Open** the folder you cloned, the one holding `pom.xml`. Wait for the Maven
   import to finish; the first one downloads BRIDGES and takes a minute or two.
2. Copy `.env.example` to `.env` in the same folder and fill in the same
   username and API key you used in A0. Leave `BRIDGES_ASSIGNMENT` at 4.
3. You are ready to write code and run `SnakeGame`.

If the import or the key gives you trouble, the "When it breaks" list in A0's
`code-along.md` covers it. This project is wired the same way.

## Testing your code

`src/test/java/comp210/a4/StarterTest.java` has six tests. Right-click the file
and choose **Run 'StarterTest'**. Gradescope runs 18 tests, including edge
cases these six skip and three scripted games played on your queue. Passing
the starter tests is a good sign and not a guarantee, so add tests of your own
for the empty-queue cases.

## What to submit

Upload these to the A4 assignment on Gradescope:

1. `LinkedQueue.java`
2. `Snake.java`
3. `writeup.md`, or a PDF of it, with your game URL at the top

The autograder runs as soon as you submit, and you can resubmit as many times
as you like before the deadline.

Leave out `.env`. Same rule as A0: an API key anywhere in your submission is an
automatic zero, and Gradescope scans every file you upload for one.

## Grading

| Part | Points | Graded by |
|---|---|---|
| `LinkedQueue` | 50 | autograder |
| `Snake` | 12 | autograder |
| Scripted games on your code | 8 | autograder |
| Game URL loads under your account | 10 | instructor |
| Write-up | 20 | instructor |
| **Extra credit:** submit at least 72 hours before the deadline | +10 | autograder |

The early bonus goes by the time of your Gradescope submission. Each
submission is checked on its own, so a late resubmission does not earn it,
even if an earlier one did.

## Files

```
your cloned folder/         <- open THIS folder in IntelliJ
  README.md                  this file
  writeup.md                 Part 4
  pom.xml                    Maven build, pulls BRIDGES, dotenv-java, JUnit
  .env.example               template for your credentials
  .gitignore                 keeps .env out of git
  src/main/java/comp210/a4/
    LinkedQueue.java         Part 1, yours
    Snake.java               Part 2, yours
    GameState.java           the rules, no edits needed
    SnakeGame.java           drawing and keys; BOMB_RULE is your choice
    Cell.java                one square on the board
    Direction.java           north, south, east, west
    BridgesConfig.java       reads .env, same as A0
  src/test/java/comp210/a4/
    StarterTest.java         six tests to start from
```
