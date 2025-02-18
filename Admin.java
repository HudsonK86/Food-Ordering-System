import java.util.List;

public class Admin extends User {
    private static final long serialVersionUID = 1L; // Version control for serialization

    // Constructor for Admin
    public Admin(String name, String userID, String password) {
        super(name, userID, password, "Admin"); // Calls the parent class (User) constructor
    }

    // Ensure that the Admin account exists (load data, check, add if necessary, save data)
    public static void ensureAdminExists() {
        // Load existing users from the file
        List<User> users = FileHandler.loadUserData();

        // Check if Admin already exists in the list
        User admin = new Admin("Admin", "Admin", "Admin"); // Hardcoded Admin credentials
        if (findUserByID(users, admin.getUserID()) == null) {
            // Admin does not exist, create and add it to the list
            users.add(admin);
            // Save the updated list back to the file
            FileHandler.saveUserData(users);
        }
    }

    // Find a user by User ID
    public static User findUserByID(List<User> users, String userID) {
        for (User user : users) {
            if (user.getUserID().equals(userID)) {
                return user;  // Return the user if found
            }
        }
        return null;
    }  

    // Register a new user
    public static String registerUser(String name, String userID, String password, String role) {
        // Load existing users from the file
        List<User> users = FileHandler.loadUserData();

        // Check if User ID is unique
        if (findUserByID(users, userID) != null) {
            return "User ID already exists. Please choose another.";
        }

        // Create the new user based on role
        User newUser = null;
        if (role.equals("Customer")) {
            newUser = new Customer(name, userID, password, 0.00);
        } else if (role.equals("Vendor")) {
            newUser = new Vendor(name, userID, password, 0.00);
        } else if (role.equals("Delivery")) {
            newUser = new Delivery(name, userID, password, 0.00, "Available");
        } else if (role.equals("Manager")) {
            newUser = new Manager(name, userID, password);
        }

        // Add the new user to the list and save to file
        if (newUser != null) {
            users.add(newUser);
            FileHandler.saveUserData(users);
            return "User registered successfully!";
        } else {
            return "Failed to register user.";
        }
    }

    // Change password for a user
    public static String changePassword(String userID, String newPassword) {
        // Load existing users from the file
        List<User> users = FileHandler.loadUserData();

        // Find the user by User ID
        User user = findUserByID(users, userID);
        if (user == null) {
            return "User ID not found.";
        }

        // Update the user's password
        user.setPassword(newPassword);

        // Save the updated list back to the file
        FileHandler.saveUserData(users);
        return "Password changed successfully!";
    }

    // Delete User
    public static String deleteUser(String userID) {
        // Load existing users from the file
        List<User> users = FileHandler.loadUserData();
    
        // Find the user by User ID
        User user = findUserByID(users, userID);
        if (user == null) {
            return "User ID not found.";
        }
    
        // Remove the user from the list
        users.remove(user);
    
        // Save the updated list back to the file
        FileHandler.saveUserData(users);
        return "User deleted successfully!";
    }


}