import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

/**
 * A computer player that will recursively calculate its next move by evaluating the state space of the possible moves
 * to the specified depth
 */
public class AI implements Player
{
    private int depth;

    /**
     * Creates a computer player
     * @param depth the number of levels in the state space of possible moves that the computer should search
     *              through to determine its next move.
     */
    public AI(int depth)
    {
        this.depth = depth;
    }

    /**
     * Recursively evaluates the state space of possible moves up to the specified depth and then chooses the best
     * possible move from this information.
     * @param state the current state of the game
     * @return the next move this player should perform
     */
    public int chooseNextMove(GameState state)
    {
        ForkJoinPool pool = new ForkJoinPool();
        MoveEvaluator moveEvaluator = new MoveEvaluator(state, depth);
        pool.invoke(moveEvaluator);
        System.out.println("Computer performed move: " + moveEvaluator.getBestChildMove());
        return moveEvaluator.getBestChildMove();
    }
}
