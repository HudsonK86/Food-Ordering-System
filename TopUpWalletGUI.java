import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Date;

public class TopUpWalletGUI {

    public TopUpWalletGUI(Admin admin) {
        // Create a JFrame for the Top-Up Wallet GUI
        JFrame frame = new JFrame("Top Up Wallet");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLocationRelativeTo(null); // Center the window

        // Create a panel for the layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));

        // Create components
        JLabel userIDLabel = new JLabel("Customer User ID:");
        JTextField userIDField = new JTextField();
        JLabel topUpAmountLabel = new JLabel("Top-Up Amount:");
        JTextField topUpAmountField = new JTextField();
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");

        // Add components to the panel
        panel.add(userIDLabel);
        panel.add(userIDField);
        panel.add(topUpAmountLabel);
        panel.add(topUpAmountField);
        panel.add(submitButton);
        panel.add(cancelButton);

        // Add the panel to the frame
        frame.add(panel);
        frame.setVisible(true);

        // Submit button action listener
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = userIDField.getText();
                String topUpAmountText = topUpAmountField.getText();

                if (userID.isEmpty() || topUpAmountText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double topUpAmount = Double.parseDouble(topUpAmountText);

                    if (topUpAmount <= 0) {
                        JOptionPane.showMessageDialog(frame, "Top-up amount must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Load users from the file
                    List<User> users = FileHandler.loadUserData();
                    Customer customer = null;

                    for (User user : users) {
                        if (user instanceof Customer && user.getUserID().equals(userID)) {
                            customer = (Customer) user;
                            break;
                        }
                    }

                    if (customer == null) {
                        JOptionPane.showMessageDialog(frame, "Customer not found!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Top up the wallet
                    customer.topUpWallet(topUpAmount);
                    FileHandler.saveUserData(users);

                    // Create and save transaction
                    String transactionID = Transaction.generateUniqueTransactionID();
                    Transaction transaction = new Transaction(transactionID, customer.getUserID(), "Top-Up", topUpAmount, new Date());
                    List<Transaction> transactions = FileHandler.loadTransactionData();
                    transactions.add(transaction);
                    FileHandler.saveTransactionData(transactions);

                    JOptionPane.showMessageDialog(frame, "Wallet topped up successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Close the window
                    frame.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid top-up amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "An error occurred. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Cancel button action listener
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }
}
