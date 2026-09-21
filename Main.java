import java.util.Scanner;

class User {
    private String username;
    private String password;
    private String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
    }
}

class MailServer {
    private Mail[] queue;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public MailServer() {
        this.capacity = 10;
        this.queue = new Mail[this.capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    public void addMail(Mail mail) {
        if (rear == capacity - 1) {
            System.out.println("Mail queue is full. Cannot add new mail.");
            return;
        }
        rear++;
        queue[rear] = mail;
        size++;
        System.out.println("Mail added to queue successfully: " + mail.getMailId() + " is PENDING.");
    }

    public void viewPendingMails() {
        if (size == 0) {
            System.out.println("No pending mails in the queue.");
            return;
        }
        System.out.println("\n--- PENDING MAILS ---");
        for (int i = front; i <= rear; i++) {
            Mail m = queue[i];
            System.out.println(m.getMailId() + " - " + m.getStatus() + " (To: " + m.getRecipient() + ", Subject: " + m.getSubject() + ")");
        }
    }

    public void processNextMail() {
        if (size == 0) {
            System.out.println("No pending mails to process.");
            return;
        }
        Mail processedMail = queue[front];
        processedMail.setStatus("SENT");
        System.out.println("Processed " + processedMail.getMailId() + ". Status is now SENT.");
        
        front++;
        size--;
        
        if (size == 0) {
            // Reset pointers when queue is empty to avoid hitting capacity unnecessarily
            front = 0;
            rear = -1;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // Predefined users (no arrays or collections)
        User user1 = new User("user1", "user123", "USER");
        User user2 = new User("user2", "user123", "USER");
        User admin = new User("admin", "admin123", "ADMIN");

        User currentUser = null;
        MailServer mailServer = new MailServer();
        int mailCounter = 1;
        Scanner scanner = new Scanner(System.in);

        System.out.println("MAIL QUEUE SYSTEM ");

        while (true) {
            if (currentUser == null) {
                System.out.println("\n--- LOGIN ---");
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();

                if (username.equals(user1.getUsername()) && password.equals(user1.getPassword())) {
                    currentUser = user1;
                    System.out.println("Login successful.");
                } else if (username.equals(user2.getUsername()) && password.equals(user2.getPassword())) {
                    currentUser = user2;
                    System.out.println("Login successful.");
                } else if (username.equals(admin.getUsername()) && password.equals(admin.getPassword())) {
                    currentUser = admin;
                    System.out.println("Login successful as Admin.");
                } else {
                    System.out.println("Invalid username or password.");
                }
            } else {
                if (currentUser.isAdmin()) {
                    System.out.println("\n--- ADMIN MENU ---");
                    System.out.println("1. VIEW PENDING MAILS");
                    System.out.println("2. PROCESS NEXT MAIL");
                    System.out.println("3. SEARCH MAIL");
                    System.out.println("4. LOGOUT");
                    System.out.println("5. EXIT");
                    System.out.print("Choice: ");

                    String choice = scanner.nextLine();
                    if (choice.equals("1")) {
                        mailServer.viewPendingMails();
                    } else if (choice.equals("2")) {
                        mailServer.processNextMail();
                    } else if (choice.equals("3")) {
                        // Dummy search option, do nothing
                    } else if (choice.equals("4")) {
                        currentUser = null;
                        System.out.println("Logged out successfully.");
                    } else if (choice.equals("5")) {
                        System.out.println("Exiting system...");
                        System.exit(0);
                    } else {
                        System.out.println("Invalid choice.");
                    }
                } else {
                    System.out.println("\n--- USER MENU ---");
                    System.out.println("1. CREATE MAIL");
                    System.out.println("2. VIEW PENDING MAILS");
                    System.out.println("3. SEARCH MAIL");
                    System.out.println("4. LOGOUT");
                    System.out.println("5. EXIT");
                    System.out.print("Choice: ");

                    String choice = scanner.nextLine();
                    if (choice.equals("1")) {
                        System.out.print("Recipient: ");
                        String recipient = scanner.nextLine();
                        System.out.print("Subject: ");
                        String subject = scanner.nextLine();

                        String mailId = "M" + String.format("%03d", mailCounter++);
                        Mail mail = new Mail(mailId, currentUser.getUsername(), recipient, subject);
                        mailServer.addMail(mail);
                    } else if (choice.equals("2")) {
                        mailServer.viewPendingMails();
                    } else if (choice.equals("3")) {
                        // Dummy search option, do nothing
                    } else if (choice.equals("4")) {
                        currentUser = null;
                        System.out.println("Logged out successfully.");
                    } else if (choice.equals("5")) {
                        System.out.println("Exiting system...");
                        System.exit(0);
                    } else {
                        System.out.println("Invalid choice.");
                    }
                }
            }
        }
    }
}
