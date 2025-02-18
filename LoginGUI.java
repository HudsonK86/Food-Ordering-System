import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI {

    private JFrame frame; // Declare the JFrame as a class-level variable

    public LoginGUI() {
        initialize(); // Initialize and show the login screen
    }

    public void initialize() {
        // Create the JFrame and set basic properties
        frame = new JFrame("Food Ordering System - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // Center the window on the screen

        // Create panels and layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        // Create components for User ID, Password, and Login button
        JLabel userLabel = new JLabel("User ID:");
        JTextField userIDField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Login");
        JButton exitButton = new JButton("Exit");

        // Add components to the panel
        panel.add(userLabel);
        panel.add(userIDField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel()); // Empty label for alignment
        panel.add(loginButton);
        panel.add(exitButton);

        // Add the panel to the frame
        frame.add(panel);
        frame.setVisible(true);

        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = userIDField.getText();
                String password = new String(passwordField.getPassword());

                // Attempt to login with the entered credentials
                User loggedInUser = User.login(userID, password);

                if (loggedInUser != null) {
                    JOptionPane.showMessageDialog(frame, "Login successful! Welcome, " + loggedInUser.getName() + ".");
                    frame.dispose(); // Close the login frame

                    if (loggedInUser instanceof Admin) {
                        new AdminMenuGUI(loggedInUser); // Open Admin Menu
                    } else if (loggedInUser instanceof Vendor) {
                        new VendorMenuGUI(loggedInUser); // Open Vendor Menu
                    } else if (loggedInUser instanceof Customer) {
                        new CustomerMenuGUI(loggedInUser); // Open Customer Menu
                    } else if (loggedInUser instanceof Delivery) {
                        new DeliveryMenuGUI(loggedInUser); // Open Delivery Menu
                    } else if (loggedInUser instanceof Manager) {
                        new ManagerMenuGUI(loggedInUser); // Open Manager Menu
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid User ID or Password. Please try again.");
                }
            }
        });

        // Exit button action
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application
            }
        });
    }

    public static void main(String[] args) {
        new LoginGUI(); // Start the login screen
    }
}
