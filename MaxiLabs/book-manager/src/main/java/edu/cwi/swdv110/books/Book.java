package edu.cwi.swdv110.books;

/**
 * One row of the books table.
 * id is 0 for a book that hasn't been saved yet. rating is 0 for "not rated", otherwise 1–5.
 */
public record Book(int id, String isbn, String title, String author, int year, int rating, boolean read) {
}
