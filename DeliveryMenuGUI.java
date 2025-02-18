import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeliveryMenuGUI {

    // Constructor that accepts the logged-in user
    public DeliveryMenuGUI(User loggedInUser) {
        // Create the JFrame for Delivery Menu
        JFrame deliveryFrame = new JFrame("Delivery Menu");
        deliveryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        deliveryFrame.setSize(400, 300);
        deliveryFrame.setLocationRelativeTo(null); // Center the window on the screen

        // Create panel and layout for Delivery Menu
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 1, 10, 10));

        // Create buttons for each option in the Delivery Menu
        JButton acceptDeclineTasksButton = new JButton("Accept/Decline Task");
        JButton viewCurrentTaskButton = new JButton("View Current Task");
        JButton viewCustomerReviewButton = new JButton("View Task History and Customer Review");
        JButton trackEarningButton = new JButton("Track Earning");
        JButton logoutButton = new JButton("Logout");

        // Add buttons to the panel
        panel.add(acceptDeclineTasksButton);
        panel.add(viewCurrentTaskButton);
        panel.add(viewCustomerReviewButton);
        panel.add(trackEarningButton);
        panel.add(logoutButton);

        // Add panel to the frame
        deliveryFrame.add(panel);
        deliveryFrame.setVisible(true);

        // Add actions for buttons
        acceptDeclineTasksButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the ManageTasksGUI window
                new ManageTaskGUI((Delivery) loggedInUser);
            }
        });

        viewCurrentTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the ViewCurrentTaskGUI window
                new ViewCurrentTaskGUI((Delivery) loggedInUser);
            }
        });

        viewCustomerReviewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the ViewCustomerReviewGUI window
                new SeeReviewForDeliveryGUI((Delivery) loggedInUser);
            }
        });

        trackEarningButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the TrackEarningGUI window
                new TrackEarningGUI((Delivery) loggedInUser);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User.logout(); // Logout logic if needed

                JOptionPane.showMessageDialog(deliveryFrame, "Logged out successfully");

                deliveryFrame.dispose(); // Close the Delivery Menu

                new LoginGUI(); // Show the login screen again
            }
        });
    }
}