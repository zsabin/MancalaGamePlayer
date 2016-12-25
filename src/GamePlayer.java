import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A class that handles the play of a game.
 */
public class GamePlayer
{
    public static List<Integer> POSSIBLE_MOVES = Collections.unmodifiableList(Arrays.asList(1, 2, 3, 4, 5, 6));

    private int[] slots;
    private int[] opponentSlots;

    private int score;
    private int opponentScore;

    private PlayerRepresentation activePlayer;

    /**
     * Creates a GamePlayer based on the specified state of the game
     */
    private GamePlayer(GameState state)
    {
        this.slots = state.getSlots();
        this.opponentSlots = state.getOpponentSlots();
        this.score = state.getScore();
        this.opponentScore = state.getOpponentScore();
        this.activePlayer = state.getActivePlayer();
    }

    /**
     * Performs the specified move in the game
     * @param state the current state of the game
     * @param slot  the slot index indicating the move to be performed
     * @return the state of the game after the move has been performed
     */
    public static GameState performMove(GameState state, int slot)
    {
        GamePlayer gamePlayer = new GamePlayer(state);
        int finalSlotIndex = gamePlayer.distributeStones(slot);

        if (gamePlayer.isCaptureOpponentStonesConditionMet(finalSlotIndex))
        {
            gamePlayer.captureOppositeStones(finalSlotIndex);
        }

        if (gamePlayer.gameIsComplete())
        {
            gamePlayer.finalizeGame();
        }
        else
        {
            // Player gets an extra turn if their move ends with placing a stone in their store
            if (finalSlotIndex != 0)
            {
                gamePlayer.nextTurn();
            }
        }
        return gamePlayer.buildGameState();
    }

    // Game ends when a player's slots are empty
    private boolean gameIsComplete()
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

    private GameState buildGameState()
    {
        return new GameState(slots, opponentSlots, score, opponentScore, activePlayer);
    }

    // returns the index of the slot in which the last stone was placed
    private int distributeStones(int slotIndex)
    {
        int stonesToDistribute = slots[slotIndex - 1];
        slots[slotIndex - 1] = 0;

        int targetIndex = slotIndex;
        for (int i = 1; i < stonesToDistribute + 1; i++)
        {
            targetIndex = Math.floorMod(slotIndex - i, slots.length + opponentSlots.length + 1 );
            if (targetIndex == 0)
            {
                score++;
            }
            else if (isActiveSlot(targetIndex))
            {
                slots[targetIndex - 1]++;
            }
            else
            {
                opponentSlots[targetIndex - slots.length - 1]++;
            }
        }
        return targetIndex;
    }

    // Player captures opposite stones if move ends with placing a stone an empty slot of their own
    private boolean isCaptureOpponentStonesConditionMet(int slot)
    {
        return isActiveSlot(slot) && slots[slot - 1 ] == 1;
    }

    private static boolean isActiveSlot(int index)
    {
        return index > 0 && index <= GameState.SLOTS_PER_SIDE;
    }

    private static boolean isOpponentSlot(int index)
    {
        return index > GameState.SLOTS_PER_SIDE && index < (GameState.SLOTS_PER_SIDE * 2);
    }

    // Each player captures all stones remaining in their slots after the completion of the game
    private void finalizeGame()
    {
        for (int i = 0; i < opponentSlots.length; i++)
        {
            opponentScore += opponentSlots[i];
            opponentSlots[i] = 0;
        }

        for (int i = 0; i < slots.length; i++)
        {
            score += slots[i];
            slots[i] = 0;
        }
    }

    // Captures the stones in the slot opposite the specified one
    private void captureOppositeStones(int slot)
    {
        int oppositeSlot = getOppositeSlot(slot);
        score += opponentSlots[oppositeSlot - 1];
        opponentSlots[oppositeSlot - 1] = 0;
    }

    private static int getOppositeSlot(int slot)
    {
        return GameState.SLOTS_PER_SIDE - slot + 1;
    }

    private void nextTurn()
    {
        int[] tempSlots = slots;
        slots = opponentSlots;
        opponentSlots = tempSlots;

        int tempScore = score;
        score = opponentScore;
        opponentScore = tempScore;

        activePlayer = PlayerRepresentation.getOpposite(activePlayer);
    }

    /**
     * Returns true if the specified slot corresponds to a valid move in the given game state.
     * Valid moves include any move corresponding to a slot on the active player's side that contains at least
     * one stone.
     */
    public static boolean isValidMove(GameState state, int slot)
    {
        return isActiveSlot(slot) && state.getSlots()[slot - 1] > 0;
    }
}
