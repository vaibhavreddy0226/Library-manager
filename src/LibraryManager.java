import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the library's books, students, and borrow records.
 * Interacts with CSVHandler to read/write data to books.csv, students.csv, and borrows.csv.
 */
public class LibraryManager {
    private List<Book> books; // List of books
    private List<Student> students; // List of students
    private List<Borrow> borrows; // List of borrow records
    private CSVHandler csvHandler; // Handles CSV file operations
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Date format

    /**
     * Initializes the library manager by loading data from CSV files.
     * Adds default books if books.csv is empty.
     */
    public LibraryManager() {
        this.csvHandler = new CSVHandler();
        this.books = csvHandler.loadBooks(); // Load books from books.csv
        this.students = csvHandler.loadStudents(); // Load students from students.csv
        this.borrows = csvHandler.loadBorrows(); // Load borrows from borrows.csv
        
        if (books.isEmpty()) {
            initializeDefaultBooks(); // Add default books if none exist
        }
    }

    /**
     * Adds default books to the library if books.csv is empty.
     * Saves to books.csv after adding.
     */
    private void initializeDefaultBooks() {
        // Add sample books
        books.add(new Book("B1", "Java Programming", true));
        books.add(new Book("B2", "Data Structures", true));
        books.add(new Book("B3", "Algorithms", true));
        books.add(new Book("B4", "Operating Systems", true));
        books.add(new Book("B5", "Database Systems", true));
        csvHandler.saveBooks(books); // Save to books.csv
    }

    /**
     * Adds a new book to the library.
     * @param book Book to add
     * @throws IllegalArgumentException if book ID already exists
     */
    public void addBook(Book book) {
        if (books.stream().anyMatch(b -> b.getId().equals(book.getId()))) {
            throw new IllegalArgumentException("Book ID already exists!");
        }
        books.add(book);
        csvHandler.saveBooks(books); // Update books.csv
    }

    /**
     * Removes a book from the library.
     * @param bookId Book's ID
     * @throws IllegalArgumentException if book not found or is borrowed
     */
    public void removeBook(String bookId) {
        Book book = findBook(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found!");
        }
        if (borrows.stream().anyMatch(b -> b.getBookId().equals(bookId) && b.getStatus().equals("Active"))) {
            throw new IllegalArgumentException("Cannot remove a borrowed book!");
        }
        books.remove(book);
        csvHandler.saveBooks(books); // Update books.csv
    }

    /**
     * Adds a new student to the library.
     * @param student Student to add
     * @throws IllegalArgumentException if student already exists
     */
    public void addStudent(Student student) {
        if (students.stream().anyMatch(s -> s.getRollNo().equals(student.getRollNo()))) {
            throw new IllegalArgumentException("Student already exists!");
        }
        students.add(student);
        csvHandler.saveStudents(students); // Update students.csv
    }

    /**
     * Authenticates an admin with hardcoded credentials.
     * @param username Admin username
     * @param password Admin password
     * @return true if credentials are valid (admin/admin123)
     */
    public boolean adminLogin(String username, String password) {
        return username.equals("admin") && password.equals("admin123");
    }

    /**
     * Authenticates a student using roll number and password.
     * @param rollNo Student's roll number
     * @param password Student's password
     * @return true if credentials match a student in students.csv
     */
    public boolean studentLogin(String rollNo, String password) {
        return students.stream()
                .anyMatch(s -> s.getRollNo().equals(rollNo) && 
                             s.getPassword().equals(password));
    }

    /**
     * Gets a copy of the books list to prevent external modification.
     * @return List of books
     */
    public List<Book> getBooks() {
        return new ArrayList<>(books);
    }

    /**
     * Gets a copy of the students list to prevent external modification.
     * @return List of students
     */
    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    /**
     * Gets a copy of the borrow records list to prevent external modification.
     * @return List of borrows
     */
    public List<Borrow> getBorrows() {
        return new ArrayList<>(borrows);
    }

    /**
     * Borrows a book for a student, updating books.csv and borrows.csv.
     * @param rollNo Student's roll number
     * @param bookId Book's ID
     * @param dueDate Due date in YYYY-MM-DD format
     * @throws IllegalArgumentException if book not found, already borrowed, or invalid due date
     */
    public void borrowBook(String rollNo, String bookId, String dueDate) {
        Book book = findBook(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found!");
        }
        // Validate due date format and range
        try {
            LocalDate borrowDate = LocalDate.now();
            LocalDate due = LocalDate.parse(dueDate, DATE_FORMATTER);
            if (!due.isAfter(borrowDate)) {
                throw new IllegalArgumentException("Due date must be after today!");
            }
            if (due.getYear() < borrowDate.getYear() || 
                due.getMonthValue() < 1 || due.getMonthValue() > 12 || 
                due.getDayOfMonth() < 1 || due.getDayOfMonth() > due.lengthOfMonth()) {
                throw new IllegalArgumentException("Invalid due date: month (1-12) or day (1-" + due.lengthOfMonth() + ")!");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid due date format! Use YYYY-MM-DD");
        }
        book.borrow(rollNo, dueDate); // Mark book as borrowed
        borrows.add(new Borrow(rollNo, bookId, LocalDate.now().toString(), dueDate, "", "Active"));
        csvHandler.saveBooks(books); // Update books.csv
        csvHandler.saveBorrows(borrows); // Update borrows.csv
    }

    /**
     * Returns a borrowed book, updating books.csv and borrows.csv.
     * @param rollNo Student's roll number
     * @param bookId Book's ID
     * @throws IllegalArgumentException if no active borrow record exists
     */
    public void returnBook(String rollNo, String bookId) {
        Borrow borrow = findBorrow(rollNo, bookId);
        if (borrow == null || borrow.getStatus().equals("Returned")) {
            throw new IllegalArgumentException("No active borrow record found!");
        }
        
        Book book = findBook(bookId);
        if (book != null) {
            book.returnItem(); // Mark book as available
            borrow.markAsReturned(LocalDate.now().toString()); // Update borrow record
            csvHandler.saveBooks(books); // Update books.csv
            csvHandler.saveBorrows(borrows); // Update borrows.csv
        }
    }

    /**
     * Finds a book by ID.
     * @param id Book's ID
     * @return Book or null if not found
     */
    private Book findBook(String id) {
        return books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds an active borrow record by roll number and book ID.
     * @param rollNo Student's roll number
     * @param bookId Book's ID
     * @return Borrow or null if not found
     */
    private Borrow findBorrow(String rollNo, String bookId) {
        return borrows.stream()
                .filter(b -> b.getRollNo().equals(rollNo) && 
                            b.getBookId().equals(bookId) && 
                            b.getStatus().equals("Active"))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets the borrowing history for admin view.
     * @return List of all borrow records
     */
    public List<Borrow> getBorrowHistory() {
        return new ArrayList<>(borrows);
    }
}