# Week 6 Coding Exercise: Book Collection Manager

**Chapter 19 – Database Integration (SQL + JDBC)**
**Estimated time:** 4–6 hours
**Builds on:** in-class practices *Query the Products Table*, *Product List Report*, *Product Manager (CRUD)*

---

## The scenario

You're building a personal library app: track the books you own, which ones you've read, and how you rated them. Unlike every exercise so far, **the data survives after the program ends**, because it lives in a real SQL database.

You'll:

1. design the table and write SQL by hand in a database browser,
2. connect from Java with **JDBC**,
3. put every SQL statement in a **DAO** (Data Access Object) class,
4. use `PreparedStatement` everywhere, and prove that it blocks **SQL injection**,
5. wire it all to a console menu.

**Next week (Chapters 12–13)** you'll put a JavaFX window on top of this same DAO, so keep it clean!

## What you'll practice

| Chapter 19 concept | Where you use it |
|---|---|
| `CREATE TABLE`, primary key, `UNIQUE`, `NOT NULL`, `CHECK` | `schema.sql` |
| `SELECT … WHERE … ORDER BY`, `LIKE`, `GROUP BY`, `COUNT`, `AVG` | `queries.sql` and the DAO |
| `INSERT`, `UPDATE`, `DELETE` | The DAO |
| JDBC: `Connection`, `PreparedStatement`, `ResultSet` | `BookDAO` |
| try-with-resources | Every DAO method |
| Generated keys | `add()` returns the new id |
| The DAO pattern | All SQL in one class; `App` contains none |
| SQL injection & parameters (`?`) | The injection tests |

## Suggested time plan

| Part | Task | Time |
|---|---|---|
| 0 | Setup (Maven + SQLite driver + DB Browser) | 30–45 min |
| 1 | Schema & SQL by hand | 45–60 min |
| 2 | Reading with JDBC (`getAll`, `findByIsbn`, `search`, `unread`) | 60 min |
| 3 | Writing with JDBC (`add`, `update`, `markRead`, `delete`) | 45–60 min |
| 4 | Reports (`countByAuthor`, `averageRating`) | 20–30 min |
| 5 | Console app (`App`) | 45 min |
| 6 | README reflection | 15 min |

---

## Part 0 – Setup

> First time using Maven? Complete **`swdv110_Exercise_Setup.md`** first.

### Why SQLite?

SQLite is a complete SQL database stored in **a single file** (`books.db`). There's no server to install, and the JDBC driver is just a Maven dependency. The SQL you write is standard and works almost unchanged on MySQL, PostgreSQL or SQL Server.

### Install a database browser (for Part 1)

Download **DB Browser for SQLite** (free, Windows/macOS/Linux) from <https://sqlitebrowser.org/dl/>. You'll use it to run SQL by hand and to look inside `books.db` while your Java program runs.

### Project layout

```
book-manager/
├── pom.xml
├── schema.sql        <- you write this (Part 1)
├── seed.sql          (given – sample books)
├── queries.sql       <- you write this (Part 1)
└── src/
    ├── main/java/edu/cwi/swdv110/books/
    │   ├── App.java
    │   ├── Book.java
    │   ├── BookDAO.java
    │   └── Database.java
    └── test/java/edu/cwi/swdv110/books/
        └── BookDAOTest.java
```

This pom adds **one new dependency**, the SQLite JDBC driver. When Maven sees it, it downloads the driver and puts it on the classpath automatically. You don't copy any `.jar` files by hand.

**File:** `pom.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>edu.cwi.swdv110</groupId>
  <artifactId>book-manager</artifactId>
  <version>1.0-SNAPSHOT</version>

  <properties>
    <maven.compiler.release>17</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <exec.mainClass>edu.cwi.swdv110.books.App</exec.mainClass>
  </properties>

  <dependencies>
    <!-- NEW: the SQLite JDBC driver -->
    <dependency>
      <groupId>org.xerial</groupId>
      <artifactId>sqlite-jdbc</artifactId>
      <version>3.53.4.0</version>
    </dependency>
    <!-- The driver logs through SLF4J; this keeps it quiet -->
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-nop</artifactId>
      <version>2.0.17</version>
    </dependency>

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

**File:** `seed.sql`

```sql
-- Sample data. App runs this automatically the first time books.db is empty.
INSERT INTO books (isbn, title, author, year, rating, is_read) VALUES ('9780134685991', 'Effective Java', 'Joshua Bloch', 2018, 5, 1);
INSERT INTO books (isbn, title, author, year, rating, is_read) VALUES ('9780132350884', 'Clean Code', 'Robert C. Martin', 2008, 4, 1);
INSERT INTO books (isbn, title, author, year, rating, is_read) VALUES ('9780201616224', 'The Pragmatic Programmer', 'Andrew Hunt', 1999, 0, 0);
INSERT INTO books (isbn, title, author, year, rating, is_read) VALUES ('9780596009205', 'Head First Java', 'Kathy Sierra', 2005, 3, 1);
INSERT INTO books (isbn, title, author, year, rating, is_read) VALUES ('9780441172719', 'Dune', 'Frank Herbert', 1965, 5, 1);
INSERT INTO books (isbn, title, author, year, rating, is_read) VALUES ('9780553418026', 'The Martian', 'Andy Weir', 2014, 4, 1);
INSERT INTO books (isbn, title, author, year, rating, is_read) VALUES ('9780593135204', 'Project Hail Mary', 'Andy Weir', 2021, 0, 0);
INSERT INTO books (isbn, title, author, year, rating, is_read) VALUES ('9780553448122', 'Artemis', 'Andy Weir', 2017, 3, 1);
INSERT INTO books (isbn, title, author, year, rating, is_read) VALUES ('9780547928227', 'The Hobbit', 'J.R.R. Tolkien', 1937, 5, 1);
INSERT INTO books (isbn, title, author, year, rating, is_read) VALUES ('9780062316097', 'Sapiens', 'Yuval Noah Harari', 2015, 0, 0);
```

### Given code

**File:** `src/main/java/edu/cwi/swdv110/books/Book.java`

```java
package edu.cwi.swdv110.books;

/**
 * One row of the books table.
 * id is 0 for a book that hasn't been saved yet. rating is 0 for "not rated", otherwise 1–5.
 */
public record Book(int id, String isbn, String title, String author, int year, int rating, boolean read) { }
```

**File:** `src/main/java/edu/cwi/swdv110/books/Database.java`

```java
package edu.cwi.swdv110.books;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** GIVEN: opening connections and running .sql script files. */
public final class Database {

    private Database() { }

    /** url looks like "jdbc:sqlite:books.db" – the file is created if it doesn't exist. */
    public static Connection connect(String url) throws SQLException {
        return DriverManager.getConnection(url);
    }

    /** Runs every statement in a .sql file. Lines starting with -- are comments. */
    public static void runScript(String url, Path script) throws SQLException, IOException {
        StringBuilder sql = new StringBuilder();
        for (String line : Files.readAllLines(script)) {
            if (!line.trim().startsWith("--")) {
                sql.append(line).append('\n');
            }
        }
        try (Connection c = connect(url); Statement st = c.createStatement()) {
            for (String statement : sql.toString().split(";")) {
                if (!statement.isBlank()) {
                    st.executeUpdate(statement);
                }
            }
        }
    }

    /** Creates the tables by running schema.sql (from the project folder). */
    public static void initialize(String url) throws SQLException, IOException {
        runScript(url, Path.of("schema.sql"));
    }
}
```

### Your code

**File:** `src/main/java/edu/cwi/swdv110/books/BookDAO.java`

```java
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
```

**File:** `src/main/java/edu/cwi/swdv110/books/App.java`

```java
package edu.cwi.swdv110.books;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.Year;
import java.util.List;
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
                System.out.println("New database – loaded seed.sql");
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
            } catch (IllegalArgumentException e) {   // also catches NumberFormatException
                System.out.println("Invalid input: " + e.getMessage());
            }
        }
    }

    private static String prompt(String label) {
        System.out.print(label + ": ");
        return sc.hasNextLine() ? sc.nextLine().trim() : "0";
    }

    /**
     * TODO Part 5: remove '-' and ' ', then require exactly 10 or 13 digits.
     * Otherwise throw IllegalArgumentException("ISBN must be 10 or 13 digits").
     *   "978-0-13-468599-1" -> "9780134685991"
     */
    static String cleanIsbn(String raw) {
        return raw;
    }

    /**
     * TODO Part 5: prompt for ISBN (cleanIsbn), title, author (both required) and year
     * (1450 through Year.now().getValue()), then dao.add(...) and print "Added with id N".
     * New books start unread with rating 0.
     */
    private static void addBook() throws SQLException {
    }

    /**
     * TODO Part 5: find the book by ISBN ("No book with that ISBN" if missing), then prompt
     * for a new title and author showing the current value in [brackets]; pressing Enter
     * keeps the current value. Build a new Book with the SAME id and call dao.update.
     */
    private static void editBook() throws SQLException {
    }

    private static void print(List<Book> books) {
        System.out.printf("%-14s %-28s %-20s %4s  %-5s %s%n", "ISBN", "TITLE", "AUTHOR", "YEAR", "READ", "RATING");
        for (Book b : books) {
            System.out.printf("%-14s %-28s %-20s %4d  %-5s %s%n", b.isbn(), b.title(), b.author(), b.year(),
                    b.read() ? "yes" : "no", "*".repeat(b.rating()));
        }
        System.out.println(books.size() + " book(s)");
    }
}
```

### Provided tests

Each test gets a brand-new database file in a temporary folder, so the tests never touch your real `books.db`. The tests run **your** `schema.sql`, so they check it too.

**File:** `src/test/java/edu/cwi/swdv110/books/BookDAOTest.java`

```java
package edu.cwi.swdv110.books;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BookDAOTest {

    @TempDir
    Path tmp;
    String url;
    BookDAO dao;

    static final Book DUNE = new Book(0, "9780441172719", "Dune", "Frank Herbert", 1965, 5, true);
    static final Book MARTIAN = new Book(0, "9780553418026", "The Martian", "Andy Weir", 2014, 0, false);
    static final Book HAIL_MARY = new Book(0, "9780593135204", "Project Hail Mary", "Andy Weir", 2021, 4, true);

    @BeforeEach
    void setUp() throws Exception {
        url = "jdbc:sqlite:" + tmp.resolve("test.db");
        Database.initialize(url);
        dao = new BookDAO(url);
        dao.add(DUNE);
        dao.add(MARTIAN);
        dao.add(HAIL_MARY);
    }

    private static List<String> titles(List<Book> books) {
        return books.stream().map(Book::title).toList();
    }

    // ---------- Part 1: your schema ----------
    @Test
    void schemaRejectsDuplicateIsbnAndBadRating() throws Exception {
        try (Connection c = Database.connect(url); Statement st = c.createStatement()) {
            assertThrows(SQLException.class, () -> st.executeUpdate(
                    "INSERT INTO books (isbn, title, author, year) VALUES ('9780441172719', 'Copy', 'X', 2000)"),
                    "isbn must be UNIQUE");
            assertThrows(SQLException.class, () -> st.executeUpdate(
                    "INSERT INTO books (isbn, title, author, year, rating) VALUES ('1111111111', 'T', 'A', 2000, 9)"),
                    "rating needs a CHECK constraint (0-5)");
            assertThrows(SQLException.class, () -> st.executeUpdate(
                    "INSERT INTO books (isbn, author, year) VALUES ('2222222222', 'A', 2000)"),
                    "title must be NOT NULL");
        }
    }

    @Test
    void schemaHasDefaults() throws Exception {
        try (Connection c = Database.connect(url); Statement st = c.createStatement()) {
            st.executeUpdate("INSERT INTO books (isbn, title, author, year) VALUES ('3333333333', 'Defaults', 'A', 2000)");
        }
        Book b = dao.findByIsbn("3333333333").orElseThrow();
        assertEquals(0, b.rating());
        assertFalse(b.read());
    }

    // ---------- Part 2: reading ----------
    @Test
    void getAllOrderedByTitle() throws SQLException {
        assertEquals(List.of("Dune", "Project Hail Mary", "The Martian"), titles(dao.getAll()));
    }

    @Test
    void findByIsbnMapsEveryColumn() throws SQLException {
        Book b = dao.findByIsbn("9780593135204").orElseThrow();
        assertTrue(b.id() > 0, "id comes from the database");
        assertEquals(new Book(b.id(), "9780593135204", "Project Hail Mary", "Andy Weir", 2021, 4, true), b);
        assertEquals(Optional.empty(), dao.findByIsbn("0000000000"));
    }

    @Test
    void searchTitleOrAuthorIgnoringCase() throws SQLException {
        assertEquals(List.of("Project Hail Mary", "The Martian"), titles(dao.search("weir")));
        assertEquals(List.of("Dune"), titles(dao.search("UN")));
        assertEquals(List.of(), titles(dao.search("tolkien")));
    }

    @Test
    void unread() throws SQLException {
        assertEquals(List.of("The Martian"), titles(dao.unread()));
    }

    // ---------- Part 3: writing ----------
    @Test
    void addReturnsNewIdAndRejectsDuplicates() throws SQLException {
        int id = dao.add(new Book(0, "9780547928227", "The Hobbit", "J.R.R. Tolkien", 1937, 0, false));
        assertTrue(id > 0);
        assertEquals(id, dao.findByIsbn("9780547928227").orElseThrow().id());
        assertThrows(SQLException.class, () -> dao.add(DUNE), "duplicate ISBN");
    }

    @Test
    void updateChangesTheRowWithThatId() throws SQLException {
        Book m = dao.findByIsbn("9780553418026").orElseThrow();
        assertTrue(dao.update(new Book(m.id(), m.isbn(), "The Martian (Classroom Ed.)", m.author(), 2016, 3, true)));
        assertEquals(new Book(m.id(), m.isbn(), "The Martian (Classroom Ed.)", "Andy Weir", 2016, 3, true),
                dao.findByIsbn(m.isbn()).orElseThrow());
        assertFalse(dao.update(new Book(9999, "x", "x", "x", 2000, 0, false)), "no row with id 9999");
    }

    @Test
    void markRead() throws SQLException {
        assertTrue(dao.markRead("9780553418026", 4));
        Book m = dao.findByIsbn("9780553418026").orElseThrow();
        assertTrue(m.read());
        assertEquals(4, m.rating());
        assertFalse(dao.markRead("0000000000", 3));
        assertThrows(IllegalArgumentException.class, () -> dao.markRead("9780553418026", 6));
        assertThrows(IllegalArgumentException.class, () -> dao.markRead("9780553418026", 0));
    }

    @Test
    void delete() throws SQLException {
        assertTrue(dao.delete("9780441172719"));
        assertFalse(dao.delete("9780441172719"), "already gone");
        assertEquals(2, dao.getAll().size());
    }

    // ---------- Part 4: reports ----------
    @Test
    void countByAuthorMostFirst() throws SQLException {
        Map<String, Integer> counts = dao.countByAuthor();
        assertEquals(Map.of("Andy Weir", 2, "Frank Herbert", 1), counts);
        assertEquals("Andy Weir", counts.keySet().iterator().next());
    }

    @Test
    void averageRatingIgnoresUnrated() throws SQLException {
        assertEquals(4.5, dao.averageRating(), 0.0001);   // (5 + 4) / 2 – The Martian is unrated
        dao.delete("9780441172719");
        dao.delete("9780593135204");
        assertEquals(0.0, dao.averageRating(), 0.0001);
    }

    // ---------- SQL injection ----------
    @Test
    void parametersBlockSqlInjection() throws SQLException {
        assertEquals(List.of(), dao.search("' OR '1'='1"));
        assertEquals(Optional.empty(), dao.findByIsbn("x' OR 1=1 --"));
        assertFalse(dao.delete("' OR 1=1 --"));
        assertEquals(3, dao.getAll().size(), "nothing was deleted");
    }
}
```

---

## Part 1 – Schema & SQL by hand

### 1a. Write `schema.sql`

Create `schema.sql` in the project folder with **one** `CREATE TABLE IF NOT EXISTS books (...)` statement:

| Column | Type | Rules |
|---|---|---|
| `id` | `INTEGER` | primary key, auto-increment |
| `isbn` | `TEXT` | required, unique |
| `title` | `TEXT` | required |
| `author` | `TEXT` | required |
| `year` | `INTEGER` | optional |
| `rating` | `INTEGER` | required, default `0`, must be between 0 and 5 (`CHECK`) |
| `is_read` | `INTEGER` | required, default `0`, must be 0 or 1. (SQLite has no `BOOLEAN` type.) |

### 1b. Try it in DB Browser

1. **New Database** → save it as `practice.db`, a throwaway file, not `books.db`.
2. In the **Execute SQL** tab, paste and run your `schema.sql`, then `seed.sql`.
3. Write the queries below in a new file named `queries.sql`, test each one in DB Browser, and paste the SQL *and* the number of rows it returned into the file as a comment.

```sql
-- 1. Every book, newest first
-- 2. Books by Andy Weir, oldest first
-- 3. Unread books, A–Z by title
-- 4. Titles containing 'the' (any case)
-- 5. How many books each author has, most first
-- 6. Average rating of rated books (rating > 0)
-- 7. Mark 'Sapiens' as read with a rating of 4
-- 8. Delete every unrated, unread book published before 2000
--    (run a SELECT with the same WHERE first – what does it return?)
```

> Why practice SQL by hand first? Every DAO method in Parts 2–4 is one of these queries with `?` in place of the values.

✅ Checkpoint: `mvn -Dtest=BookDAOTest#schema* test`. The two schema tests pass. (The other tests still fail.)

## Part 2 – Reading with JDBC

The pattern from class, for every read method:

```java
String sql = "SELECT id, isbn, title, author, year, rating, is_read FROM books WHERE isbn = ?";
try (Connection c = Database.connect(url);
     PreparedStatement ps = c.prepareStatement(sql)) {
    ps.setString(1, isbn);                      // parameters are numbered from 1!
    try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) { ... toBook(rs) ... }
    }
}
```

`getAll`, `search` and `unread` all build a list the same way. Consider a private helper, for example `query(String sql, String... params)`, so you write the loop once.

## Part 3 – Writing with JDBC

- Use `executeUpdate()` for `INSERT`, `UPDATE` and `DELETE`. It returns **how many rows changed**, which is how `update`, `markRead` and `delete` decide what to return.
- `boolean` → `int`: `ps.setInt(6, b.read() ? 1 : 0)`.

## Part 4 – Reports

Let the database do the math: `GROUP BY author` with `COUNT(*)`, and `AVG(rating)`. `AVG` returns `NULL` when there are no rows; `rs.getDouble` turns `NULL` into `0.0`.

✅ Checkpoint: `mvn test`. All 13 tests pass, including the SQL injection test.

## Part 5 – The console app

Complete `cleanIsbn`, `addBook` and `editBook`, then run `mvn -q compile exec:java`:

```text
New database – loaded seed.sql

1) List all   2) Search   3) Unread   4) Add   5) Mark read
6) Edit       7) Delete   8) Stats    0) Quit
Choice: 2
Title or author contains: weir
ISBN           TITLE                        AUTHOR               YEAR  READ  RATING
9780553448122  Artemis                      Andy Weir            2017  yes   ***
9780593135204  Project Hail Mary            Andy Weir            2021  no    
9780553418026  The Martian                  Andy Weir            2014  yes   ****
3 book(s)

1) List all   2) Search   3) Unread   4) Add   5) Mark read
6) Edit       7) Delete   8) Stats    0) Quit
Choice: 4
ISBN: 978-0-06-112008-4
Title: To Kill a Mockingbird
Author: Harper Lee
Year: 1960
Added with id 11

1) List all   2) Search   3) Unread   4) Add   5) Mark read
6) Edit       7) Delete   8) Stats    0) Quit
Choice: 5
ISBN: 9780593135204
Rating 1-5: 5
Updated

1) List all   2) Search   3) Unread   4) Add   5) Mark read
6) Edit       7) Delete   8) Stats    0) Quit
Choice: 8
Books per author: {Andy Weir=3, Andrew Hunt=1, Frank Herbert=1, Harper Lee=1, J.R.R. Tolkien=1, Joshua Bloch=1, Kathy Sierra=1, Robert C. Martin=1, Yuval Noah Harari=1}
Average rating: 4.25

1) List all   2) Search   3) Unread   4) Add   5) Mark read
6) Edit       7) Delete   8) Stats    0) Quit
Choice: 4
ISBN: 9780441172719
Title: Dune again
Author: Frank Herbert
Year: 1965
Database error: [SQLITE_CONSTRAINT_UNIQUE] A UNIQUE constraint failed (UNIQUE constraint failed: books.isbn)

1) List all   2) Search   3) Unread   4) Add   5) Mark read
6) Edit       7) Delete   8) Stats    0) Quit
Choice: 0
Bye!
```

Quit and run it again: your changes are still there. Open `books.db` in DB Browser to see the rows. Close DB Browser before running the app again, because SQLite allows only one writer at a time.

---

## Part 6 – README reflection

1. In your own words, what is **SQL injection**? Show the SQL that `search` would have built from `' OR '1'='1` if you had used `+` instead of `?`.
2. Why does every DAO method open and close its own `Connection` with try-with-resources? What could go wrong if you forgot to close them?
3. `App` contains no SQL at all. Name two benefits of keeping SQL inside the DAO. (Hint: think about next week.)
4. Paste your `queries.sql` results summary: each query and its row count.

---

## Stretch goals (pick any)

- **Loans table:** add a `loans` table (`id`, `book_id` → `books.id`, `friend`, `loaned_on`, `returned_on`) and a menu option to list books currently loaned out, using a `JOIN`.
- **CSV import:** read a CSV of books and insert them all in **one transaction** with `setAutoCommit(false)` / `commit()` / `rollback()`.
- **Sort option:** let the user list books sorted by title, author, year or rating. Use a *whitelist* to choose the `ORDER BY` column, since you can't use `?` for a column name. Explain why in your README.
- **Swap databases:** describe in your README what would change to move from SQLite to MySQL. (Hint: very little outside the URL and the pom.)

---

## Submission checklist

- [ ] `schema.sql` and `queries.sql` committed
- [ ] `mvn test`: all provided tests pass
- [ ] No string-concatenated values in any SQL (`?` parameters only)
- [ ] `App` runs: add, edit, mark read, delete, search, stats
- [ ] `README.md` answers the Part 6 questions
- [ ] `.gitignore` includes `*.db` (don't commit the database file)

## Grading (100 points)

| Area | Points |
|---|---|
| `schema.sql` (schema tests pass) | 10 |
| `queries.sql`: 8 working queries | 10 |
| DAO tests pass (read, write, reports, injection) | 40 |
| `App`: `cleanIsbn`, `addBook`, `editBook`, error handling | 15 |
| Code quality: try-with-resources, no SQL outside the DAO, `?` parameters only | 10 |
| README reflection | 10 |
| Git: meaningful commits | 5 |
| Stretch goals | up to +5 bonus |
