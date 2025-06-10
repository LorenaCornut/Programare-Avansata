package org.example;

/**
 * Manages statistics of executed tests.
 * <p>
 * Keeps track of the number of passed and failed tests,
 * provides accessors for these counts, and prints summary statistics.
 */
public class TestStatistics {

    private int passedTests = 0;
    private int failedTests = 0;

    /**
     * Records a passed test by incrementing the passed test count.
     */
    public void recordPassed() {
        passedTests++;
    }

    /**
     * Records a failed test by incrementing the failed test count.
     */
    public void recordFailed() {
        failedTests++;
    }

    /**
     * Returns the number of tests that passed.
     *
     * @return the count of passed tests
     */
    public int getPassedCount() {
        return passedTests;
    }

    /**
     * Returns the number of tests that failed.
     *
     * @return the count of failed tests
     */
    public int getFailedCount() {
        return failedTests;
    }

    /**
     * Returns the total number of tests executed.
     *
     * @return the sum of passed and failed tests
     */
    public int getTotalCount() {
        return passedTests + failedTests;
    }

    /**
     * Prints a summary of the test statistics to the console,
     * including counts and the success rate percentage.
     */
    public void printStats() {
        System.out.println("\n=== Test Statistics ===");
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + failedTests);
        System.out.println("Total: " + getTotalCount());

        if (getTotalCount() > 0) {
            double successRate = (double) passedTests / getTotalCount() * 100;
            System.out.printf("Success Rate: %.2f%%\n", successRate);
        }
    }
}
