import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Transaction {
    private String transactionID;
    private String userID;
    private String type; // "Order" or "TopUp" or "Refund"
    private int orderID;
    private double amount;
    private Date date;

    public Transaction(String transactionID, String userID, String type, int orderID, double amount, Date date) {
        this.transactionID = transactionID;
        this.userID = userID;
        this.type = type;
        this.orderID = orderID;
        this.amount = amount;
        this.date = date;
    }

    // Overloaded constructor for transactions without an orderID
    public Transaction(String transactionID, String userID, String type, double amount, Date date) {
        this(transactionID, userID, type, 0, amount, date); // Use 0 as the default orderID
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return transactionID + "," + userID + "," + type + "," + orderID + "," + amount + "," + date;
    }

    // Method to generate a unique 6-digit transactionID
    public static String generateUniqueTransactionID() {
        Set<String> existingTransactionIDs = new HashSet<>();
        List<Transaction> existingTransactions = FileHandler.loadTransactionData();
        for (Transaction transaction : existingTransactions) {
            existingTransactionIDs.add(transaction.getTransactionID());
        }

        Random random = new Random();
        String transactionID;
        do {
            transactionID = String.valueOf(100000 + random.nextInt(900000)); // Generate a 6-digit number
        } while (existingTransactionIDs.contains(transactionID));
        return transactionID;
    }
}