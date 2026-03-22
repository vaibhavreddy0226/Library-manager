import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;

/**
 * Student GUI frame for viewing and managing book borrowings.
 * Displays buttons on the left for actions and a text area on the right for book details.
 */
public class StudentFrame extends JFrame {
    private LibraryManager libraryManager; // Manages library data and operations
    private String rollNo; // Student's roll number
    private JTextArea bookArea; // Displays book lists and borrow details
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Date format

    /**
     * Initializes the student frame for the given student.
     * @param libraryManager Instance for accessing library data and operations
     * @param rollNo Student's roll number
     */
    public StudentFrame(LibraryManager libraryManager, String rollNo) {
        this.libraryManager = libraryManager;
        this.rollNo = rollNo;
        setTitle("Student Panel - " + rollNo); // Window title
        setSize(500, 400); // Window dimensions
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close frame on exit
        setLayout(new BorderLayout(10, 10)); // Main layout with 10px gaps

        // Create button panel with vertical layout
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
        JButton showBooksButton = new JButton("Show Available Books"); // List all books
        JButton borrowButton = new JButton("Borrow a Book"); // Borrow a book
        JButton returnButton = new JButton("Return a Book"); // Return a book
        JButton viewBorrowedButton = new JButton("View My Borrowed Books"); // List borrowed books
        JButton backButton = new JButton("Logout"); // Return to main menu

        // Add buttons to panel
        buttonPanel.add(showBooksButton);
        buttonPanel.add(borrowButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(viewBorrowedButton);
        buttonPanel.add(backButton);

        // Create text area for displaying books
        bookArea = new JTextArea();
        bookArea.setEditable(false); // Prevent editing
        JScrollPane scrollPane = new JScrollPane(bookArea); // Add scroll functionality

        // Add components to frame
        add(buttonPanel, BorderLayout.WEST); // Buttons on left
        add(scrollPane, BorderLayout.CENTER); // Text area in center

        // Show all books with availability
        showBooksButton.addActionListener(e -> {
            bookArea.setText("=== Available Books ===\n");
            List<Book> books = libraryManager.getBooks();
            for (Book book : books) {
                bookArea.append(
                    String.format("%s: %-25s - %s\n",
                        book.getId(),
                        book.getTitle(),
                        book.isAvailable() ? "AVAILABLE" : "BORROWED"
                    )
                );
            }
        });

        // Borrow a book with ID and due date inputs
        borrowButton.addActionListener(e -> {
            String bookId = JOptionPane.showInputDialog(
                this,
                "Enter Book ID to Borrow:",
                "Borrow Book",
                JOptionPane.QUESTION_MESSAGE
            );

            if (bookId != null && !bookId.trim().isEmpty()) {
                String dueDate = JOptionPane.showInputDialog(
                    this,
                    "Enter Due Date (YYYY-MM-DD):",
                    "Due Date",
                    JOptionPane.QUESTION_MESSAGE
                );

                if (dueDate != null && !dueDate.trim().isEmpty()) {
                    try {
                        // Validate due date
                        LocalDate due = LocalDate.parse(dueDate, DATE_FORMATTER);
                        LocalDate today = LocalDate.now();
                        if (!due.isAfter(today)) {
                            JOptionPane.showMessageDialog(
                                this,
                                "Due date must be after today!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }
                        if (due.getMonthValue() < 1 || due.getMonthValue() > 12 ||
                            due.getDayOfMonth() < 1 || due.getDayOfMonth() > due.lengthOfMonth()) {
                            JOptionPane.showMessageDialog(
                                this,
                                "Invalid due date: month (1-12) or day (1-" + due.lengthOfMonth() + ")!",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }
                        libraryManager.borrowBook(rollNo, bookId.trim(), dueDate.trim()); // Borrow book
                        JOptionPane.showMessageDialog(
                            this,
                            "Book borrowed successfully!\nDue Date: " + dueDate,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                        showBooksButton.doClick(); // Refresh book list
                    } catch (IllegalArgumentException | DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(
                            this,
                            ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });

        // Return a borrowed book
        returnButton.addActionListener(e -> {
            String bookId = JOptionPane.showInputDialog(
                this,
                "Enter Book ID to Return:",
                "Return Book",
                JOptionPane.QUESTION_MESSAGE
            );

            if (bookId != null && !bookId.trim().isEmpty()) {
                try {
                    libraryManager.returnBook(rollNo, bookId.trim()); // Return book
                    JOptionPane.showMessageDialog(
                        this,
                        "Book returned successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    showBooksButton.doClick(); // Refresh book list
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(
                        this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        // Show student's borrowed books
        viewBorrowedButton.addActionListener(e -> {
            bookArea.setText("=== My Borrowed Books ===\n");
            List<Borrow> myBorrows = libraryManager.getBorrows().stream()
                .filter(b -> b.getRollNo().equals(rollNo))
                .toList();
            if (myBorrows.isEmpty()) {
                bookArea.append("No books currently or previously borrowed.\n");
            } else {
                for (Borrow borrow : myBorrows) {
                    Book book = libraryManager.getBooks().stream()
                        .filter(b -> b.getId().equals(borrow.getBookId()))
                        .findFirst()
                        .orElse(null);
                    String title = book != null ? book.getTitle() : "Unknown";
                    String returnedInfo = borrow.getStatus().equals("Returned") 
                        ? "Returned on: " + borrow.getReturnedDate() 
                        : "Not Returned";
                    bookArea.append(
                        String.format("%s: %-25s - Borrowed: %s, Due: %s, Status: %s\n",
                            borrow.getBookId(),
                            title,
                            borrow.getBorrowedDate(),
                            borrow.getDueDate(),
                            returnedInfo
                        )
                    );
                }
            }
        });

        // Logout and return to main menu
        backButton.addActionListener(e -> {
            new MainFrame().setVisible(true); // Open main menu
            dispose(); // Close student frame
        });

        setLocationRelativeTo(null); // Center window
        setVisible(true); // Show frame
    }
}