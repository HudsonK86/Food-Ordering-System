import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SeeReviewForDeliveryGUI extends JFrame {
    private Delivery delivery;
    private DefaultTableModel tableModel;
    private JTable taskTable;
    private JButton seeReviewButton;
    private JButton backButton;

    public SeeReviewForDeliveryGUI(Delivery delivery) {
        this.delivery = delivery;

        setTitle("See Reviews for Delivery");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize tableModel
        tableModel = new DefaultTableModel(new Object[]{"Task ID", "Order ID", "Status", "Assigned Date", "Completed Date"}, 0);
        taskTable = new JTable(tableModel);
        loadTaskDataIntoTable();

        JScrollPane scrollPane = new JScrollPane(taskTable);
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

        seeReviewButton = new JButton("See Review");
        seeReviewButton.addActionListener(e -> seeReview());
        buttonPanel.add(seeReviewButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadTaskDataIntoTable() {
        List<Task> tasks = FileHandler.loadTaskData();
        tableModel.setRowCount(0); // Clear existing rows

        for (Task task : tasks) {
            if (task.getDeliveryID().equals(delivery.getUserID()) && task.getStatus().equals("Completed")) {
                tableModel.addRow(new Object[]{
                    task.getTaskID(),
                    task.getOrderID(),
                    task.getStatus(),
                    task.getAssignedDate(),
                    task.getCompletedDate()
                });
            }
        }
    }

    private void seeReview() {
        int selectedRow = taskTable.getSelectedRow();
        if (selectedRow != -1) {
            int orderID = Integer.parseInt((String) tableModel.getValueAt(selectedRow, 1));
            List<Review> reviews = FileHandler.loadReviewData();

            for (Review review : reviews) {
                if (review.getOrderID() == orderID) {
                    JOptionPane.showMessageDialog(this, "Review: " + review.getReviewText() + "\nRating: " + review.getRating());
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "No Review for this order.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a task to see the review.");
        }
    }
}