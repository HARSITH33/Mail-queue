import java.util.HashMap;
import java.util.Scanner;

public class Main {
    private static HashMap<String, User> users = new HashMap<>();
    private static User currentUser = null;
    private static MailQueueManager queueManager = new MailQueueManager();
    private static int mailCounter = 1;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Predefined users
        users.put("user1", new User("user1", "user123", "USER"));
        users.put("user2", new User("user2", "user123", "USER"));
        users.put("admin", new User("admin", "admin123", "ADMIN"));

        System.out.println("MAIL QUEUE SYSTEM");

        while (true) {
            if (currentUser == null) {
                login();
            } else {
                if (currentUser.isAdmin()) {
                    adminMenu();
                } else {
                    userMenu();
                }
            }
        }
    }

    private static void login() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login successful.");
            currentUser = user;
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void userMenu() {
        System.out.println("\n--- USER MENU ---");
        System.out.println("1. MAIL");
        System.out.println("2. PENDING");
        System.out.println("3. SEARCH");
        System.out.println("4. REMOVE");
        System.out.println("5. SENT");
        System.out.println("6. LOGOUT");
        System.out.println("7. EXIT");
        System.out.print("Choice: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                createMail();
                break;
            case "2":
                displayPendingForUser();
                break;
            case "3":
                searchUserMail();
                break;
            case "4":
                removeUserMail();
                break;
            case "5":
                displaySentForUser();
                break;
            case "6":
                currentUser = null;
                System.out.println("Logged out successfully.");
                break;
            case "7":
                System.out.println("Exiting system...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void adminMenu() {
        System.out.println("\n--- ADMIN MENU ---");
        System.out.println("1. PENDING");
        System.out.println("2. SEARCH");
        System.out.println("3. REMOVE");
        System.out.println("4. NEXT");
        System.out.println("5. SENT");
        System.out.println("6. LOGOUT");
        System.out.println("7. EXIT");
        System.out.print("Choice: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1":
                displayAllPending();
                break;
            case "2":
                searchAnyMail();
                break;
            case "3":
                removeAnyMail();
                break;
            case "4":
                processNextMail();
                break;
            case "5":
                displayAllSent();
                break;
            case "6":
                currentUser = null;
                System.out.println("Logged out successfully.");
                break;
            case "7":
                System.out.println("Exiting system...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void createMail() {
        System.out.print("Recipient: ");
        String recipient = scanner.nextLine();
        System.out.print("Subject: ");
        String subject = scanner.nextLine();

        String mailId = String.format("M%03d", mailCounter++);
        Mail mail = new Mail(mailId, currentUser.getUsername(), recipient, subject);
        
        queueManager.addMail(mail);
        System.out.println("Mail created successfully: " + mailId + " is PENDING.");
    }

    private static void displayPendingForUser() {
        boolean found = false;
        System.out.println("\nYour Pending Mails:");
        for (Mail mail : queueManager.getPendingMails()) {
            if (mail.getSender().equals(currentUser.getUsername())) {
                System.out.println(mail.getMailId() + " -> To: " + mail.getRecipient() + " | Subject: " + mail.getSubject());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No pending mails.");
        }
    }

    private static void displaySentForUser() {
        boolean found = false;
        System.out.println("\nYour Sent Mails:");
        for (Mail mail : queueManager.getMailMap().values()) {
            if (mail.getSender().equals(currentUser.getUsername()) && "SENT".equals(mail.getStatus())) {
                System.out.println(mail.getMailId() + " -> To: " + mail.getRecipient() + " | Subject: " + mail.getSubject());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No sent mails.");
        }
    }

    private static void searchUserMail() {
        System.out.print("Enter Mail ID: ");
        String mailId = scanner.nextLine();
        
        Mail mail = queueManager.searchMail(mailId);
        if (mail == null) {
            System.out.println("Mail not found.");
        } else if (!mail.getSender().equals(currentUser.getUsername())) {
            System.out.println("Unauthorized: This is not your mail.");
        } else {
            System.out.println("Mail ID: " + mail.getMailId());
            System.out.println("Recipient: " + mail.getRecipient());
            System.out.println("Subject: " + mail.getSubject());
            System.out.println("Status: " + mail.getStatus());
        }
    }

    private static void removeUserMail() {
        System.out.print("Enter Mail ID to remove: ");
        String mailId = scanner.nextLine();

        Mail mail = queueManager.searchMail(mailId);
        if (mail == null) {
            System.out.println("Mail not found.");
        } else if (!mail.getSender().equals(currentUser.getUsername())) {
            System.out.println("Unauthorized: You can only remove your own mails.");
        } else if ("SENT".equals(mail.getStatus())) {
            System.out.println("Cannot remove SENT mail.");
        } else if ("REMOVED".equals(mail.getStatus())) {
            System.out.println("Mail is already removed.");
        } else {
            if (queueManager.removeMail(mailId)) {
                System.out.println("Mail " + mailId + " has been REMOVED.");
            } else {
                System.out.println("Failed to remove mail.");
            }
        }
    }

    // --- ADMIN METHODS ---

    private static void displayAllPending() {
        boolean found = false;
        System.out.println("\nAll Pending Mails:");
        for (Mail mail : queueManager.getPendingMails()) {
            System.out.println(mail.getMailId() + " -> From: " + mail.getSender() + " | To: " + mail.getRecipient() + " | Subject: " + mail.getSubject());
            found = true;
        }
        if (!found) {
            System.out.println("No pending mails.");
        }
    }

    private static void displayAllSent() {
        boolean found = false;
        System.out.println("\nAll Sent Mails:");
        for (Mail mail : queueManager.getMailMap().values()) {
            if ("SENT".equals(mail.getStatus())) {
                System.out.println(mail.getMailId() + " -> From: " + mail.getSender() + " | To: " + mail.getRecipient() + " | Subject: " + mail.getSubject());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No sent mails.");
        }
    }

    private static void searchAnyMail() {
        System.out.print("Enter Mail ID: ");
        String mailId = scanner.nextLine();
        
        Mail mail = queueManager.searchMail(mailId);
        if (mail == null) {
            System.out.println("Mail not found.");
        } else {
            System.out.println("Mail ID: " + mail.getMailId());
            System.out.println("Sender: " + mail.getSender());
            System.out.println("Recipient: " + mail.getRecipient());
            System.out.println("Subject: " + mail.getSubject());
            System.out.println("Status: " + mail.getStatus());
        }
    }

    private static void removeAnyMail() {
        System.out.print("Enter Mail ID to remove: ");
        String mailId = scanner.nextLine();

        Mail mail = queueManager.searchMail(mailId);
        if (mail == null) {
            System.out.println("Mail not found.");
        } else if ("SENT".equals(mail.getStatus())) {
            System.out.println("Cannot remove SENT mail.");
        } else if ("REMOVED".equals(mail.getStatus())) {
            System.out.println("Mail is already removed.");
        } else {
            if (queueManager.removeMail(mailId)) {
                System.out.println("Mail " + mailId + " has been REMOVED.");
            } else {
                System.out.println("Failed to remove mail.");
            }
        }
    }

    private static void processNextMail() {
        Mail processed = queueManager.processNextMail();
        if (processed != null) {
            System.out.println("Processed " + processed.getMailId() + ". Status is now SENT.");
        } else {
            System.out.println("No pending mails.");
        }
    }
}
