package puzzles.tipover.ptui;

import puzzles.common.Observer;
import puzzles.tipover.model.TipOverModel;

import java.util.Scanner;

/**
 * Class for making a text-based user interface of the game, Tip Over
 *
 * @author Ricky Leung
 */
public class TipOverPTUI implements Observer<TipOverModel, String> {
    /**`Model of the game */
    private static TipOverModel model;
    /** Scanner used to take in user input */
    private Scanner in;
    /** Boolean to represent if the game is on */
    private boolean gameOn;

    /**
     * Constructor for the text-based user interface of the game, Tip Over
     */
    public TipOverPTUI() {
        model = new TipOverModel();
        model.addObserver(this);
        this.in = new Scanner(System.in);
        gameOn = true;
    }

    /**
     * Method used to display all values from the board
     */
    public void displayBoard() {
        System.out.println(model.getConfig());
    }

    /**
     * Redisplay the values of the board that is based on the command that
     * was trying to be run
     *
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param message optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(TipOverModel model, String message) {
        if (gameOn) {
            if (message.equals("Invalid command")) {
                System.out.println("h(int)              -- hint next move\n" +
                        "l(oad) filename     -- load new puzzle file\n" +
                        "m(ove) {N|S|E|W}    -- move the tipper in the given direction\n" +
                        "q(uit)              -- quit the game\n" +
                        "r(eset)             -- reset the current game");
            } else {
                System.out.println(message + "\n");
                if (message.equals("I WON!")) {
                    gameOn = false;
                }
                displayBoard();
            }
        } else {
            System.out.println("Current board is already solved.\n");
            displayBoard();
        }
    }

    /**
     * Load board from file to model
     *
     * @param filename String representing the file name
     * @return True if successfull load
     */
    public boolean loadFromFile(String filename) {
        boolean ready = false;
        while(!ready) {
            ready = model.loadBoardFromFile(filename);
        }
        return true;
    }

    /**
     * Method ran to start the game:
     *      - Prints out all commands that the user can use
     *      - Makes gameOn true
     */
    private void gameStart() {
        System.out.println("h(int)              -- hint next move\n" +
                "l(oad) filename     -- load new puzzle file\n" +
                "m(ove) {N|S|E|W}    -- move the tipper in the given direction\n" +
                "q(uit)              -- quit the game\n" +
                "r(eset)             -- reset the current game");

        gameOn = true;
    }

    /**
     * Method used to continuously take in user input for commands until
     * the user quits out of the game:
     *      - tells the model of the game what command has been ran
     */
    private void gameLoop() {
        String msg;

        while (true) {
            System.out.print("> ");
            String command = in.nextLine().toString();
            if (command.equalsIgnoreCase("r") || command.equalsIgnoreCase("reset")) {
                gameOn = true;
                model.resetBoard();
            } else if (command.equalsIgnoreCase("q") || command.equalsIgnoreCase("quit")) {
                return;
            } else if (command.split(" ")[0].equalsIgnoreCase("m") || command.split(" ")[0].equalsIgnoreCase("move")) {
                model.move(command);
            } else if (command.split(" ")[0].equalsIgnoreCase("l") || command.split(" ")[0].equalsIgnoreCase("load")) {
                gameOn = true;
                model.load(command);
            } else if (command.equalsIgnoreCase("h") || command.equalsIgnoreCase("hint")) {
                model.getHint();
            } else {
                gameStart();
            }
        }
    }

    /**
     * Runs the Text UI for Tip Over
     *
     * @param args filename
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java TipOverPTUI filename");
        } else {
            TipOverPTUI ui = new TipOverPTUI();
            boolean ready = model.loadBoardFromFile(args[0]);
            if (ready) {
                ui.gameStart();
                ui.gameLoop();
            }
        }
    }
}
