package puzzles.tipover.model;

import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Model for the MVC pattern of the game, Tip Over; Implements the game rules of the game
 * and updates the configuration of the current game state based on calls from the
 * Controller(in this case, TipOverGUI or TipOverPTUI)
 *
 * @author Ricky Leung
 */
public class TipOverModel {
    /** the collection of observers of this model */
    private final List<Observer<TipOverModel, String>> observers = new LinkedList<>();
    /** the current configuration */
    private TipOverConfig currentConfig;
    /** Name of the file that is currently loaded */
    private String dataFile;

    /**
     * Constructor of the model
     */
    public TipOverModel() {

    }

    /**
     * Getter method for the current configuration
     *
     * @return TipOverConfig
     */
    public TipOverConfig getConfig() {
        return currentConfig;
    }

    /**
     * Method used to perform the next best move of the game, Tip Over,
     * and alerts all observers based on whether a valid hint/move can be produced
     *
     * @return TipOverConfig with the next best move already performed
     */
    public TipOverConfig getHint() {
        // Already at solution
        if (currentConfig.isSolution()) {
            alertObservers("Current board is already solved.");
            return currentConfig;
        }

        Solver solver = new Solver();
        List<Configuration> result = (List<Configuration>) solver.solve(currentConfig);
        // No solution found
        if (result == null) {
            alertObservers("No solution.");
            return null;
        }

        // Solution found
        TipOverConfig updatedConfig = (TipOverConfig) result.get(1);
        currentConfig = updatedConfig;
        if (currentConfig.isSolution()) {
            alertObservers("I WON!");
        } else {
            alertObservers("Next step!");
        }
        return currentConfig;
    }

    /**
     * Method used to load a file from the given command
     *
     * @param command The command that is ran(contains filename)
     * @return True if successful load of file
     */
    public boolean load(String command) {
        String[] args = command.split(" ");
        if (args.length == 2) {
            String file = args[1];
            return loadBoardFromFile(file);
        } else {
            alertObservers("Invalid command");
        }
        return false;
    }

    /**
     * Method used to load a file based on given file name
     *      - alerts all observers if successful load of the file
     *
     * @param fileName Name of the file
     * @return True if successful load of file
     */
    public boolean loadBoardFromFile(String fileName) {
        boolean loaded = loadBoardFromFile(new File(fileName));
        if (loaded) {
            alertObservers("Loaded: " + fileName);
            return true;
        }
        return false;
    }

    /**
     * Method used to load a file based on given File object
     *
     * @param file File object
     * @return True if successful load of file
     */
    public boolean loadBoardFromFile(File file) {
        this.dataFile = "data/tipover/" + file.getName();
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.dataFile));

            // Get info about the board
            String[] info = br.readLine().split(" ");
            int[][] board = new int[Integer.parseInt(info[0])][Integer.parseInt(info[1])];
            Coordinates initialPos = new Coordinates(Integer.parseInt(info[2]), Integer.parseInt(info[3]));
            Coordinates currentPos = new Coordinates(Integer.parseInt(info[2]), Integer.parseInt(info[3]));
            Coordinates goal = new Coordinates(Integer.parseInt(info[4]), Integer.parseInt(info[5]));
            // Populate initial board
            for (int row = 0; row < board.length; row++) {
                String[] rowValues = br.readLine().split(" ");
                for (int col = 0; col < rowValues.length; col++) {
                    board[row][col] = Integer.parseInt(rowValues[col]);
                }
            }

            currentConfig = new TipOverConfig(initialPos, goal, board, currentPos);
        } catch (FileNotFoundException e) {
            alertObservers("Failed to load: " + file.getName());
            return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Method used update the current configuration based on the
     * command that was given; The command given consists of moving
     * north, east, south, or west
     *      - alerts all observers of whether the move is valid or not
     *
     * @param command Move command that was ran by the user
     */
    public void move(String command) {
        // Already at solution
        if (currentConfig.isSolution()) {
            alertObservers("Current board is already solved.");
            return;
        }

        String msg = "";
        String[] args = command.split(" ");
        if (args.length == 2) {
            String direction = args[1];
            if (direction.equalsIgnoreCase("n")) {
                msg = currentConfig.move("N");
                alertObservers(msg);
            } else if (direction.equalsIgnoreCase("s")) {
                msg = currentConfig.move("S");
                alertObservers(msg);
            } else if (direction.equalsIgnoreCase("e")) {
                msg = currentConfig.move("E");
                alertObservers(msg);
            } else if (direction.equalsIgnoreCase("w")){
                msg = currentConfig.move("W");
                alertObservers(msg);
            } else {
                alertObservers("Invalid command");
            }
        } else {
            alertObservers("Invalid command");
        }
    }

    /**
     * Method used to reset the board by loading the initial file
     * of the current configuration
     *      - alerts all observers that the puzzle has been reset
     *
     * @return
     */
    public boolean resetBoard() {
        boolean loaded = loadBoardFromFile(new File(this.dataFile));
        alertObservers("Puzzle reset!");
        return loaded;
    }

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<TipOverModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }
}
