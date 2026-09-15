import java.util.HashMap;

public class AuthService {
    private HashMap<String, User> users;

    public AuthService() {
        users = new HashMap<>();
        // Predefined accounts
        users.put("user1", new User("user1", "user123", "USER"));
        users.put("user2", new User("user2", "user123", "USER"));
        users.put("admin", new User("admin", "admin123", "ADMIN"));
    }

    public User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
