package puzzles.tipover.model;

import puzzles.common.Coordinates;
import puzzles.common.solver.Configuration;
import java.util.*;

/**
 * Configuration class for the game, Tip Over
 *
 * @author Ricky Leung
 */
public class TipOverConfig implements Configuration {
    /** Coordinates of the starting position of the initial configuration */
    private final Coordinates INITIAL;
    /** Coordinates of the final position you're trying to go to */
    private final Coordinates GOAL;
    /** 2d array representing the height at each coordinate */
    private int[][] board;
    /** Coordinate of your current location */
    private Coordinates currentLocation;
    /** The height from your current location */
    private int currentHeight;

    /**
     * Constructor for the configuration of the game, Tip Over
     *
     * @param INITIAL The initial location
     * @param GOAL The final location
     * @param board 2d array of ints representing the height at each location
     * @param currentLocation Your current location on the board
     */
    public TipOverConfig(Coordinates INITIAL, Coordinates GOAL, int[][] board, Coordinates currentLocation) {
        this.INITIAL = INITIAL;
        this.GOAL = GOAL;
        this.board = board;
        this.currentLocation = currentLocation;
        this.currentHeight = board[currentLocation.row()][currentLocation.col()];
    }

    /**
     * Checks the current location of the configuration and sees if
     * it is the same as the goal by checking if row and column of both
     * coordinates are the same
     *
     * @return True if current location is at the goal
     */
    @Override
    public boolean isSolution() {
        return currentLocation.row() == GOAL.row() &&
                currentLocation.col() == GOAL.col();
    }

    /**
     * Gets the neighbors of the current configuration by checking if:
     *      - there is a valid crate/tower(height > 0) to move to in each cardinal direction
     *      - if no valid crate/tower in each cardinal direction, check if you can tip over:
     *              - currentHeight > 1
     *              - enough space in each cardinal direction
     *              - all spaces are 0
     *
     * @return ArrayList of neighbors of the current configuration
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> result = new ArrayList<>();
        // Current row
        int row = currentLocation.row();
        // Current col
        int col = currentLocation.col();

        // North hop
        if (row > 0 && board[row - 1][col] > 0) {
            Coordinates newLocation = new Coordinates(row - 1, col);
            result.add(new TipOverConfig(this.INITIAL, this.GOAL, board, newLocation));
        }
        // South hop
        if (row < board.length - 1 && board[row + 1][col] > 0) {
            Coordinates newLocation = new Coordinates(row + 1, col);
            result.add(new TipOverConfig(this.INITIAL, this.GOAL, board, newLocation));
        }
        // East hop
        if (col < board[0].length - 1 && board[row][col + 1] > 0) {
            Coordinates newLocation = new Coordinates(row, col + 1);
            result.add(new TipOverConfig(this.INITIAL, this.GOAL, board, newLocation));
        }
        // West hop
        if (col > 0 && board[row][col - 1] > 0) {
            Coordinates newLocation = new Coordinates(row, col - 1);
            result.add(new TipOverConfig(this.INITIAL, this.GOAL, board, newLocation));
        }

        // North tower tip over
        if (board[row][col] > 1 && row - board[row][col] >= 0) {
            // Deep copy
            int[][] newBoard = new int[board.length][];
            for (int i = 0; i < board.length; i++) {
                newBoard[i] = board[i].clone();
            }

            // Check there are no crates
            boolean clear = true;
            for (int i = 1; i <= board[row][col]; i++) {
                if (board[row - i][col] != 0) {
                    clear = false;
                    break;
                }
                newBoard[row - i][col] = 1;
            }

            if (clear) {
                // Update original location to 0 since tower was tipped
                newBoard[row][col] = 0;

                Coordinates newLocation = new Coordinates(row - 1, col);
                result.add(new TipOverConfig(this.INITIAL, this.GOAL, newBoard, newLocation));
            }
        }

        // South tower tip over
        if (board[row][col] > 1 && row + board[row][col] < board.length) {
            // Deep copy
            int[][] newBoard = new int[board.length][];
            for (int i = 0; i < board.length; i++) {
                newBoard[i] = board[i].clone();
            }

            // Check there are no crates
            boolean clear = true;
            for (int i = 1; i <= board[row][col]; i++) {
                if (board[row + i][col] != 0) {
                    clear = false;
                    break;
                }
                newBoard[row + i][col] = 1;
            }

            if (clear) {
                // Update original location to 0 since tower was tipped
                newBoard[row][col] = 0;

                Coordinates newLocation = new Coordinates(row + 1, col);
                result.add(new TipOverConfig(this.INITIAL, this.GOAL, newBoard, newLocation));
            }
        }

        // East tower tip over
        if (board[row][col] > 1 && col + board[row][col] < board[0].length) {
            // Deep copy
            int[][] newBoard = new int[board.length][];
            for (int i = 0; i < board.length; i++) {
                newBoard[i] = board[i].clone();
            }

            // Check there are no crates
            boolean clear = true;
            for (int i = 1; i <= board[row][col]; i++) {
                if (board[row][col + i] != 0) {
                    clear = false;
                    break;
                }
                newBoard[row][col + i] = 1;
            }

            if (clear) {
                // Update original location to 0 since tower was tipped
                newBoard[row][col] = 0;

                Coordinates newLocation = new Coordinates(row, col + 1);
                result.add(new TipOverConfig(this.INITIAL, this.GOAL, newBoard, newLocation));
            }
        }

        // West tower tip over
        if (board[row][col] > 1 && col - board[row][col] >= 0) {
            // Deep copy
            int[][] newBoard = new int[board.length][];
            for (int i = 0; i < board.length; i++) {
                newBoard[i] = board[i].clone();
            }

            // Check there are no crates
            boolean clear = true;
            for (int i = 1; i <= board[row][col]; i++) {
                if (board[row][col - i] != 0) {
                    clear = false;
                    break;
                }
                newBoard[row][col - i] = 1;
            }

            if (clear) {
                // Update original location to 0 since tower was tipped
                newBoard[row][col] = 0;

                Coordinates newLocation = new Coordinates(row, col - 1);
                result.add(new TipOverConfig(this.INITIAL, this.GOAL, newBoard, newLocation));
            }
        }

        return result;
    }

    /**
     * Method to try and move in a certain direction(hop or tip over), and
     * update the location and board of this configuration to the new board
     * and location
     *
     * @param direction String of "N", "E", "S", or "W"
     * @return String representing what occurs when you try to move in
     * the specified direction
     */
    public String move(String direction) {
        // Returned message
        String msg = "";
        // Current row
        int row = currentLocation.row();
        // Current column
        int col = currentLocation.col();

        // North move
        if (direction.equals("N")) {
            // Check if current location is at edge of board
            if (row > 0) {
                Coordinates newLocation = new Coordinates(row - 1, col);
                if (board[row - 1][col] != 0) {
                    currentLocation = newLocation;
                    currentHeight = board[currentLocation.row()][currentLocation.col()];
                    if (isSolution()) {
                        msg = "I WON!";
                    }
                } else if (board[row][col] > 1 && validNorthTip()) {
                    performNorthTip();
                    currentLocation = newLocation;
                    currentHeight = 1;
                    if (isSolution()) {
                        msg = "I WON!";
                    } else {
                        msg = "A tower has been tipped over.";
                    }
                } else {
                    if (board[row][col] > 1 && !validNorthTip()) {
                        msg = "Tower cannot be tipped over.";
                    } else {
                        msg = "No crate or tower there.";
                    }
                }
            } else {
                msg = "Move goes off the board.";
            }
        // South move
        } else if (direction.equals("S")) {
            // Check if current location is at edge of board
            if (row < board.length - 1) {
                Coordinates newLocation = new Coordinates(row + 1, col);
                if (board[row + 1][col] != 0) {
                    currentLocation = newLocation;
                    currentHeight = board[currentLocation.row()][currentLocation.col()];
                    if (isSolution()) {
                        msg = "I WON!";
                    }
                } else if (board[row][col] > 1 && validSouthTip()) {
                    performSouthTip();
                    currentLocation = newLocation;
                    currentHeight = 1;
                    if (isSolution()) {
                        msg = "I WON!";
                    } else {
                        msg = "A tower has been tipped over.";
                    }
                } else {
                    if (board[row][col] > 1 && !validSouthTip()) {
                        msg = "Tower cannot be tipped over.";
                    } else {
                        msg = "No crate or tower there.";
                    }
                }
            } else {
                msg = "Move goes off the board.";
            }
        // East move
        } else if (direction.equals("E")) {
            // Check if current location is at edge of board
            if (col < board[0].length - 1) {
                Coordinates newLocation = new Coordinates(row, col + 1);
                if (board[row][col + 1] != 0) {
                    currentLocation = newLocation;
                    currentHeight = board[currentLocation.row()][currentLocation.col()];
                    if (isSolution()) {
                        msg = "I WON!";
                    }
                } else if (board[row][col] > 1 && validEastTip()) {
                    performEastTip();
                    currentLocation = newLocation;
                    currentHeight = 1;
                    if (isSolution()) {
                        msg = "I WON!";
                    } else {
                        msg = "A tower has been tipped over.";
                    }
                } else {
                    if (board[row][col] > 1 && !validEastTip()) {
                        msg = "Tower cannot be tipped over.";
                    } else {
                        msg = "No crate or tower there.";
                    }
                }
            } else {
                msg = "Move goes off the board.";
            }
        // West move
        } else {
            // Check if current location is at edge of board
            if (col > 0) {
                Coordinates newLocation = new Coordinates(row, col - 1);
                if (board[row][col - 1] != 0) {
                    currentLocation = newLocation;
                    currentHeight = board[currentLocation.row()][currentLocation.col()];
                    if (isSolution()) {
                        msg = "I WON!";
                    }
                } else if (board[row][col] > 1 && validWestTip()) {
                    performWestTip();
                    currentLocation = newLocation;
                    currentHeight = 1;
                    if (isSolution()) {
                        msg = "I WON!";
                    } else {
                        msg = "A tower has been tipped over.";
                    }
                } else {
                    if (board[row][col] > 1 && !validWestTip()) {
                        msg = "Tower cannot be tipped over.";
                    } else {
                        msg = "No crate or tower there.";
                    }
                }
            } else {
                msg = "Move goes off the board";
            }
        }

        return msg;
    }

    /**
     * Checks if you can perform a north tip over by checking:
     *      - for n = height of current location:
     *          - if there are n spaces north with all height 0
     *
     * @return True if there is space north
     */
    public boolean validNorthTip() {
        int row = currentLocation.row();
        int col = currentLocation.col();
        for (int i = 1; i <= board[row][col]; i++) {
            if (row - i < 0 || board[row - i][col] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Performs the north tip over by:
     *      - for n = height of current location:
     *          - iterate n spaces upward and set each height to 1
     *          - update height of current location to 0
     */
    public void performNorthTip() {
        int row = currentLocation.row();
        int col = currentLocation.col();
        int height = board[row][col];
        board[row][col] = 0;
        for (int i = 1; i <= height; i++) {
            board[row - i][col] = 1;
        }
    }

    /**
     * Checks if you can perform a south tip over by checking:
     *      - for n = height of current location:
     *          - if there are n spaces south with all height 0
     *
     * @return True if there is space south
     */
    public boolean validSouthTip() {
        int row = currentLocation.row();
        int col = currentLocation.col();
        for (int i = 1; i <= board[row][col]; i++) {
            if (row + i >= board.length || board[row + i][col] != 0) {
                return false;
            }
        }
        return true;
    }
    /**
     * Performs the south tip over by:
     *      - for n = height of current location:
     *          - iterate n spaces downward and set each height to 1
     *          - update height of current location to 0
     */
    public void performSouthTip() {
        int row = currentLocation.row();
        int col = currentLocation.col();
        int height = board[row][col];
        board[row][col] = 0;
        for (int i = 1; i <= height; i++) {
            board[row + i][col] = 1;
        }
    }

    /**
     * Checks if you can perform an east tip over by checking:
     *      - for n = height of current location:
     *          - if there are n spaces east with all height 0
     *
     * @return True if there is space east
     */
    public boolean validEastTip() {
        int row = currentLocation.row();
        int col = currentLocation.col();
        for (int i = 1; i <= board[row][col]; i++) {
            if (col + i >= board[0].length || board[row][col + i] != 0) {
                return false;
            }
        }
        return true;
    }
    /**
     * Performs the east tip over by:
     *      - for n = height of current location:
     *          - iterate n spaces rightward and set each height to 1
     *          - update height of current location to 0
     */
    public void performEastTip() {
        int row = currentLocation.row();
        int col = currentLocation.col();
        int height = board[row][col];
        board[row][col] = 0;
        for (int i = 1; i <= height; i++) {
            board[row][col + i] = 1;
        }
    }

    /**
     * Checks if you can perform a west tip over by checking:
     *      - for n = height of current location:
     *          - if there are n spaces west with all height 0
     *
     * @return True if there is space west
     */
    public boolean validWestTip() {
        int row = currentLocation.row();
        int col = currentLocation.col();
        for (int i = 1; i <= board[row][col]; i++) {
            if (col - i < 0 || board[row][col - i] != 0) {
                return false;
            }
        }
        return true;
    }
    /**
     * Performs the west tip over by:
     *      - for n = height of current location:
     *          - iterate n spaces leftward and set each height to 1
     *          - update height of current location to 0
     */
    public void performWestTip() {
        int row = currentLocation.row();
        int col = currentLocation.col();
        int height = board[row][col];
        board[row][col] = 0;
        for (int i = 1; i <= height; i++) {
            board[row][col - i] = 1;
        }
    }

    /**
     * Getter method for the current board
     *
     * @return 2d array of heights
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Getter method for the number of rows on the board
     *
     * @return int of rows on board
     */
    public int getRows() {
        return board.length;
    }

    /**
     * Getter method for the number of columns on the board
     *
     * @return int of columns on board
     */
    public int getCols() {
        return board[0].length;
    }

    /**
     * Getter method for the coordinates of the goal
     *
     * @return COORDINATES of the goal
     */
    public Coordinates getGOAL() {
        return this.GOAL;
    }

    /**
     * Getter method for the coordinates of the current location
     *
     * @return COORDINATES of the current location
     */
    public Coordinates getCurrentLocation() {
        return currentLocation;
    }

    /**
     * Overriden equals method that is used when trying to generate
     * the best path with Solver class; Checks if every attribute is the same
     *
     * @param other TipOverConfig that is being compared to
     * @return boolean representing if this and other TipOverConfig is the same
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof TipOverConfig) {
            TipOverConfig otherConfig = (TipOverConfig) other;
            return this.INITIAL.equals(otherConfig.INITIAL) &&
                    this.GOAL.equals(otherConfig.GOAL) &&
                    Arrays.deepEquals(this.board, otherConfig.board) &&
                    this.currentLocation.equals(otherConfig.currentLocation) &&
                    this.currentHeight == otherConfig.currentHeight;
        }
        return false;
    }

    /**
     * Overriden hashCode method that is used when trying to generate the
     * best path with Solver class; Hash codes every attribute of this class
     * and adds them
     *
     * @return int of the hash code of this class
     */
    @Override
    public int hashCode() {
        return this.INITIAL.hashCode() +
                this.GOAL.hashCode() +
                Arrays.deepHashCode(this.board) +
                this.currentLocation.hashCode() +
                Objects.hashCode(this.currentHeight);
    }

    /**
     * Overriden toString method to display every value within the board
     *
     * @return String representing the entire board
     */
    @Override
    public String toString() {
        String result = "    ";
        for (int i = 0; i < board[0].length; i++) {
            result += "  " + i;
        }
        String temp = "___";
        result += "\n    " + temp.repeat(board[0].length);

        for (int row = 0; row < board.length; row++) {
            result += "\n " + row + " |";
            for (int col = 0; col < board[0].length; col++) {
                result += " ";
                if (currentLocation.row() == row && currentLocation.col() == col) {
                    result += "*";
                } else if (GOAL.row() == row && GOAL.col() == col) {
                    result += "!";
                } else {
                    result += " ";
                }
                if (board[row][col] == 0) {
                    result += "_";
                } else {
                    result += board[row][col];
                }
            }
        }
        result += "\n";

        return result;
    }
}
