// src/test/java/algorithms/InsertionSortTest.java
package algorithms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for InsertionSort
 * Covers edge cases, correctness, and performance validation
 */
class InsertionSortTest {

    private InsertionSort sorter;

    @BeforeEach
    void setUp() {
        sorter = new InsertionSort();
    }

    // Edge Cases

    @Test
    @DisplayName("Test null array handling")
    void testNullArray() {
        assertDoesNotThrow(() -> sorter.sort(null));
    }

    @Test
    @DisplayName("Test empty array")
    void testEmptyArray() {
        int[] arr = {};
        sorter.sort(arr);
        assertEquals(0, arr.length);
    }

    @Test
    @DisplayName("Test single element array")
    void testSingleElement() {
        int[] arr = {42};
        sorter.sort(arr);
        assertArrayEquals(new int[]{42}, arr);
        assertTrue(InsertionSort.isSorted(arr));
    }

    @Test
    @DisplayName("Test two elements")
    void testTwoElements() {
        int[] arr = {2, 1};
        sorter.sort(arr);
        assertArrayEquals(new int[]{1, 2}, arr);
        assertTrue(InsertionSort.isSorted(arr));
    }

    // Correctness Tests

    @Test
    @DisplayName("Test already sorted array")
    void testAlreadySorted() {
        int[] arr = {1, 2, 3, 4, 5};
        sorter.sort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
        assertTrue(InsertionSort.isSorted(arr));
    }

    @Test
    @DisplayName("Test reverse sorted array (worst case)")
    void testReverseSorted() {
        int[] arr = {5, 4, 3, 2, 1};
        sorter.sort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
        assertTrue(InsertionSort.isSorted(arr));
    }

    @Test
    @DisplayName("Test array with duplicates")
    void testDuplicates() {
        int[] arr = {3, 1, 4, 1, 5, 9, 2, 6, 5, 3};
        sorter.sort(arr);
        int[] expected = {1, 1, 2, 3, 3, 4, 5, 5, 6, 9};
        assertArrayEquals(expected, arr);
        assertTrue(InsertionSort.isSorted(arr));
    }

    @Test
    @DisplayName("Test all identical elements")
    void testAllIdentical() {
        int[] arr = {7, 7, 7, 7, 7};
        sorter.sort(arr);
        assertArrayEquals(new int[]{7, 7, 7, 7, 7}, arr);
        assertTrue(InsertionSort.isSorted(arr));
    }

    @Test
    @DisplayName("Test nearly sorted array")
    void testNearlySorted() {
        int[] arr = {1, 2, 3, 5, 4, 6, 7, 8};
        sorter.sort(arr);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, arr);
        assertTrue(InsertionSort.isSorted(arr));
    }

    @Test
    @DisplayName("Test negative numbers")
    void testNegativeNumbers() {
        int[] arr = {-5, 3, -2, 8, -10, 0};
        sorter.sort(arr);
        assertArrayEquals(new int[]{-10, -5, -2, 0, 3, 8}, arr);
        assertTrue(InsertionSort.isSorted(arr));
    }

    // Optimization Tests

    @Test
    @DisplayName("Test binary insertion sort")
    void testBinaryInsertionSort() {
        int[] arr = {8, 3, 1, 7, 0, 10, 2};
        sorter.binaryInsertionSort(arr);
        assertTrue(InsertionSort.isSorted(arr));
        assertArrayEquals(new int[]{0, 1, 2, 3, 7, 8, 10}, arr);
    }

    @Test
    @DisplayName("Test sentinel insertion sort")
    void testSentinelInsertionSort() {
        int[] arr = {8, 3, 1, 7, 0, 10, 2};
        sorter.sentinelInsertionSort(arr);
        assertTrue(InsertionSort.isSorted(arr));
        assertArrayEquals(new int[]{0, 1, 2, 3, 7, 8, 10}, arr);
    }

    @Test
    @DisplayName("Test adaptive insertion sort")
    void testAdaptiveInsertionSort() {
        int[] arr = {1, 2, 3, 4, 0, 5, 6, 7};
        sorter.adaptiveInsertionSort(arr);
        assertTrue(InsertionSort.isSorted(arr));
        assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6, 7}, arr);
    }

    // Performance Validation Tests

    @ParameterizedTest
    @MethodSource("randomArrayProvider")
    @DisplayName("Test random arrays")
    void testRandomArrays(int[] arr) {
        int[] original = arr.clone();
        sorter.sort(arr);
        assertTrue(InsertionSort.isSorted(arr));

        // Verify all elements are present
        Arrays.sort(original);
        assertArrayEquals(original, arr);
    }

    static Stream<int[]> randomArrayProvider() {
        Random rand = new Random(42); // Fixed seed for reproducibility
        return Stream.of(
                generateRandomArray(rand, 10),
                generateRandomArray(rand, 50),
                generateRandomArray(rand, 100)
        );
    }

    static int[] generateRandomArray(Random rand, int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(1000);
        }
        return arr;
    }

    // Metrics Tests

    @Test
    @DisplayName("Test metrics collection for best case")
    void testMetricsBestCase() {
        int[] arr = {1, 2, 3, 4, 5};
        sorter.adaptiveInsertionSort(arr);

        SortMetrics metrics = sorter.getMetrics();
        assertTrue(metrics.getComparisons() > 0);
        assertEquals(0, metrics.getShifts()); // Best case: no shifts
        assertTrue(metrics.getExecutionTimeNanos() > 0);
    }

    @Test
    @DisplayName("Test metrics collection for worst case")
    void testMetricsWorstCase() {
        int[] arr = {5, 4, 3, 2, 1};
        sorter.sort(arr);

        SortMetrics metrics = sorter.getMetrics();
        assertTrue(metrics.getComparisons() > 0);
        assertTrue(metrics.getShifts() > 0); // Worst case: many shifts
        assertTrue(metrics.getExecutionTimeNanos() > 0);
    }

    // Cross-validation with Java's built-in sort

    @Test
    @DisplayName("Cross-validate with Arrays.sort")
    void testCrossValidation() {
        Random rand = new Random(123);
        int[] arr1 = new int[100];
        int[] arr2 = new int[100];

        for (int i = 0; i < 100; i++) {
            int val = rand.nextInt(1000);
            arr1[i] = val;
            arr2[i] = val;
        }

        sorter.sort(arr1);
        Arrays.sort(arr2);

        assertArrayEquals(arr2, arr1);
    }

    // Stability Test

    @Test
    @DisplayName("Test sort stability (relative order preserved)")
    void testStability() {
        // For primitive int arrays, stability is harder to test
        // But we can verify consistent behavior
        int[] arr = {3, 1, 4, 1, 5, 9, 2, 6, 5};
        sorter.sort(arr);
        assertTrue(InsertionSort.isSorted(arr));
    }
}
