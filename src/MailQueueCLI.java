import java.util.Scanner;

public class MailQueueCLI {
    private AuthService authService;
    private MailQueueManager mailQueueManager;
    private User currentUser;
    private Scanner scanner;
    private int mailCounter = 1;

    public MailQueueCLI() {
        this.authService = new AuthService("data/users.txt");
        this.mailQueueManager = new MailQueueManager();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=========================================");
        System.out.println("           MAIL QUEUE SYSTEM             ");
        System.out.println("=========================================");

        login();

        if (currentUser != null) {
            showMenu();
        }
    }

    private void login() {
        while (currentUser == null) {
            System.out.print("\nUsername: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            currentUser = authService.authenticate(username, password);

            if (currentUser == null) {
                System.out.println("Invalid credentials. Please try again.");
            }
        }

        System.out.println("\nLogin successful!");
        System.out.println("Logged in as: " + currentUser.getUsername() + " (Role: " + currentUser.getRole() + ")");
    }

    private void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n-----------------------------------------");
            System.out.println("Available Commands:");
            if (currentUser.isAdmin()) {
                System.out.println("  [SEARCH]  [PENDING]  [REMOVE]  [NEXT]");
                System.out.println("  [SENT]    [REPORT]   [EXIT]");
            } else {
                System.out.println("  [MAIL]    [SEARCH]   [PENDING]  [REMOVE]");
                System.out.println("  [SENT]    [REPORT]   [EXIT]");
            }
            System.out.print("\nEnter command: ");
            String input = scanner.nextLine().trim();
            String command = input.toUpperCase();

            if (currentUser.isAdmin()) {
                switch (command) {
                    case "PENDING":
                        showPendingMails();
                        break;
                    case "SEARCH":
                    case "REMOVE":
                    case "NEXT":
                    case "SENT":
                    case "REPORT":
                        System.out.println(command + " command selected.");
                        break;
                    case "EXIT":
                        System.out.println("Exiting system. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid command.");
                        break;
                }
            } else {
                switch (command) {
                    case "MAIL":
                        createMail();
                        break;
                    case "PENDING":
                        showPendingMails();
                        break;
                    case "SEARCH":
                    case "REMOVE":
                    case "SENT":
                    case "REPORT":
                        System.out.println(command + " command selected.");
                        break;
                    case "EXIT":
                        System.out.println("Exiting system. Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid command.");
                        break;
                }
            }
        }
    }

    private void createMail() {
        System.out.print("Enter Recipient: ");
        String recipient = scanner.nextLine().trim();

        System.out.print("Enter Subject: ");
        String subject = scanner.nextLine().trim();

        String mailId = String.format("M%03d", mailCounter++);
        Mail mail = new Mail(mailId, currentUser.getUsername(), recipient, subject);
        mailQueueManager.addMail(mail);

        System.out.println("Mail created successfully.");
        System.out.println("Mail ID: " + mail.getMailId());
        System.out.println("Status: " + mail.getStatus());
    }

    private void showPendingMails() {
        boolean found = false;
        for (Mail mail : mailQueueManager.getPendingMails()) {
            if (currentUser.isAdmin()) {
                System.out.println("Mail ID: " + mail.getMailId() + ", Sender: " + mail.getSender() + ", Recipient: " + mail.getRecipient() + ", Subject: " + mail.getSubject() + ", Status: " + mail.getStatus());
                found = true;
            } else if (mail.getSender().equalsIgnoreCase(currentUser.getUsername())) {
                System.out.println("Mail ID: " + mail.getMailId() + ", Recipient: " + mail.getRecipient() + ", Subject: " + mail.getSubject() + ", Status: " + mail.getStatus());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No pending mails.");
        }
    }
}
