# Week 5 Coding Exercise: Disk Usage Analyzer

**Chapter 17 – Recursion and Algorithms**
**Estimated time:** 4–6 hours
**Builds on:** in-class practices *Recursive Warm-ups*, *Binary Search*, *Sort Race*

---

## The scenario

"My laptop is out of space. What's taking it all up?" Tools like WinDirStat, DaisyDisk and `du` answer that question. Under the hood they all do the same thing: a **recursive** walk through folders inside folders inside folders.

A folder is a naturally recursive structure: *a folder contains files and other folders*. You'll build your own disk usage analyzer that:

- computes folder sizes, file counts and nesting depth **recursively**,
- prints an indented tree,
- finds the largest files with a **merge sort you write yourself**,
- looks up files by path with a **recursive binary search**,
- races selection sort against merge sort to show **Big-O** in action.

## What you'll practice

| Chapter 17 concept | Where you use it |
|---|---|
| Base case + recursive case | `totalSize`, `countFiles`, `maxDepth` |
| Recursion on a tree-shaped structure | Walking folders |
| Helper methods with extra parameters (accumulator, depth) | `listAll`, `tree`, `humanSize` |
| Recursive binary search, O(log n) | `binarySearchByPath` |
| Selection sort, O(n²) | `selectionSort` |
| Merge sort, O(n log n): divide, conquer, merge | `mergeSort` |
| Measuring growth rates | The sort race |

You'll also use a `Comparator` (Week 4), a `TreeMap` (Week 3) and `String.format` (Week 1).

## Suggested time plan

| Part | Task | Time |
|---|---|---|
| 0 | Setup | 20–30 min |
| 1 | Recursive folder stats | 60 min |
| 2 | Listing, searching & formatting sizes | 45–60 min |
| 3 | The tree printer | 30–45 min |
| 4 | Sorting & binary search | 60–75 min |
| 5 | The program, sort race & Big-O write-up | 45–60 min |

---

## Part 0 – Setup

> First time using Maven? Complete **`swdv110_Exercise_Setup.md`** first.

```
disk-usage/
├── pom.xml
└── src/
    ├── main/java/edu/cwi/swdv110/diskusage/
    │   ├── Algorithms.java
    │   ├── App.java
    │   ├── FileInfo.java
    │   └── FileScanner.java
    └── test/java/edu/cwi/swdv110/diskusage/
        ├── AlgorithmsTest.java
        └── FileScannerTest.java
```

**File:** `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>edu.cwi.swdv110</groupId>
  <artifactId>disk-usage</artifactId>
  <version>1.0-SNAPSHOT</version>

  <properties>
    <maven.compiler.release>17</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <exec.mainClass>edu.cwi.swdv110.diskusage.App</exec.mainClass>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter</artifactId>
      <version>5.14.4</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.15.0</version>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>3.6.0</version>
      </plugin>
      <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>exec-maven-plugin</artifactId>
        <version>3.6.4</version>
      </plugin>
    </plugins>
  </build>
</project>
```

### Rules for this exercise

- **No** `Files.walk`, `Files.walkFileTree`, `Collections.sort`, `List.sort`, `Arrays.sort` or streams `sorted()` **except** inside the given `sortedChildren` helper. The point is to write the recursion and the sorting yourself.
- Use `java.io.File`:
  - `isFile()` and `isDirectory()` tell you what something is.
  - `length()` gives a file's size in bytes.
  - `getName()` gives its name.
  - `listFiles()` returns its children, or `null` if it can't be read.

### Starter code

**File:** `src/main/java/edu/cwi/swdv110/diskusage/FileInfo.java`

```java
package edu.cwi.swdv110.diskusage;

/** One file: its path relative to the scanned folder (always using '/') and its size in bytes. */
public record FileInfo(String path, long size) { }
```

**File:** `src/main/java/edu/cwi/swdv110/diskusage/FileScanner.java`

```java
package edu.cwi.swdv110.diskusage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/** Recursive folder walking. Files.walk() and other built-in walkers are off-limits. */
public final class FileScanner {

    private static final String[] UNITS = {"B", "KB", "MB", "GB", "TB"};

    private FileScanner() { }

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
        // TODO
        return 0;
    }

    /** Number of FILES (not folders) in f and everything under it. A single file counts as 1. */
    public static int countFiles(File f) {
        // TODO
        return 0;
    }

    /**
     * How many levels deep the nesting goes.
     *   a file, or an empty folder           -> 0
     *   a folder that only contains files     -> 1
     *   otherwise                             -> 1 + the deepest child
     */
    public static int maxDepth(File f) {
        // TODO
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
        // TODO call collect(...) to fill result
        return result;
    }

    /** Recursive helper: adds the files in dir to out. prefix is the path so far, e.g. "docs/img/". */
    private static void collect(File dir, String prefix, List<FileInfo> out) {
        // TODO
    }

    /** Files whose name ends with "." + ext (ignore case), in listAll order. */
    public static List<FileInfo> findByExtension(File root, String ext) {
        // TODO
        return new ArrayList<>();
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

    private static String humanSize(double value, int unit) {
        // TODO – "%.0f %s" for bytes, "%.1f %s" for every other unit
        return "";
    }

    // ---------------- Part 3 ----------------

    /**
     * An indented tree, two spaces per level, folders end with '/', each line shows the size:
     *   myfolder/ (2.4 KB)
     *     a.txt (100 B)
     *     docs/ (2.2 KB)
     *       notes.txt (300 B)
     * Folders deeper than maxDepth are listed but their contents are not
     * (their size still includes everything inside).
     */
    public static String tree(File root, int maxDepth) {
        StringBuilder sb = new StringBuilder();
        // TODO call the recursive helper
        return sb.toString();
    }

    private static void tree(File f, int depth, int maxDepth, StringBuilder sb) {
        // TODO – use System.lineSeparator() at the end of each line
    }
}
```

**File:** `src/main/java/edu/cwi/swdv110/diskusage/Algorithms.java`

```java
package edu.cwi.swdv110.diskusage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Hand-written searching and sorting. Collections.sort / List.sort are off-limits here. */
public final class Algorithms {

    /** Largest first; equal sizes ordered by path A–Z. */
    public static final Comparator<FileInfo> BY_SIZE_DESC =
            Comparator.comparingLong(FileInfo::size).reversed().thenComparing(FileInfo::path);

    /** Path A–Z (the order binary search needs). */
    public static final Comparator<FileInfo> BY_PATH = Comparator.comparing(FileInfo::path);

    private Algorithms() { }

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
```

**File:** `src/main/java/edu/cwi/swdv110/diskusage/App.java`

```java
package edu.cwi.swdv110.diskusage;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

public class App {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Folder to scan (Enter for this project): ");
        String input = sc.hasNextLine() ? sc.nextLine().trim() : "";
        File root = Path.of(input.isEmpty() ? "." : input).toAbsolutePath().normalize().toFile();
        if (!root.isDirectory()) {
            System.out.println("Not a folder: " + root);
            return;
        }

        // TODO Part 5a: print the tree, 2 levels deep
        // TODO Part 5b: print "Files: N   Total: X   Deepest nesting: N levels"
        // TODO Part 5c: print the 5 largest files (size right-aligned in 10 columns, then path)
        // TODO Part 5d: count files per extension in a TreeMap<String, Integer>
        //               (files without a '.' count as "(none)") and print the map
        // TODO Part 5e: loop – 'quit' ends, 'race' calls sortRace(), anything else is a path
        //               to look up with binarySearchByPath (sort the list BY_PATH first!)
    }

    /** GIVEN: times both sorts on random data that doubles in size each round. */
    static void sortRace() {
        Random rnd = new Random(110);
        System.out.printf("%8s %14s %12s%n", "n", "selection ms", "merge ms");
        for (int n = 1_000; n <= 16_000; n *= 2) {
            List<FileInfo> data = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                data.add(new FileInfo("file" + rnd.nextInt(1_000_000), rnd.nextInt(10_000_000)));
            }
            long t0 = System.nanoTime();
            Algorithms.selectionSort(data, Algorithms.BY_SIZE_DESC);
            long t1 = System.nanoTime();
            Algorithms.mergeSort(data, Algorithms.BY_SIZE_DESC);
            long t2 = System.nanoTime();
            System.out.printf("%8d %14.1f %12.1f%n", n, (t1 - t0) / 1e6, (t2 - t1) / 1e6);
        }
    }
}
```

### Provided tests

`@TempDir` gives each test a fresh, empty folder that JUnit deletes afterward. The `setUp` method builds this small folder tree inside it:

```
root/
├── a.txt            100 bytes
├── b.log             50 bytes
├── docs/
│   ├── img/
│   │   └── cat.png 2000 bytes
│   └── notes.txt    300 bytes
└── empty/
```

**File:** `src/test/java/edu/cwi/swdv110/diskusage/FileScannerTest.java`

```java
package edu.cwi.swdv110.diskusage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileScannerTest {

    @TempDir
    Path tmp;
    File root;

    private static void makeFile(Path p, int bytes) throws IOException {
        Files.createDirectories(p.getParent());
        Files.write(p, new byte[bytes]);
    }

    @BeforeEach
    void setUp() throws IOException {
        Path r = tmp.resolve("root");
        makeFile(r.resolve("a.txt"), 100);
        makeFile(r.resolve("b.log"), 50);
        makeFile(r.resolve("docs/notes.txt"), 300);
        makeFile(r.resolve("docs/img/cat.png"), 2000);
        Files.createDirectories(r.resolve("empty"));
        root = r.toFile();
    }

    // ---------- Part 1 ----------
    @Test
    void totalSize() {
        assertEquals(2450, FileScanner.totalSize(root));
        assertEquals(2300, FileScanner.totalSize(new File(root, "docs")));
        assertEquals(100, FileScanner.totalSize(new File(root, "a.txt")));
        assertEquals(0, FileScanner.totalSize(new File(root, "empty")));
    }

    @Test
    void countFiles() {
        assertEquals(4, FileScanner.countFiles(root));
        assertEquals(1, FileScanner.countFiles(new File(root, "a.txt")));
        assertEquals(0, FileScanner.countFiles(new File(root, "empty")));
    }

    @Test
    void maxDepth() {
        assertEquals(3, FileScanner.maxDepth(root));
        assertEquals(2, FileScanner.maxDepth(new File(root, "docs")));
        assertEquals(1, FileScanner.maxDepth(new File(root, "docs/img")));
        assertEquals(0, FileScanner.maxDepth(new File(root, "empty")));
        assertEquals(0, FileScanner.maxDepth(new File(root, "a.txt")));
    }

    // ---------- Part 2 ----------
    @Test
    void listAllUsesRelativePathsInNameOrder() {
        assertEquals(List.of(
                new FileInfo("a.txt", 100),
                new FileInfo("b.log", 50),
                new FileInfo("docs/img/cat.png", 2000),
                new FileInfo("docs/notes.txt", 300)), FileScanner.listAll(root));
    }

    @Test
    void findByExtension() {
        assertEquals(List.of(new FileInfo("a.txt", 100), new FileInfo("docs/notes.txt", 300)),
                FileScanner.findByExtension(root, "TXT"));
        assertEquals(List.of(), FileScanner.findByExtension(root, "java"));
    }

    @Test
    void humanSize() {
        assertEquals("0 B", FileScanner.humanSize(0));
        assertEquals("999 B", FileScanner.humanSize(999));
        assertEquals("1.0 KB", FileScanner.humanSize(1024));
        assertEquals("1.5 KB", FileScanner.humanSize(1536));
        assertEquals("1.0 MB", FileScanner.humanSize(1024 * 1024));
        assertEquals("2.5 GB", FileScanner.humanSize(2_684_354_560L));
    }

    // ---------- Part 3 ----------
    @Test
    void treeFullDepth() {
        String nl = System.lineSeparator();
        String expected =
                "root/ (2.4 KB)" + nl
              + "  a.txt (100 B)" + nl
              + "  b.log (50 B)" + nl
              + "  docs/ (2.2 KB)" + nl
              + "    img/ (2.0 KB)" + nl
              + "      cat.png (2.0 KB)" + nl
              + "    notes.txt (300 B)" + nl
              + "  empty/ (0 B)" + nl;
        assertEquals(expected, FileScanner.tree(root, 10));
    }

    @Test
    void treeStopsAtMaxDepth() {
        String nl = System.lineSeparator();
        String expected =
                "root/ (2.4 KB)" + nl
              + "  a.txt (100 B)" + nl
              + "  b.log (50 B)" + nl
              + "  docs/ (2.2 KB)" + nl
              + "  empty/ (0 B)" + nl;
        assertEquals(expected, FileScanner.tree(root, 1));
    }
}
```

**File:** `src/test/java/edu/cwi/swdv110/diskusage/AlgorithmsTest.java`

```java
package edu.cwi.swdv110.diskusage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class AlgorithmsTest {

    private static final List<FileInfo> FILES = List.of(
            new FileInfo("b.log", 50),
            new FileInfo("docs/img/cat.png", 2000),
            new FileInfo("a.txt", 100),
            new FileInfo("z.txt", 100),
            new FileInfo("docs/notes.txt", 300));

    @Test
    void selectionSortIntegersAndDoesNotModifyInput() {
        List<Integer> input = new ArrayList<>(List.of(5, 3, 9, 1, 3));
        assertEquals(List.of(1, 3, 3, 5, 9), Algorithms.selectionSort(input, Comparator.naturalOrder()));
        assertEquals(List.of(5, 3, 9, 1, 3), input, "input list must not change");
    }

    @Test
    void mergeSortIntegers() {
        assertEquals(List.of(), Algorithms.mergeSort(List.<Integer>of(), Comparator.naturalOrder()));
        assertEquals(List.of(7), Algorithms.mergeSort(List.of(7), Comparator.naturalOrder()));
        assertEquals(List.of(9, 5, 3, 3, 1),
                Algorithms.mergeSort(List.of(5, 3, 9, 1, 3), Comparator.reverseOrder()));
    }

    @Test
    void mergeSortMatchesBuiltInOnRandomData() {
        Random rnd = new Random(17);
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 2_000; i++) {
            data.add(rnd.nextInt(500));
        }
        List<Integer> expected = new ArrayList<>(data);
        expected.sort(null);
        assertEquals(expected, Algorithms.mergeSort(data, Comparator.naturalOrder()));
        assertEquals(expected, Algorithms.selectionSort(data, Comparator.naturalOrder()));
    }

    @Test
    void mergeSortIsStable() {
        // Sort only by size: a.txt and z.txt are equal, so they must keep their input order.
        List<FileInfo> sorted = Algorithms.mergeSort(FILES, Comparator.comparingLong(FileInfo::size));
        assertEquals(List.of("b.log", "a.txt", "z.txt", "docs/notes.txt", "docs/img/cat.png"),
                sorted.stream().map(FileInfo::path).toList());
    }

    @Test
    void largest() {
        assertEquals(List.of(new FileInfo("docs/img/cat.png", 2000),
                        new FileInfo("docs/notes.txt", 300),
                        new FileInfo("a.txt", 100)),
                Algorithms.largest(FILES, 3));
        assertEquals(5, Algorithms.largest(FILES, 50).size());
    }

    @Test
    void binarySearch() {
        List<FileInfo> byPath = Algorithms.mergeSort(FILES, Algorithms.BY_PATH);
        assertEquals(0, Algorithms.binarySearchByPath(byPath, "a.txt"));
        assertEquals(3, Algorithms.binarySearchByPath(byPath, "docs/notes.txt"));
        assertEquals(4, Algorithms.binarySearchByPath(byPath, "z.txt"));
        assertEquals(-1, Algorithms.binarySearchByPath(byPath, "missing.txt"));
        assertEquals(-1, Algorithms.binarySearchByPath(List.of(), "a.txt"));
    }
}
```

---

## Part 1 – Recursive folder stats

For every recursive method, write two comments **before** you write code:

```java
// Base case: ...
// Recursive case: ...
```

`totalSize`, `countFiles` and `maxDepth` all have the same shape: *if it's a file, answer directly; otherwise loop over `sortedChildren(f)` and combine the answers from the recursive calls.* What changes is how you **combine** them: sum, count, or max.

> Each folder makes one recursive call **per child**, so the calls form a tree, not a straight line. This is the kind of problem where recursion is much easier than a loop.

## Part 2 – Listing, searching & formatting

- `listAll` uses the **helper method** pattern: the public method starts the recursion, and the private helper carries the extra information it needs, the `prefix` and the shared `out` list.
- `humanSize` is recursion on a *number*. Each call divides by 1024 and moves to the next unit, and the base case is "small enough to print."

## Part 3 – The tree printer

The helper carries `depth` (for indentation, `"  ".repeat(depth)`) and appends one line for `f` itself. If `f` is a folder **and** `depth < maxDepth`, it then recurses into each child with `depth + 1`.

✅ Checkpoint: `mvn -Dtest=FileScannerTest test` passes.

## Part 4 – Sorting & binary search

- Start with `selectionSort`; it's the version from class with `cmp.compare(...)` in place of `<`.
- `mergeSort`: `list.subList(0, mid)` and `list.subList(mid, list.size())` give you the halves. The `merge` step walks both halves with two indexes and always takes the smaller front item. Use `<= 0` when you compare; that's what makes it **stable**.
- `binarySearch`: compare `a.get(mid).path()` with `path` using `compareTo`, then recurse into the half that could still contain it.

✅ Checkpoint: `mvn test`. All 14 tests pass.

## Part 5 – The program, sort race & Big-O

Finish `App` and run `mvn -q compile exec:java`. Scan this project folder first (press Enter), then try a bigger folder like your Documents. Output looks like this (your sizes will differ):

```text
Folder to scan (Enter for this project):
disk-usage/ (62.8 KB)
  pom.xml (1.4 KB)
  src/ (16.2 KB)
    main/ (9.4 KB)
    test/ (6.7 KB)
  target/ (45.2 KB)
    classes/ (18.0 KB)
    generated-sources/ (0 B)
    generated-test-sources/ (0 B)
    maven-status/ (1.4 KB)
    surefire-reports/ (16.5 KB)
    test-classes/ (9.3 KB)

Files: 21   Total: 62.8 KB   Deepest nesting: 8 levels

Largest files:
      8.0 KB  target/surefire-reports/TEST-edu.cwi.swdv110.diskusage.FileScannerTest.xml
      7.8 KB  target/surefire-reports/TEST-edu.cwi.swdv110.diskusage.AlgorithmsTest.xml
      5.9 KB  target/classes/edu/cwi/swdv110/diskusage/App.class
      5.8 KB  target/classes/edu/cwi/swdv110/diskusage/FileScanner.class
      4.8 KB  target/test-classes/edu/cwi/swdv110/diskusage/FileScannerTest.class

Files by extension: {class=6, java=6, lst=4, txt=2, xml=3}

Find path (or 'race' / 'quit'): pom.xml
Found at index 0: 1.4 KB

Find path (or 'race' / 'quit'): race
       n   selection ms     merge ms
    1000           10.7          1.2
    2000           14.5          0.9
    4000           43.8          1.1
    8000          184.9          6.3
   16000          721.0          3.5

Find path (or 'race' / 'quit'): quit
```

> The first rows of the race are noisy because the JVM is still "warming up" (compiling hot code as it runs). Watch the trend in the bigger rows. For a cleaner comparison, run `race` twice and use the second set of numbers.

### Big-O write-up (in `README.md`)

1. Copy your **race** results into a table. Each time `n` doubles, about how many times longer does selection sort take? Merge sort? Explain how those ratios match O(n²) and O(n log n).
2. What is the Big-O of `totalSize` in terms of *f*, the number of files and folders? Why?
3. Binary search needs a sorted list. If you only need to search **once**, is it worth sorting first? What if you search 1,000 times?
4. What would happen if `maxDepth` had no base case? What error would Java throw, and why?
5. Name one more real-world problem (not folders) that has a recursive structure.

---

## Stretch goals (pick any)

- **Biggest folders:** list the 5 largest *folders*, not just files.
- **Duplicates:** find files with the same size and name in different folders (`Map<String, List<FileInfo>>`).
- **Iterative version:** rewrite `totalSize` without recursion using an `ArrayDeque` as a stack. Compare it with the recursive version in your README.
- **Quicksort:** add a recursive quicksort to the race. Then try it on data that's *already sorted*. What happens?
- **Memoized Fibonacci:** add `fib(int n)` with a `HashMap` cache and time `fib(40)` with and without it.

---

## Submission checklist

- [ ] `mvn test`: all provided tests pass
- [ ] No banned methods (`Files.walk`, `Collections.sort`, `List.sort`…) outside `sortedChildren`
- [ ] Every recursive method has `// Base case` and `// Recursive case` comments
- [ ] `App` runs, including the sort race
- [ ] `README.md` contains the Big-O write-up
- [ ] Pushed to GitHub. Remember the `.gitignore`, or `target/` will show up in your own scans!

## Grading (100 points)

| Area | Points |
|---|---|
| `FileScannerTest` passes | 25 |
| `AlgorithmsTest` passes | 25 |
| `App` output (tree, totals, largest, extensions, search, race) | 15 |
| Recursion quality: clear base/recursive cases, helper methods | 10 |
| Big-O write-up | 20 |
| Git: meaningful commits | 5 |
| Stretch goals | up to +5 bonus |
