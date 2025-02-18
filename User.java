import java.io.Serializable;
import java.util.List;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L; // Version control for serialization
    private String name;
    private String userID;
    private String password;
    private String role; // Vendor, Customer, Delivery, Administrator, Manager
    private static User loggedInUser = null; // Track currently logged-in user

    // Constructor
    public User(String name, String userID, String password, String role) {
        this.name = name;
        this.userID = userID;
        this.password = password;
        this.role = role;
    }

    // Getter and Setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    // Login method
    public static User login(String userID, String password) {
        // Load existing users from the file
        List<User> users = FileHandler.loadUserData();
    
        // Validate credentials by checking if the userID and password match any user
        for (User u : users) {
            if (u.getUserID().equals(userID) && u.getPassword().equals(password)) {
                loggedInUser = u; // Set the logged-in user
                return u; // Return the user object
            }
        }
        return null; // Return null if no match found
    }
      
    // Logout method
    public static void logout() {
        if (loggedInUser != null) {
            loggedInUser = null;
        }
    }
    
}
