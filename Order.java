import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Order implements DeliverySystem {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private int orderID;
    private List<OrderItem> items;
    private String orderType;
    private double totalPrice;
    private String status;
    private Date orderDate;
    private String customerID;
    private String vendorID;

    // Constructor
    public Order(String customerID, String vendorID, int orderID, List<OrderItem> items, String orderType, double totalPrice, String status, Date orderDate) {
        this.customerID = customerID;
        this.vendorID = vendorID;
        this.orderID = orderID;
        this.items = items;
        this.orderType = orderType;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderDate = orderDate;
    }

    // Getters and Setters
    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getVendorID() {
        return vendorID;
    }

    public void setVendorID(String vendorID) {
        this.vendorID = vendorID;
    }
        
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(customerID).append("|")
          .append(vendorID).append("|")
          .append(orderID).append("|")
          .append(orderType).append("|")
          .append(totalPrice).append("|")
          .append(status).append("|")
          .append(dateFormat.format(orderDate)).append("|");

        for (OrderItem item : items) {
            sb.append(item.toString()).append(";");
        }

        // Remove the last semicolon
        if (!items.isEmpty()) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }

    public static Order fromString(String orderString) throws ParseException {
        String[] parts = orderString.split("\\|");
        String customerID = parts[0];
        String vendorID = parts[1];
        int orderID = Integer.parseInt(parts[2]);
        String orderType = parts[3];
        double totalPrice = Double.parseDouble(parts[4]);
        String status = parts[5];
        Date orderDate = dateFormat.parse(parts[6]);

        List<OrderItem> items = new ArrayList<>();
        if (parts.length > 7 && !parts[7].isEmpty()) {
            String[] itemStrings = parts[7].split(";");
            for (String itemString : itemStrings) {
                items.add(OrderItem.fromString(itemString));
            }
        }

        return new Order(customerID, vendorID, orderID, items, orderType, totalPrice, status, orderDate);
    }

    // Method to generate a unique 6-digit orderID
    public static int generateUniqueOrderID(List<Order> existingOrders) {
        Set<Integer> existingOrderIDs = new HashSet<>();
        for (Order order : existingOrders) {
            existingOrderIDs.add(order.getOrderID());
        }

        Random random = new Random();
        int orderID;
        do {
            orderID = 100000 + random.nextInt(900000); // Generate a 6-digit number
        } while (existingOrderIDs.contains(orderID));
        return orderID;
    }

    // Method to random assign task for a specific available delivery person
    @Override
    public void assignDeliveryToOrder(Order order) {
        List<User> users = FileHandler.loadUserData();
        List<Task> tasks = FileHandler.loadTaskData();

        List<Delivery> deliveries = new ArrayList<>();
    
        for (User user : users) {
            if (user instanceof Delivery) {
                deliveries.add((Delivery) user);
            }
        }

        for (Delivery delivery : deliveries) {
            if ("Available".equalsIgnoreCase(delivery.getStatus())) {
                boolean hasPendingTask = tasks.stream()
                    .anyMatch(task -> task.getDeliveryID().equals(delivery.getUserID()) && "Pending".equalsIgnoreCase(task.getStatus()));
                boolean hasDeclinedTask = tasks.stream()
                    .anyMatch(task -> task.getDeliveryID().equals(delivery.getUserID()) && task.getOrderID().equals(String.valueOf(order.getOrderID())) && "Declined".equalsIgnoreCase(task.getStatus()));

                if (!hasPendingTask && !hasDeclinedTask) {
                    Task task = new Task(Task.generateUniqueTaskID(), String.valueOf(order.getOrderID()), delivery.getUserID(), "Pending", new Date(), null);
                    tasks.add(task);
                    FileHandler.saveTaskData(tasks);
                    return; // Task assigned, exit the method
                }
            }
        }

        // If no available delivery found, update order status
        order.setStatus("No Delivery Available");
        List<Order> orders = FileHandler.loadOrderData();
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderID() == order.getOrderID()) {
                orders.set(i, order);
                break;
            }
        }
        FileHandler.saveOrderData(orders);

        Customer customer = (Customer) FileHandler.loadUserData().stream()
                .filter(u -> u.getUserID().equals(order.getCustomerID()))
                .findFirst()
                .orElse(null);
        
        if (customer != null) {
            customer.topUpWallet(order.getTotalPrice());
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUserID().equals(customer.getUserID())) {
                    users.set(i, customer);
                    break;
                }
            }
            FileHandler.saveUserData(users);

            // Create and save transaction
            String transactionID = Transaction.generateUniqueTransactionID();
            Transaction transaction = new Transaction(transactionID, customer.getUserID(), "Refund", order.getOrderID(), order.getTotalPrice(), new Date());
            List<Transaction> transactions = FileHandler.loadTransactionData();
            transactions.add(transaction);
            FileHandler.saveTransactionData(transactions);
        }
    }
}
