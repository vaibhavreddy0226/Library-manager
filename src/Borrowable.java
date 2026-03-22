/**
 * Interface for items that can be borrowed, such as books.
 * Defines methods for borrowing and returning items.
 */
public interface Borrowable {
    /**
     * Borrows the item for a student with a specified due date.
     * Updates item availability (e.g., in books.csv for books).
     * @param rollNo Student's roll number
     * @param dueDate Due date for return (YYYY-MM-DD)
     */
    void borrow(String rollNo, String dueDate);

    /**
     * Returns the borrowed item, making it available again.
     * Updates item availability (e.g., in books.csv for books).
     */
    void returnItem();
}