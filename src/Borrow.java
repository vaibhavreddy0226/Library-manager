
/**
 * Represents a borrow record linking a student and a book with dates and status.
 * Stored in borrows.csv with format: rollNo,bookId,borrowedDate,dueDate,returnedDate,status.
 */
public class Borrow {
    private String rollNo; // Student's roll number
    private String bookId; // Book's ID
    private String borrowedDate; // Borrow date (YYYY-MM-DD)
    private String dueDate; // Due date (YYYY-MM-DD)
    private String returnedDate; // Return date (YYYY-MM-DD, empty if not returned)
    private String status; // Status (Active or Returned)

    /**
     * Constructs a Borrow record with the given details.
     * @param rollNo Student's roll number
     * @param bookId Book's ID
     * @param borrowedDate Date the book was borrowed (YYYY-MM-DD)
     * @param dueDate Due date for return (YYYY-MM-DD)
     * @param returnedDate Date the book was returned (YYYY-MM-DD, nullable)
     * @param status Borrow status (Active or Returned)
     * @throws IllegalArgumentException if required parameters are empty or invalid
     */
    public Borrow(String rollNo, String bookId, String borrowedDate, String dueDate, String returnedDate, String status) {
        // Validate required fields
        if (rollNo == null || rollNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Roll number cannot be empty");
        }
        if (bookId == null || bookId.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be empty");
        }
        if (borrowedDate == null || borrowedDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Borrowed date cannot be empty");
        }
        if (dueDate == null || dueDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Due date cannot be empty");
        }
        if (status == null || (!status.equals("Active") && !status.equals("Returned"))) {
            throw new IllegalArgumentException("Status must be Active or Returned");
        }
        // Initialize fields with trimmed values
        this.rollNo = rollNo.trim();
        this.bookId = bookId.trim();
        this.borrowedDate = borrowedDate.trim();
        this.dueDate = dueDate.trim();
        this.returnedDate = returnedDate != null ? returnedDate.trim() : "";
        this.status = status;
    }

    /**
     * Marks the borrow as returned with the given date.
     * Updates borrows.csv via CSVHandler.
     * @param returnedDate Date of return (YYYY-MM-DD)
     * @throws IllegalArgumentException if returned date is empty
     */
    public void markAsReturned(String returnedDate) {
        if (returnedDate == null || returnedDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Returned date cannot be empty");
        }
        this.returnedDate = returnedDate.trim();
        this.status = "Returned";
    }

    /**
     * Gets the student's roll number.
     * @return roll number
     */
    public String getRollNo() { return rollNo; }

    /**
     * Gets the book's ID.
     * @return book ID
     */
    public String getBookId() { return bookId; }

    /**
     * Gets the borrowed date.
     * @return borrowed date
     */
    public String getBorrowedDate() { return borrowedDate; }

    /**
     * Gets the due date.
     * @return due date
     */
    public String getDueDate() { return dueDate; }

    /**
     * Gets the returned date.
     * @return returned date or empty string if not returned
     */
    public String getReturnedDate() { return returnedDate; }

    /**
     * Gets the borrow status.
     * @return status (Active or Returned)
     */
    public String getStatus() { return status; }

    /**
     * Returns the borrow record for CSV storage.
     * @return String in format "rollNo,bookId,borrowedDate,dueDate,returnedDate,status"
     */
    @Override
    public String toString() {
        return rollNo + "," + bookId + "," + borrowedDate + "," + dueDate + "," + returnedDate + "," + status;
    }
}