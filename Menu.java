import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Menu {
    private List<MenuItem> menuItems;

    public Menu() {
        this.menuItems = new ArrayList<>();
    }

    public void addMenuItem(MenuItem item) {
        menuItems.add(item);
    }

    public void removeMenuItem(MenuItem item) {
        menuItems.remove(item);
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public String toString() {
        return menuItems.toString();
    }
    
    
    // Method to generate a unique Item ID
    public static int generateUniqueItemID(List<MenuItem> menuItems) {
        Random random = new Random();
        int newItemID;

        // Generate a unique ID between 100 and 999
        while (true) {
            newItemID = 100 + random.nextInt(900); // Generate a number between 100 and 999

            // Check if ID is unique
            boolean isUnique = true;
            for (MenuItem item : menuItems) {
                if (item.getItemID() == newItemID) {
                    isUnique = false;
                    break;
                }
            }

            if (isUnique) {
                break; // Exit the loop if the ID is unique
            }
        }

        return newItemID;
    }

    // Find a menu item for Manager
    public static MenuItem findMenuItem(List<MenuItem> menuItems, int itemID) {
        return menuItems.stream()
                .filter(item -> item.getItemID() == itemID)
                .findFirst()
                .orElse(null);
    }

    // Find a menu item for Vendor
    public static MenuItem findMenuItem(List<MenuItem> menuItems, int itemID, Vendor vendor) {
        return menuItems.stream()
                .filter(item -> item.getItemID() == itemID && item.getVendorID().equals(vendor.getUserID()))
                .findFirst()
                .orElse(null);
    }

}
