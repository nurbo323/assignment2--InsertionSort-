package cli;

import algorithms.InsertionSort;
import algorithms.SortMetrics;
import metrics.PerformanceTracker;

import java.io.IOException;
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
            System.out.flush(); // Принудительный вывод

            // Уменьшаем количество прогонов для больших размеров
            int warmup = size >= 100000 ? 1 : WARMUP_RUNS;
            int measurements = size >= 100000 ? 2 : MEASUREMENT_RUNS;

            // Test different data distributions
            benchmarkDataType(tracker, "Random", size, DataType.RANDOM, warmup, measurements);
            benchmarkDataType(tracker, "Sorted", size, DataType.SORTED, warmup, measurements);

            // Пропускаем Reverse для 100000 (слишком долго - O(n²) worst case)
            if (size < 100000) {
                benchmarkDataType(tracker, "Reverse", size, DataType.REVERSE, warmup, measurements);
            } else {
                System.out.println("Reverse: SKIPPED (O(n²) worst case, would take too long)");
            }

            benchmarkDataType(tracker, "NearlySorted", size, DataType.NEARLY_SORTED, warmup, measurements);
            benchmarkDataType(tracker, "FewUnique", size, DataType.FEW_UNIQUE, warmup, measurements);
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
                                   int size, DataType type, int warmupRuns, int measurementRuns) {
        System.out.printf("  %s: ", dataType);
        System.out.flush();

        InsertionSort sorter = new InsertionSort();

        // Warmup
        for (int i = 0; i < warmupRuns; i++) {
            int[] arr = generateData(type, size);
            sorter.adaptiveInsertionSort(arr);
            System.out.print("w");
            System.out.flush();
        }

        System.out.print(" | ");
        System.out.flush();

        // Measurement
        long totalComparisons = 0;
        long totalSwaps = 0;
        long totalShifts = 0;
        long totalArrayAccesses = 0;
        long totalTime = 0;

        for (int i = 0; i < measurementRuns; i++) {
            int[] arr = generateData(type, size);

            // Run sort
            sorter.adaptiveInsertionSort(arr);

            SortMetrics metrics = sorter.getMetrics();
            totalComparisons += metrics.getComparisons();
            totalSwaps += metrics.getSwaps();
            totalShifts += metrics.getShifts();
            totalArrayAccesses += metrics.getArrayAccesses();
            totalTime += metrics.getExecutionTimeNanos();

            // Verify correctness
            if (!InsertionSort.isSorted(arr)) {
                System.err.println("\nERROR: Array not sorted!");
                return;
            }

            System.out.print(".");
            System.out.flush();
        }

        // Calculate averages
        long avgComparisons = totalComparisons / measurementRuns;
        long avgSwaps = totalSwaps / measurementRuns;
        long avgShifts = totalShifts / measurementRuns;
        long avgArrayAccesses = totalArrayAccesses / measurementRuns;
        long avgTimeNanos = totalTime / measurementRuns;
        double avgTimeMillis = avgTimeNanos / 1_000_000.0;

        // Create metrics object with averaged values
        SortMetrics avgMetrics = new SortMetrics();
        // Manually set the values by calling increment methods
        for (long i = 0; i < avgComparisons; i++) avgMetrics.incrementComparisons();
        for (long i = 0; i < avgSwaps; i++) avgMetrics.incrementSwaps();
        for (long i = 0; i < avgShifts; i++) avgMetrics.incrementShifts();
        for (long i = 0; i < avgArrayAccesses; i++) avgMetrics.incrementArrayAccesses();

        tracker.addResult(dataType, size, avgMetrics);

        System.out.printf(" ✓ %.2f ms, %d comparisons, %d shifts\n",
                avgTimeMillis, avgComparisons, avgShifts);
        System.out.flush();
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
