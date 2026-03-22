/**
 * Abstract base class for library items, such as books.
 * Provides common fields and methods for ID, title, and availability.
 */
public abstract class LibraryItem {
    protected String id; // Unique identifier
    protected String title; // Item title
    protected boolean available; // Availability status

    /**
     * Constructs a LibraryItem with the given details.
     * @param id Item's unique identifier
     * @param title Item's title
     * @param available Whether the item is available
     */
    protected LibraryItem(String id, String title, boolean available) {
        this.id = id;
        this.title = title;
        this.available = available;
    }

    /**
     * Gets the item's ID.
     * @return ID
     */
    public String getId() { return id; }

    /**
     * Gets the item's title.
     * @return title
     */
    public String getTitle() { return title; }

    /**
     * Checks if the item is available.
     * @return true if available
     */
    public boolean isAvailable() { return available; }

    /**
     * Sets the item's availability.
     * @param available availability status
     */
    public void setAvailable(boolean available) { this.available = available; }

    /**
     * Gets the item's details in a formatted string.
     * @return formatted details
     */
    public abstract String getDetails();
}