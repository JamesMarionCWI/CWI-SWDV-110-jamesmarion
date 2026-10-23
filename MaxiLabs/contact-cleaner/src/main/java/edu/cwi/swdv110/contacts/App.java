package edu.cwi.swdv110.contacts;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static String getUserInputForSearch(Scanner scanner) {
        System.out.println();
        System.out.print("Search (or 'quit'): ");
        return scanner.nextLine().trim().toLowerCase();
    }

    public static void main(String[] args) throws IOException {
        // Reading files is covered later in Murach – this one line gives you every line of the file.
        List<String> lines = Files.readAllLines(Path.of("contacts.txt"));
        List<Contact> contacts = new ArrayList<>();

        // Part 5a: loop over lines
        // - skip blank lines and lines that start with '#'
        // - parseLine(...) each remaining line and add it to contacts
        // - catch IllegalArgumentException and print "Skipped line N: <message>"
        // (N is the line number in the file, starting at 1)
        int numErrors = 0;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).isBlank() || lines.get(i).charAt(0) == '#') {
                // Skip blank lines and header lines
                continue;
            }
            try {
                contacts.add(ContactParser.parseLine(lines.get(i)));
            } catch (IllegalArgumentException ex) {
                if (numErrors == 0) {
                    // Empty line for formatting
                    System.out.println();
                }
                System.err.printf("Skipped line %d: %s%n", i + 1, ex.getMessage());
                numErrors++;
            }
        }

        // Empty line for formatting
        System.out.println();

        // Part 5b: print ContactReport.buildReport(contacts)
        System.out.println(ContactReport.buildReport(contacts));

        // Part 5c: search loop
        // - prompt "Search (or 'quit'): "
        // - 'quit' (any capitalization) ends the program
        // - otherwise print every contact whose name contains the term, ignoring case,
        // showing the name and the MASKED email, then "N match(es)"
        try (Scanner sc = new Scanner(System.in)) {
            String userInput = getUserInputForSearch(sc);
            while (!userInput.equals("quit")) {
                int numMatches = 0;
                for (Contact contact : contacts) {
                    if (contact.name().toLowerCase().contains(userInput)) {
                        System.out.printf("%-22s %-30s%n", contact.name(), ContactParser.maskEmail(contact.email()));
                        numMatches++;
                    }
                }
                System.out.printf("%d match(es)%n", numMatches);

                userInput = getUserInputForSearch(sc);
            }
        }

        System.out.println("Bye!");
    }
}
