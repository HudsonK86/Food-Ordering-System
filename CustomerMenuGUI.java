import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerMenuGUI {

    // Constructor that accepts the logged-in user
    public CustomerMenuGUI(User loggedInUser) {
        // Create the JFrame for Customer Menu
        JFrame customerFrame = new JFrame("Customer Menu");
        customerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        customerFrame.setSize(400, 300);
        customerFrame.setLocationRelativeTo(null); // Center the window on the screen

        // Create panel and layout for Customer Menu
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));

        // Create buttons for each option in the Customer Menu
        JButton viewMenuButton = new JButton("View Menu and Place Order");
        JButton checkOrderStatusButton = new JButton("Check Order Status");
        JButton cancelOrderButton = new JButton("Cancel Order");
        JButton checkOrderHistoryButton = new JButton("Check Order History");
        JButton checkTransactionHistoryButton = new JButton("Check Transaction History");
        JButton giveReviewButton = new JButton("Give Review/Complaint");
        JButton checkBalanceButton = new JButton("Check Balance");
        JButton logoutButton = new JButton("Logout");

        // Add buttons to the panel
        panel.add(viewMenuButton);
        panel.add(checkOrderStatusButton);
        panel.add(cancelOrderButton);
        panel.add(checkOrderHistoryButton);
        panel.add(checkTransactionHistoryButton);
        panel.add(giveReviewButton);
        panel.add(checkBalanceButton);
        panel.add(logoutButton);

        // Add panel to the frame
        customerFrame.add(panel);

        // Add action listeners to buttons
        viewMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the GUI for viewing the menu
                new ViewMenuAndPlaceOrderGUI((Customer) loggedInUser);               
            }
        });

        checkOrderStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the GUI for checking order status
                new CheckOrderStatusGUI((Customer) loggedInUser);
            }
        });

        cancelOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the GUI for checking order status
                new CancelOrderGUI((Customer) loggedInUser);
            }
        });

        checkOrderHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the GUI for checking order history
                new CheckOrderHistoryGUI((Customer) loggedInUser).setVisible(true);
            }
        });

        checkTransactionHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the GUI for checking transaction history
                new CheckTransactionHistoryGUI((Customer) loggedInUser);
            }
        });

        giveReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the GUI for giving review or complaint
                new GiveReviewGUI((Customer) loggedInUser).setVisible(true);
            }
        });

        checkBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Display the wallet balance of the customer
                new CheckBalanceGUI((Customer) loggedInUser);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User.logout(); // Logout the user

                JOptionPane.showMessageDialog(customerFrame, "Logged out successfully");

                customerFrame.dispose(); // Close the Customer Menu

                new LoginGUI(); // Show the login screen again
            }
        });

        // Set the frame to be visible
        customerFrame.setVisible(true);
    }

}