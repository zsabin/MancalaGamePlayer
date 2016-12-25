import java.io.IOException;
import java.util.Scanner;

/**
 * A game client class that hosts a game via a command line interface
 */
public class GameClient
{
    private static final Scanner sc = new Scanner(System.in);

    private static boolean interactive = true;
    public static void main(String[] args) throws IOException
    {
        if (args.length > 0)
        {
            interactive = Boolean.parseBoolean(args[0]);
        }
        new GameClient().run();
    }

    public void run() throws IOException {
        while (true)
        {
            displayWelcomeMessage();
            Game game;
            switch(sc.nextLine().toLowerCase())
            {
                case "1":
                    game = Game.createSinglePlayerGame(readDifficultyLevel(sc));
                    break;
                case "2":
                    game = Game.createTwoPlayerGame();
                    break;
                case "3":
                    game = Game.createAIGame(readDifficultyLevel(sc));
                    break;
                case "q":
                case "quit":
                case "exit":
                    return;
                default:
                    System.out.println("Invalid command");
                    continue;
            }
            game.start();
        }
    }

    // Prompts the user for the difficulty level of the computer(s) and reads the incoming value, handling any errors
    private int readDifficultyLevel(Scanner sc)
    {
        while (true)
        {
            System.out.println("Please enter the difficulty level of the computer:");
            int level;
            try
            {
                level = Integer.parseInt(sc.nextLine());
                if (level <= 0)
                {
                    System.out.println("Invalid value. Must be a positive integer.");
                    continue;
                }
            }
            catch(Exception e)
            {
                System.out.println("Invalid value. Must be a positive integer.");
                continue;
            }
            return level;
        }
    }

    private void displayWelcomeMessage()
    {
        clearScreen();
        System.out.println();
        System.out.println("  Welcome to Mancala!");
        System.out.println("----------------------------");
        System.out.println("  Choose an option to begin:");
        System.out.println("  1: One-Player Game");
        System.out.println("  2: Two-Player Game");
        System.out.println("  3: AI Game");
        System.out.println("  Q: Quit");
        System.out.println("----------------------------");
        System.out.flush();
    }

    public static void promptUserForNextMove(Player activePlayer)
    {
        if (interactive && activePlayer instanceof AI)
        {
            GameClient.pause();
        }
        if (activePlayer instanceof Human)
        {
            System.out.println("Enter a slot number or type 'q' to quit");
        }
    }

    /**
     * Clears the game screen
     */
    public static void clearScreen()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Pauses the game until the user presses the 'enter' key
     */
    public static void pause()
    {
        System.out.println("Press ENTER to continue...");
        System.out.flush();
        sc.nextLine();
    }
}