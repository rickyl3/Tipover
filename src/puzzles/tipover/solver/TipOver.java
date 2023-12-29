package puzzles.tipover.solver;

import puzzles.common.Coordinates;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tipover.model.TipOverConfig;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

/**
 * Solver class for the game, Tip Over
 *
 * @author Ricky Leung
 */
public class TipOver {
    /**
     * The main function used to solve the game
     *
     * @param args filename
     */
    public static void main(String[] args){
        if (args.length != 1) {
            System.out.println("Usage: java TipOver filename");
        } else {
            System.out.println("File: " + args[0]);
            try {
                BufferedReader br = new BufferedReader(new FileReader(args[0]));

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

                // Solve the configuration
                Solver solver = new Solver();
                TipOverConfig start = new TipOverConfig(initialPos, goal, board, currentPos);
                System.out.println(start);
                Collection<Configuration> result = solver.solve(start);

                // Print out configuration count
                System.out.println("Total configs: " + solver.getConfigCount());
                System.out.println("Unique configs: " + solver.getUniqueConfigCount());

                // Print out result
                if (result == null) {
                    System.out.println("No solution");
                } else {
                    // Print out steps if a solution is found
                    int step = 0;
                    for (Configuration c: result) {
                        System.out.println("Step " + step + ": \n" + c);
                        step++;
                    }
                }

            } catch (FileNotFoundException e) {
                System.out.println("Invalid file");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
