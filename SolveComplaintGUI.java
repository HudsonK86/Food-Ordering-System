import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class SolveComplaintGUI extends JFrame {
    private DefaultTableModel tableModel;
    private JTable complaintTable;
    private JButton viewButton;
    private JButton solveButton;
    private JButton backButton;
    private Manager manager;
    private List<Review> complaints;

    public SolveComplaintGUI(Manager manager) {
        this.manager = manager;
        this.complaints = FileHandler.loadReviewData().stream()
                .filter(review -> review.getRating() == 0)
                .collect(Collectors.toList());

        setTitle("Solve Complaints");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize tableModel
        tableModel = new DefaultTableModel(new Object[]{"Order ID", "Type", "Review Text", "Rating"}, 0);
        complaintTable = new JTable(tableModel);
        loadComplaintDataIntoTable();

        JScrollPane scrollPane = new JScrollPane(complaintTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        viewButton = new JButton("View");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewComplaint();
            }
        });
        buttonPanel.add(viewButton);

        solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveComplaint();
            }
        });
        buttonPanel.add(solveButton);

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

    private void loadComplaintDataIntoTable() {
        tableModel.setRowCount(0); // Clear existing rows
        for (Review complaint : complaints) {
            tableModel.addRow(new Object[]{complaint.getOrderID(), complaint.getType(), complaint.getReviewText(), complaint.getRating()});
        }
    }

    private void viewComplaint() {
        int selectedRow = complaintTable.getSelectedRow();
        if (selectedRow != -1) {
            String reviewText = (String) tableModel.getValueAt(selectedRow, 2);
            JOptionPane.showMessageDialog(this, "Complaint: " + reviewText);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a complaint to view.");
        }
    }

    private void solveComplaint() {
        int selectedRow = complaintTable.getSelectedRow();
        if (selectedRow != -1) {
            int orderID = (int) tableModel.getValueAt(selectedRow, 0);
            for (Review complaint : complaints) {
                if (complaint.getOrderID() == orderID) {
                    complaint.setRating(-1);
                    updateReviewFile(complaint);
                    complaints.remove(complaint);
                    loadComplaintDataIntoTable();
                    JOptionPane.showMessageDialog(this, "Complaint solved.");
                    return;
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a complaint to solve.");
        }
    }

    private void updateReviewFile(Review updatedReview) {
        List<Review> allReviews = FileHandler.loadReviewData();
        for (Review review : allReviews) {
            if (review.getOrderID() == updatedReview.getOrderID()) {
                review.setRating(-1);
                break;
            }
        }
        FileHandler.saveReviewData(allReviews);
    }
}