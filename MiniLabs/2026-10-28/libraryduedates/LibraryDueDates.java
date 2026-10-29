import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map.Entry;

public class LibraryDueDates {
    // Used for printing a list of user-friendly indexed books
    private static void displayBooks(List<String> books) {
        if(books.size() > 0) {
            for(int i = 0; i < books.size(); i++) {
                // Print an index that the user can use to access the books
                System.out.printf("%d. %s%n", i+1, books.get(i));
            }
        } else {
            System.out.println("No books to display!");
        }
    }
    // Used for displaying the due books
    private static void displayDueBooks(Map<LocalDate, List<String>> dueBooks) {
        if(dueBooks.size() > 0) {
            System.out.println("Listing due books...");
            for(Entry<LocalDate, List<String>> entry : dueBooks.entrySet()) {
                // For each LocalDate, print all the books that are due on that date
                System.out.printf("Books due on %s:%n", entry.getKey());
                displayBooks(entry.getValue());
            }
        } else {
            System.out.println("No books are due at this time!");
        }
    }
    // Entry Point
    public static void main(String[] args) {
        // Welcome the user
        System.out.println("Welcome to the library due date manager application!");

        // Build the library of available books
        List<String> availableBooks = new ArrayList<>();
        availableBooks.add("A Book by An Author");
        availableBooks.add("B Book by An Author");
        availableBooks.add("C Book by An Author");
        availableBooks.add("D Book by An Author");
        availableBooks.add("E Book by An Author");
        Collections.sort(availableBooks);

        // Also make a list of due books, sorted by LocalDate
        // Stretch: store due dates for several books in a TreeMap<LocalDate, String>
        // (Note: TreeMap<LocalDate, String> is not really that good of a list because we can technically check out multiple books on the same day and get the same DueDate)
        // (Using TreeMap<LocalDate, List<String>> instead to account for this case of same-day multiple-checkouts)
        Map<LocalDate, List<String>> dueBooks = new TreeMap<>();
        try(Scanner scanner = new Scanner(System.in)) {
            String userInput = "";
            // Loop user input until eventually they give up
            while(!userInput.equals("quit")) {
                // Empty line for formatting purposes
                System.out.println();
                // Display available commands
                System.out.println("Commands:\n- available\n- checkout\n- due\n- return\n- quit");
                // Empty line for formatting purposes
                System.out.println();
                // Get user input and normalize it a bit
                userInput = scanner.nextLine().trim().toLowerCase();
                // Empty line for formatting purposes
                System.out.println();
                switch(userInput) {
                    case "available" -> {
                        displayBooks(availableBooks);
                    }
                    case "checkout" -> {
                        displayBooks(availableBooks);
                        if(availableBooks.size() > 0) {
                            // Empty line for formatting purposes
                            System.out.println();
                            // Get the index of the book the user wants
                            System.out.println("Please enter the index of the book you would like to check out:");
                            String userBookIndex = scanner.nextLine().trim();
                            // Empty line for formatting purposes
                            System.out.println();
                            try {
                                int bookIndex = Integer.parseInt(userBookIndex) - 1;
                                // Ensure user index is valid
                                if(bookIndex >= 0 && bookIndex < availableBooks.size()) {
                                    // Remove from the available books
                                    String checkedOutBook = availableBooks.remove(bookIndex);
                                    // 1. Checkout date = today; books are due in 21 days
                                    LocalDate checkoutDate = LocalDate.now();
                                    LocalDate dueDate = checkoutDate.plusDays(21);
                                    // 2. If the due date lands on a weekend, move it to Monday
                                    if(dueDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
                                        dueDate = dueDate.plusDays(2);
                                    } else if(dueDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                                        dueDate = dueDate.plusDays(1);
                                    }
                                    // 3. Print the due date and how many days until it is due
                                    System.out.printf("Checked out: %s%n", checkedOutBook);
                                    System.out.printf("Due date will be on: %s%n", dueDate);
                                    System.out.printf("There are %d days until it is due.%n", ChronoUnit.DAYS.between(LocalDate.now(), dueDate));
                                    if(dueBooks.containsKey(dueDate)) {
                                        List<String> booksOnDueDate = dueBooks.get(dueDate);
                                        booksOnDueDate.add(checkedOutBook);
                                        Collections.sort(booksOnDueDate);
                                        dueBooks.put(dueDate, booksOnDueDate);
                                    } else {
                                        List<String> booksOnDueDate = new ArrayList<String>();
                                        booksOnDueDate.add(checkedOutBook);
                                        dueBooks.put(dueDate, booksOnDueDate);
                                    }
                                } else {
                                    System.err.println("Error: That index is out of range...");
                                }
                            } catch (NumberFormatException ex) {
                                System.err.println("Error: That was not a valid number...");
                            }
                        }
                    }
                    case "due" -> {
                        displayDueBooks(dueBooks);
                    }
                    case "return" -> {
                        displayDueBooks(dueBooks);
                        if(dueBooks.size() > 0) {
                            // Empty line for formatting purposes
                            System.out.println();
                            // 4. Ask for a return date; if late, charge $0.25 per day
                            System.out.println("Please enter the due date of the book(s) you would like to return (yyyy-mm-dd):");
                            String userDueDate = scanner.nextLine().trim();
                            // Empty line for formatting purposes
                            System.out.println();
                            try {
                                LocalDate dueDateToReturn = LocalDate.parse(userDueDate);
                                List<String> dueBooksOnReturnDate = dueBooks.get(dueDateToReturn);
                                if(dueBooksOnReturnDate != null && dueBooksOnReturnDate.size() > 0) {
                                    // 4. Ask for a return date; if late, charge $0.25 per day
                                    long lateDays = ChronoUnit.DAYS.between(dueDateToReturn, LocalDate.now());
                                    if(lateDays > 0) {
                                        System.out.printf("The book(s) on this date are late by %d days!%n", lateDays);
                                        // 5. Print the fee formatted as currency
                                        System.out.printf("Upon finishing the return, you will owe: %s%n", NumberFormat.getCurrencyInstance(Locale.US).format(0.25f*lateDays));
                                        // Empty line for formatting purposes
                                        System.out.println();
                                    }
                                    // Now that we know the date the user wants to check out, get the specific book they're returning
                                    System.out.println("Please enter the index of the book you would like to return:");
                                    String userBookIndex = scanner.nextLine().trim();
                                    // Empty line for formatting purposes
                                    System.out.println();
                                    try {
                                        int bookIndex = Integer.parseInt(userBookIndex) - 1;
                                        // Ensure valid index
                                        if(bookIndex >= 0 && bookIndex < dueBooksOnReturnDate.size()) {
                                            // Remove from due books, add back to available, sort available
                                            String returnedBook = dueBooksOnReturnDate.remove(bookIndex);
                                            availableBooks.add(returnedBook);
                                            Collections.sort(availableBooks);
                                            // Clean up dueBooks with the updated data
                                            if(dueBooksOnReturnDate.size() == 0) {
                                                dueBooks.remove(dueDateToReturn);
                                            } else {
                                                dueBooks.put(dueDateToReturn, dueBooksOnReturnDate);
                                            }
                                            System.out.printf("Successfully Returned: %s%n", returnedBook);
                                        } else {
                                            System.err.println("Error: That index is out of range...");
                                        }
                                    } catch (NumberFormatException ex) {
                                        System.err.println("Error: That was not a valid number...");
                                    }
                                } else {
                                    System.err.println("Error: No books found on that date...");
                                }
                            } catch (DateTimeParseException ex) {
                                System.err.println("Error: That was not a valid date...");
                            }
                        }
                    }
                    case "quit" -> {
                        System.out.println("Logging out...");
                    }
                }
            }
        }
        System.out.println("Bye!");
    }
}
