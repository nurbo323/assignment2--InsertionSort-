package metrics;

import algorithms.SortMetrics;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Tracks and exports performance data for empirical analysis
 */
public class PerformanceTracker {
    private final List<BenchmarkResult> results;

    public PerformanceTracker() {
        this.results = new ArrayList<>();
    }

    public void addResult(String dataType, int size, SortMetrics metrics) {
        results.add(new BenchmarkResult(dataType, size, metrics));
    }

    public void exportToCSV(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("DataType,InputSize,Comparisons,Swaps,Shifts,ArrayAccesses");

            for (BenchmarkResult result : results) {
                writer.printf("%s,%d,%d,%d,%d,%d%n",
                        result.dataType,
                        result.size,
                        result.comparisons,
                        result.swaps,
                        result.shifts,
                        result.arrayAccesses
                );
            }
        }
    }

    public void printSummary() {
        System.out.println("\n=== Performance Summary ===");
        for (BenchmarkResult result : results) {
            System.out.printf("%s (n=%d): %d comparisons, %d shifts%n",
                    result.dataType, result.size, result.comparisons, result.shifts);
        }
    }

    private static class BenchmarkResult {
        String dataType;
        int size;
        long comparisons;
        long swaps;
        long shifts;
        long arrayAccesses;

        BenchmarkResult(String dataType, int size, SortMetrics metrics) {
            this.dataType = dataType;
            this.size = size;
            this.comparisons = metrics.getComparisons();
            this.swaps = metrics.getSwaps();
            this.shifts = metrics.getShifts();
            this.arrayAccesses = metrics.getArrayAccesses();
        }
    }
}
