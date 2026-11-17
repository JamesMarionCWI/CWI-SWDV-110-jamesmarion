-- 1. Every book, newest first

/*
Result: 10 rows returned in 32ms
At line 1:
*/
SELECT *
FROM books
ORDER BY year DESC;

-- 2. Books by Andy Weir, oldest first
/*
Result: 3 rows returned in 12ms
At line 1:
*/
SELECT *
FROM books
WHERE author = 'Andy Weir'
ORDER BY year;

-- 3. Unread books, A–Z by title
/*
Result: 3 rows returned in 6ms
At line 1:
*/
SELECT *
FROM books
WHERE is_read = 0
ORDER BY title;

-- 4. Titles containing 'the' (any case)
/*
Result: 3 rows returned in 3ms
At line 1:
*/
SELECT title
FROM books
WHERE title LIKE '%the%';

-- 5. How many books each author has, most first
/*
Result: 8 rows returned in 4ms
At line 1:
*/
SELECT
    author,
    COUNT(*) AS num_books
FROM books
GROUP BY author
ORDER BY num_books DESC;

-- 6. Average rating of rated books (rating > 0)
SELECT AVG(rating) AS avg_rating
FROM books
WHERE rating > 0;

-- 7. Mark 'Sapiens' as read with a rating of 4
/*
Result: query executed successfully. Took 1ms, 1 rows affected
At line 1:
*/
UPDATE books
SET is_read = 1, rating = 4
WHERE title = 'Sapiens';

-- 8. Delete every unrated, unread book published before 2000
--    (run a SELECT with the same WHERE first – what does it return?)
/*
Result: 1 rows returned in 7ms
At line 1:
*/
SELECT *
FROM books
WHERE rating = 0 AND is_read = 0 AND year < 2000;
/*
Result: query executed successfully. Took 1ms, 1 rows affected
At line 1:
*/
DELETE FROM books
WHERE rating = 0 AND is_read = 0 AND year < 2000;
