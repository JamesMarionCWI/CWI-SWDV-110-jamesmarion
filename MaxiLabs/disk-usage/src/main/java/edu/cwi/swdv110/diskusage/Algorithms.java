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
        // TODO
        return a;
    }

    /**
     * Returns a NEW sorted list. O(n log n).
     *   base case:      size 0 or 1 -> a copy of the list
     *   recursive case: mergeSort each half (list.subList is handy), then merge them
     * The merge must be STABLE: when two items compare equal, take the one from the left half.
     */
    public static <T> List<T> mergeSort(List<T> list, Comparator<T> cmp) {
        // TODO
        return new ArrayList<>(list);
    }

    private static <T> List<T> merge(List<T> left, List<T> right, Comparator<T> cmp) {
        // TODO
        return new ArrayList<>();
    }

    /**
     * RECURSIVE binary search over a list already sorted BY_PATH.
     * Returns the index of the file with this exact path, or -1.
     */
    public static int binarySearchByPath(List<FileInfo> sortedByPath, String path) {
        // TODO call the helper with the whole range
        return -1;
    }

    private static int binarySearch(List<FileInfo> a, String path, int low, int high) {
        // TODO – base case: low > high
        return -1;
    }

    /** The n largest files (fewer if there aren't n), using YOUR mergeSort. */
    public static List<FileInfo> largest(List<FileInfo> files, int n) {
        // TODO
        return new ArrayList<>();
    }
}
