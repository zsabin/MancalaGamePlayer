import java.util.Arrays;

/**
 * A class representing the state of a Mancala game
 */
public class GameState
{
    public static final int SLOTS_PER_SIDE = 6;
    public static final int INITIAL_STONES_PER_SLOT = 4;
    public static final PlayerRepresentation FIRST_PLAYER = PlayerRepresentation.LEFT;

    private final int[] slots;
    private final int[] opponentSlots;

    private final int score;
    private final int opponentScore;

    private final PlayerRepresentation activePlayer;

    public GameState(int[] slots, int[] opponentSlots, int score, int opponentScore, PlayerRepresentation activePlayer)
    {
        this.slots = slots;
        this.opponentSlots = opponentSlots;
        this.score = score;
        this.opponentScore = opponentScore;
        this.activePlayer = activePlayer;
    }

    /**
     * Gets the state of a new Mancala game
     */
    public static GameState getInitialState()
    {
        int[] slots = new int[GameState.SLOTS_PER_SIDE];
        for (int i = 0; i < slots.length; i++)
        {
            slots[i] = GameState.INITIAL_STONES_PER_SLOT;
        }

        int[] opponentSlots = new int[GameState.SLOTS_PER_SIDE];
        for (int i = 0; i < opponentSlots.length; i++)
        {
            opponentSlots[i] = GameState.INITIAL_STONES_PER_SLOT;
        }

        int score = 0;
        int opponentScore = 0;

        return new GameState(slots, opponentSlots, score, opponentScore, GameState.FIRST_PLAYER);
    }

    public int[] getSlots()
    {
        return Arrays.copyOf(slots, slots.length);
    }

    public int[] getOpponentSlots()
    {
        return Arrays.copyOf(opponentSlots, opponentSlots.length);
    }

    public int getScore()
    {
        return score;
    }

    public int getOpponentScore()
    {
        return opponentScore;
    }

    public PlayerRepresentation getActivePlayer()
    {
        return activePlayer;
    }

    public Board getBoard()
    {
        if (activePlayer == PlayerRepresentation.LEFT)
        {
            return new Board(slots, opponentSlots, score, opponentScore);
        }
        else
        {
            return new Board(opponentSlots, slots, opponentScore, score);
        }
    }

    // Game ends when either players' slots are empty
    public boolean gameIsComplete()
    {
        return isEmpty(slots) || isEmpty(opponentSlots);
    }

    private boolean isEmpty(int[] array)
    {
        for (int i = 0; i < array.length; i++)
        {
            if (array[i] > 0)
            {
                return false;
            }
        }
        return true;
    }
}
