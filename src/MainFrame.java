import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Main GUI frame for the Library Management System.
 * Provides options for student login, student registration, and admin login.
 */
public class MainFrame extends JFrame {
    private LibraryManager libraryManager; // Manages library data and operations

    /**
     * Initializes the main frame with login and registration options.
     */ 
    public MainFrame() {
        libraryManager = new LibraryManager(); // Initialize library manager
        setTitle("Library Management System"); // Window title
        setSize(400, 300); // Window dimensions
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit app on close
        setLocationRelativeTo(null); // Center window

        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Library Management System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Title font

        // Create button panel with vertical layout
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding
        JButton studentLoginButton = new JButton("Student Login"); // Student login button
        JButton studentRegisterButton = new JButton("Student Registration"); // Student registration button
        JButton adminLoginButton = new JButton("Admin Login"); // Admin login button

        // Add buttons to panel
        buttonPanel.add(studentLoginButton);
        buttonPanel.add(studentRegisterButton);
        buttonPanel.add(adminLoginButton);

        // Add components to main panel
        mainPanel.add(welcomeLabel, BorderLayout.NORTH); // Title at top
        mainPanel.add(buttonPanel, BorderLayout.CENTER); // Buttons in center
        add(mainPanel); // Add panel to frame

        // Register action listeners for buttons
        studentLoginButton.addActionListener(e -> showLoginDialog("Student Login", true, false));
        studentRegisterButton.addActionListener(e -> showLoginDialog("Student Registration", false, false));
        adminLoginButton.addActionListener(e -> showLoginDialog("Admin Login", true, true));

        setVisible(true); // Show the frame
    }

    /**
     * Shows a dialog for login or registration with fields for username/roll number and password.
     * @param title Dialog title
     * @param isLogin True for login, false for registration
     * @param isAdmin True for admin login, false for student
     */
    private void showLoginDialog(String title, boolean isLogin, boolean isAdmin) {
        JDialog dialog = new JDialog(this, title, true); // Modal dialog
        dialog.setSize(300, 150); // Dialog size
        dialog.setLocationRelativeTo(this); // Center dialog

        // Create panel with 3x2 grid for inputs
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding

        JLabel userLabel = new JLabel(isAdmin ? "Username:" : "Roll No:"); // Label for username or roll number
        JTextField userField = new JTextField(); // Input field
        JLabel passLabel = new JLabel("Password:"); // Password label
        JPasswordField passField = new JPasswordField(); // Password input
        JButton actionButton = new JButton(isLogin ? "Login" : "Register"); // Action button

        // Add components to panel
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(new JLabel()); // Empty cell
        panel.add(actionButton);

        // Handle button click
        actionButton.addActionListener(e -> {
            String user = userField.getText().trim(); // Get username/roll number
            String password = new String(passField.getPassword()).trim(); // Get password

            // Validate inputs
            if (user.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                if (isAdmin) {
                    // Admin login
                    if (libraryManager.adminLogin(user, password)) {
                        new AdminFrame(libraryManager).setVisible(true); // Open admin panel
                        dialog.dispose(); // Close dialog
                        dispose(); // Close main frame
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (isLogin) {
                    // Student login
                    if (libraryManager.studentLogin(user, password)) {
                        new StudentFrame(libraryManager, user).setVisible(true); // Open student panel
                        dialog.dispose(); // Close dialog
                        dispose(); // Close main frame
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Student registration
                    libraryManager.addStudent(new Student(user, password)); // Add student to students.csv
                    JOptionPane.showMessageDialog(dialog, "Registration successful!\nYou can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose(); // Close dialog
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(panel); // Add panel to dialog
        dialog.setVisible(true); // Show dialog
    }

    /**
     * Main entry point to launch the application.
     * Runs the GUI on the Event Dispatch Thread.
     * @param args Command-line arguments (unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}