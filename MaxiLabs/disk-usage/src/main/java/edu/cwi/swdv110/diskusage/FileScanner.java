package edu.cwi.swdv110.diskusage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/** Recursive folder walking. Files.walk() and other built-in walkers are off-limits. */
public final class FileScanner {

    private static final String[] UNITS = { "B", "KB", "MB", "GB", "TB" };

    private FileScanner() {
    }

    /**
     * GIVEN: the children of a folder sorted by name, or an empty array if the folder
     * can't be read (listFiles() returns null for protected folders – this prevents a crash).
     */
    static File[] sortedChildren(File dir) {
        File[] kids = dir.listFiles();
        if (kids == null) {
            return new File[0];
        }
        Arrays.sort(kids, Comparator.comparing(File::getName));
        return kids;
    }

    // ---------------- Part 1 ----------------

    /**
     * Base case:      a file's size is its length().
     * Recursive case: a folder's size is the sum of the totalSize of each child.
     */
    public static long totalSize(File f) {
        // Base case:      a file's size is its length().
        if (f.isFile()) {
            return f.length();
        }
        // Recursive case: a folder's size is the sum of the totalSize of each child.
        if (f.isDirectory()) {
            long totalSizeOfFolder = 0;
            for (File childFile : sortedChildren(f)) {
                totalSizeOfFolder += totalSize(childFile);
            }
            return totalSizeOfFolder;
        }
        // Extra case: if something is somehow neither a file nor a folder
        // And yes, the only piece of tech I own is a printer
        // And I have something to shoot the printer if it makes a funny noise >.>
        return 0;
    }

    /** Number of FILES (not folders) in f and everything under it. A single file counts as 1. */
    /**
     * Base case:      a file counts as 1
     * Recursive case: a folder's file count is the sum of each file child
     */
    public static int countFiles(File f) {
        // Base case:      a file counts as 1
        if (f.isFile()) {
            return 1;
        }
        // Recursive case: a folder's file count is the sum of each file child
        if (f.isDirectory()) {
            int numberFilesInFolder = 0;
            for (File childFile : sortedChildren(f)) {
                numberFilesInFolder += countFiles(childFile);
            }
            return numberFilesInFolder;
        }
        // Extra case: if something is somehow neither a file nor a folder
        return 0;
    }

    /**
     * How many levels deep the nesting goes.
     *   a file, or an empty folder           -> 0
     *   a folder that only contains files     -> 1
     *   otherwise                             -> 1 + the deepest child
     */
    /**
     * Base case:      a file or an empty folder counts as 0
     * Recursive case: a folder's depth is the largest depth out of each child plus itself
     */
    public static int maxDepth(File f) {
        if (f.isFile()) {
            // Files don't increase the depth
            return 0;
        }
        if (f.isDirectory()) {
            // Empty folder doesn't increase the depth, so by default 0 until proven otherwise
            int maximumDepthFoundInFolder = 0;
            File[] childFiles = sortedChildren(f);
            // If a child has a larger depth, set the depth to that
            for (File childFile : childFiles) {
                int depthOfThisChild = maxDepth(childFile);
                if (maximumDepthFoundInFolder < depthOfThisChild) {
                    maximumDepthFoundInFolder = depthOfThisChild;
                }
            }
            // A folder that contains stuff counts as 1 by default
            // It's odd that the test cases don't test for empty folder inside of empty folder
            // Personally, empty folders should count because of that, but it is what it is
            if (childFiles.length > 0) {
                maximumDepthFoundInFolder += 1;
            }
            return maximumDepthFoundInFolder;
        }
        // Extra case: if something is somehow neither a file nor a folder
        return 0;
    }

    // ---------------- Part 2 ----------------

    /**
     * Every file under root, in name order at each level, with paths relative to root:
     *   "a.txt", "docs/img/cat.png", "docs/notes.txt"
     * Do the work in the private recursive helper below; this method just starts it.
     */
    public static List<FileInfo> listAll(File root) {
        List<FileInfo> result = new ArrayList<>();
        collect(root, "", result);
        return result;
    }

    /** Recursive helper: adds the files in dir to out. prefix is the path so far, e.g. "docs/img/". */
    /**
     * Base case:      a file ends the path and gets added to out
     * Recursive case: a folder that needs to look through all its file children
     */
    private static void collect(File dir, String prefix, List<FileInfo> out) {
        for (File childFile : sortedChildren(dir)) {
            if (childFile.isFile()) {
                out.add(new FileInfo(prefix + childFile.getName(), totalSize(childFile)));
            } else if (childFile.isDirectory()) {
                collect(childFile, prefix + childFile.getName() + "/", out);
            }
        }
    }

    /** Files whose name ends with "." + ext (ignore case), in listAll order. */
    public static List<FileInfo> findByExtension(File root, String ext) {
        String lowerCaseExt = ext.toLowerCase();
        return listAll(root).stream().filter(file -> file.path().toLowerCase().endsWith(lowerCaseExt)).toList();
    }

    /**
     * Human-readable size, 1024 bytes per KB:
     *   0 -> "0 B"   999 -> "999 B"   1536 -> "1.5 KB"   1048576 -> "1.0 MB"
     * Write it RECURSIVELY with the helper below: if the value is under 1024 (or you've
     * run out of units) format it; otherwise call yourself with value / 1024 and the next unit.
     * Use String.format(Locale.US, ...) so the decimal point is '.' on every computer.
     */
    public static String humanSize(long bytes) {
        return humanSize(bytes, 0);
    }

    /**
     * Base case:      a number < 1024 of the current unit
     * Recursive case: a number >= 1024, move to the next unit
     */
    private static String humanSize(double value, int unit) {
        // "%.0f %s" for bytes, "%.1f %s" for every other unit
        if (value < 1024) {
            if (unit == 0) {
                return String.format(Locale.US, "%.0f %s", value, UNITS[unit]);
            } else {
                return String.format(Locale.US, "%.1f %s", value, UNITS[unit]);
            }
        } else {
            return humanSize(value / 1024, unit + 1);
        }
    }

    // ---------------- Part 3 ----------------

    /**
     * An indented tree, two spaces per level, folders end with '/', each line shows the size:
     *   myfolder/ (2.4 KB)
     *     a.txt (100 B)
     *     docs/ (2.2 KB)
     *       notes.txt (300 B)
     * Folders deeper than maxDepth are listed but their contents are not
     * - Per the tests and assignment instructions, this is a lie
     * (their size still includes everything inside).
     */
    public static String tree(File root, int maxDepth) {
        StringBuilder sb = new StringBuilder();
        // call the recursive helper
        tree(root, 0, maxDepth, sb);
        return sb.toString();
    }

    /**
     * Base case:      a file
     * Recursive case: a folder that has contents so long as depth < maxDepth
     */
    private static void tree(File f, int depth, int maxDepth, StringBuilder sb) {
        // use System.lineSeparator() at the end of each line
        if (f.isFile()) {
            // Equivalent String.format:
            // %s%s (%s)%n
            sb.append("  ".repeat(depth));
            sb.append(f.getName());
            sb.append(" (");
            sb.append(humanSize(totalSize(f)));
            sb.append(")");
            sb.append(System.lineSeparator());
        } else if (f.isDirectory()) {
            // Equivalent String.format:
            // %s%s/ (%s)%n
            sb.append("  ".repeat(depth));
            sb.append(f.getName());
            sb.append("/ (");
            sb.append(humanSize(totalSize(f)));
            sb.append(")");
            sb.append(System.lineSeparator());
            if (depth < maxDepth) {
                for (File childFile : sortedChildren(f)) {
                    tree(childFile, depth + 1, maxDepth, sb);
                }
            }
        }
    }
}
