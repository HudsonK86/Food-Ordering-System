import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateSalesReportGUI extends JFrame {
    private Vendor vendor;
    private DefaultTableModel tableModel;
    private JTable reportTable;
    private JButton backButton;

    public GenerateSalesReportGUI(Vendor vendor) {
        this.vendor = vendor;

        setTitle("Generate Sales Report");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize tableModel
        tableModel = new DefaultTableModel(new Object[]{"Month", "Total Revenue"}, 0);
        reportTable = new JTable(tableModel);
        loadSalesDataIntoTable();

        JScrollPane scrollPane = new JScrollPane(reportTable);
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

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadSalesDataIntoTable() {
        List<Order> orders = FileHandler.loadOrderData();
        Map<String, Double> monthlyRevenue = new HashMap<>();
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");

        for (Order order : orders) {
            if (order.getVendorID().equals(vendor.getUserID()) && order.getStatus().equals("Successful")) {
                String month = monthFormat.format(order.getOrderDate());
                monthlyRevenue.put(month, monthlyRevenue.getOrDefault(month, 0.0) + order.getTotalPrice());
            }
        }

        tableModel.setRowCount(0); // Clear existing rows
        for (Map.Entry<String, Double> entry : monthlyRevenue.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }
}