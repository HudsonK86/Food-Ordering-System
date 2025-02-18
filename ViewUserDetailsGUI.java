import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ViewUserDetailsGUI {
    private JFrame frame;
    private JTable userTable;
    private JComboBox<String> roleComboBox;
    private JTextField userIdTextField;
    private DefaultTableModel tableModel;

    public ViewUserDetailsGUI(Admin admin) {
        frame = new JFrame("View User Details");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Initialize components
        tableModel = new DefaultTableModel(new Object[]{"User ID", "Name", "Password", "Role", "Balance"}, 0);
        userTable = new JTable(tableModel);
        roleComboBox = new JComboBox<>(new String[]{"All", "Admin", "Vendor", "Customer", "Delivery", "Manager"});
        userIdTextField = new JTextField(15);

        // Load user data into the table
        loadUserData();

        // Add components to the frame
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Role:"));
        topPanel.add(roleComboBox);
        topPanel.add(new JLabel("User ID:"));
        topPanel.add(userIdTextField);
        JButton searchButton = new JButton("Search");
        topPanel.add(searchButton);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(userTable), BorderLayout.CENTER);

        // Add Back button
        JButton backButton = new JButton("Back");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Add action listeners
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement search functionality
                searchUserData();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the current window
                new AdminMenuGUI(admin); // Open the Admin Menu
            }
        });

        frame.setVisible(true);
    }

    private void loadUserData() {
        // Load user data from the file and populate the table
        List<User> users = FileHandler.loadUserData();
        for (User user : users) {
            String balance = getBalance(user);
            tableModel.addRow(new Object[]{user.getUserID(), user.getName(), user.getPassword(), user.getRole(), balance});
        }
    }

    private void searchUserData() {
        // Implement search functionality based on role and user ID
        String selectedRole = (String) roleComboBox.getSelectedItem();
        String userId = userIdTextField.getText().trim();

        List<User> users = FileHandler.loadUserData();
        tableModel.setRowCount(0); // Clear existing rows

        for (User user : users) {
            if ((selectedRole.equals("All") || user.getRole().equals(selectedRole)) &&
                (userId.isEmpty() || user.getUserID().equals(userId))) {
                String balance = getBalance(user);
                tableModel.addRow(new Object[]{user.getUserID(), user.getName(), user.getPassword(), user.getRole(), balance});
            }
        }
    }

    private String getBalance(User user) {
        if (user instanceof Customer) {
            return String.valueOf(((Customer) user).getWalletBalance());
        } else {
            return "N/A";
        }
    }
}
