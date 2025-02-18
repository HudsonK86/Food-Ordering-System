import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerMenuGUI {

    // Constructor that accepts the logged-in user
    public ManagerMenuGUI(User loggedInUser) {
        // Create the JFrame for Manager Menu
        JFrame managerFrame = new JFrame("Manager Menu");
        managerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managerFrame.setSize(400, 300);
        managerFrame.setLocationRelativeTo(null); // Center the window on the screen

        // Create panel and layout for Manager Menu
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        // Create buttons for each option in the Manager Menu
        JButton vendorPerformanceButton = new JButton("Vendor Performance");
        JButton deliveryPerformanceButton = new JButton("Delivery Performance");
        JButton solveComplaintButton = new JButton("Solve Complaint");
        JButton deleteInappropriateItemButton = new JButton("Delete Inappropriate Item");
        JButton logoutButton = new JButton("Logout");

        // Add buttons to the panel
        panel.add(vendorPerformanceButton);
        panel.add(deliveryPerformanceButton);
        panel.add(solveComplaintButton);
        panel.add(deleteInappropriateItemButton);
        panel.add(logoutButton);

        // Add panel to the frame
        managerFrame.add(panel);
        managerFrame.setVisible(true);

        // Add actions for buttons
        vendorPerformanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the VendorPerformanceGUI window
                new VendorPerformanceGUI((Manager) loggedInUser);
            }
        });

        deliveryPerformanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the DeliveryPerformanceGUI window
                new DeliveryPerformanceGUI((Manager) loggedInUser);
            }
        });

        solveComplaintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the SolveComplaintGUI window
                new SolveComplaintGUI((Manager) loggedInUser);
            }
        });

        deleteInappropriateItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the DeleteInappropriateItemGUI window
                new DeleteInappropriateItemGUI((Manager) loggedInUser);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User.logout(); // Logout logic if needed

                JOptionPane.showMessageDialog(managerFrame, "Logged out successfully");

                managerFrame.dispose(); // Close the Manager Menu

                new LoginGUI(); // Show the login screen again
            }
        });
    }
}