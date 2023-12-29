package puzzles.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.*;

/**
 * Main class for the clock puzzle.
 *
 * @author Ricky Leung & Boya Li
 */
public class Clock {
    /**
     * Run an instance of the clock puzzle.
     *
     * @param args [0]: the number of hours in the clock;
     *             [1]: the starting hour;
     *             [2]: the finish hour.
     */
    public static void main(String[] args) {
        if (args.length < 3 || args.length > 3) {
            System.out.println(("Usage: java Clock hours start finish"));
        } else {
            // Print out inputs
            System.out.println("Hours: " + args[0] + ", " +
                    "Start: " + args[1] + ", " +
                    "End: " + args[2]);

            int hour = Integer.parseInt(args[0]);
            int start = Integer.parseInt(args[1]);
            int end = Integer.parseInt(args[2]);
            Solver solver = new Solver();
            Collection<Configuration> result = solver.solve(new ClockConfig(hour, start, end, start));

            // Print out Configuration counts
            System.out.println("Total configs: " + solver.getConfigCount());
            System.out.println("Unique configs: " + solver.getUniqueConfigCount());

            if (result == null) {
                System.out.println("No solution");
            } else {
                // Print out steps if a solution is found
                int step = 0;
                for (Configuration c: result) {
                    System.out.println("Step " + step + ": " + c);
                    step++;
                }
            }
        }
    }
}