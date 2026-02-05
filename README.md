# Library Management System

A complete Java Swing-based library management system for managing books, student registrations, book issuance, and returns.

## Features

- 🔐 **Secure Login System** - Admin authentication with MySQL database
- 📚 **Book Management** - Add and view books with availability tracking
- 👤 **Student Registration** - Register and manage student records
- 📤 **Issue Books** - Issue books to registered students
- 📥 **Return Books** - Process book returns with automatic inventory updates
- 📊 **Real-time Views** - Live availability and student borrowing statistics

## Technology Stack

- **Language**: Java 8+
- **GUI**: Java Swing with custom dark theme
- **Database**: MySQL 8.0+
- **JDBC Driver**: MySQL Connector/J 9.5.0

## Project Structure

```
project-root/
├── src/
│   ├── Connect.java          # Database connection factory
│   ├── Loading1.java         # Login screen (entry point)
│   ├── HomePage.java         # Main dashboard
│   ├── addBook.java          # Add new books
│   ├── availableBook.java    # View available books
│   ├── studentRegistration.java  # Register students
│   ├── studentDetails.java   # View student info
│   ├── IssueBook.java        # Issue books
│   └── ReturnBook.java       # Return books
├── build/
│   └── classes/              # Compiled output
├── lib/
│   └── mysql-connector-j-9.5.0.jar
├── databaseOfLibrary/
│   └── library2.sql          # Database schema + sample data
├── build.xml                 # Ant build file
├── manifest.mf               # JAR manifest
└── README.md
```

## Setup Instructions

### 1. Database Setup

1. Install MySQL Server 8.0 or higher
2. Start MySQL server
3. Run the database script:

```bash
mysql -u root -p < databaseOfLibrary/library2.sql
```

Or import via MySQL Workbench.

### 2. Configure Database Connection

Edit `src/Connect.java` if your MySQL credentials differ:

```java
private static final String URL = "jdbc:mysql://localhost:3306/library2";
private static final String USERNAME = "root";
private static final String PASSWORD = "@Qwerty123";
```

### 3. Download MySQL JDBC Driver

Download MySQL Connector/J from:
https://dev.mysql.com/downloads/connector/j/

Place the JAR file in the `lib/` directory:
```
lib/mysql-connector-j-9.5.0.jar
```

### 4. Compile and Run

#### Quick Commands (Windows PowerShell)

```powershell
# Navigate to project directory
cd c:\Users\thisp\OneDrive\Desktop\sh

# COMPILE (run once after any code changes)
javac -d build/classes -cp "lib/*" src/*.java

# RUN THE APPLICATION
java -cp "build/classes;lib/*" Loading1
```

#### One-liner to Run (after compiling once)
```powershell
cd c:\Users\thisp\OneDrive\Desktop\sh; java -cp "build/classes;lib/*" Loading1
```

#### Using Ant (alternative):

```bash
ant run      # Build and run
ant jar      # Create JAR
ant clean    # Clean build
```

## Default Login Credentials

| Username | Password | Name |
|----------|----------|------|
| jonny@123 | #123$ | JONNY ROY |
| admin | admin123 | Administrator |
| librarian | lib@2024 | Head Librarian |

## Database Schema

### Tables

- **admin** - Login credentials (USER_ID, NAME, PASSWORD, CONTACT)
- **book** - Book catalog (BOOK_ID, CATEGORY, NAME, AUTHOR, COPIES, TOTAL_COPIES, STATUS)
- **student** - Student registry (Stu_ID, NAME, CONTACT)
- **issue** - Book issue tracking (ISSUE_ID, Stu_ID, BOOK_ID, ISSUE_DATE, RETURN_DATE)

### Views

- **available_books_view** - Shows book availability (total, issued, available copies)
- **student_details_view** - Shows student borrowing summary

## Screenshots

The application features a modern dark theme with:
- Gradient backgrounds
- Styled input fields
- Hover effects on buttons
- Card-based dashboard navigation

## License

This project is for educational purposes.
