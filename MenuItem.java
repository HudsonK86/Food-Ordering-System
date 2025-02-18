public class MenuItem {
    private String vendorID;
    private int itemID;
    private String name;
    private double price;
    private String category; // Either "Food" or "Drink"

    // Constructor
    public MenuItem(String vendorID, int itemID, String name, double price, String category) {
        this.vendorID = vendorID;
        this.itemID = itemID;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // Getters and Setters
    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }
    
    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return vendorID + "," + itemID + "," + category + "," + name + "," + price;
    }

    public static MenuItem fromString(String itemString) {
        String[] parts = itemString.split(",");
        String vendorID = parts[0];
        int itemID = Integer.parseInt(parts[1]);
        String category = parts[2];
        String name = parts[3];
        double price = Double.parseDouble(parts[4]);

        return new MenuItem(vendorID, itemID, name, price, category);
    }
}
