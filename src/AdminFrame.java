import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

/**
 * Admin GUI frame for managing books, students, and viewing library statistics and borrowing history.
 * Displays buttons on the left for actions and a text area on the right for output.
 */
public class AdminFrame extends JFrame {
    private LibraryManager libraryManager; // Manages library data and operations
    private JTextArea outputArea; // Displays action results and reports

    /**
     * Initializes the admin frame with the given library manager.
     * @param libraryManager Instance for accessing library data and operations
     */
    public AdminFrame(LibraryManager libraryManager) {
        this.libraryManager = libraryManager;
        setupUI(); // Configure the UI components
    }

    /**
     * Sets up the UI with a button panel (west) and output text area (center).
     * Uses BorderLayout with a vertical GridLayout for buttons.
     */
    private void setupUI() {
        setTitle("Admin Panel"); // Window title
        setSize(600, 400); // Window dimensions
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close frame on exit
        setLayout(new BorderLayout(10, 10)); // Main layout with 10px gaps

        // Create button panel with vertical layout for admin actions
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
        JButton addBookButton = new JButton("Add Book"); // Button to add a book
        JButton removeBookButton = new JButton("Remove Book"); // Button to remove a book
        JButton addStudentButton = new JButton("Add Student"); // Button to add a student
        JButton viewStatsButton = new JButton("View Statistics"); // Button to view stats
        JButton viewBorrowHistoryButton = new JButton("View Borrowing History"); // Button for history
        JButton backButton = new JButton("Back to Main"); // Button to return to main menu

        // Add buttons to panel
        buttonPanel.add(addBookButton);
        buttonPanel.add(removeBookButton);
        buttonPanel.add(addStudentButton);
        buttonPanel.add(viewStatsButton);
        buttonPanel.add(viewBorrowHistoryButton);
        buttonPanel.add(backButton);

        // Create output area for displaying results
        outputArea = new JTextArea();
        outputArea.setEditable(false); // Prevent user editing
        JScrollPane scrollPane = new JScrollPane(outputArea); // Add scroll functionality

        // Add components to frame
        add(buttonPanel, BorderLayout.WEST); // Buttons on left
        add(scrollPane, BorderLayout.CENTER); // Output area in center

        // Register event handlers for buttons
        addBookButton.addActionListener(e -> showAddBookDialog());
        removeBookButton.addActionListener(e -> showRemoveBookDialog());
        addStudentButton.addActionListener(e -> showAddStudentDialog());
        viewStatsButton.addActionListener(e -> showStatistics());
        viewBorrowHistoryButton.addActionListener(e -> showBorrowHistory());
        backButton.addActionListener(e -> returnToMain());

        setLocationRelativeTo(null); // Center window on screen
        setVisible(true); // Show the frame
    }

    /**
     * Displays a dialog for adding a new book with fields for ID and title.
     * Saves the book via LibraryManager and updates books.csv.
     */
    private void showAddBookDialog() {
        JDialog dialog = new JDialog(this, "Add New Book", true); // Modal dialog
        dialog.setLayout(new GridLayout(3, 2, 5, 5)); // 3x2 grid for inputs

        JTextField idField = new JTextField(); // Book ID input
        JTextField titleField = new JTextField(); // Book title input
        JButton addButton = new JButton("Add"); // Confirm button

        // Add components to dialog
        dialog.add(new JLabel("Book ID:"));
        dialog.add(idField);
        dialog.add(new JLabel("Title:"));
        dialog.add(titleField);
        dialog.add(new JLabel()); // Empty cell
        dialog.add(addButton);

        // Handle add button click
        addButton.addActionListener(e -> {
            try {
                Book book = new Book(
                    idField.getText().trim(),
                    titleField.getText().trim(),
                    true // New books are available by default
                );
                libraryManager.addBook(book); // Add book to library
                outputArea.append("Added book: " + book.getId() + " - " + book.getTitle() + "\n");
                dialog.dispose(); // Close dialog
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.pack(); // Size dialog to fit components
        dialog.setLocationRelativeTo(this); // Center dialog
        dialog.setVisible(true); // Show dialog
    }

    /**
     * Displays a dialog to remove a book by selecting from a list.
     * Updates books.csv if removal is successful.
     */
    private void showRemoveBookDialog() {
        List<Book> books = libraryManager.getBooks(); // Get current books
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No books available to remove!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create array of book options for dropdown
        String[] bookOptions = books.stream()
            .map(b -> b.getId() + ": " + b.getTitle())
            .toArray(String[]::new);

        // Show dropdown dialog for book selection
        String selected = (String) JOptionPane.showInputDialog(
            this,
            "Select book to remove:",
            "Remove Book",
            JOptionPane.QUESTION_MESSAGE,
            null,
            bookOptions,
            bookOptions[0]);

        if (selected != null) { // If a book was selected
            try {
                String bookId = selected.split(":")[0].trim(); // Extract book ID
                libraryManager.removeBook(bookId); // Remove book
                outputArea.append("Removed book: " + selected + "\n");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Displays a dialog to add a new student with roll number and password.
     * Includes a hint for password requirements and updates students.csv.
     */
    private void showAddStudentDialog() {
        JDialog dialog = new JDialog(this, "Add New Student", true); // Modal dialog
        dialog.setLayout(new GridLayout(4, 2, 5, 5)); // 4x2 grid for inputs and hint

        JTextField rollField = new JTextField(); // Roll number input
        JPasswordField passField = new JPasswordField(); // Password input
        JLabel passHint = new JLabel("Min 7 chars, 1 upper, 1 lower, 1 num"); // Password rules
        JButton addButton = new JButton("Add"); // Confirm button

        // Add components to dialog
        dialog.add(new JLabel("Roll No:"));
        dialog.add(rollField);
        dialog.add(new JLabel("Password:"));
        dialog.add(passField);
        dialog.add(new JLabel());
        dialog.add(passHint);
        dialog.add(new JLabel());
        dialog.add(addButton);

        // Handle add button click
        addButton.addActionListener(e -> {
            try {
                Student student = new Student(
                    rollField.getText().trim(),
                    new String(passField.getPassword()).trim()
                );
                libraryManager.addStudent(student); // Add student to library
                outputArea.append("Added student: " + student.getRollNo() + "\n");
                dialog.dispose(); // Close dialog
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.pack(); // Size dialog to fit components
        dialog.setLocationRelativeTo(this); // Center dialog
        dialog.setVisible(true); // Show dialog
    }

    /**
     * Displays library statistics in the output area, including book and student counts.
     */
    private void showStatistics() {
        List<Book> books = libraryManager.getBooks(); // Get current books
        long available = books.stream().filter(Book::isAvailable).count(); // Count available books
        long borrowed = books.size() - available; // Calculate borrowed books

        // Display statistics
        outputArea.setText("=== Library Statistics ===\n");
        outputArea.append("Total Books: " + books.size() + "\n");
        outputArea.append("Available: " + available + "\n");
        outputArea.append("Borrowed: " + borrowed + "\n");
        outputArea.append("Total Students: " + libraryManager.getStudents().size() + "\n");
    }

    /**
     * Displays the borrowing history of all books in the output area.
     * Shows book details, student, dates, and status from borrows.csv.
     */
    private void showBorrowHistory() {
        outputArea.setText("=== Borrowing History ===\n");
        List<Borrow> borrows = libraryManager.getBorrowHistory(); // Get all borrow records
        if (borrows.isEmpty()) {
            outputArea.append("No borrowing records found.\n");
            return;
        }
        for (Borrow borrow : borrows) {
            // Find book details
            Book book = libraryManager.getBooks().stream()
                .filter(b -> b.getId().equals(borrow.getBookId()))
                .findFirst()
                .orElse(null);
            String title = book != null ? book.getTitle() : "Unknown";
            String returnedInfo = borrow.getStatus().equals("Returned") 
                ? "Returned on: " + borrow.getReturnedDate() 
                : "Not Returned";
            // Append formatted borrow record
            outputArea.append(
                String.format("Book: %s (%s), Student: %s, Borrowed: %s, Due: %s, Status: %s\n",
                    borrow.getBookId(),
                    title,
                    borrow.getRollNo(),
                    borrow.getBorrowedDate(),
                    borrow.getDueDate(),
                    returnedInfo
                )
            );
        }
    }

    /**
     * Closes the admin frame and opens the main frame.
     */
    private void returnToMain() {
        new MainFrame().setVisible(true); // Open main menu
        dispose(); // Close admin frame
    }
}