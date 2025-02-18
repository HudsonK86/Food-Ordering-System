import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class CheckOrderStatusGUI extends JFrame {
    private Customer customer;
    private DefaultTableModel tableModel;
    private JTable orderTable;
    private JButton backButton;

    public CheckOrderStatusGUI(Customer customer) {
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
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadOrderDataIntoTable() {
        tableModel.setRowCount(0); // Clear existing rows
        List<Order> customerOrders = FileHandler.loadOrderData().stream()
                .filter(order -> order.getCustomerID().equals(customer.getUserID()) &&
                        (order.getStatus().equals("Pending") ||
                        order.getStatus().equals("Waiting for Delivery") ||
                        order.getStatus().equals("Delivery On The Way")))
                .collect(Collectors.toList());

        for (Order order : customerOrders) {
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