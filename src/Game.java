/**
 * A class representing a Mancala game.
 */
public class Game
{
    private GameState state;
    private Board board;

    private Player leftPlayer;
    private Player rightPlayer;

    private Game(Player leftPlayer, Player rightPlayer)
    {
        this.leftPlayer = leftPlayer;
        this.rightPlayer = rightPlayer;
    }

    /**
     * Creates a game between a human player and a computer player
     * @param depth the number of levels in the state space of possible moves that the computer should search
     *              through to determine its next move.
     * @return the resulting game object
     */
    public static Game createSinglePlayerGame(int depth)
    {
        return new Game(new Human(), new AI(depth));
    }

    /**
     * Creates a game between two human players
     * @return the resulting game object
     */
    public static Game createTwoPlayerGame()
    {
        return new Game(new Human(), new Human());
    }

    /**
     * Creates a game between two computer players
     * @param depth the number of levels in the state space of possible moves that the computers should search
     *              through to determine their next move.
     * @return the resulting game object
     */
    public static Game createAIGame(int depth)
    {
        return new Game(new AI(depth), new AI(depth));
    }

    /**
     * Starts the game
     */
    public void start()
    {
        this.state = GameState.getInitialState();
        this.board = state.getBoard();

        while (!state.gameIsComplete())
        {
            displayBoard();
            System.out.println(state.getActivePlayer() + " player's turn");

            GameClient.promptUserForNextMove(getActivePlayer());
            int move = getActivePlayer().chooseNextMove(state);
            if (move == -1)
            {
                return;
            }
            state = GamePlayer.performMove(state, move);
            board = state.getBoard();
        }
        displayResult();
    }

    private Player getActivePlayer()
    {
        return state.getActivePlayer() == PlayerRepresentation.LEFT ? leftPlayer : rightPlayer;
    }

    /**
     * Displays the current state of the board
     */
    private void displayBoard()
    {
        GameClient.clearScreen();
        System.out.println(board);
        System.out.flush();
    }

    /**
     * Displays the results of the completed game
     */
    private void displayResult()
    {
        displayBoard();
        System.out.println("---------------------");
        if (board.getLeftScore() > board.getRightScore())
        {
            System.out.println("LEFT player wins");
        }
        else if (board.getRightScore() > board.getLeftScore())
        {
            System.out.println("RIGHT player wins");
        }
        else
        {
            System.out.println("TIE");
        }
        System.out.println("---------------------");
        GameClient.pause();
    }
}
