package puzzles.clock;

import puzzles.common.solver.Configuration;

import java.util.*;

/**
 * Clock configuration class that implements the Configuration
 * interface and is used to solve the Clock puzzle
 *
 * @author Ricky Leung & Boya Li
 */
public class ClockConfig implements Configuration {
    /** Max number of hours on the clock */
    private final int HOURS;
    /** The original start hour of the clock */
    private final int START;
    /** The end goal of the clock */
    private final int END;
    /** The current hour of the clock */
    private int current;

    /**
     * Constructor for the Clock Configuration class
     *
     * @param HOURS Number of hours on the clock
     * @param START Start hour of the clock
     * @param END End goal of the clock
     * @param current Current hour of the clock
     */
    public ClockConfig(int HOURS, int START, int END, int current) {
        this.HOURS = HOURS;
        this.START = START;
        this.END = END;
        this.current = current;
    }

    /**
     * Checks to see if the current configuration is the solution
     * by seeing if the current hour has reached the end goal
     *
     * @return Boolean representing if the current
     * configuration is the solution
     */
    @Override
    public boolean isSolution() {
        return END == current;
    }

    /**
     * Gets the 2 neighbors of the current configuration by creating
     * new instances of ClockConfig with decremented and incremented current
     * hours. If the current hour goes to 0 and goes past the max amount of
     * hours possible for the clock, it wraps back around to the other side
     *
     * @return Collection of neighbor configuration
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        int left = current - 1;
        int right = current + 1;
        ArrayList<Configuration> result = new ArrayList<>();
        if (left == 0) {
            left = HOURS;
        }
        if (right == HOURS + 1) {
            right = 1;
        }
        result.add(new ClockConfig(HOURS, START, END, left));
        result.add(new ClockConfig(HOURS, START, END, right));

        return result;
    }

    /**
     * Checks if the current configuration is equal to the other
     * configuration by comparing all attributes
     *
     * @param other Other configuration we are using to compare
     * @return Boolean representing if both configurations are equal
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof ClockConfig) {
            ClockConfig otherConfig = (ClockConfig) other;
            return this.HOURS == otherConfig.HOURS && this.START == otherConfig.START && this.END == otherConfig.END && this.current == this.current;
        }
        return false;
    }

    /**
     * Hashes the current configuration by using its current hour
     *
     * @return Hash code of this configuration
     */
    @Override
    public int hashCode() {
        return Integer.valueOf(this.current).hashCode();
    }

    /**
     * Clock Config's toString method
     *
     * @return String representing what the current hour
     * of the configuration is
     */
    @Override
    public String toString() {
        return Integer.toString(current);
    }
}