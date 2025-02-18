import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewMenuAndPlaceOrderGUI {
    private JFrame frame;
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<Vendor> vendorComboBox;
    private JComboBox<String> orderTypeComboBox;
    private JLabel totalPriceLabel;
    private List<MenuItem> menuItems;
    private Customer customer;
    private List<OrderItem> orderItems;
    private List<Order> existingOrders = new ArrayList<>();
    private double totalPrice = 0.0;
    private boolean isDeliveryFeeAdded = false;
    private Vendor selectedVendor; // Declare selectedVendor as an instance variable

    public ViewMenuAndPlaceOrderGUI(User customer) {
        this.orderItems = new ArrayList<>();
        this.customer = (Customer) customer;

        frame = new JFrame("View Menu and Place Order");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Vendor selection
        List<Vendor> vendors = FileHandler.loadVendors();
        vendorComboBox = new JComboBox<>(vendors.toArray(new Vendor[0]));
        vendorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedVendor = (Vendor) vendorComboBox.getSelectedItem(); // Set the selectedVendor
                if (selectedVendor != null) {
                    menuItems = FileHandler.loadMenuData(selectedVendor);
                    loadMenuDataIntoTable();
                }
            }
        });

        // Order type selection
        orderTypeComboBox = new JComboBox<>(new String[]{"Dine-In", "Takeaway", "Delivery"});
        orderTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOrderType = (String) orderTypeComboBox.getSelectedItem();
                if ("Delivery".equals(selectedOrderType)) {
                    if (!isDeliveryFeeAdded) {
                        addDeliveryFee();
                        isDeliveryFeeAdded = true;
                    }
                } else {
                    if (isDeliveryFeeAdded) {
                        removeDeliveryFee();
                        isDeliveryFeeAdded = false;
                    }
                }
                updateTotalPrice();
            }
        });

        // Create a panel for both combo boxes
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Vendor:"));
        topPanel.add(vendorComboBox);
        topPanel.add(new JLabel("Order Type:"));
        topPanel.add(orderTypeComboBox);
        frame.add(topPanel, BorderLayout.NORTH);

        // Initialize tableModel
        tableModel = new DefaultTableModel(new Object[]{"Item ID", "Name", "Price", "Category", "Quantity", "Subprice", "+", "-"}, 0);
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6 || column == 7; // Only + and - buttons are editable
            }
        };
        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox(), true));
        table.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox(), false));

        loadMenuDataIntoTable();

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add and Back buttons
        JPanel buttonPanel = new JPanel();
        JButton placeOrderButton = new JButton("Place Order");
        placeOrderButton.addActionListener(e -> {
            if (selectedVendor != null) {
                placeOrder(selectedVendor.getUserID()); // Use the selectedVendor
            } else {
                // Handle the case where no vendor is selected
                JOptionPane.showMessageDialog(null, "Please select a vendor first.");
            }
        });
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> frame.dispose());
        buttonPanel.add(placeOrderButton);
        buttonPanel.add(backButton);

        // Total price label
        totalPriceLabel = new JLabel("Total Price: $0.00");
        buttonPanel.add(totalPriceLabel);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void loadMenuDataIntoTable() {
        tableModel.setRowCount(0); // Clear existing rows
        if (menuItems != null) {
            for (MenuItem item : menuItems) {
                tableModel.addRow(new Object[]{item.getItemID(), item.getName(), item.getPrice(), item.getCategory(), 0, 0.0, "+", "-"});
            }
        }
    }

    private void placeOrder(String vendorID) {
        // Create a new order
        int orderID = Order.generateUniqueOrderID(FileHandler.loadOrderData());
        String orderType = (String) orderTypeComboBox.getSelectedItem();
        double totalPrice = calculateTotalPrice();

        // Add 12 to the total price if the order type is "Delivery"
        if ("Delivery".equals(orderType)) {
            totalPrice += 12;
        }

        Order order = new Order(customer.getUserID(), vendorID, orderID, orderItems, orderType, totalPrice, "Pending", new Date());

        // Deduct balance from customer
        if (customer.deductBalance(order.getTotalPrice())) {
            // Add order to customer's order history
            customer.addOrder(order);

            // Save order to file
            List<Order> orders = FileHandler.loadOrderData();
            orders.add(order);
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
            Transaction transaction = new Transaction(transactionID, customer.getUserID(), "Order", orderID, order.getTotalPrice(), new Date());
            List<Transaction> transactions = FileHandler.loadTransactionData();
            transactions.add(transaction);
            FileHandler.saveTransactionData(transactions);

            JOptionPane.showMessageDialog(frame, "Order placed successfully!");
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Insufficient balance!");
        }
    }

    private double calculateTotalPrice() {
        double totalPrice = 0.0;
        for (OrderItem item : orderItems) {
            totalPrice += item.getSubTotal();
        }
        return totalPrice;
    }

    private void addDeliveryFee() {
        // Add delivery fee to the total price
        double deliveryFee = 12.00; // Example delivery fee
        totalPrice += deliveryFee;
    }

    private void removeDeliveryFee() {
        // Remove delivery fee from the total price
        double deliveryFee = 12.00; // Example delivery fee
        totalPrice -= deliveryFee;
    }

    private void updateTotalPrice() {
        double calculatedTotal = calculateTotalPrice();
        
        // Ensure delivery fee is added only once
        if ("Delivery".equals(orderTypeComboBox.getSelectedItem())) {
            calculatedTotal += 12.00;
        }
        
        totalPriceLabel.setText("Total Price: $" + String.format("%.2f", calculatedTotal));
    }
    
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean isAdd;
        private int row;

        public ButtonEditor(JCheckBox checkBox, boolean isAdd) {
            super(checkBox);
            this.isAdd = isAdd;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                    int quantity = (int) tableModel.getValueAt(row, 4);
                    double price = (double) tableModel.getValueAt(row, 2);
                    if (isAdd) {
                        quantity++;
                    } else {
                        quantity--;
                    }
                    if (quantity < 0) quantity = 0;
                    tableModel.setValueAt(quantity, row, 4);
                    tableModel.setValueAt(quantity * price, row, 5);

                    MenuItem menuItem = menuItems.get(row);
                    OrderItem orderItem = orderItems.stream().filter(item -> item.getMenuItem().getItemID() == menuItem.getItemID()).findFirst().orElse(null);
                    if (orderItem == null && quantity > 0) {
                        orderItem = new OrderItem(menuItem, quantity);
                        orderItems.add(orderItem);
                    } else if (orderItem != null) {
                        if (quantity > 0) {
                            orderItem.setQuantity(quantity);
                        } else {
                            orderItems.remove(orderItem);
                        }
                    }
                    updateTotalPrice();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            button.setText((value == null) ? "" : value.toString());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }
}
