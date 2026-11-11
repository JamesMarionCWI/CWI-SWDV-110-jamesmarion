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
            sc.close();
            return;
        }

        // Part 5a: print the tree, 2 levels deep
        System.out.println(FileScanner.tree(root, 2));

        // Part 5b: print "Files: N   Total: X   Deepest nesting: N levels"

        System.out.printf("Files: %d   Total: %s   Deepest nesting: %d levels%n", FileScanner.countFiles(root), FileScanner.humanSize(FileScanner.totalSize(root)), FileScanner.maxDepth(root));

        // Part 5c: print the 5 largest files (size right-aligned in 10 columns, then path)

        // Empty line for formatting purposes
        System.out.println();
        System.out.println("Largest files:");
        for (FileInfo largeFile : Algorithms.largest(FileScanner.listAll(root), 5)) {
            System.out.printf("%-10s %s%n", FileScanner.humanSize(largeFile.size()), largeFile.path());
        }

        // Part 5d: count files per extension in a TreeMap<String, Integer>
        //               (files without a '.' count as "(none)") and print the map

        // Empty line for formatting purposes
        System.out.println();

        Map<String, Integer> extensionCounts = new TreeMap<>();
        for (FileInfo file : FileScanner.listAll(root)) {
            String fileName = file.path().substring(file.path().lastIndexOf('/') + 1);
            // Ignore all dots up until the last '/' is found, if any.
            // This means we're ignoring all "hidden" folders.
            int extensionDotIndex = fileName.lastIndexOf('.');

            // Files that start with a dot are "hidden" files and should be ignored
            // Files that end with a dot are weird as hell and should also be ignored
            if (extensionDotIndex > 0 && extensionDotIndex < fileName.length() - 1) {
                // Add 1 per occurrence
                extensionCounts.merge(fileName.substring(extensionDotIndex + 1).toLowerCase(), 1, Integer::sum);
            }
        }
        System.out.println("Files by extension: " + extensionCounts);

        // Part 5e: loop – 'quit' ends, 'race' calls sortRace(), anything else is a path
        //               to look up with binarySearchByPath (sort the list BY_PATH first!)
        input = "";
        List<FileInfo> sorted = FileScanner.listAll(root).stream().sorted(Algorithms.BY_PATH).toList();
        while (!input.equals("quit")) {
            // Empty line for formatting purposes
            System.out.println();

            System.out.print("Find path (or 'race' / 'quit'): ");
            input = sc.hasNextLine() ? sc.nextLine().trim() : "";
            if (!input.isBlank()) {
                switch (input) {
                case "quit":
                    break;
                case "race":
                    sortRace();
                    break;
                default:
                    int indexOfInput = Algorithms.binarySearchByPath(sorted, input);
                    if (indexOfInput == -1) {
                        System.out.println("Not found");
                    } else {
                        System.out.printf("Found at index %d: %s%n", indexOfInput, FileScanner.humanSize(sorted.get(indexOfInput).size()));
                    }
                    break;
                }
            }

        }

        sc.close();
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
