/**
 * Represents a student in the library system.
 * Stored in students.csv with format: rollNo,password.
 */
public class Student {
    private String rollNo; // Student's unique roll number
    private String password; // Student's password

    /**
     * Constructs a Student with the given roll number and password.
     * Password must be at least 7 characters, with at least one uppercase letter,
     * one lowercase letter, and one number.
     * @param rollNo Student's unique roll number
     * @param password Student's password
     * @throws IllegalArgumentException if rollNo or password is invalid
     */
    public Student(String rollNo, String password) {
        // Validate roll number
        if (rollNo == null || rollNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Roll number cannot be empty");
        }
        // Validate password
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        // Enforce password rules
        if (password.length() < 7) {
            throw new IllegalArgumentException("Password must be at least 7 characters long");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Password must contain at least one number");
        }
        this.rollNo = rollNo.trim();
        this.password = password.trim();
    }

    /**
     * Gets the student's roll number.
     * @return roll number
     */
    public String getRollNo() { return rollNo; }

    /**
     * Gets the student's password.
     * @return password
     */
    public String getPassword() { return password; }

    /**
     * Returns the student's data for CSV storage.
     * @return String in format "rollNo,password"
     */
    @Override
    public String toString() {
        return rollNo + "," + password;
    }
}