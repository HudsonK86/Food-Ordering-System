import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VendorMenuGUI {

    // Constructor that accepts the logged-in user
    public VendorMenuGUI(User loggedInUser) {
        // Create the JFrame for Vendor Menu
        JFrame vendorFrame = new JFrame("Vendor Menu");
        vendorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        vendorFrame.setSize(400, 300);
        vendorFrame.setLocationRelativeTo(null); // Center the window on the screen

        // Create panel and layout for Vendor Menu
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1, 10, 10));

        // Create buttons for each option in the Vendor Menu
        JButton viewMenuButton = new JButton("View and Update Menu");
        JButton viewReviewsButton = new JButton("View Customer Reviews");
        JButton acceptDeclineOrderButton = new JButton("Accept/Decline Orders");
        JButton viewCurrentOrdersButton = new JButton("View Current Orders");
        JButton generateReportButton = new JButton("Generate Sales Report");
        JButton logoutButton = new JButton("Logout");

        // Add buttons to the panel
        panel.add(viewMenuButton);
        panel.add(viewReviewsButton);
        panel.add(acceptDeclineOrderButton);
        panel.add(viewCurrentOrdersButton);
        panel.add(generateReportButton);
        panel.add(logoutButton);

        // Add panel to the frame
        vendorFrame.add(panel);
    
        // Add actions for buttons
        viewMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the ViewMenuByVendorGUI window
                new ViewAndUpdateMenuGUI((Vendor) loggedInUser);         
            }
        });

        viewReviewsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the ViewReviewsGUI window
                new SeeReviewForVendorGUI((Vendor) loggedInUser);
            }
        });

        acceptDeclineOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the AcceptRejectOrderGUI window
                new ManageOrderGUI((Vendor) loggedInUser);
            }
        });

        viewCurrentOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the ViewCurrentOrdersGUI window
                new CheckCurrentOrderGUI((Vendor) loggedInUser);
            }
        });

        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the GenerateSalesReportGUI window
                new GenerateSalesReportGUI((Vendor) loggedInUser);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User.logout(); // Logout logic if needed

                JOptionPane.showMessageDialog(vendorFrame, "Logged out successfully");

                vendorFrame.dispose(); // Close the Vendor Menu

                new LoginGUI(); // Show the login screen again
            }
        });

        // Set the frame visible
        vendorFrame.setVisible(true);
    }
}
