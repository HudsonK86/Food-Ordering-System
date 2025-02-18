import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FileHandler {

    private static final String USER_FILE = "user.ser"; // File to store serialized user data
    private static final String MENU_FILE = "menu.txt"; // File to store menu data
    private static final String ORDER_FILE = "order.txt"; // File to store serialized order data
    private static final String TASK_FILE = "task.txt"; // File to store serialized task data
    private static final String TRANSACTION_FILE = "transaction.txt";
    private static final String REVIEW_FILE = "review.txt"; // File to store review data

    // Method to load user data from file
    @SuppressWarnings("unchecked")
    public static List<User> loadUserData() {
        List<User> users = new ArrayList<>();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(USER_FILE))) {
            // Deserialize and cast directly to List<User>
            users = (List<User>) objectInputStream.readObject();  // Automatically reconstructs correct subclass instances
        } catch (EOFException e) {
        } catch (IOException | ClassNotFoundException e) {
        }
        
        return users;
    }

    // Method to load all vendors from the file
    public static List<Vendor> loadVendors() {
        List<Vendor> vendors = new ArrayList<>();
        
        List<User> users = loadUserData();
        for (User user : users) {
            if (user instanceof Vendor) {
                vendors.add((Vendor) user);
            }
        }
        return vendors;
    }

    // Method to load delivery persons from the file
    public static List<Delivery> loadDeliveries() {
        List<Delivery> deliveries = new ArrayList<>();
        
        List<User> users = loadUserData();
        for (User user : users) {
            if (user instanceof Delivery) {
                deliveries.add((Delivery) user);
            }
        }
        return deliveries;
    }

    // Save users to file
    public static void saveUserData(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load all menu items from the file
    public static List<MenuItem> loadMenuData() {
        List<MenuItem> menuItems = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(MENU_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String vendorID = parts[0];
                    int itemID = Integer.parseInt(parts[1]);
                    String category = parts[2];
                    String name = parts[3];
                    double price = Double.parseDouble(parts[4]);
                    menuItems.add(new MenuItem(vendorID, itemID, name, price, category));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading menu file: " + e.getMessage());
        }
        return menuItems;
    }

    // Method to load menu items for a specific vendor
    public static List<MenuItem> loadMenuData(Vendor vendor) {
        List<MenuItem> menuItems = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(MENU_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5 && parts[0].equals(vendor.getUserID())) {
                    String vendorID = parts[0];
                    int itemID = Integer.parseInt(parts[1]);
                    String category = parts[2];
                    String name = parts[3];
                    double price = Double.parseDouble(parts[4]);
                    menuItems.add(new MenuItem(vendorID, itemID, name, price, category));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading menu file: " + e.getMessage());
        }
        return menuItems;
    }

    // Method to save all menu items to the file
    public static void saveMenuData(List<MenuItem> menuItems) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MENU_FILE))) {
            for (MenuItem item : menuItems) {
                writer.write(item.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to menu file: " + e.getMessage());
        }
    }

    // Method to load all orders from the file
    public static void saveOrderData(List<Order> orders) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_FILE))) {
            for (Order order : orders) {
                writer.write(order.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load all orders from the file
    public static List<Order> loadOrderData() {
        List<Order> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                orders.add(Order.fromString(line));
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Method to load orders for a specific customer
    public static List<Order> loadOrderData(Customer customer) {
        List<Order> customerOrders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Order order = Order.fromString(line);
                if (order.getCustomerID().equals(customer.getUserID())) {
                    customerOrders.add(order);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return customerOrders;
    }

    // Method to load order by orderID
    public static Order loadOrderData(int orderID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Order order = Order.fromString(line);
                if (order.getOrderID() == orderID) {
                    return order;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to save task data to file
    public static void saveTaskData(List<Task> tasks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TASK_FILE))) {
            for (Task task : tasks) {
                writer.write(task.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load task data from file
    public static List<Task> loadTaskData() {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TASK_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int taskID = Integer.parseInt(parts[0]);
                String orderID = parts[1];
                String deliveryID = parts[2];
                String status = parts[3];
                Date assignedDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(parts[4]);
                Date completedDate = parts[5].equals("null") ? null : new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(parts[4]);
                Task task = new Task(taskID, orderID, deliveryID, status,  assignedDate, completedDate);
                tasks.add(task);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // Method to save transactions to a file
    public static void saveTransactionData(List<Transaction> transactions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTION_FILE))) {
            for (Transaction transaction : transactions) {
                writer.write(transaction.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load transactions from a file
    public static List<Transaction> loadTransactionData() {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String transactionID = parts[0];
                    String userID = parts[1];
                    String type = parts[2];
                    int orderID = Integer.parseInt(parts[3]);
                    double amount = Double.parseDouble(parts[4]);
                    Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(parts[5]);
                    transactions.add(new Transaction(transactionID, userID, type, orderID, amount, date));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // Method to save reviews to file
    public static void saveReviewData(List<Review> reviews) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(REVIEW_FILE))) {
            for (Review review : reviews) {
                writer.write(review.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to review file: " + e.getMessage());
        }
    }

    // Method to load reviews from file
    public static List<Review> loadReviewData() {
        List<Review> reviews = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(REVIEW_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                reviews.add(Review.fromString(line));
            }
        } catch (IOException e) {
            System.err.println("Error reading review file: " + e.getMessage());
        }
        return reviews;
    }

}
