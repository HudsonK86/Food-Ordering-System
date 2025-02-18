import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteInappropriateItemGUI extends JFrame {
    private DefaultTableModel tableModel;
    private JTable menuTable;
    private JButton deleteButton;
    private JButton backButton;
    private JComboBox<Vendor> vendorComboBox;
    private List<MenuItem> menuItems;
    private Manager manager;

    public DeleteInappropriateItemGUI(Manager manager) {
        this.manager = manager;
        this.menuItems = FileHandler.loadMenuData(); // Load all menu items

        setTitle("Delete Inappropriate Item");
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
                loadMenuDataIntoTable();
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Vendor:"));
        topPanel.add(vendorComboBox);
        add(topPanel, BorderLayout.NORTH);

        // Initialize tableModel
        tableModel = new DefaultTableModel(new Object[]{"Item ID", "Name", "Price", "Category"}, 0);
        menuTable = new JTable(tableModel);
        loadMenuDataIntoTable();

        JScrollPane scrollPane = new JScrollPane(menuTable);
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

        deleteButton = new JButton("Delete Item");
        deleteButton.addActionListener(e -> deleteItem());
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadMenuDataIntoTable() {
        tableModel.setRowCount(0); // Clear existing rows
        Vendor selectedVendor = (Vendor) vendorComboBox.getSelectedItem();
        if (selectedVendor != null) {
            List<MenuItem> vendorMenuItems = menuItems.stream()
                    .filter(item -> item.getVendorID().equals(selectedVendor.getUserID()))
                    .collect(Collectors.toList());

            for (MenuItem item : vendorMenuItems) {
                tableModel.addRow(new Object[]{item.getItemID(), item.getName(), item.getPrice(), item.getCategory()});
            }
        }
    }

    private void deleteItem() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow != -1) {
            int itemID = (int) tableModel.getValueAt(selectedRow, 0);
            menuItems = menuItems.stream()
                    .filter(menuItem -> menuItem.getItemID() != itemID)
                    .collect(Collectors.toList());
            FileHandler.saveMenuData(menuItems);
            loadMenuDataIntoTable();
            JOptionPane.showMessageDialog(this, "Item deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.");
        }
    }
}