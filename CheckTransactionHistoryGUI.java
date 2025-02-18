import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CheckTransactionHistoryGUI extends JFrame {
    private Customer customer;
    private DefaultTableModel tableModel;
    private JTable transactionTable;

    public CheckTransactionHistoryGUI(Customer customer) {
        this.customer = customer;

        setTitle("Check Transaction History");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Initialize tableModel
        tableModel = new DefaultTableModel(new Object[]{"Transaction ID", "Type", "Order ID", "Amount", "Date"}, 0);
        transactionTable = new JTable(tableModel);
        loadTransactionDataIntoTable();

        JScrollPane scrollPane = new JScrollPane(transactionTable);
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

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadTransactionDataIntoTable() {
        List<Transaction> transactions = FileHandler.loadTransactionData();
        tableModel.setRowCount(0); // Clear existing rows

        for (Transaction transaction : transactions) {
            if (transaction.getUserID().equals(customer.getUserID())) {
                String amount = transaction.getAmount() + "";
                if (transaction.getType().equals("Top-Up") || transaction.getType().equals("Refund")) {
                    amount = "+" + amount;
                } else if (transaction.getType().equals("Order")) {
                    amount = "-" + amount;
                }
                tableModel.addRow(new Object[]{
                    transaction.getTransactionID(),
                    transaction.getType(),
                    transaction.getOrderID(),
                    amount,
                    transaction.getDate()
                });
            }
        }
    }
}