import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMenuGUI {

    // Constructor that accepts the logged-in user
    public AdminMenuGUI(User loggedInUser) {
        // Create the JFrame for Admin Menu
        JFrame adminFrame = new JFrame("Admin Menu");
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.setSize(400, 300);
        adminFrame.setLocationRelativeTo(null); // Center the window on the screen

        // Create panel and layout for Admin Menu
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));

        // Create buttons for each option in the Admin Menu
        JButton registerButton = new JButton("Register User");
        JButton changePasswordButton = new JButton("Change Password");
        JButton deleteUserButton = new JButton("Delete User");
        JButton topUpWalletButton = new JButton("Top Up Wallet");
        JButton viewUserDetailsButton = new JButton("View User Details");
        JButton logoutButton = new JButton("Logout");

        // Add buttons to the panel
        panel.add(registerButton);
        panel.add(changePasswordButton);
        panel.add(deleteUserButton);
        panel.add(topUpWalletButton);
        panel.add(viewUserDetailsButton);
        panel.add(logoutButton);

        // Add panel to the frame
        adminFrame.add(panel);
        adminFrame.setVisible(true);

        // Add actions for buttons
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the RegisterGUI window
                new RegisterUserGUI((Admin) loggedInUser);
            }
        });
        
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loggedInUser instanceof Admin) {
                    new ChangePasswordGUI((Admin) loggedInUser);
                }
            }
        });

        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeleteUserGUI((Admin) loggedInUser);
            }
        });
        
        topUpWalletButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TopUpWalletGUI((Admin) loggedInUser); // Open the GUI for top-up
            }
        });        

        viewUserDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the viewUserDetails method from the Admin class
                new ViewUserDetailsGUI((Admin) loggedInUser);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User.logout(); // Logout logic if needed
        
                JOptionPane.showMessageDialog(adminFrame, "Logged out successfully");
        
                adminFrame.dispose(); // Close the Admin Menu
        
                new LoginGUI(); // Show the login screen again
            }
        });        
    }
}
