import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ManageOrderGUI extends JFrame {
    private DefaultTableModel tableModel;
    private JTable orderTable;
    private JButton acceptButton;
    private JButton declineButton;
    private JButton backButton;
    private List<Order> orders;
    private Vendor vendor;

    public ManageOrderGUI(Vendor vendor) {
        this.vendor = vendor;
        this.orders = FileHandler.loadOrderData(); // Load all orders

        setTitle("Manage Orders");
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

        acceptButton = new JButton("Accept");
        acceptButton.addActionListener(e -> acceptOrder());
        buttonPanel.add(acceptButton);

        declineButton = new JButton("Decline");
        declineButton.addActionListener(e -> declineOrder());
        buttonPanel.add(declineButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void acceptOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            int orderID = (int) tableModel.getValueAt(selectedRow, 0);
            for (Order order : orders) {
                if (order.getOrderID() == orderID) {
                    if ("Dine-In".equals(order.getOrderType()) || "Takeaway".equals(order.getOrderType())) {
                        updateOrderStatus(order, "Successful");
                    } else if ("Delivery".equals(order.getOrderType())) {
                        updateOrderStatus(order, "Waiting for Delivery");
                        order.assignDeliveryToOrder(order); // Assign delivery to the order
                    }
                    return;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to accept.");
        }
    }

    private void declineOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            int orderID = (int) tableModel.getValueAt(selectedRow, 0);
            for (Order order : orders) {
                if (order.getOrderID() == orderID) {
                    updateOrderStatus(order, "Vendor Declined");
                    refundCustomer(order);
                    return;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to decline.");
        }
    }

    private void loadOrderDataIntoTable() {
        tableModel.setRowCount(0); // Clear existing rows
        List<Order> vendorOrders = orders.stream()
                .filter(order -> order.getVendorID().equals(vendor.getUserID()) &&
                        ("Pending".equals(order.getStatus())))
                .collect(Collectors.toList());

        for (Order order : vendorOrders) {
            StringBuilder items = new StringBuilder();
            for (OrderItem item : order.getItems()) {
                items.append(item.getMenuItem().getName()).append(" (").append(item.getQuantity()).append("), ");
            }
            if (items.length() > 0) {
                items.setLength(items.length() - 2); // Remove the last comma and space
            }
            tableModel.addRow(new Object[]{order.getOrderID(), order.getCustomerID(), order.getOrderType(), order.getTotalPrice(), order.getStatus(), order.getOrderDate(), items.toString()});
        }
    }

    private void updateOrderStatus(Order order, String status) {
        order.setStatus(status);
        FileHandler.saveOrderData(orders); // Save the updated orders
        JOptionPane.showMessageDialog(this, "Order status updated to: " + status);
        loadOrderDataIntoTable(); // Refresh the table
    }

    private void refundCustomer(Order order) {
        Customer customer = (Customer) FileHandler.loadUserData().stream()
                .filter(u -> u.getUserID().equals(order.getCustomerID()))
                .findFirst()
                .orElse(null);
        if (customer != null) {
            customer.topUpWallet(order.getTotalPrice());
            List<User> users = FileHandler.loadUserData();
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUserID().equals(customer.getUserID())) {
                    users.set(i, customer);
                    break;
                }
            }
            FileHandler.saveUserData(users);

            // Create and save transaction
            String transactionID = Transaction.generateUniqueTransactionID();
            Transaction transaction = new Transaction(transactionID, customer.getUserID(), "Refund", order.getOrderID(), order.getTotalPrice(), new Date());
            List<Transaction> transactions = FileHandler.loadTransactionData();
            transactions.add(transaction);
            FileHandler.saveTransactionData(transactions);
        }
    }
}