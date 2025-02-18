import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

public class CheckOrderHistoryGUI extends JFrame {
    private Customer customer;
    private DefaultTableModel tableModel;
    private JTable orderTable;

    public CheckOrderHistoryGUI(Customer customer) {
        this.customer = customer;

        setTitle("Check Order History");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize tableModel
        tableModel = new DefaultTableModel(new Object[]{"Order ID", "Vendor ID", "Order Type", "Total Price", "Status", "Order Date", "Items"}, 0);
        orderTable = new JTable(tableModel);
        CheckOrderHistory(); // Load order data into table

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

        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(e -> placeOrder());
        buttonPanel.add(placeOrderButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void CheckOrderHistory() {
        List<Order> orders = FileHandler.loadOrderData(customer);
        tableModel.setRowCount(0); // Clear existing rows

        for (Order order : orders) {
            String status = order.getStatus();
            if (!status.equals("Pending") && !status.equals("Waiting for Delivery") && !status.equals("Delivery On The Way")) {
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

    private void placeOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            int orderID = (int) tableModel.getValueAt(selectedRow, 0);
            List<Order> orders = FileHandler.loadOrderData(customer);
            for (Order order : orders) {
                if (order.getOrderID() == orderID) {
                    // Create a new order based on the selected order
                    Order newOrder = new Order(customer.getUserID(), order.getVendorID(), Order.generateUniqueOrderID(FileHandler.loadOrderData()), order.getItems(), order.getOrderType(), order.getTotalPrice(), "Pending", new Date());

                    // Deduct balance from customer
                    if (customer.deductBalance(newOrder.getTotalPrice())) {
                        // Add order to customer's order history
                        customer.addOrder(newOrder);

                        // Save order to file
                        orders.add(newOrder);
                        FileHandler.saveOrderData(orders);

                        // Update customer data
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
                        Transaction transaction = new Transaction(transactionID, customer.getUserID(), "Order", newOrder.getOrderID(), newOrder.getTotalPrice(), new Date());
                        List<Transaction> transactions = FileHandler.loadTransactionData();
                        transactions.add(transaction);
                        FileHandler.saveTransactionData(transactions);

                        JOptionPane.showMessageDialog(this, "Order placed successfully!");
                        return;
                    } else {
                        JOptionPane.showMessageDialog(this, "Insufficient balance!");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to place.");
        }
    }
}