package algorithms;

/**
 * Performance metrics for sorting algorithm analysis
 * Tracks comparisons, swaps, array accesses, and memory usage
 */
public class SortMetrics {
    private long comparisons;
    private long swaps;
    private long arrayAccesses;
    private long shifts;
    private long startTime;
    private long endTime;

    public SortMetrics() {
        this.comparisons = 0;
        this.swaps = 0;
        this.arrayAccesses = 0;
        this.shifts = 0;
    }

    public void startTimer() {
        this.startTime = System.nanoTime();
    }

    public void stopTimer() {
        this.endTime = System.nanoTime();
    }

    public void incrementComparisons() {
        this.comparisons++;
    }

    public void incrementSwaps() {
        this.swaps++;
    }

    public void incrementArrayAccesses() {
        this.arrayAccesses++;
    }

    public void incrementShifts() {
        this.shifts++;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getSwaps() {
        return swaps;
    }

    public long getArrayAccesses() {
        return arrayAccesses;
    }

    public long getShifts() {
        return shifts;
    }

    public long getExecutionTimeNanos() {
        return endTime - startTime;
    }

    public double getExecutionTimeMillis() {
        return (endTime - startTime) / 1_000_000.0;
    }

    public void reset() {
        this.comparisons = 0;
        this.swaps = 0;
        this.arrayAccesses = 0;
        this.shifts = 0;
        this.startTime = 0;
        this.endTime = 0;
    }

    @Override
    public String toString() {
        return String.format(
                "Metrics{comparisons=%d, swaps=%d, shifts=%d, arrayAccesses=%d, time=%.2fms}",
                comparisons, swaps, shifts, arrayAccesses, getExecutionTimeMillis()
        );
    }
}
