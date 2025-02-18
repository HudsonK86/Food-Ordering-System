import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteUserGUI {

    public DeleteUserGUI(Admin admin) {
        // Create a JFrame for the Delete User GUI
        JFrame frame = new JFrame("Delete User");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null); // Center the window

        // Create a panel for the layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));

        // Create components
        JLabel userIDLabel = new JLabel("User ID:");
        JTextField userIDField = new JTextField();
        JButton deleteButton = new JButton("Delete");
        JButton cancelButton = new JButton("Cancel");

        // Add components to the panel
        panel.add(userIDLabel);
        panel.add(userIDField);
        panel.add(deleteButton);
        panel.add(cancelButton);

        // Add panel to the frame
        frame.add(panel);
        frame.setVisible(true);

        // Action for Delete button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get input value
                String userID = userIDField.getText();

                // Validate input
                if (userID.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "User ID is required.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Delete the user using the Admin class
                String result = Admin.deleteUser(userID);
                JOptionPane.showMessageDialog(frame, result, result.startsWith("User deleted") ? "Success" : "Error", JOptionPane.INFORMATION_MESSAGE);

                if (result.startsWith("User deleted")) {
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