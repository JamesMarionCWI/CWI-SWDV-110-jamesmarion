package edu.cwi.swdv110.diskusage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Hand-written searching and sorting. Collections.sort / List.sort are off-limits here. */
public final class Algorithms {

    /** Largest first; equal sizes ordered by path A–Z. */
    public static final Comparator<FileInfo> BY_SIZE_DESC = Comparator.comparingLong(FileInfo::size).reversed().thenComparing(FileInfo::path);

    /** Path A–Z (the order binary search needs). */
    public static final Comparator<FileInfo> BY_PATH = Comparator.comparing(FileInfo::path);

    private Algorithms() {
    }

    /*
     * About <T>: these methods are GENERIC – they sort a List of any type T, as long as you
     * give them a Comparator<T> that says how to order two T's. Use cmp.compare(a, b)
     * exactly where the in-class version used a < b.
     */

    /** Returns a NEW sorted list (don't modify the input). O(n²). */
    public static <T> List<T> selectionSort(List<T> list, Comparator<T> cmp) {
        List<T> a = new ArrayList<>(list);

        for (int i = 0; i < a.size() - 1; i++) {
            int minIndex = i;
            // find the smallest remaining item
            for (int j = i + 1; j < a.size(); j++) {
                if (cmp.compare(a.get(j), a.get(minIndex)) < 0) {
                    minIndex = j;
                }
            }
            T temp = a.get(i); // swap into place
            a.set(i, a.get(minIndex));
            a.set(minIndex, temp);
        }
        return a;
    }

    /**
     * Returns a NEW sorted list. O(n log n).
     *   base case:      size 0 or 1 -> a copy of the list
     *   recursive case: mergeSort each half (list.subList is handy), then merge them
     * The merge must be STABLE: when two items compare equal, take the one from the left half.
     */
    public static <T> List<T> mergeSort(List<T> list, Comparator<T> cmp) {
        List<T> a = new ArrayList<>(list);

        if (a.size() < 2)
            return a; // base case
        int mid = a.size() / 2;
        List<T> left = a.subList(0, mid);
        List<T> right = a.subList(mid, a.size());
        left = mergeSort(left, cmp); // recursive cases
        right = mergeSort(right, cmp);
        a = merge(left, right, cmp);
        return a;
    }

    private static <T> List<T> merge(List<T> left, List<T> right, Comparator<T> cmp) {
        List<T> a = new ArrayList<>();

        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            a.add(k++, (cmp.compare(left.get(i), right.get(j)) <= 0 ? left.get(i++) : right.get(j++)));
        }
        while (i < left.size())
            a.add(k++, left.get(i++));
        while (j < right.size())
            a.add(k++, right.get(j++));

        return a;
    }

    /**
     * RECURSIVE binary search over a list already sorted BY_PATH.
     * Returns the index of the file with this exact path, or -1.
     */
    public static int binarySearchByPath(List<FileInfo> sortedByPath, String path) {
        // call the helper with the whole range
        return binarySearch(sortedByPath, path, 0, sortedByPath.size() - 1);
    }

    /**
     * Base case:      low index is higher than high index
     *                 OR the element at the middle index matches the target
     * Recursive case: No match and still have halves to search
     *                 Sorted element is less than middle, search bottom half with mid as the upper bound
     *                 Sorted element is greater than middle, search top half with mid as the lower bound
     */
    private static int binarySearch(List<FileInfo> a, String path, int low, int high) {
        // base case: low > high
        if (low > high) {
            return -1;
        }

        int middleIndex = (low + high) / 2;
        if (a.get(middleIndex).path().equals(path)) {
            return middleIndex;
        }

        // Sadly need another check to find our missing element, sooo it's time to use recursion
        if (path.compareTo(a.get(middleIndex).path()) > 0) {
            // If the target element is bigger than the currently found item
            // Search the larger half
            return binarySearch(a, path, middleIndex + 1, high);
        } else {
            // Otherwise search the lower half
            return binarySearch(a, path, low, middleIndex - 1);
        }
    }

    /** The n largest files (fewer if there aren't n), using YOUR mergeSort. */
    public static List<FileInfo> largest(List<FileInfo> files, int n) {
        return mergeSort(files, BY_SIZE_DESC).stream().limit(n).toList();
    }
}
