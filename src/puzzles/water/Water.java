package puzzles.water;

import puzzles.clock.ClockConfig;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.*;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Ricky Leung & Boya Li
 */
public class Water {

    /**
     * Run an instance of the water buckets puzzle.
     *
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(
                    ("Usage: java Water amount bucket1 bucket2 ...")
            );
        } else {
            int index = 0;
            int DESIRED_AMOUNT = Integer.parseInt(args[index]);
            ArrayList<Integer> capacityList = new ArrayList<>();
            ArrayList<Integer> amountList = new ArrayList<>();

            // Populate lists current amount in each bucket and their max amounts
            for (String bucket: args) {
                if (index != 0) {
                    capacityList.add(Integer.valueOf(bucket));
                    amountList.add(0);
                }
                index++;
            }

            // Print out inputs
            System.out.println("Amount: " + args[0] +
                    ", Buckets: " + capacityList );

            Solver solver = new Solver();
            Collection<Configuration> result = solver.solve(new WaterConfig(DESIRED_AMOUNT, capacityList, amountList));

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