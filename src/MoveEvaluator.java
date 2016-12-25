import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.RecursiveAction;

/**
 * A class that uses a fork/join structure to recursively evaluate the state space
 * of possible moves to find the best move
 */
public class MoveEvaluator extends RecursiveAction
{
    private int depth;

    private GameState parentState;
    private GameState state;
    private int move = -1;

    private int bestChildMove = -1;
    private int maxChildFitness = Integer.MIN_VALUE;

    /**
     * Constructor to build the root move evaluator
     * @param state the state to evaluate
     * @param depth the number of levels of moves to evaluate
     */
    public MoveEvaluator(GameState state, int depth)
    {
        this.state = state;
        this.depth = depth;
    }

    /**
     * Constructor to build a move evaluator
     * @param parentState the state of the parent
     * @param move the move to evaluate
     * @param depth the number of levels of moves to evaluate
     */
    public MoveEvaluator(GameState parentState, int move, int depth)
    {
        this.parentState = parentState;
        this.move = move;
        this.depth = depth;
    }

    public int getBestChildMove()
    {
        return bestChildMove;
    }

    public int getMove()
    {
        return move;
    }

    /**
     * Computes the fitness of the specified move either directly, if depth is set to 0, or recursively, if otherwise.
     */
    public void compute()
    {
        if (!isRoot())
        {
            this.state = GamePlayer.performMove(parentState, move);

            if (state.gameIsComplete())
            {
                updateBestMove(move, calculateFitness());
                return;
            }

            // We increment depth if we have an extra move to prevent bias when evaluating the fitness of
            // paths involving extra moves
            if (hasExtraMove())
            {
                depth++;
            }

            // Evaluate the fitness of the move directly
            if (depth == 0)
            {
                updateBestMove(move, calculateFitness());
                return;
            }
        }

        // Evaluate fitness of the move recursively by finding the best child move
        Stack<MoveEvaluator> subtasks = evaluateChildMoves();
        while (!subtasks.empty())
        {
            MoveEvaluator subtask = subtasks.pop();
            subtask.join();
            int subtaskFitness = calculateFitness(subtask);
            updateBestMove(subtask.move, subtaskFitness);
        }
    }

    private boolean isRoot()
    {
        return parentState == null;
    }

    // If the active player did not change from the parent state this will indicate the existence of an extra move
    private boolean hasExtraMove()
    {
        return parentState.getActivePlayer().equals(state.getActivePlayer());
    }

    // Recursively evaluates all child moves, forking all but the last one,
    // which will be evaluated directly in this thread
    private Stack<MoveEvaluator> evaluateChildMoves()
    {
        Stack<MoveEvaluator> subtasks = new Stack<>();
        List<Integer> validChildMoves = getValidChildMoves();
        for (int i = 0; i < validChildMoves.size() - 1; i++)
        {
            subtasks.push(evaluateSingleChildMove(validChildMoves.get(i), true));
        }
        //Evaluate the last move directly instead of creating a new thread to do that
        int lastValidMove = validChildMoves.get(validChildMoves.size() - 1);
        subtasks.push(evaluateSingleChildMove(lastValidMove, false));
        return subtasks;
    }

    // Recursively evaluates a single child move
    private MoveEvaluator evaluateSingleChildMove(int slot, boolean fork)
    {
        MoveEvaluator moveEvaluator = new MoveEvaluator(state, slot, depth - 1);
        if (fork)
        {
            moveEvaluator.fork();
        }
        else
        {
            moveEvaluator.invoke();
        }
        return moveEvaluator;
    }

    private List<Integer> getValidChildMoves()
    {
        List<Integer> validMoves = new ArrayList<>();
        for (int i = 0; i < GamePlayer.POSSIBLE_MOVES.size(); i++)
        {
            int move = GamePlayer.POSSIBLE_MOVES.get(i);
            if (GamePlayer.isValidMove(state, move))
            {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    // Calculates the fitness value of the move directly by comparing the active player's score with the opponent's
    // score after the move has been completed
    private int calculateFitness()
    {
        return state.getScore() - state.getOpponentScore();
    }

    // Gets the fitness value of the specified subtask, taking the inverse of the value if the subtask
    // represents the fitness from the opponent player's perspective
    private int calculateFitness(MoveEvaluator subtask)
    {
        if (state.getActivePlayer() != subtask.state.getActivePlayer())
        {
            return -subtask.maxChildFitness;
        }
        return subtask.maxChildFitness;
    }

    private void updateBestMove(int move, int fitness)
    {
        if (fitness > maxChildFitness)
        {
            maxChildFitness = fitness;
            bestChildMove = move;
        }
    }
}
