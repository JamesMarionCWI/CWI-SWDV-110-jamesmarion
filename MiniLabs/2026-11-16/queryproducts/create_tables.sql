-- (1. Open products.db in the VS Code SQLite extension)

-- Create the table
-- (Drop if exists to be able to run this script multiple times without issues)
-- (Very useful for assignments and testing)
-- (but not a good command to have access to in the real world outside of specific scenarios)
DROP TABLE IF EXISTS products;
CREATE TABLE products (
    code TEXT PRIMARY KEY,
    description TEXT NOT NULL,
    price REAL NOT NULL
);

-- 2. Add at least 20 products using INSERT
INSERT INTO products (code, description, price)
VALUES 
('WC-600', 'HD webcam', 59.95),
('WC-601', 'HD webcam 1', 120.85),
('WC-602', 'HD webcam 2', 20.45),
('WC-603', 'HD webcam 3', 55.99),
('WC-604', 'HD webcam 4', 49.99),
('WC-613', 'HD webcam 13', 44.44),
('WC-614', 'HD webcam 14', 55.55),
('WC-615', 'HD webcam 15', 66.66),
('WC-616', 'HD webcam 16', 77.77),
('WC-617', 'HD webcam 17', 88.88),
('WC-605', 'Cable webcam 5', 35.45),
('WC-606', 'Cable webcam 6', 65.98),
('WC-607', 'Cable webcam 7', 42.42),
('WC-608', 'Cable webcam 8', 69.69),
('WC-609', 'Cable webcam 9', 67.67),
('WC-610', 'HD webcam 10', 420.69),
('WC-611', 'HD webcam 11', 33.33),
('WC-612', 'HD webcam 12', 22.22),
('WC-618', 'HD webcam 18', 99.99),
('WC-619', 'HD webcam 19', 11.11);

-- 3. List all products sorted by description A to Z
SELECT * FROM products
ORDER BY description;

-- 4. Show products between $20 and $100
SELECT * FROM products
WHERE price >= 20 AND price <= 100;

-- 5. Find products whose description contains cable
SELECT * FROM products
WHERE description LIKE '%cable%';

-- 6. Show the count, minimum, maximum and average price
SELECT
    COUNT(*) as num_products,
    MIN(price) as min_price,
    MAX(price) as max_price,
    AVG(price) as avg_price
FROM products;

-- Stretch: show the 2 most expensive products with ORDER BY and LIMIT
SELECT * FROM products
ORDER BY price DESC
LIMIT 2;
