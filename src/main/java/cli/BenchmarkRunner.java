// src/main/java/cli/BenchmarkRunner.java
package cli;

import algorithms.InsertionSort;
import algorithms.SortMetrics;
import metrics.PerformanceTracker;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * Command-line benchmark runner for empirical analysis
 * Tests different input sizes and distributions
 */
public class BenchmarkRunner {

    private static final int[] TEST_SIZES = {100, 1000, 10000, 100000};
    private static final int WARMUP_RUNS = 3;
    private static final int MEASUREMENT_RUNS = 5;

    public static void main(String[] args) {
        System.out.println("=== Insertion Sort Performance Benchmark ===\n");

        BenchmarkRunner runner = new BenchmarkRunner();
        runner.runAllBenchmarks();
    }

    public void runAllBenchmarks() {
        PerformanceTracker tracker = new PerformanceTracker();

        for (int size : TEST_SIZES) {
            System.out.printf("\n--- Testing with n = %d ---\n", size);

            // Test different data distributions
            benchmarkDataType(tracker, "Random", size, DataType.RANDOM);
            benchmarkDataType(tracker, "Sorted", size, DataType.SORTED);
            benchmarkDataType(tracker, "Reverse", size, DataType.REVERSE);
            benchmarkDataType(tracker, "NearlySorted", size, DataType.NEARLY_SORTED);
            benchmarkDataType(tracker, "FewUnique", size, DataType.FEW_UNIQUE);
        }

        tracker.printSummary();

        try {
            tracker.exportToCSV("performance_results.csv");
            System.out.println("\n✓ Results exported to performance_results.csv");
        } catch (IOException e) {
            System.err.println("Error exporting results: " + e.getMessage());
        }
    }

    private void benchmarkDataType(PerformanceTracker tracker, String dataType,
                                   int size, DataType type) {
        InsertionSort sorter = new InsertionSort();

        // Warmup
        for (int i = 0; i < WARMUP_RUNS; i++) {
            int[] arr = generateData(type, size);
            sorter.adaptiveInsertionSort(arr);
        }

        // Measurement
        long totalComparisons = 0;
        long totalSwaps = 0;
        long totalShifts = 0;
        long totalTime = 0;

        for (int i = 0; i < MEASUREMENT_RUNS; i++) {
            int[] arr = generateData(type, size);
            sorter.adaptiveInsertionSort(arr);

            SortMetrics metrics = sorter.getMetrics();
            totalComparisons += metrics.getComparisons();
            totalSwaps += metrics.getSwaps();
            totalShifts += metrics.getShifts();
            totalTime += metrics.getExecutionTimeNanos();

            // Verify correctness
            if (!InsertionSort.isSorted(arr)) {
                System.err.println("ERROR: Array not sorted!");
            }
        }

        // Average metrics
        SortMetrics avgMetrics = new SortMetrics();
        avgMetrics.startTimer();
        long avgTime = totalTime / MEASUREMENT_RUNS;
        avgMetrics.stopTimer();

        // Create averaged metrics for tracking
        SortMetrics reportMetrics = createMetrics(
                totalComparisons / MEASUREMENT_RUNS,
                totalSwaps / MEASUREMENT_RUNS,
                totalShifts / MEASUREMENT_RUNS,
                avgTime
        );

        tracker.addResult(dataType, size, reportMetrics);

        System.out.printf("%s: %.2f ms, %d comparisons, %d shifts\n",
                dataType, reportMetrics.getExecutionTimeMillis(),
                reportMetrics.getComparisons(), reportMetrics.getShifts());
    }

    private SortMetrics createMetrics(long comparisons, long swaps, long shifts, long timeNanos) {
        SortMetrics metrics = new SortMetrics();
        for (int i = 0; i < comparisons; i++) metrics.incrementComparisons();
        for (int i = 0; i < swaps; i++) metrics.incrementSwaps();
        for (int i = 0; i < shifts; i++) metrics.incrementShifts();

        metrics.startTimer();
        try {
            Thread.sleep(0, (int)(timeNanos % 1_000_000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        metrics.stopTimer();

        return metrics;
    }

    private int[] generateData(DataType type, int size) {
        Random rand = new Random();
        int[] arr = new int[size];

        switch (type) {
            case RANDOM:
                for (int i = 0; i < size; i++) {
                    arr[i] = rand.nextInt(size * 10);
                }
                break;

            case SORTED:
                for (int i = 0; i < size; i++) {
                    arr[i] = i;
                }
                break;

            case REVERSE:
                for (int i = 0; i < size; i++) {
                    arr[i] = size - i;
                }
                break;

            case NEARLY_SORTED:
                for (int i = 0; i < size; i++) {
                    arr[i] = i;
                }
                // Disturb 5% of elements
                int disturbCount = Math.max(1, size / 20);
                for (int i = 0; i < disturbCount; i++) {
                    int idx1 = rand.nextInt(size);
                    int idx2 = rand.nextInt(size);
                    int temp = arr[idx1];
                    arr[idx1] = arr[idx2];
                    arr[idx2] = temp;
                }
                break;

            case FEW_UNIQUE:
                int uniqueValues = Math.max(5, size / 100);
                for (int i = 0; i < size; i++) {
                    arr[i] = rand.nextInt(uniqueValues);
                }
                break;
        }

        return arr;
    }

    private enum DataType {
        RANDOM, SORTED, REVERSE, NEARLY_SORTED, FEW_UNIQUE
    }
}
