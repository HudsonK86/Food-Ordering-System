public class OrderItem {
    private MenuItem menuItem;
    private int quantity;
    private double subTotal;

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.subTotal = menuItem.getPrice() * quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.subTotal = menuItem.getPrice() * quantity;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public String toString() {
        return menuItem.toString() + "," + quantity + "," + subTotal;
    }

    public static OrderItem fromString(String itemString) {
        String[] parts = itemString.split(",");
        MenuItem menuItem = MenuItem.fromString(String.join(",", parts[0], parts[1], parts[2], parts[3], parts[4]));
        int quantity = Integer.parseInt(parts[5]);
        double subTotal = Double.parseDouble(parts[6]);

        return new OrderItem(menuItem, quantity);
    }

}
