public class Vendor extends User {
    private static final long serialVersionUID = 1L; // Version control for serialization
    private transient Menu menu; // Marked as transient to exclude from serialization

    public Vendor(String name, String userID, String password, double revenue) {
        super(name, userID, password, "Vendor"); // Calls the parent class (User) constructor
        this.menu = new Menu();
    }
    
    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return getName(); // Assuming getName() is a method in the User class that returns the vendor's name
    }
}
