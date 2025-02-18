import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterUserGUI {

    public RegisterUserGUI(Admin admin) {
        // Create the JFrame for Register User
        JFrame registerFrame = new JFrame("Register User");
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setSize(400, 400);
        registerFrame.setLocationRelativeTo(null); // Center the window

        // Create a panel for the registration form
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));

        // Add labels and input fields
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel userIDLabel = new JLabel("User ID:");
        JTextField userIDField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JLabel roleLabel = new JLabel("Role:");
        String[] roles = {"Customer", "Vendor", "Delivery", "Manager"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);

        // Buttons
        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");

        // Add components to the panel
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(userIDLabel);
        panel.add(userIDField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(roleLabel);
        panel.add(roleComboBox);
        panel.add(registerButton);
        panel.add(cancelButton);

        // Add panel to the frame
        registerFrame.add(panel);
        registerFrame.setVisible(true);

        // Action for Register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get input values
                String name = nameField.getText();
                String userID = userIDField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();

                // Validate inputs
                if (name.isEmpty() || userID.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(registerFrame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Register the user using the Admin class
                String result = Admin.registerUser(name, userID, password, role);
                JOptionPane.showMessageDialog(registerFrame, result, result.startsWith("User registered") ? "Success" : "Error", JOptionPane.INFORMATION_MESSAGE);

                if (result.startsWith("User registered")) {
                    registerFrame.dispose();
                }
            }
        });

        // Action for Cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerFrame.dispose();
            }
        });
    }
}
