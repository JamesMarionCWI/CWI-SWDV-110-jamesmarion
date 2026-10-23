package edu.cwi.swdv110.contacts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws IOException {
        // Reading files is covered later in Murach – this one line gives you every line of the file.
        List<String> lines = Files.readAllLines(Path.of("contacts.txt"));
        List<Contact> contacts = new ArrayList<>();

        // TODO Part 5a: loop over lines
        // - skip blank lines and lines that start with '#'
        // - parseLine(...) each remaining line and add it to contacts
        // - catch IllegalArgumentException and print "Skipped line N: <message>"
        // (N is the line number in the file, starting at 1)

        // TODO Part 5b: print ContactReport.buildReport(contacts)

        // TODO Part 5c: search loop
        // - prompt "Search (or 'quit'): "
        // - 'quit' (any capitalization) ends the program
        // - otherwise print every contact whose name contains the term, ignoring case,
        // showing the name and the MASKED email, then "N match(es)"
        Scanner sc = new Scanner(System.in);
    }
}
