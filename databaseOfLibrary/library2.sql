-- Library Management System Database Schema
-- Database: library2

-- Create database
DROP DATABASE IF EXISTS library2;
CREATE DATABASE library2;
USE library2;

-- ============================================
-- TABLE: admin - Login credentials
-- ============================================
CREATE TABLE admin (
    USER_ID VARCHAR(50) UNIQUE NOT NULL,
    NAME VARCHAR(100),
    PASSWORD VARCHAR(50),
    CONTACT VARCHAR(50)
);

-- Insert sample admin
INSERT INTO admin (USER_ID, NAME, PASSWORD, CONTACT) VALUES
('jonny@123', 'JONNY ROY', '#123$', '9988776655'),
('admin', 'Administrator', 'admin123', '9876543210'),
('librarian', 'Head Librarian', 'lib@2024', '8765432109');

-- ============================================
-- TABLE: book - Book catalog
-- ============================================
CREATE TABLE book (
    BOOK_ID VARCHAR(50) PRIMARY KEY,
    CATEGORY VARCHAR(50),
    NAME VARCHAR(100),
    AUTHOR VARCHAR(100),
    COPIES SMALLINT UNSIGNED,
    TOTAL_COPIES SMALLINT UNSIGNED,
    STATUS VARCHAR(50) DEFAULT 'Available'
);

-- Trigger to auto-populate TOTAL_COPIES
DELIMITER //
CREATE TRIGGER before_book_insert
BEFORE INSERT ON book
FOR EACH ROW
BEGIN
    SET NEW.TOTAL_COPIES = NEW.COPIES;
END//
DELIMITER ;

-- Insert 18+ diverse books
INSERT INTO book (BOOK_ID, CATEGORY, NAME, AUTHOR, COPIES) VALUES
('B001', 'Fiction', 'To Kill a Mockingbird', 'Harper Lee', 5),
('B002', 'Fiction', 'The Great Gatsby', 'F. Scott Fitzgerald', 4),
('B003', 'Science', 'A Brief History of Time', 'Stephen Hawking', 3),
('B004', 'Science', 'The Origin of Species', 'Charles Darwin', 2),
('B005', 'Technology', 'Clean Code', 'Robert C. Martin', 6),
('B006', 'Technology', 'The Pragmatic Programmer', 'David Thomas', 4),
('B007', 'Romance', 'Pride and Prejudice', 'Jane Austen', 5),
('B008', 'Romance', 'The Notebook', 'Nicholas Sparks', 3),
('B009', 'Fantasy', 'Harry Potter and the Sorcerer''s Stone', 'J.K. Rowling', 8),
('B010', 'Fantasy', 'The Lord of the Rings', 'J.R.R. Tolkien', 4),
('B011', 'Mystery', 'The Da Vinci Code', 'Dan Brown', 5),
('B012', 'Mystery', 'Gone Girl', 'Gillian Flynn', 3),
('B013', 'Biography', 'Steve Jobs', 'Walter Isaacson', 4),
('B014', 'Biography', 'The Diary of a Young Girl', 'Anne Frank', 6),
('B015', 'Education', 'Introduction to Algorithms', 'Thomas H. Cormen', 5),
('B016', 'Education', 'Design Patterns', 'Gang of Four', 3),
('B017', 'Novel', '1984', 'George Orwell', 4),
('B018', 'Novel', 'Animal Farm', 'George Orwell', 5),
('B019', 'Technology', 'Java: The Complete Reference', 'Herbert Schildt', 7),
('B020', 'Science', 'Cosmos', 'Carl Sagan', 3);

-- ============================================
-- TABLE: student - Student registry
-- ============================================
CREATE TABLE student (
    Stu_ID VARCHAR(50) PRIMARY KEY,
    NAME VARCHAR(100),
    CONTACT VARCHAR(50)
);

-- Insert 16 student records
INSERT INTO student (Stu_ID, NAME, CONTACT) VALUES
('S001', 'John Smith', '9876543210'),
('S002', 'Emily Johnson', '9876543211'),
('S003', 'Michael Brown', '9876543212'),
('S004', 'Sarah Davis', '9876543213'),
('S005', 'James Wilson', '9876543214'),
('S006', 'Emma Martinez', '9876543215'),
('S007', 'William Anderson', '9876543216'),
('S008', 'Olivia Taylor', '9876543217'),
('S009', 'Benjamin Thomas', '9876543218'),
('S010', 'Sophia Jackson', '9876543219'),
('S011', 'Lucas White', '9876543220'),
('S012', 'Isabella Harris', '9876543221'),
('S013', 'Henry Martin', '9876543222'),
('S014', 'Mia Garcia', '9876543223'),
('S015', 'Alexander Robinson', '9876543224'),
('S016', 'Charlotte Clark', '9876543225');

-- ============================================
-- TABLE: issue - Book issue tracking
-- ============================================
CREATE TABLE issue (
    ISSUE_ID INT AUTO_INCREMENT PRIMARY KEY,
    Stu_ID VARCHAR(50),
    BOOK_ID VARCHAR(50),
    ISSUE_DATE DATE,
    RETURN_DATE DATE NULL,
    FOREIGN KEY (Stu_ID) REFERENCES student(Stu_ID),
    FOREIGN KEY (BOOK_ID) REFERENCES book(BOOK_ID)
);

-- Insert 10 sample issue records
INSERT INTO issue (Stu_ID, BOOK_ID, ISSUE_DATE, RETURN_DATE) VALUES
('S001', 'B001', '2024-01-15', '2024-01-30'),
('S002', 'B003', '2024-01-20', '2024-02-05'),
('S003', 'B005', '2024-02-01', NULL),
('S004', 'B007', '2024-02-10', '2024-02-25'),
('S005', 'B009', '2024-02-15', NULL),
('S006', 'B011', '2024-02-20', NULL),
('S007', 'B013', '2024-03-01', '2024-03-15'),
('S008', 'B015', '2024-03-05', NULL),
('S009', 'B017', '2024-03-10', '2024-03-25'),
('S010', 'B019', '2024-03-15', NULL);

-- Update book copies for issued books (not returned)
UPDATE book SET COPIES = COPIES - 1 WHERE BOOK_ID = 'B005';
UPDATE book SET COPIES = COPIES - 1 WHERE BOOK_ID = 'B009';
UPDATE book SET COPIES = COPIES - 1 WHERE BOOK_ID = 'B011';
UPDATE book SET COPIES = COPIES - 1 WHERE BOOK_ID = 'B015';
UPDATE book SET COPIES = COPIES - 1 WHERE BOOK_ID = 'B019';

-- ============================================
-- VIEW: available_books_view
-- ============================================
CREATE VIEW available_books_view AS
SELECT 
    b.BOOK_ID,
    b.NAME,
    b.AUTHOR,
    b.CATEGORY,
    b.TOTAL_COPIES,
    COALESCE(issued.Issued_Copies, 0) AS Issued_Copies,
    (b.TOTAL_COPIES - COALESCE(issued.Issued_Copies, 0)) AS Available_Copies
FROM book b
LEFT JOIN (
    SELECT BOOK_ID, COUNT(*) AS Issued_Copies
    FROM issue
    WHERE RETURN_DATE IS NULL
    GROUP BY BOOK_ID
) issued ON b.BOOK_ID = issued.BOOK_ID;

-- ============================================
-- VIEW: student_details_view
-- ============================================
CREATE VIEW student_details_view AS
SELECT 
    s.Stu_ID,
    s.NAME,
    s.CONTACT,
    COALESCE(stats.Total_Issued, 0) AS Total_Issued,
    COALESCE(stats.Returned_Books, 0) AS Returned_Books,
    COALESCE(stats.Currently_Issued, 0) AS Currently_Issued
FROM student s
LEFT JOIN (
    SELECT 
        Stu_ID,
        COUNT(*) AS Total_Issued,
        SUM(CASE WHEN RETURN_DATE IS NOT NULL THEN 1 ELSE 0 END) AS Returned_Books,
        SUM(CASE WHEN RETURN_DATE IS NULL THEN 1 ELSE 0 END) AS Currently_Issued
    FROM issue
    GROUP BY Stu_ID
) stats ON s.Stu_ID = stats.Stu_ID;

-- Verify data
SELECT 'Admin table:' AS Info;
SELECT * FROM admin;

SELECT 'Book table (sample):' AS Info;
SELECT * FROM book LIMIT 5;

SELECT 'Student table (sample):' AS Info;
SELECT * FROM student LIMIT 5;

SELECT 'Issue table:' AS Info;
SELECT * FROM issue;

SELECT 'Available Books View:' AS Info;
SELECT * FROM available_books_view;

SELECT 'Student Details View:' AS Info;
SELECT * FROM student_details_view;
