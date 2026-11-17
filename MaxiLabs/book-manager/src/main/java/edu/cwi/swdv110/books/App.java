package edu.cwi.swdv110.books;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.Year;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/** Console front end. Notice: there is NO SQL in this class – it only talks to the DAO. */
public class App {

    private static final String URL = "jdbc:sqlite:books.db";
    private static final BookDAO dao = new BookDAO(URL);
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        try {
            Database.initialize(URL);
            if (dao.getAll().isEmpty()) {
                Database.runScript(URL, Path.of("seed.sql"));
                System.out.println("New database - loaded seed.sql");
            }
        } catch (SQLException e) {
            System.out.println("Could not open the database: " + e.getMessage());
            return;
        }

        while (true) {
            System.out.println("""

                    1) List all   2) Search   3) Unread   4) Add   5) Mark read
                    6) Edit       7) Delete   8) Stats    0) Quit""");
            String choice = prompt("Choice");
            try {
                switch (choice) {
                case "1" -> print(dao.getAll());
                case "2" -> print(dao.search(prompt("Title or author contains")));
                case "3" -> print(dao.unread());
                case "4" -> addBook();
                case "5" -> {
                    String isbn = cleanIsbn(prompt("ISBN"));
                    int rating = Integer.parseInt(prompt("Rating 1-5"));
                    System.out.println(dao.markRead(isbn, rating) ? "Updated" : "No book with that ISBN");
                }
                case "6" -> editBook();
                case "7" -> {
                    String isbn = cleanIsbn(prompt("ISBN to delete"));
                    if (prompt("Are you sure? (y/n)").equalsIgnoreCase("y")) {
                        System.out.println(dao.delete(isbn) ? "Deleted" : "No book with that ISBN");
                    }
                }
                case "8" -> {
                    System.out.println("Books per author: " + dao.countByAuthor());
                    System.out.printf("Average rating: %.2f%n", dao.averageRating());
                }
                case "0" -> {
                    System.out.println("Bye!");
                    return;
                }
                default -> System.out.println("Pick 0-8");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (IllegalArgumentException e) { // also catches NumberFormatException
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
    }

    private static String prompt(String label) {
        System.out.print(label + ": ");
        return sc.hasNextLine() ? sc.nextLine().trim() : "0";
    }

    /**
     * Part 5: remove '-' and ' ', then require exactly 10 or 13 digits.
     * Otherwise throw IllegalArgumentException("ISBN must be 10 or 13 digits").
     *   "978-0-13-468599-1" -> "9780134685991"
     */
    static String cleanIsbn(String raw) {
        String cleaned = raw.replace("-", "").replace(" ", "");
        if (cleaned.length() != 10 && cleaned.length() != 13) {
            throw new IllegalArgumentException("ISBN must be 10 or 13 digits");
        }
        return cleaned;
    }

    /**
     * Part 5: prompt for ISBN (cleanIsbn), title, author (both required) and year
     * (1450 through Year.now().getValue()), then dao.add(...) and print "Added with id N".
     * New books start unread with rating 0.
     */
    private static void addBook() throws SQLException {
        try {
            String isbn = cleanIsbn(prompt("ISBN"));
            String title = prompt("Title");
            String author = prompt("Author");
            String yearInput = prompt("Year");
            Year year = Year.parse(yearInput);
            if (year.isBefore(Year.of(1450)) || year.isAfter(Year.now())) {
                throw new IllegalArgumentException("Year is not in a valid range...");
            }
            int id = dao.add(new Book(0, isbn, title, author, year.getValue(), 0, false));
            System.out.printf("Added with id %d%n", id);
        } catch (DateTimeParseException ex) {
            System.out.println("Invalid Input: " + ex);
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid Input: " + ex);
        }
    }

    /**
     * Part 5: find the book by ISBN ("No book with that ISBN" if missing), then prompt
     * for a new title and author showing the current value in [brackets]; pressing Enter
     * keeps the current value. Build a new Book with the SAME id and call dao.update.
     */
    private static void editBook() throws SQLException {
        String isbn = cleanIsbn(prompt("ISBN to edit"));
        Optional<Book> bookOpt = dao.findByIsbn(isbn);
        if (bookOpt.isEmpty()) {
            System.out.println("No book with that ISBN");
            return;
        }
        Book book = bookOpt.get();
        boolean doesUpdateExist = false;

        String newTitle = prompt("Title [" + book.title() + "]");
        if (newTitle.isBlank()) {
            newTitle = book.title();
        } else {
            doesUpdateExist = true;
        }

        String newAuthor = prompt("Author [" + book.author() + "]");
        if (newAuthor.isBlank()) {
            newAuthor = book.author();
        } else {
            doesUpdateExist = true;
        }

        // No point in running the SQL if we're just keeping the defaults
        if (doesUpdateExist) {
            Book newBook = new Book(book.id(), book.isbn(), newTitle, newAuthor, book.year(), book.rating(), book.read());
            // If something happened that makes our update fail,
            // then clearly someone in another thread or process deleted the book
            // in between the time we accessed the database to get the book and now.
            // Either way, if this somehow returns false, then there's no book to update.
            System.out.println(dao.update(newBook) ? "Updated" : "No book with that ISBN");
        } else {
            System.out.println("No changes to make. Keeping current values.");
        }
    }

    private static void print(List<Book> books) {
        System.out.printf("%-14s %-28s %-20s %4s  %-5s %s%n", "ISBN", "TITLE", "AUTHOR", "YEAR", "READ", "RATING");
        for (Book b : books) {
            System.out.printf("%-14s %-28s %-20s %4d  %-5s %s%n", b.isbn(), b.title(), b.author(), b.year(), b.read() ? "yes" : "no", "*".repeat(b.rating()));
        }
        System.out.println(books.size() + " book(s)");
    }
}
