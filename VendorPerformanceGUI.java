import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class VendorPerformanceGUI extends JFrame {
    private JComboBox<Vendor> vendorComboBox;
    private DefaultTableModel tableModel;
    private JTable performanceTable;
    private JButton backButton;
    private Manager manager;

    public VendorPerformanceGUI(Manager manager) {
        this.manager = manager;

        setTitle("Vendor Performance");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Vendor selection
        List<Vendor> vendors = FileHandler.loadVendors();
        vendorComboBox = new JComboBox<>(vendors.toArray(new Vendor[0]));
        vendorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPerformanceDataIntoTable();
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Vendor:"));
        topPanel.add(vendorComboBox);
        add(topPanel, BorderLayout.NORTH);

        // Initialize tableModel
        tableModel = new DefaultTableModel(new Object[]{"Month", "Total Revenue"}, 0);
        performanceTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(performanceTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new ManagerMenuGUI(manager); // Go back to Manager Menu
            }
        });
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadPerformanceDataIntoTable() {
        tableModel.setRowCount(0); // Clear existing rows
        Vendor selectedVendor = (Vendor) vendorComboBox.getSelectedItem();
        if (selectedVendor != null) {
            List<Order> orders = FileHandler.loadOrderData();
            Map<String, Double> monthlyRevenue = new HashMap<>();
            SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");

            for (Order order : orders) {
                if (order.getVendorID().equals(selectedVendor.getUserID()) && order.getStatus().equals("Successful")) {
                    String month = monthFormat.format(order.getOrderDate());
                    monthlyRevenue.put(month, monthlyRevenue.getOrDefault(month, 0.0) + order.getTotalPrice());
                }
            }

            for (Map.Entry<String, Double> entry : monthlyRevenue.entrySet()) {
                tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
            }
        }
    }
}