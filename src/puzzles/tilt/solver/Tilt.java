package puzzles.tilt.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.tilt.model.TiltConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

/**
 * Main class for the tilt puzzle.
 *
 * @author Boya Li
 */
public class Tilt {
    /**
     * Run an instance of the tilt puzzle.
     *
     * @param args [0]: the file the program would access;
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Tilt filename");
        }
        else {
            FileReader file = null;
            int size = 0;
            String[][] board = new String[0][];
            int row = 0;
            int col = 0;
            int greenSliderNum = 0;

            try {
                file = new FileReader(args[0]);
            }
            catch(Exception f) {
                System.out.println(f.getMessage());
                System.exit(1);
            }

            BufferedReader in = new BufferedReader(file);
            String line;

            if((line = in.readLine()) != null) {
                size = Integer.parseInt(line);
                board = new String[size][size];
            }

            while((line = in.readLine()) != null) {
                String[] line1 = line.split(" ");
                for(String value : line1) {
                    if(value.equals("G")) {
                        greenSliderNum++;
                    }
                    board[row][col] = value;
                    col++;
                }
                col = 0;
                row++;
            }

            file.close();
            in.close();

            TiltConfig start = new TiltConfig(size, greenSliderNum, board);

            System.out.println(start);

            Solver solver = new Solver();

            Collection<Configuration> path = solver.solve(start);

            System.out.println("Total configs: " + solver.getConfigCount() +
                    "\nUnique configs: " + solver.getUniqueConfigCount());


            if (path == null) {
                System.out.println("No solution");
            }
            if (path != null && path.size() >= 1) {
                int stepNumber = 0;
                for(Configuration current : path) {
                    if(current instanceof TiltConfig curr) {
                        System.out.println("Step " + stepNumber + ":\n" + curr.toString() + "\n");
                        stepNumber++;
                    }
                }
            }
        }
    }
}
