public class Application {
    public static void main(String[] args) {
        Admin.ensureAdminExists(); // Ensure that the Admin account exists
        LoginGUI.main(args);
    }
}
