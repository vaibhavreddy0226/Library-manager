import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * Handles reading and writing of books, students, and borrow records to CSV files.
 * Manages three files: books.csv, students.csv, and borrows.csv.
 */
public class CSVHandler {
    private static final String BOOKS_FILE = "books.csv"; // Stores books (id,title,available)
    private static final String STUDENTS_FILE = "students.csv"; // Stores students (rollNo,password)
    private static final String BORROWS_FILE = "borrows.csv"; // Stores borrows (rollNo,bookId,borrowedDate,dueDate,returnedDate,status)

    /**
     * Loads books from books.csv.
     * Expected format: id,title,available
     * @return List of Book objects
     */
    public List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length != 3) {
                    System.err.println("Skipping invalid line in books.csv: " + line);
                    continue;
                }
                try {
                    // Parse and create Book object
                    books.add(new Book(data[0].trim(), data[1].trim(), Boolean.parseBoolean(data[2].trim())));
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid book data: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading books: " + e.getMessage());
        }
        return books;
    }

    /**
     * Saves books to books.csv.
     * Writes each book in format: id,title,available
     * @param books List of books to save
     */
    public void saveBooks(List<Book> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            for (Book book : books) {
                writer.write(book.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving books: " + e.getMessage());
        }
    }

    /**
     * Loads students from students.csv.
     * Expected format: rollNo,password
     * @return List of Student objects
     */
    public List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length != 2) {
                    System.err.println("Skipping invalid line in students.csv: " + line);
                    continue;
                }
                try {
                    // Parse and create Student object
                    students.add(new Student(data[0].trim(), data[1].trim()));
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid student data: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading students: " + e.getMessage());
        }
        return students;
    }

    /**
     * Saves students to students.csv.
     * Writes each student in format: rollNo,password
     * @param students List of students to save
     */
    public void saveStudents(List<Student> students) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(STUDENTS_FILE))) {
            for (Student student : students) {
                writer.write(student.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving students: " + e.getMessage());
        }
    }

    /**
     * Loads borrow records from borrows.csv.
     * Supports new format: rollNo,bookId,borrowedDate,dueDate,returnedDate,status
     * Backward compatible with old format: rollNo,bookId,dueDate
     * @return List of Borrow objects
     */
    public List<Borrow> loadBorrows() {
        List<Borrow> borrows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BORROWS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                try {
                    if (data.length == 3) {
                        // Handle old format: rollNo,bookId,dueDate
                        String rollNo = data[0].trim();
                        String bookId = data[1].trim();
                        String dueDate = data[2].trim();
                        // Estimate borrowedDate as 14 days before dueDate
                        LocalDate due = LocalDate.parse(dueDate);
                        String borrowedDate = due.minusDays(14).toString();
                        borrows.add(new Borrow(rollNo, bookId, borrowedDate, dueDate, "", "Active"));
                    } else if (data.length == 6) {
                        // Handle new format: rollNo,bookId,borrowedDate,dueDate,returnedDate,status
                        borrows.add(new Borrow(
                            data[0].trim(),
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim(),
                            data[4].trim(),
                            data[5].trim()
                        ));
                    } else {
                        System.err.println("Skipping invalid line in borrows.csv: " + line);
                    }
                } catch (IllegalArgumentException | java.time.format.DateTimeParseException e) {
                    System.err.println("Invalid borrow data: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading borrows: " + e.getMessage());
        }
        return borrows;
    }

    /**
     * Saves borrow records to borrows.csv.
     * Writes each borrow in format: rollNo,bookId,borrowedDate,dueDate,returnedDate,status
     * @param borrows List of borrow records to save
     */
    public void saveBorrows(List<Borrow> borrows) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BORROWS_FILE))) {
            for (Borrow borrow : borrows) {
                writer.write(borrow.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving borrows: " + e.getMessage());
        }
    }
}