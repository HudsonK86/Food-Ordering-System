import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class DeliveryPerformanceGUI extends JFrame {
    private JComboBox<Delivery> deliveryComboBox;
    private DefaultTableModel tableModel;
    private JTable performanceTable;
    private JButton backButton;
    private Manager manager;

    public DeliveryPerformanceGUI(Manager manager) {
        this.manager = manager;

        setTitle("Delivery Performance");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Delivery selection
        List<Delivery> deliveries = FileHandler.loadDeliveries();
        deliveryComboBox = new JComboBox<>(deliveries.toArray(new Delivery[0]));
        deliveryComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPerformanceDataIntoTable();
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Delivery:"));
        topPanel.add(deliveryComboBox);
        add(topPanel, BorderLayout.NORTH);

        // Initialize tableModel
        tableModel = new DefaultTableModel(new Object[]{"Month", "Total Earnings"}, 0);
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
        Delivery selectedDelivery = (Delivery) deliveryComboBox.getSelectedItem();
        if (selectedDelivery != null) {
            List<Task> tasks = FileHandler.loadTaskData();
            Map<String, Double> monthlyEarnings = new HashMap<>();
            SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");

            for (Task task : tasks) {
                if (task.getDeliveryID().equals(selectedDelivery.getUserID()) && "Completed".equalsIgnoreCase(task.getStatus())) {
                    String month = monthFormat.format(task.getCompletedDate());
                    monthlyEarnings.put(month, monthlyEarnings.getOrDefault(month, 0.0) + 12.0);
                }
            }

            for (Map.Entry<String, Double> entry : monthlyEarnings.entrySet()) {
                tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
            }
        }
    }
}