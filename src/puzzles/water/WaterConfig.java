package puzzles.water;

import puzzles.common.solver.Configuration;

import java.util.*;

/**
 * Water configuration class that implements the Configuration
 * interface and is used to solve the Water puzzle
 *
 * @author Ricky Leung & Boya Li
 */
public class WaterConfig implements Configuration {
    /** The amount of water desired in a single bucket */
    private final int DESIRED_AMOUNT;
    /** List representing the max amount of water possible in each bucket */
    private final List<Integer> capacityList;
    /*8 List representing the amount of water in each bucket */
    private List<Integer> amountList;

    /**
     * Constructor for the Water Configuration class
     *
     * @param DESIRED_AMOUNT Desired amount in a single bucket
     * @param capacityList List of max water possible for each bucket
     * @param amountList List of current amount of water in each bucket
     */
    public WaterConfig(int DESIRED_AMOUNT, List<Integer> capacityList, List<Integer> amountList) {
        this.DESIRED_AMOUNT = DESIRED_AMOUNT;
        this.capacityList = capacityList;
        this.amountList = amountList;
    }

    /**
     * Checks to see if the current list of buckets has any bucket
     * that contains the desired amount of water
     *
     * @return Boolean representing if the current
     * configuration is the solution
     */
    @Override
    public boolean isSolution() {
        return amountList.contains(DESIRED_AMOUNT);
    }

    /**
     * Gets all possible neighbors of the current configuration by creating
     * new instances of WaterConfig based on whether you can fill a bucket,
     * empty a bucket, or pour the current bucket into any other bucket.
     *
     * @return Collection of neighbor configurations
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        ArrayList<Configuration> result = new ArrayList<>();

        // Pouring between buckets
        for (int current = 0; current < amountList.size(); current++) {
            // Filling buckets
            if (amountList.get(current) != capacityList.get(current)) {
                ArrayList<Integer> newAmountsList = new ArrayList<>(amountList);
                newAmountsList.set(current, capacityList.get(current));
                result.add(new WaterConfig(DESIRED_AMOUNT, capacityList, newAmountsList));
            }

            // Dumping buckets
            if (amountList.get(current) != 0) {
                ArrayList<Integer> newAmountsList = new ArrayList<>(amountList);
                newAmountsList.set(current, 0);
                result.add(new WaterConfig(DESIRED_AMOUNT, capacityList, newAmountsList));
            }

            for (int other = 0; other < amountList.size(); other++) {
                int currentAmount = amountList.get(current);
                int currentCapacity = capacityList.get(current);
                int otherAmount = amountList.get(other);
                if (current != other && otherAmount != 0 && currentAmount != currentCapacity) {
                    ArrayList<Integer> newAmountsList = new ArrayList<>(amountList);
                    int currentNewAmount = otherAmount + currentAmount;
                    int otherNewAmount = 0;
                    if (currentNewAmount > currentCapacity) {
                        otherNewAmount = currentNewAmount - currentCapacity;
                        currentNewAmount = currentCapacity;
                    }
                    newAmountsList.set(current, currentNewAmount);
                    newAmountsList.set(other, otherNewAmount);

                    result.add(new WaterConfig(DESIRED_AMOUNT, capacityList, newAmountsList));
                }
            }
        }
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
        if (other instanceof WaterConfig) {
            WaterConfig otherConfig = (WaterConfig) other;
            return this.DESIRED_AMOUNT == otherConfig.DESIRED_AMOUNT &&
                    this.capacityList.equals(otherConfig.capacityList) &&
                    this.amountList.equals(otherConfig.amountList);
        }
        return false;
    }

    /**
     * Hashes the current configuration by hashing all of its attributes
     *
     * @return Hash code of this configuration
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(DESIRED_AMOUNT) + capacityList.hashCode() + amountList.hashCode();
    }

    /**
     * Water Config's toString method
     *
     * @return String representing what the current amount for
     * each bucket is
     */
    @Override
    public String toString() {
        return amountList.toString();
    }
}