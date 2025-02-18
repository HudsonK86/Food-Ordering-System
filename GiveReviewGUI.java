import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GiveReviewGUI extends JFrame {
    private Customer customer;
    private DefaultTableModel tableModel;
    private JTable orderTable;

    public GiveReviewGUI(Customer customer) {
        this.customer = customer;

        setTitle("Give Review or Complaint");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize tableModel
        tableModel = new DefaultTableModel(new Object[]{"Order ID", "Vendor ID", "Order Type", "Total Price", "Status", "Order Date", "Items"}, 0);
        orderTable = new JTable(tableModel);
        loadOrderHistory(); // Load order data into table

        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(backButton);

        JButton giveReviewButton = new JButton("Give Review");
        giveReviewButton.addActionListener(e -> giveReviewOrComplaint(true));
        buttonPanel.add(giveReviewButton);

        JButton giveComplaintButton = new JButton("Give Complaint");
        giveComplaintButton.addActionListener(e -> giveReviewOrComplaint(false));
        buttonPanel.add(giveComplaintButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadOrderHistory() {
        List<Order> orders = FileHandler.loadOrderData(customer);
        tableModel.setRowCount(0); // Clear existing rows

        for (Order order : orders) {
            String status = order.getStatus();
            if (status.equals("Successful")) {
                StringBuilder items = new StringBuilder();
                for (OrderItem item : order.getItems()) {
                    items.append(item.getMenuItem().getName()).append(" (").append(item.getQuantity()).append("), ");
                }
                if (items.length() > 0) {
                    items.setLength(items.length() - 2); // Remove the last comma and space
                }
                tableModel.addRow(new Object[]{
                    order.getOrderID(),
                    order.getVendorID(),
                    order.getOrderType(),
                    order.getTotalPrice(),
                    status,
                    order.getOrderDate(),
                    items.toString()
                });
            }
        }
    }

    private void giveReviewOrComplaint(boolean isReview) {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            int orderID = (int) tableModel.getValueAt(selectedRow, 0);
            List<Review> reviews = FileHandler.loadReviewData();

            for (Review review : reviews) {
                if (review.getOrderID() == orderID) {
                    JOptionPane.showMessageDialog(this, "You already shared your review on this order.");
                    return;
                }
            }

            String type = isReview ? "Review" : "Complaint";
            String reviewText = JOptionPane.showInputDialog(this, "Enter your " + type.toLowerCase() + ":");
            int rating = 0;

            if (isReview) {
                Object[] options = {"1", "2", "3", "4", "5"};
                int selectedOption = JOptionPane.showOptionDialog(this, "Select rating (1-5):", "Rating",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (selectedOption >= 0) {
                    rating = Integer.parseInt(options[selectedOption].toString());
                } else {
                    JOptionPane.showMessageDialog(this, "Rating is required for a review.");
                    return;
                }
            }

            Review newReview = new Review(orderID, type, reviewText, rating);
            reviews.add(newReview);
            FileHandler.saveReviewData(reviews);

            JOptionPane.showMessageDialog(this, type + " submitted successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to give a " + (isReview ? "review" : "complaint") + ".");
        }
    }
}