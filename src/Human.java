import java.util.Scanner;

/**
 * A player object that will receive commands from the user
 */
public class Human implements Player
{
    private Scanner sc;

    public Human()
    {
        this.sc = new Scanner(System.in);
    }

    /**
     * Reads a move from the user and validates the move based on the given game state
     */
    public int chooseNextMove(GameState gameState)
    {
        int move;
        while (true)
        {
            String command = sc.nextLine();
            try {
                move = Integer.parseInt(command);
                if (!GamePlayer.isValidMove(gameState, move))
                {
                    System.out.println("Invalid move");
                    continue;
                }
                break;
            } catch(Exception e)
            {
                if (command.toLowerCase().equals("q"))
                {
                    return -1;
                }
                System.out.println("Invalid command");
            }
        }
        return move;
    }
}
