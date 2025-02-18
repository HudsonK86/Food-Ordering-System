import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class TrackEarningGUI extends JFrame {
    private Delivery delivery;
    private DefaultTableModel tableModel;
    private JTable earningTable;
    private JButton backButton;

    public TrackEarningGUI(Delivery delivery) {
        this.delivery = delivery;

        setTitle("Track Earnings");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize tableModel
        tableModel = new DefaultTableModel(new Object[]{"Month", "Total Earnings"}, 0);
        earningTable = new JTable(tableModel);
        loadEarningDataIntoTable();

        JScrollPane scrollPane = new JScrollPane(earningTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new DeliveryMenuGUI(delivery); // Go back to Delivery Menu
            }
        });
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadEarningDataIntoTable() {
        List<Task> tasks = FileHandler.loadTaskData();
        Map<String, Double> monthlyEarnings = new HashMap<>();
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");

        for (Task task : tasks) {
            if (task.getDeliveryID().equals(delivery.getUserID()) && "Completed".equalsIgnoreCase(task.getStatus())) {
                String month = monthFormat.format(task.getCompletedDate());
                monthlyEarnings.put(month, monthlyEarnings.getOrDefault(month, 0.0) + 12.0);
            }
        }

        tableModel.setRowCount(0); // Clear existing rows
        for (Map.Entry<String, Double> entry : monthlyEarnings.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }
}