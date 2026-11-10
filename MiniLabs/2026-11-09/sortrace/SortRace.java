import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

public class SortRace {
    public static void selectionSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            int minIndex = i;
            // find the smallest remaining item
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < a[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = a[i]; // swap into place
            a[i] = a[minIndex];
            a[minIndex] = temp;
        }
    }

    public static void mergeSort(int[] a) {
        if (a.length < 2) return; // base case
        int mid = a.length / 2;
        int[] left = Arrays.copyOfRange(a, 0, mid);
        int[] right = Arrays.copyOfRange(a, mid, a.length);
        mergeSort(left); // recursive cases
        mergeSort(right);
        merge(a, left, right);
    }

    private static void merge(int[] a, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;
        while (i < left.length && j < right.length) {
            a[k++] = (left[i] <= right[j]) ? left[i++] : right[j++];
        }
        while (i < left.length) a[k++] = left[i++];
        while (j < right.length) a[k++] = right[j++];
    }

    // 4. Verify each result is sorted with a helper method
    private static boolean testSortedAscendingOrder(int[] a) {
        // Empty arrays and arrays that are only filled with one element are considered sorted
        if(a.length <= 1) return true;
        for(int i = 1; i < a.length; i++) {
            // If the current element is less than the previous element, it's not sorted in ascending order
            if(a[i] < a[i-1]) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        // 5. Double the size to 100,000 - how did each time change?
        for(int sizeFactor = 1; sizeFactor <= 2; sizeFactor++) {
            // 1. Fill an int[] with 50,000 random numbers (Random.nextInt)
            int[] numbersToSort = new int[50000 * sizeFactor];
            Random rand = new Random();
            for(int i = 0; i < numbersToSort.length; i++) {
                numbersToSort[i] = rand.nextInt();
            }
            // 2. Copy it three times with Arrays.copyOf()
            int[] numbersToSortWithSelection = Arrays.copyOf(numbersToSort, 50000 * sizeFactor);
            int[] numbersToSortWithMerge = Arrays.copyOf(numbersToSort, 50000 * sizeFactor);
            int[] numbersToSortWithArrays = Arrays.copyOf(numbersToSort, 50000 * sizeFactor);

            // 3. Time selectionSort, mergeSort, and Arrays.sort using Instant + Duration
            Instant start, end;
            Duration elapsed;

            System.out.printf("Testing the sorting algorithms now for a randomly filled array of size %d%n", numbersToSort.length);

            // Empty Line for Formatting Purposes
            System.out.println();

            start = Instant.now();
            selectionSort(numbersToSortWithSelection);
            end = Instant.now();
            elapsed = Duration.between(start, end);
            System.out.println("Selection sort finished in: " + elapsed);
            System.out.println("Sort status: " + testSortedAscendingOrder(numbersToSortWithSelection));

            // Empty Line for Formatting Purposes
            System.out.println();

            start = Instant.now();
            mergeSort(numbersToSortWithMerge);
            end = Instant.now();
            elapsed = Duration.between(start, end);
            System.out.println("Merge sort finished in: " + elapsed);
            System.out.println("Sort status: " + testSortedAscendingOrder(numbersToSortWithMerge));

            // Empty Line for Formatting Purposes
            System.out.println();

            start = Instant.now();
            Arrays.sort(numbersToSortWithArrays);
            end = Instant.now();
            elapsed = Duration.between(start, end);
            System.out.println("Arrays sort finished in: " + elapsed);
            System.out.println("Sort status: " + testSortedAscendingOrder(numbersToSortWithArrays));

            // Empty Line for Formatting Purposes
            System.out.println();
        }
    }
}
