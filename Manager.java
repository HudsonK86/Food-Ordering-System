public class Manager extends User {
    private static final long serialVersionUID = 1L; // Version control for serialization

    // Constructor for Manager
    public Manager(String name, String userID, String password) {
        super(name, userID, password, "Manager"); // Calls the parent class (User) constructor
    }

}