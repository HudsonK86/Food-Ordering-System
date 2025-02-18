import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangePasswordGUI {

    public ChangePasswordGUI(Admin admin) {
        // Create a JFrame for the Change Password GUI
        JFrame frame = new JFrame("Change Password");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null); // Center the window

        // Create a panel for the layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        // Create components
        JLabel userIDLabel = new JLabel("User ID:");
        JTextField userIDField = new JTextField();
        JLabel newPasswordLabel = new JLabel("New Password:");
        JPasswordField newPasswordField = new JPasswordField();
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");

        // Add components to the panel
        panel.add(userIDLabel);
        panel.add(userIDField);
        panel.add(newPasswordLabel);
        panel.add(newPasswordField);
        panel.add(submitButton);
        panel.add(cancelButton);

        // Add panel to the frame
        frame.add(panel);
        frame.setVisible(true);

        // Action for Submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get input values
                String userID = userIDField.getText();
                String newPassword = new String(newPasswordField.getPassword());

                // Validate inputs
                if (userID.isEmpty() || newPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Change the password using the Admin class
                String result = Admin.changePassword(userID, newPassword);
                JOptionPane.showMessageDialog(frame, result, result.startsWith("Password changed") ? "Success" : "Error", JOptionPane.INFORMATION_MESSAGE);

                if (result.startsWith("Password changed")) {
                    frame.dispose();
                }
            }
        });

        // Action for Cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }
}
