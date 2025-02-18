import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SeeReviewForVendorGUI extends JFrame {
    private Vendor vendor;
    private DefaultTableModel tableModel;
    private JTable orderTable;
    private JButton seeReviewButton;
    private JButton backButton;

    public SeeReviewForVendorGUI(Vendor vendor) {
        this.vendor = vendor;

        setTitle("See Reviews for Vendor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize tableModel
        tableModel = new DefaultTableModel(new Object[]{"Order ID", "Customer ID", "Order Type", "Total Price", "Status", "Order Date", "Items"}, 0);
        orderTable = new JTable(tableModel);
        loadOrderDataIntoTable();

        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new VendorMenuGUI(vendor); // Go back to Vendor Menu
            }
        });
        buttonPanel.add(backButton);

        seeReviewButton = new JButton("See Review");
        seeReviewButton.addActionListener(e -> seeReview());
        buttonPanel.add(seeReviewButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadOrderDataIntoTable() {
        List<Order> orders = FileHandler.loadOrderData();
        tableModel.setRowCount(0); // Clear existing rows

        for (Order order : orders) {
            if (order.getVendorID().equals(vendor.getUserID()) && order.getStatus().equals("Successful")) {
                StringBuilder items = new StringBuilder();
                for (OrderItem item : order.getItems()) {
                    items.append(item.getMenuItem().getName()).append(" (").append(item.getQuantity()).append("), ");
                }
                if (items.length() > 0) {
                    items.setLength(items.length() - 2); // Remove the last comma and space
                }
                tableModel.addRow(new Object[]{
                    order.getOrderID(),
                    order.getCustomerID(),
                    order.getOrderType(),
                    order.getTotalPrice(),
                    order.getStatus(),
                    order.getOrderDate(),
                    items.toString()
                });
            }
        }
    }

    private void seeReview() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            int orderID = (int) tableModel.getValueAt(selectedRow, 0);
            List<Review> reviews = FileHandler.loadReviewData();

            for (Review review : reviews) {
                if (review.getOrderID() == orderID) {
                    JOptionPane.showMessageDialog(this, "Review: " + review.getReviewText() + "\nRating: " + review.getRating());
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "No Review for this order.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to see the review.");
        }
    }
}