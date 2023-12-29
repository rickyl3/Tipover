package puzzles.tilt.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * The model for the Tilt puzzle.
 *
 * @author Boya Li
 */
public class TiltModel {
    /** The collection of observers of this model.*/
    private final List<Observer<TiltModel, String>> observers = new LinkedList<>();
    /** The current configuration.*/
    private TiltConfig currentConfig;
    /** The current file.*/
    private String file;

    /**
     * Constructor for the TiltModel.
     */
    public TiltModel() { }

    /**
     * Get method for the current configuration.
     *
     * @return A TiltConfig representing the current configuration.
     */
    public TiltConfig getConfig() { return currentConfig; }

    /**
     * Get method for the puzzle's hint. It would check whether the
     * puzzle has already been solved. If it is, then it'll alert the
     * observers so and return the current configuration. Otherwise, it
     * will call the solver method and check whether the current
     * configuration is solvable or not. If it is, then it'll return the
     * next configuration for the solution and alert the observers so.
     * If it is not, then it'll return null and alert the observers that
     * there is no solution.
     *
     * @return A TiltConfig representing the hint, next step the user
     * should take.
     */
    public TiltConfig getHint() {
        if(currentConfig.isSolution()) {
            alertObservers("Current board is already solved.");
            return currentConfig;
        }

        Solver solver = new Solver();
        List<Configuration> result = (List<Configuration>) solver.solve(currentConfig);
        if (result == null) {
            alertObservers("No solution.");
            return null;
        }

        TiltConfig updatedConfig = (TiltConfig) result.get(1);
        currentConfig = updatedConfig;
        if(currentConfig.isSolution()) {
            alertObservers("You win, congratulations!");
        }
        else {
            alertObservers("Next step!");
        }
        return currentConfig;
    }

    /**
     * Loads the board from the specified file name inputted from the user.
     * @param command The command from the user.
     * @return A boolean representing if the file loaded successfully or not.
     */
    public boolean load(String command) {
        String[] args = command.split(" ");
        if(args.length == 2) {
            String file = args[1];
            return loadBoardFromFile(file);
        }
        else {
            alertObservers("Invalid command");
        }
        return false;
    }

    /**
     * It would attempt to load a board from the specified file name.
     * If it was successful, then it'll alert observers that it was
     * successful.
     *
     * @param fileName The file that would be accessed.
     * @return A boolean representing if it loaded successfully or not.
     */
    public boolean loadBoardFromFile(String fileName) {
        boolean loaded = loadBoardFromFile(new File(fileName));
        if(loaded) {
            alertObservers("Loaded " + fileName);
            return true;
        }
        return false;
    }

    /**
     * It would attempt to load a board from the specified file name
     * by reading the information from the file provided. If it was
     * successful, then it'll get the size of the board, number of
     * green sliders, board, and set the current configuration to a
     * TiltConfig with everything that was obtained.
     *
     * @param file The file that would be accessed.
     * @return A boolean representing if it loaded successfully or not.
     */
    public boolean loadBoardFromFile(File file) {
        this.file = "data/tilt/" + file.getName();
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.file));
            int size = Integer.parseInt(br.readLine());
            int greenSliderNum = 0;
            String[][] board = new String[size][size];
            for (int row = 0; row < size; row++) {
                String[] rowValues = br.readLine().split(" ");
                for (int col = 0; col < size; col++) {
                    if(rowValues[col].equals("G")) {
                        greenSliderNum++;
                    }
                    board[row][col] = rowValues[col];
                }
            }

            currentConfig = new TiltConfig(size, greenSliderNum, board);
        }
        catch (FileNotFoundException e) {
            alertObservers("Failed to load " + file.getName());
            return false;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * The method would check if the board is completed. If it is, then
     * it'll alert observers that it is already solved. If it isn't, then
     * it would check the command that was made. Based on whatever
     * direction the command is, it would call the tilt method in
     * currentConfig with the direction as the parameter and alert
     * observers with the respective message of what was done.
     *
     * @param command The move that was made.
     */
    public void tilt(String command) {
        if (currentConfig.isSolution()) {
            alertObservers("Current board is already solved.");
            return;
        }

        String msg = "";
        String[] args = command.split(" ");
        if(args.length == 1) {
            String direction = args[0];
            if(direction.equalsIgnoreCase("n")) {
                msg = currentConfig.tilt("N");
                alertObservers(msg);
            }
            else if (direction.equalsIgnoreCase("s")) {
                msg = currentConfig.tilt("S");
                alertObservers(msg);
            }
            else if (direction.equalsIgnoreCase("e")) {
                msg = currentConfig.tilt("E");
                alertObservers(msg);
            }
            else if (direction.equalsIgnoreCase("w")){
                msg = currentConfig.tilt("W");
                alertObservers(msg);
            }
            else {
                alertObservers("Invalid command");
            }
        }
        else {
            alertObservers("Invalid command");
        }
    }

    /**
     * This would reset the board to the original board.
     *
     * @return A boolean representing whether the puzzle was successfully
     * reset or not.
     */
    public boolean resetBoard() {
        boolean loaded = loadBoardFromFile(new File(this.file));
        alertObservers("Puzzle reset!");
        return loaded;
    }

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer The view.
     */
    public void addObserver(Observer<TiltModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method.
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }
}
