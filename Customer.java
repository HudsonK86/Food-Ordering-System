import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private static final long serialVersionUID = 1L; // Version control for serialization
    private double walletBalance;  // Wallet object to store the wallet balance
    private transient List<Order> orderHistory;  // List to keep track of orders placed by the customer

    // Constructor
    public Customer(String name, String userID, String password, double walletBalance) {
        super(name, userID, password, "Customer");
        this.walletBalance = walletBalance;  // Initialize the wallet object
        this.orderHistory = new ArrayList<>();  // Initialize the order history list
    }

    // Getter for wallet balance
    public double getWalletBalance() {
        return walletBalance;  // Return the current balance
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }

    // Method to top up wallet
    public void topUpWallet(double amount) {
        this.walletBalance += amount;  // Add to the balance
    }

    // Method to check if the balance is sufficient
    public boolean hasSufficientBalance(double amount) {
        return this.walletBalance >= amount;  // Check if balance is enough
    }

    // Method to deduct balance and return success status
    public boolean deductBalance(double amount) {
        if (hasSufficientBalance(amount)) {
            this.walletBalance -= amount;  // Deduct the amount from balance
            return true;
        } else {
            return false;
        }
    }

    // Method to add an order to the order history
    public void addOrder(Order order) {
        orderHistory.add(order);  // Add the order to the list
    }

    // Getter for order history
    public List<Order> getOrderHistory() {
        return orderHistory;  // Return the list of orders
    }

    // Custom deserialization logic
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        orderHistory = new ArrayList<>(); // Initialize orderHistory after deserialization
    }

    

}