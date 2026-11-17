# Reflection

### In your own words, what is SQL injection? Show the SQL that search would have built from ' OR '1'='1 if you had used + instead of ?.
SQL Injection is a type of attack vector that allows hackers to run just about any SQL queries they want using the SQL connection that the running program has access to.

```sql
SELECT id, isbn, title, author, year, rating, is_read
FROM books
WHERE title LIKE '%' OR '1'='1user Input%'
OR author LIKE '%' OR '1'='1user Input%'
ORDER BY title
```
In this case, the hacker is hoping to obtain all records in the table by taking advantage of the fact that
```sql
LIKE '%'
```
always returns true in SQL. While no destruction of data was caused in this case, what the hacker receives from this injection is information that says that this app is vulnerable to much more dangerous attacks.

### Why does every DAO method open and close its own Connection with try-with-resources? What could go wrong if you forgot to close them?
try-with-resources helps to prevent resource leaks once the application leaves the try block, no matter if an error caused the program to leave it or otherwise. Leaving connections open can cause unstable conditions, such as a resource exhaustion on the connections. Some operations can also Lock the database and leave other operations unable to function at all, which is why we shouldn't really use DB Browser for SQL while the program is running and vice versa.

### App contains no SQL at all. Name two benefits of keeping SQL inside the DAO. (Hint: think about next week.)
Benefit 1: We can switch to any backend persistence layer that we want to use without changing our apps frontend functionality. We could switch from SQLite to MySQL for example.

Benefit 2: We keep access to the database constrained and limited. This helps ensure that data access and modification rules are easier to enforce thanks to encapsulation.

### Paste your queries.sql results summary: each query and its row count.
See the comments in [queries.sql](queries.sql)
