public class Delivery extends User {
    private static final long serialVersionUID = 1L; // Version control for serialization
    private String status; // Status of the delivery person (e.g., available, busy)

    // Constructor for Delivery
    public Delivery(String name, String userID, String password, double earnings, String status) {
        super(name, userID, password, "Delivery"); // Calls the parent class (User) constructor
        this.status = status;
    }

    // Getter for status
    public String getStatus() {
        return status;
    }

    // Setter for status
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return getName(); // or any other meaningful representation
    }
}
