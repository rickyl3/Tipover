package puzzles.tilt.ptui;

import puzzles.common.Observer;
import puzzles.tilt.model.TiltModel;

import java.util.Scanner;

/**
 * A text user interface for Tilt.
 *
 * @author Boya Li
 */
public class TiltPTUI implements Observer<TiltModel, String> {
    /** The model.*/
    private static TiltModel model;
    /** Scanner for user input.*/
    private Scanner in;
    /** Boolean for whether the game is on or not.*/
    private boolean gameOn;

    /**
     * The constructor for the Tilt puzzle, which would create a new
     * model, add the observer, turn the game on, and take user input.
     */
    public TiltPTUI() {
        model = new TiltModel();
        model.addObserver(this);
        gameOn = true;
        in = new Scanner(System.in);
    }

    /**
     * Displays the current model's configuration's board.
     */
    public void displayBoard() { System.out.println(model.getConfig()); }

    /**
     * Checks whether the game is on or not and would update
     * the current board along with displaying the respective
     * message.
     *
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(TiltModel model, String message) {
        if(gameOn) {
            if(message.equals("Invalid command")) {
                System.out.println("h(int)              -- hint next move\n" +
                        "l(oad) filename     -- load new puzzle file\n" +
                        "t(ilt) {N|S|E|W}    -- tilt the board in the given direction\n" +
                        "q(uit)              -- quit the game\n" +
                        "r(eset)             -- reset the current game");
            }
            else {
                System.out.println(message + "\n");
                if(message.equals("You win, congratulations!")) {
                    gameOn = false;
                }
                displayBoard();
            }
        }
        else {
            System.out.println("Current board is already solved.\n");
            displayBoard();
        }
    }

    /**
     * Attempts to load the file that the user chooses.
     *
     * @param fileName The file name that would be accessed.
     * @return A boolean representing if the game was loaded
     * successfully.
     */
    public boolean loadFromFile(String fileName) {
        boolean ready = false;
        while(!ready) {
            ready = model.loadBoardFromFile(fileName);
        }
        return true;
    }

    /**
     * Loads a new Tilt game.
     */
    public void gameStart() {
       System.out.println("h(int)              -- hint next move\n" +
               "l(oad) filename     -- load new puzzle file\n" +
               "t(ilt) {N|S|E|W}    -- tilt the board in the given direction\n" +
               "q(uit)              -- quit the game\n" +
               "r(eset)             -- reset the current game");
       gameOn = true;
    }

    /**
     * While the game is on, it would handle the actual gameplay.
     * It would check for the user's input and whether the game
     * is over or not.
     */
    private void gameLoop() {
        while(true) {
            System.out.println("Enter N E S W to tile the board in the specific direction," +
                    " (H)int, (R)eset, or (Q)uit to main menu.");
            String command = in.nextLine();
            if(command.equalsIgnoreCase("q") || command.equalsIgnoreCase("quit")) {
                System.out.println("Quitting.");
                return;
            }
            else if(command.split(" ")[0].equalsIgnoreCase("l") || command.split(" ")[0].equalsIgnoreCase("load")) {
                gameOn = true;
                model.load(command);
            }
            else if(command.equalsIgnoreCase("h") || command.equalsIgnoreCase("hint")) {
                model.getHint();
            }
            else if(command.equalsIgnoreCase("r") || command.equalsIgnoreCase("reset")) {
                model.resetBoard();
            }
            else if(command.equalsIgnoreCase("n") ||
                    command.equalsIgnoreCase("e") ||
                    command.equalsIgnoreCase("s") ||
                    command.equalsIgnoreCase("w")){
                model.tilt(command);
            }
            else {
                gameStart();
            }
        }
    }

    /**
     * It would run the Text UI for Tilt.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TiltPTUI filename");
        }
        else {
            TiltPTUI ui = new TiltPTUI();
            boolean ready = model.loadBoardFromFile(args[0]);
            if(ready) {
                ui.gameStart();
                ui.gameLoop();
            }
        }
    }
}
