import java.util.Objects;

/**
 * Represents a book in the library, extending LibraryItem and implementing Borrowable.
 * Stored in books.csv with format: id,title,available.
 */
public class Book extends LibraryItem implements Borrowable {
    /**
     * Constructs a Book with the given ID, title, and availability.
     * @param id Book's unique identifier
     * @param title Book's title
     * @param available Whether the book is available for borrowing
     * @throws IllegalArgumentException if ID or title is empty or null
     */
    public Book(String id, String title, boolean available) {
        super(id, title, available); // Initialize parent class
        // Validate inputs
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Book ID cannot be empty");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }
        this.id = id.trim();
        this.title = title.trim();
        this.available = available;
    }

    /**
     * Returns a formatted string of the book's details.
     * @return String in format "id: title (Available/Borrowed)"
     */
    @Override
    public String getDetails() {
        return id + ": " + title + " (" + (available ? "Available" : "Borrowed") + ")";
    }

    /**
     * Marks the book as borrowed if available.
     * Updates availability for saving to books.csv.
     * @param rollNo Student's roll number
     * @param dueDate Due date for return
     * @throws IllegalStateException if book is already borrowed
     */
    @Override
    public void borrow(String rollNo, String dueDate) {
        if (!isAvailable()) {
            throw new IllegalStateException("Book is already borrowed");
        }
        setAvailable(false); // Mark as borrowed
    }

    /**
     * Marks the book as returned.
     * Updates availability for saving to books.csv.
     */
    @Override
    public void returnItem() {
        setAvailable(true); // Mark as available
    }

    /**
     * Returns the book's data for CSV storage.
     * @return String in format "id,title,available"
     */
    @Override
    public String toString() {
        return id + "," + title + "," + available;
    }

    /**
     * Checks equality based on book ID.
     * @param o Object to compare
     * @return true if IDs match
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return id.equals(book.id);
    }

    /**
     * Generates hash code based on book ID.
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}