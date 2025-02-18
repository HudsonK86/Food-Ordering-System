import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class ViewAndUpdateMenuGUI extends JFrame {
    private DefaultTableModel tableModel;
    private JTable menuTable;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton backButton;
    private List<MenuItem> menuItems;
    private Vendor vendor;

    public ViewAndUpdateMenuGUI(Vendor vendor) {
        this.vendor = vendor;
        this.menuItems = FileHandler.loadMenuData(); // Load all menu items

        setTitle("View and Update Menu");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

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
                new VendorMenuGUI(vendor); // Go back to Vendor Menu
            }
        });
        buttonPanel.add(backButton);

        addButton = new JButton("Add Item");
        addButton.addActionListener(e -> addItem());
        buttonPanel.add(addButton);

        updateButton = new JButton("Update Item");
        updateButton.addActionListener(e -> updateItem());
        buttonPanel.add(updateButton);

        deleteButton = new JButton("Delete Item");
        deleteButton.addActionListener(e -> deleteItem());
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadMenuDataIntoTable() {
        tableModel.setRowCount(0); // Clear existing rows
        List<MenuItem> vendorMenuItems = menuItems.stream()
                .filter(item -> item.getVendorID().equals(vendor.getUserID()))
                .collect(Collectors.toList());

        for (MenuItem item : vendorMenuItems) {
            tableModel.addRow(new Object[]{item.getItemID(), item.getName(), item.getPrice(), item.getCategory()});
        }
    }

    private void addItem() {
        // Logic to add a new menu item
        String name = JOptionPane.showInputDialog(this, "Enter item name:");
        String priceStr = JOptionPane.showInputDialog(this, "Enter item price:");
        String[] categories = {"Food", "Drink"};
        String category = (String) JOptionPane.showInputDialog(this, "Select item category:", "Category",
                JOptionPane.QUESTION_MESSAGE, null, categories, categories[0]);

        if (name != null && priceStr != null && category != null) {
            try {
                double price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    JOptionPane.showMessageDialog(this, "Price must be positive.");
                    return;
                }
                int itemID = Menu.generateUniqueItemID(menuItems); // Generate a unique item ID
                MenuItem newItem = new MenuItem(vendor.getUserID(), itemID, name, price, category);
                menuItems.add(newItem);
                FileHandler.saveMenuData(menuItems);
                loadMenuDataIntoTable();
                JOptionPane.showMessageDialog(this, "Item added successfully!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid price format.");
            }
        }
    }

    private void updateItem() {
        int selectedRow = menuTable.getSelectedRow();
        if (selectedRow != -1) {
            int itemID = (int) tableModel.getValueAt(selectedRow, 0);
            MenuItem item = Menu.findMenuItem(menuItems, itemID, vendor);

            if (item != null) {
                String name = JOptionPane.showInputDialog(this, "Enter new item name:", item.getName());
                String priceStr = JOptionPane.showInputDialog(this, "Enter new item price:", item.getPrice());
                String[] categories = {"Food", "Drink"};
                String category = (String) JOptionPane.showInputDialog(this, "Select new item category:", "Category",
                        JOptionPane.QUESTION_MESSAGE, null, categories, item.getCategory());

                if (name != null && priceStr != null && category != null) {
                    try {
                        double price = Double.parseDouble(priceStr);
                        if (price <= 0) {
                            JOptionPane.showMessageDialog(this, "Price must be positive.");
                            return;
                        }
                        item.setName(name);
                        item.setPrice(price);
                        item.setCategory(category);
                        FileHandler.saveMenuData(menuItems);
                        loadMenuDataIntoTable();
                        JOptionPane.showMessageDialog(this, "Item updated successfully!");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Invalid price format.");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to update.");
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
