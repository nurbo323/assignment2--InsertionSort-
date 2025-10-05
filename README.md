# Insertion Sort Implementation - Assignment 2

## Overview
This project implements an optimized Insertion Sort algorithm with multiple optimization strategies for nearly-sorted data, including:
- Binary Insertion Sort (reduces comparisons)
- Sentinel Optimization (eliminates boundary checks)
- Adaptive strategy (early termination)

## Complexity Analysis

### Time Complexity
- **Best Case**: Θ(n) - Array already sorted
- **Average Case**: Θ(n²) - Random distribution
- **Worst Case**: Θ(n²) - Reverse sorted array

### Space Complexity
- **Θ(1)** - In-place sorting with constant auxiliary space

## Project Structure
assignment2-insertion-sort/
├── src/main/java/
│ ├── algorithms/
│ │ ├── InsertionSort.java # Main implementation
│ │ └── SortMetrics.java # Performance tracking
│ ├── metrics/
│ │ └── PerformanceTracker.java # CSV export
│ └── cli/
│ └── BenchmarkRunner.java # Benchmark harness
├── src/test/java/
│ └── algorithms/
│ └── InsertionSortTest.java # Test suite
└── pom.xml

text

## Building and Running

### Compile
mvn clean compile

text

### Run Tests
mvn test

text

### Run Benchmarks
mvn exec:java -Dexec.mainClass="cli.BenchmarkRunner"

text

## Usage Example

InsertionSort sorter = new InsertionSort();
int[] arr = {5, 2, 8, 1, 9};

// Standard sort
sorter.sort(arr);

// Binary insertion sort (fewer comparisons)
sorter.binaryInsertionSort(arr);

// Adaptive sort (best for nearly-sorted)
sorter.adaptiveInsertionSort(arr);

// Get metrics
SortMetrics metrics = sorter.getMetrics();
System.out.println(metrics);

text

## Optimizations

### 1. Binary Insertion Sort
- Uses binary search to find insertion position
- Reduces comparisons from O(n) to O(log n) per element
- Best for data with expensive comparison operations

### 2. Sentinel Optimization
- Places minimum element at index 0
- Eliminates boundary checking in inner loop
- Excellent for nearly-sorted sequences

### 3. Adaptive Strategy
- Detects already-sorted subsequences
- Skips unnecessary comparisons and shifts
- Optimal for data with long sorted runs

## Performance Results
Run benchmarks to generate `performance_results.csv` with empirical data for:
- Random data
- Sorted data (best case)
- Reverse sorted (worst case)
- Nearly sorted (optimization showcase)
- Few unique values

## Testing
Comprehensive test suite covers:
- Edge cases (null, empty, single element)
- Correctness validation
- Cross-validation with Arrays.sort()
- Performance metrics verification
- All optimization variants