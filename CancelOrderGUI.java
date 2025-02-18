import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;

public class CancelOrderGUI extends JFrame {
    private Customer customer;
    private DefaultTableModel tableModel;
    private JTable orderTable;
    private JButton cancelButton;

    public CancelOrderGUI(Customer customer) {
        this.customer = customer;

        setTitle("Check Order Status");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize tableModel
        tableModel = new DefaultTableModel(new Object[]{"Order ID", "Vendor ID", "Order Type", "Total Price", "Status", "Order Date", "Items"}, 0);
        orderTable = new JTable(tableModel);
        loadOrderDataIntoTable();

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

        cancelButton = new JButton("Cancel Order");
        cancelButton.addActionListener(e -> cancelOrder());
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void cancelOrder() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            int orderID = (int) tableModel.getValueAt(selectedRow, 0);
            List<Order> orders = FileHandler.loadOrderData(customer);
            for (Order order : orders) {
                if (order.getOrderID() == orderID) {
                    // Refund the order amount to the customer's balance
                    customer.topUpWallet(order.getTotalPrice());

                    // Create and save transaction
                    String transactionID = Transaction.generateUniqueTransactionID();
                    Transaction transaction = new Transaction(transactionID, customer.getUserID(), "Refund", orderID, order.getTotalPrice(), new Date());
                    List<Transaction> transactions = FileHandler.loadTransactionData();
                    transactions.add(transaction);
                    FileHandler.saveTransactionData(transactions);

                    // Update the customer data in the file
                    List<User> users = FileHandler.loadUserData();
                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).getUserID().equals(customer.getUserID())) {
                            users.set(i, customer);
                            break;
                        }
                    }
                    FileHandler.saveUserData(users);

                    // Set the order status to "Canceled"
                    order.setStatus("Canceled");
                    FileHandler.saveOrderData(orders);

                    // Reload the order data into the table
                    loadOrderDataIntoTable();

                    JOptionPane.showMessageDialog(this, "Order has been canceled and amount refunded.");
                    return;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an order to cancel.");
        }
    }

    private void loadOrderDataIntoTable() {
        tableModel.setRowCount(0); // Clear existing rows
        List<Order> orders = FileHandler.loadOrderData(customer); // Load orders for the specific customer

        List<Order> filteredOrders = orders.stream()
                .filter(order -> order.getStatus().equals("Pending"))
                .collect(Collectors.toList());

        for (Order order : filteredOrders) {
            StringBuilder items = new StringBuilder();
            for (OrderItem item : order.getItems()) {
                items.append(item.getMenuItem().getName()).append(" (").append(item.getQuantity()).append("), ");
            }
            if (items.length() > 0) {
                items.setLength(items.length() - 2); // Remove the last comma and space
            }
            tableModel.addRow(new Object[]{order.getOrderID(), order.getVendorID(), order.getOrderType(), order.getTotalPrice(), order.getStatus(), order.getOrderDate(), items.toString()});
        }
    }
}
