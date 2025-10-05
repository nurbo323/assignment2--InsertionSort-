// src/main/java/algorithms/InsertionSort.java
package algorithms;

/**
 * Optimized Insertion Sort Implementation with enhancements for nearly-sorted data
 *
 * TIME COMPLEXITY:
 * - Best Case: Θ(n) - already sorted, only comparisons
 * - Average Case: Θ(n²) - random distribution
 * - Worst Case: Θ(n²) - reverse sorted
 *
 * SPACE COMPLEXITY: Θ(1) - in-place sorting
 *
 * OPTIMIZATIONS:
 * 1. Binary search for insertion position (reduces comparisons)
 * 2. Sentinel optimization (eliminates boundary checks)
 * 3. Early termination for sorted sequences
 * 4. Efficient shifting instead of swapping
 *
 * @author Your Name
 */
public class InsertionSort {

    private SortMetrics metrics;

    public InsertionSort() {
        this.metrics = new SortMetrics();
    }

    /**
     * Standard insertion sort implementation
     * Best for educational purposes and baseline comparison
     */
    public void sort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }

        metrics.reset();
        metrics.startTimer();

        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            metrics.incrementArrayAccesses();
            int j = i - 1;

            // Shift elements greater than key to the right
            while (j >= 0) {
                metrics.incrementComparisons();
                if (arr[j] > key) {
                    metrics.incrementArrayAccesses(); // read arr[j]
                    arr[j + 1] = arr[j];
                    metrics.incrementArrayAccesses(); // write arr[j+1]
                    metrics.incrementShifts();
                    j--;
                } else {
                    break;
                }
            }

            arr[j + 1] = key;
            metrics.incrementArrayAccesses();
        }

        metrics.stopTimer();
    }

    /**
     * Optimized insertion sort using binary search for insertion position
     * Reduces comparisons from O(n) to O(log n) per element
     * Particularly efficient for nearly-sorted data
     */
    public void binaryInsertionSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }

        metrics.reset();
        metrics.startTimer();

        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            metrics.incrementArrayAccesses();

            // Find insertion position using binary search
            int pos = binarySearch(arr, 0, i - 1, key);

            // Shift elements to make space
            for (int j = i - 1; j >= pos; j--) {
                arr[j + 1] = arr[j];
                metrics.incrementArrayAccesses(); // read
                metrics.incrementArrayAccesses(); // write
                metrics.incrementShifts();
            }

            arr[pos] = key;
            metrics.incrementArrayAccesses();
        }

        metrics.stopTimer();
    }

    /**
     * Binary search to find insertion position
     * Returns the index where key should be inserted
     */
    private int binarySearch(int[] arr, int left, int right, int key) {
        while (left <= right) {
            int mid = left + (right - left) / 2;

            metrics.incrementComparisons();
            metrics.incrementArrayAccesses();

            if (arr[mid] == key) {
                return mid + 1;
            } else if (arr[mid] < key) {
                metrics.incrementComparisons();
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return left;
    }

    /**
     * Insertion sort with sentinel optimization
     * Eliminates boundary checking in inner loop
     * Excellent for nearly-sorted data
     */
    public void sentinelInsertionSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }

        metrics.reset();
        metrics.startTimer();

        // Find minimum element and place it at index 0 (sentinel)
        int minIndex = 0;
        for (int i = 1; i < arr.length; i++) {
            metrics.incrementComparisons();
            metrics.incrementArrayAccesses();
            if (arr[i] < arr[minIndex]) {
                minIndex = i;
            }
        }

        // Swap minimum to front (sentinel position)
        if (minIndex != 0) {
            swap(arr, 0, minIndex);
        }

        // Now perform insertion sort without boundary checks
        for (int i = 2; i < arr.length; i++) {
            int key = arr[i];
            metrics.incrementArrayAccesses();
            int j = i - 1;

            // No need to check j >= 0 because sentinel guarantees termination
            while (arr[j] > key) {
                metrics.incrementComparisons();
                metrics.incrementArrayAccesses();
                arr[j + 1] = arr[j];
                metrics.incrementArrayAccesses();
                metrics.incrementShifts();
                j--;
            }

            metrics.incrementComparisons(); // Final comparison that fails
            arr[j + 1] = key;
            metrics.incrementArrayAccesses();
        }

        metrics.stopTimer();
    }

    /**
     * Adaptive insertion sort - detects and skips sorted runs
     * Optimal for nearly-sorted data with long sorted sequences
     */
    public void adaptiveInsertionSort(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return;
        }

        metrics.reset();
        metrics.startTimer();

        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            metrics.incrementArrayAccesses();

            // Early termination: if already in correct position, skip
            metrics.incrementComparisons();
            metrics.incrementArrayAccesses();
            if (key >= arr[i - 1]) {
                continue; // Already sorted, move to next element
            }

            int j = i - 1;

            // Find insertion position and shift
            while (j >= 0) {
                metrics.incrementComparisons();
                metrics.incrementArrayAccesses();
                if (arr[j] > key) {
                    arr[j + 1] = arr[j];
                    metrics.incrementArrayAccesses();
                    metrics.incrementShifts();
                    j--;
                } else {
                    break;
                }
            }

            arr[j + 1] = key;
            metrics.incrementArrayAccesses();
        }

        metrics.stopTimer();
    }

    /**
     * Helper method to swap two elements
     */
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        metrics.incrementSwaps();
        metrics.incrementArrayAccesses(); // 3 accesses for swap
        metrics.incrementArrayAccesses();
        metrics.incrementArrayAccesses();
    }

    /**
     * Get performance metrics from last sort operation
     */
    public SortMetrics getMetrics() {
        return metrics;
    }

    /**
     * Validate if array is sorted
     */
    public static boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                return false;
            }
        }
        return true;
    }
}
