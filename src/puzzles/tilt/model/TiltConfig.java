package puzzles.tilt.model;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Tilt configuration class that implements the Configuration
 * interface and is used to solve the Tilt puzzles.
 *
 * @author Boya Li
 */
public class TiltConfig implements Configuration {
    /** The size of the board.*/
    private int size;
    /** The number of green sliders left for that specific board.*/
    private int greenSliderNum;
    /** The number of green sliders left for the GUI board.*/
    private int greenSliderNumGUI;
    /** The board of the current configuration.*/
    private String[][] board;
    /** The board of the GUI.*/
    private String[][] boardGUI;

    /**
     * Constructor for the Tilt Configuration class.
     *
     * @param size The size of the board.
     * @param greenSliderNum The number of green sliders left.
     * @param board The board of the configuration.
     */
    public TiltConfig(int size, int greenSliderNum, String[][] board) {
        this.size = size;
        this.greenSliderNum = greenSliderNum;
        this.greenSliderNumGUI = greenSliderNum;
        this.board = board;
        this.boardGUI = board;
    }

    /**
     * Get method for the size of the baord.
     *
     * @return An int representing the size of the board.
     */
    public int getSize() { return size; }

    /**
     * Get method for the board of the current config.
     *
     * @return A 2D String array representing the board.
     */
    public String[][] getBoard() { return board; }

    /**
     * Get method for the board of the GUI.
     *
     * @return A 2D String array representing the board of the GUI.
     */
    public String[][] getBoardGUI() { return boardGUI; }

    /**
     * Get method for the value in the specified row and column.
     *
     * @param row The row of the value.
     * @param col The column of the value.
     * @return The string, or value in the specified row and col.
     */
    public String getValue(int row, int col) {
        return board[row][col];
    }

    /**
     * Checks to see if the current configuration is the solution by
     * seeing if the number of green sliders has reached zero.
     *
     * @return A boolean representing if the current configuration is
     * the solution or not.
     */
    @Override
    public boolean isSolution() {
        return greenSliderNum == 0;
    }

    /**
     * Gets the four neighbors of the current configuration by creating
     * new instances of TiltConfig of the four different directions that
     * the board, or sliders can be tilted to.
     *
     * @return Collection of neighbor configurations.
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> result = new ArrayList<>();
        ArrayList<int[]> queue1 = new ArrayList<>();
        ArrayList<int[]> queue2 = new ArrayList<>();
        ArrayList<int[]> queue3 = new ArrayList<>();
        ArrayList<int[]> queue4 = new ArrayList<>();
        String[][] board1 = new String[size][size];
        String[][] board2 = new String[size][size];
        String[][] board3 = new String[size][size];
        String[][] board4 = new String[size][size];
        int greenSliderNum1 = greenSliderNum;
        int greenSliderNum2 = greenSliderNum;
        int greenSliderNum3 = greenSliderNum;
        int greenSliderNum4 = greenSliderNum;
        boolean blueFallsThrough1 = false;
        boolean blueFallsThrough2 = false;
        boolean blueFallsThrough3 = false;
        boolean blueFallsThrough4 = false;

        for(int row = 0; row < size; row++) {
            board1[row] = board[row].clone();
        }
        for(int row = 0; row < size; row++) {
            board2[row] = board[row].clone();
        }
        for(int row = 0; row < size; row++) {
            board3[row] = board[row].clone();
        }
        for(int row = 0; row < size; row++) {
            board4[row] = board[row].clone();
        }

        //North
        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                if(board[row][col].equals("B") || board[row][col].equals("G")) {
                    int[] arr = new int[]{row, col};
                    queue1.add(arr);
                }
            }
        }
        for(int[] curr : queue1) {
            String value = board1[curr[0]][curr[1]];
            int row = curr[0];
            int col = curr[1];
            boolean greenFallsThrough = false;
            if(row != 0) {
                while(row > -1) {
                    if(row == 0) {
                        break;
                    }
                    if(board1[row - 1][col].equals("O")) {
                        if(value.equals("G")) {
                            greenSliderNum1--;
                            board1[row][col] = ".";
                            greenFallsThrough = true;
                        }
                        else {
                            blueFallsThrough1 = true;
                        }
                    }
                    else if(board1[row - 1][col].equals(".") && !greenFallsThrough) {
                        board1[row - 1][col] = value;
                        board1[row][col] = ".";
                    }
                    else {
                        break;
                    }
                    row--;
                }
            }
        }
        if(!blueFallsThrough1) {
            result.add(new TiltConfig(size, greenSliderNum1, board1));
        }

        //East
        for(int col = size - 1; col > -1; col--) {
            for(int row = size - 1; row > -1; row--) {
                if(board[row][col].equals("B") || board[row][col].equals("G")) {
                    int[] arr = new int[]{row, col};
                    queue2.add(arr);
                }
            }
        }
        for(int[] curr : queue2) {
            String value = board2[curr[0]][curr[1]];
            int row = curr[0];
            int col = curr[1];
            boolean greenFallsThrough = false;
            if(col != size - 1) {
                while(col < size - 1) {
                    if(col == size - 1) {
                        break;
                    }
                    if(board2[row][col + 1].equals("O")) {
                        if(value.equals("G")) {
                            greenSliderNum2--;
                            board2[row][col] = ".";
                            greenFallsThrough = true;
                        }
                        else {
                            blueFallsThrough2 = true;
                        }
                    }
                    else if(board2[row][col + 1].equals(".") && !greenFallsThrough) {
                        board2[row][col + 1] = value;
                        board2[row][col] = ".";
                    }
                    else {
                        break;
                    }
                    col++;
                }
            }
        }
        if(!blueFallsThrough2) {
            result.add(new TiltConfig(size, greenSliderNum2, board2));
        }

        //South
        for(int row = size - 1; row > -1; row--) {
            for(int col = size - 1; col > -1; col--) {
                if(board[row][col].equals("B") || board[row][col].equals("G")) {
                    int[] arr = new int[]{row, col};
                    queue3.add(arr);
                }
            }
        }
        for(int[] curr : queue3) {
            String value = board3[curr[0]][curr[1]];
            int row = curr[0];
            int col = curr[1];
            boolean greenFallsThrough = false;
            if(row != size - 1) {
                while(row < size - 1) {
                    if(board3[row + 1][col].equals("O")) {
                        if(value.equals("G")) {
                            greenSliderNum3--;
                            board3[row][col] = ".";
                            greenFallsThrough = true;
                        }
                        else {
                            blueFallsThrough3 = true;
                        }

                    }
                    else if(board3[row + 1][col].equals(".") && !greenFallsThrough) {
                        board3[row + 1][col] = value;
                        board3[row][col] = ".";
                    }
                    else {
                        break;
                    }
                    row++;
                }
            }
        }
        if(!blueFallsThrough3) {
            result.add(new TiltConfig(size, greenSliderNum3, board3));
        }

        //West
        for(int col = 0; col < size; col++) {
            for(int row = 0; row < size; row++) {
                if(board[row][col].equals("B") || board[row][col].equals("G")) {
                    int[] arr = new int[]{row, col};
                    queue4.add(arr);
                }
            }
        }
        for(int[] curr : queue4) {
            String value = board4[curr[0]][curr[1]];
            int row = curr[0];
            int col = curr[1];
            boolean greenFallsThrough = false;
            if(col != 0) {
                while(col > -1) {
                    if(col == 0) {
                        break;
                    }
                    if(board4[row][col - 1].equals("O")) {
                        if(value.equals("G")) {
                            greenSliderNum4--;
                            board4[row][col] = ".";
                            greenFallsThrough = true;
                        }
                        else {
                            blueFallsThrough4 = true;
                        }
                    }
                    else if(board4[row][col - 1].equals(".") && !greenFallsThrough) {
                        board4[row][col - 1] = value;
                        board4[row][col] = ".";
                    }
                    else {
                        break;
                    }
                    col--;
                }
            }
        }
        if(!blueFallsThrough4) {
            result.add(new TiltConfig(size, greenSliderNum4, board4));
        }

        return result;
    }

    /**
     * Tilts the current GUI's board in the specific direction specified
     * by the user. If a move is invalid, if a blue slider would fall
     * through the hole, nothing would be changed in the GUI board and
     * the respective message would be returned. Otherwise, if a move is
     * valid, it will return the respective message, telling the user
     * that it was. Finally, if the move is valid and gets the solution,
     * the respective message congratulating the user would be returned.
     *
     * @param direction The direction the board of the GUI would be tilted
     *                  in.
     * @return A String representing the respective message of what was
     * done or not.
     */
    public String tilt(String direction) {
        String msg = "";
        ArrayList<int[]> queue1 = new ArrayList<>();
        ArrayList<int[]> queue2 = new ArrayList<>();
        ArrayList<int[]> queue3 = new ArrayList<>();
        ArrayList<int[]> queue4 = new ArrayList<>();
        String[][] original = new String[size][size];
        greenSliderNumGUI = 0;
        boolean blueFallsThrough1 = false;
        boolean blueFallsThrough2 = false;
        boolean blueFallsThrough3 = false;
        boolean blueFallsThrough4 = false;

        for(int row = 0; row < size; row++) {
            boardGUI[row] = board[row].clone();
            for(int col = 0; col < size; col++) {
                if(boardGUI[row][col].equals("G")) {
                    greenSliderNumGUI++;
                }
            }
        }
        for(int row = 0; row < size; row++) {
            original[row] = boardGUI[row].clone();
        }

        if(direction.equals("N")) {
            for(int row = 0; row < size; row++) {
                for(int col = 0; col < size; col++) {
                    if(board[row][col].equals("B") || board[row][col].equals("G")) {
                        int[] arr = new int[]{row, col};
                        queue1.add(arr);
                    }
                }
            }
            for(int[] curr : queue1) {
                String value = boardGUI[curr[0]][curr[1]];
                int row = curr[0];
                int col = curr[1];
                boolean greenFallsThrough = false;
                if(row != 0) {
                    while(row > -1) {
                        if(row == 0) {
                            break;
                        }
                        if(boardGUI[row - 1][col].equals("O")) {
                            if(value.equals("G")) {
                                greenSliderNumGUI--;
                                boardGUI[row][col] = ".";
                                greenFallsThrough = true;
                            }
                            else {
                                blueFallsThrough1 = true;
                            }
                        }
                        else if(boardGUI[row - 1][col].equals(".") && !greenFallsThrough) {
                            boardGUI[row - 1][col] = value;
                            boardGUI[row][col] = ".";
                        }
                        else {
                            break;
                        }
                        row--;
                    }
                }
            }
            if(!blueFallsThrough1) {
                msg = "Tilted North";
                if(greenSliderNumGUI == 0) {
                    msg = "You win, congratulations!";
                }

            }
            else {
                msg = "Illegal move. A blue slider will fall through the hole!";
                for(int row = 0; row < size; row++) {
                    boardGUI[row] = original[row].clone();
                }
            }
        }
        else if(direction.equals("E")) {
            for(int col = size - 1; col > -1; col--) {
                for(int row = size - 1; row > -1; row--) {
                    if(board[row][col].equals("B") || board[row][col].equals("G")) {
                        int[] arr = new int[]{row, col};
                        queue2.add(arr);
                    }
                }
            }
            for(int[] curr : queue2) {
                String value = boardGUI[curr[0]][curr[1]];
                int row = curr[0];
                int col = curr[1];
                boolean greenFallsThrough = false;
                if(col != size - 1) {
                    while(col < size - 1) {
                        if(col == size - 1) {
                            break;
                        }
                        if(boardGUI[row][col + 1].equals("O")) {
                            if(value.equals("G")) {
                                greenSliderNumGUI--;
                                boardGUI[row][col] = ".";
                                greenFallsThrough = true;
                            }
                            else {
                                blueFallsThrough2 = true;
                            }
                        }
                        else if(boardGUI[row][col + 1].equals(".") && !greenFallsThrough) {
                            boardGUI[row][col + 1] = value;
                            boardGUI[row][col] = ".";
                        }
                        else {
                            break;
                        }
                        col++;
                    }
                }
            }
            if(!blueFallsThrough2) {
                msg = "Tilted East";
                if(greenSliderNumGUI == 0) {
                    msg = "You win, congratulations!";
                }
            }
            else {
                msg = "Illegal move. A blue slider will fall through the hole!";
                for(int row = 0; row < size; row++) {
                    boardGUI[row] = original[row].clone();
                }
            }
        }
        else if(direction.equals("S")) {
            for(int row = size - 1; row > -1; row--) {
                for(int col = size - 1; col > -1; col--) {
                    if(board[row][col].equals("B") || board[row][col].equals("G")) {
                        int[] arr = new int[]{row, col};
                        queue3.add(arr);
                    }
                }
            }
            for(int[] curr : queue3) {
                String value = boardGUI[curr[0]][curr[1]];
                int row = curr[0];
                int col = curr[1];
                boolean greenFallsThrough = false;
                if(row != size - 1) {
                    while(row < size - 1) {
                        if(boardGUI[row + 1][col].equals("O")) {
                            if(value.equals("G")) {
                                greenSliderNumGUI--;
                                boardGUI[row][col] = ".";
                                greenFallsThrough = true;
                            }
                            else {
                                blueFallsThrough3 = true;
                            }

                        }
                        else if(boardGUI[row + 1][col].equals(".") && !greenFallsThrough) {
                            boardGUI[row + 1][col] = value;
                            boardGUI[row][col] = ".";
                        }
                        else {
                            break;
                        }
                        row++;
                    }
                }
            }
            if(!blueFallsThrough3) {
                msg = "Tilted South";
                if(greenSliderNumGUI == 0) {
                    msg = "You win, congratulations!";
                }
            }
            else {
                msg = "Illegal move. A blue slider will fall through the hole!";
                for(int row = 0; row < size; row++) {
                    boardGUI[row] = original[row].clone();
                }
            }
        }
        else {
            for(int col = 0; col < size; col++) {
                for(int row = 0; row < size; row++) {
                    if(board[row][col].equals("B") || board[row][col].equals("G")) {
                        int[] arr = new int[]{row, col};
                        queue4.add(arr);
                    }
                }
            }
            for(int[] curr : queue4) {
                String value = boardGUI[curr[0]][curr[1]];
                int row = curr[0];
                int col = curr[1];
                boolean greenFallsThrough = false;
                if(col != 0) {
                    while(col > -1) {
                        if(col == 0) {
                            break;
                        }
                        if(boardGUI[row][col - 1].equals("O")) {
                            if(value.equals("G")) {
                                greenSliderNumGUI--;
                                boardGUI[row][col] = ".";
                                greenFallsThrough = true;
                            }
                            else {
                                blueFallsThrough4 = true;
                            }
                        }
                        else if(boardGUI[row][col - 1].equals(".") && !greenFallsThrough) {
                            boardGUI[row][col - 1] = value;
                            boardGUI[row][col] = ".";
                        }
                        else {
                            break;
                        }
                        col--;
                    }
                }
            }
            if(!blueFallsThrough4) {
                msg = "Tiled West";
                if(greenSliderNumGUI == 0) {
                    msg = "You win, congratulations!";
                }
            }
            else {
                msg = "Illegal move. A blue slider will fall through the hole!";
                for(int row = 0; row < size; row++) {
                    boardGUI[row] = original[row].clone();
                }
            }
        }
        return msg;
    }

    /**
     * Checks if the current configuration is equal to the other
     * configuration by comparing their boards.
     *
     * @param other Other configuration we are using to compare.
     * @return A boolean representing if both configurations are equal.
     */
    @Override
    public boolean equals(Object other) {
        TiltConfig tiltConfig = (TiltConfig) other;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (!this.board[row][col].equals(tiltConfig.board[row][col])) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Hashes the current configuration by using its size, number of
     * green sliders, and board.
     *
     * @return An int representing the hash code of this configuration.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(size) + Objects.hashCode(greenSliderNum) + Arrays.deepHashCode(board);
    }

    /**
     * Tilt Config's toString method.
     *
     * @return A string representation of the puzzle.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                result.append(board[row][col]).append(" ");
            }
            if (row != size - 1) {
                result.append(System.lineSeparator());
            }
        }
        return result.toString();
    }
}
