/**
 * A representation of a Mancala board.
 */
public class Board
{
    private final int[] leftSlots;
    private final int[] rightSlots;

    private final int leftScore;
    private final int rightScore;

    public Board(int[] leftSlots, int[] rightSlots, int leftScore, int rightScore)
    {
        this.leftSlots = leftSlots;
        this.rightSlots = rightSlots;
        this.leftScore = leftScore;
        this.rightScore = rightScore;
    }

    public int getLeftScore()
    {
        return leftScore;
    }

    public int getRightScore()
    {
        return rightScore;
    }

    /**
     * Generates a String to be used for display on a command-line interface.
     */
    public String toString()
    {
        String s ="\n";
        s += String.format("| L |%2s |%2s |%2s |%2s |%2s |%2s |\n", 1, 2, 3, 4, 5, 6);
        s += "=================================\n";
        s += String.format("|   |%2s |%2s |%2s |%2s |%2s |%2s |   |\n",
                leftSlots[0], leftSlots[1], leftSlots[2], leftSlots[3], leftSlots[4], leftSlots[5]);
        s += String.format("|%2s |-----------------------|%2s |\n", leftScore, rightScore);
        s += String.format("|   |%2s |%2s |%2s |%2s |%2s |%2s |   |\n",
                rightSlots[5], rightSlots[4], rightSlots[3], rightSlots[2], rightSlots[1], rightSlots[0]);
        s += "=================================\n";
        s += String.format("    |%2s |%2s |%2s |%2s |%2s |%2s | R |\n", 6, 5, 4, 3, 2, 1);
        return s;
    }
}
