package puzzles.common.solver;

import java.util.*;

public class Solver {
    /** The number of configurations created */
    private int configCount;
    /** The number of unique configurations created */
    private int uniqueConfigCount;


    /**
     * Constructor for the Solver class
     */
    public Solver() {
        configCount = 1;
        uniqueConfigCount = 0;
    }

    /**
     * Solver method for using BFS to solve each puzzle:
     * Uses a queue to store neighbors and a hashmap to keep track of
     * paths for traveling between configurations.
     *
     * @param c The start configuration
     * @return The shortest path, if there is a solution possible
     */
    public Collection<Configuration> solve(Configuration c) {
        Queue<Configuration> queue = new LinkedList<>();
        queue.add(c);

        Map<Configuration, Configuration> predecessors = new HashMap<>();
        predecessors.put(c, c);

        Configuration current = null;
        boolean solutionFound = false;

        while (!queue.isEmpty() && !solutionFound) {
            current = queue.remove();
            if (current.isSolution()) {
                solutionFound = true;
                break;
            }

            Collection<Configuration> neighbors = current.getNeighbors();
            configCount += neighbors.size();

            for (Configuration neighbor: neighbors) {
                if (!predecessors.containsKey(neighbor)) {
                    predecessors.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        uniqueConfigCount = predecessors.size();

        if (solutionFound) {
            return constructPath(predecessors, c, current);
        }
        return null;
    }

    /**
     * Helper method used in solve to construct the path based on
     * the given predecessor map
     *
     * @param predecessors Map of configuration traversals
     * @param start Original start configuration
     * @param end End configuration
     * @return List of configurations representing the shortest path
     */
    public List<Configuration> constructPath(Map<Configuration, Configuration> predecessors, Configuration start, Configuration end) {
        List<Configuration> path = new LinkedList<>();
        Configuration current = end;
        while (current != start) {
            path.add(0, current);
            current = predecessors.get(current);
        }
        path.add(0, current);
        return path;
    }

    /**
     * Getter method for the configuration count
     *
     * @return Configuration count
     */
    public int getConfigCount() {
        return configCount;
    }

    /**
     * Getter method for the unique configuration count
     *
     * @return Unique configuration count
     */
    public int getUniqueConfigCount() {
        return uniqueConfigCount;
    }
}
