package edu.cwi.swdv110.books;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Every SQL statement for the books table lives in this class.
 * RULES: every method opens its own Connection in try-with-resources,
 * and every value from outside goes in through a ? parameter – never with +.
 */
public class BookDAO {

    private final String url;

    public BookDAO(String url) {
        this.url = url;
    }

    /** Helper: builds a Book from the CURRENT row of a ResultSet. is_read is stored as 0/1. */
    private Book toBook(ResultSet rs) throws SQLException {
        // TODO Part 2
        return null;
    }

    // ---------------- Part 2: reading ----------------

    /** All books ordered by title. */
    public List<Book> getAll() throws SQLException {
        // TODO
        return new ArrayList<>();
    }

    /** The book with this exact ISBN, or Optional.empty(). */
    public Optional<Book> findByIsbn(String isbn) throws SQLException {
        // TODO
        return Optional.empty();
    }

    /**
     * Books whose title OR author contains term (SQLite's LIKE ignores case for A–Z),
     * ordered by title. Hint: the % wildcards go in the PARAMETER, not the SQL:
     *   ps.setString(1, "%" + term + "%");
     */
    public List<Book> search(String term) throws SQLException {
        // TODO
        return new ArrayList<>();
    }

    /** Books not yet read, ordered by title. */
    public List<Book> unread() throws SQLException {
        // TODO
        return new ArrayList<>();
    }

    // ---------------- Part 3: writing ----------------

    /**
     * Inserts the book (ignore b.id() – the database assigns it) and returns the new id.
     * Hint: prepareStatement(sql, Statement.RETURN_GENERATED_KEYS), then getGeneratedKeys().
     * A duplicate ISBN makes the database throw SQLException – let it propagate.
     */
    public int add(Book b) throws SQLException {
        // TODO
        return -1;
    }

    /** Updates every column of the row whose id = b.id(). True if a row changed. */
    public boolean update(Book b) throws SQLException {
        // TODO
        return false;
    }

    /**
     * Marks a book read and sets its rating. Rating outside 1–5 ->
     * IllegalArgumentException BEFORE touching the database. True if a row changed.
     */
    public boolean markRead(String isbn, int rating) throws SQLException {
        // TODO
        return false;
    }

    /** Deletes by ISBN. True if a row was deleted. */
    public boolean delete(String isbn) throws SQLException {
        // TODO
        return false;
    }

    // ---------------- Part 4: reports ----------------

    /**
     * author -> number of books, most books first, ties by author A–Z.
     * Let SQL do the work: GROUP BY, COUNT(*), ORDER BY. Use a LinkedHashMap to keep that order.
     */
    public Map<String, Integer> countByAuthor() throws SQLException {
        // TODO
        return new LinkedHashMap<>();
    }

    /** Average rating of RATED books (rating > 0); 0.0 if none are rated. Use SQL's AVG. */
    public double averageRating() throws SQLException {
        // TODO
        return 0.0;
    }
}
