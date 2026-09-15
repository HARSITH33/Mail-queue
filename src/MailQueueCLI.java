import java.util.Scanner;

public class MailQueueCLI {
    private AuthService authService;
    private MailQueueManager mailQueueManager;
    private Scanner scanner;
    private int mailCounter;
    private User currentUser;

    public MailQueueCLI() {
        this.authService = new AuthService();
        this.mailQueueManager = new MailQueueManager();
        this.scanner = new Scanner(System.in);
        this.mailCounter = 1;
    }

    public void start() {
        System.out.println("MAIL QUEUE SYSTEM");
        
        while (true) {
            if (currentUser == null) {
                login();
            } else {
                if (currentUser.isAdmin()) {
                    showAdminMenu();
                } else {
                    showUserMenu();
                }
            }
        }
    }

    private void login() {
        System.out.println("\n--- Login ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = authService.login(username, password);
        if (user != null) {
            currentUser = user;
            System.out.println("Login successful! Welcome, " + currentUser.getUsername());
        } else {
            System.out.println("Error: Invalid username or password. Try again.");
        }
    }

    private void showUserMenu() {
        System.out.println("\n--- USER MENU ---");
        System.out.println("1. MAIL");
        System.out.println("2. PENDING");
        System.out.println("3. SEARCH");
        System.out.println("4. REMOVE");
        System.out.println("5. SENT");
        System.out.println("6. EXIT");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine();

        if (choice.equals("1") || choice.equalsIgnoreCase("MAIL")) {
            sendMail();
        } else if (choice.equals("2") || choice.equalsIgnoreCase("PENDING")) {
            viewPending();
        } else if (choice.equals("3") || choice.equalsIgnoreCase("SEARCH")) {
            searchMail();
        } else if (choice.equals("4") || choice.equalsIgnoreCase("REMOVE")) {
            removeMail();
        } else if (choice.equals("5") || choice.equalsIgnoreCase("SENT")) {
            viewSent();
        } else if (choice.equals("6") || choice.equalsIgnoreCase("EXIT")) {
            System.out.println("Exiting...");
            System.exit(0);
        } else {
            System.out.println("Invalid option.");
        }
    }

    private void showAdminMenu() {
        System.out.println("\n--- ADMIN MENU ---");
        System.out.println("1. PENDING");
        System.out.println("2. SEARCH");
        System.out.println("3. REMOVE");
        System.out.println("4. NEXT");
        System.out.println("5. SENT");
        System.out.println("6. EXIT");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine();

        if (choice.equals("1") || choice.equalsIgnoreCase("PENDING")) {
            viewPending();
        } else if (choice.equals("2") || choice.equalsIgnoreCase("SEARCH")) {
            searchMail();
        } else if (choice.equals("3") || choice.equalsIgnoreCase("REMOVE")) {
            removeMail();
        } else if (choice.equals("4") || choice.equalsIgnoreCase("NEXT")) {
            processNext();
        } else if (choice.equals("5") || choice.equalsIgnoreCase("SENT")) {
            viewSent();
        } else if (choice.equals("6") || choice.equalsIgnoreCase("EXIT")) {
            System.out.println("Exiting...");
            System.exit(0);
        } else {
            System.out.println("Invalid option.");
        }
    }

    private void sendMail() {
        System.out.print("Recipient: ");
        String recipient = scanner.nextLine();
        System.out.print("Subject: ");
        String subject = scanner.nextLine();

        String mailId = "M";
        if (mailCounter < 10) {
            mailId += "00" + mailCounter;
        } else if (mailCounter < 100) {
            mailId += "0" + mailCounter;
        } else {
            mailId += mailCounter;
        }
        mailCounter++;

        Mail newMail = new Mail(mailId, currentUser.getUsername(), recipient, subject);
        mailQueueManager.addMail(newMail);

        System.out.println("Mail created successfully! ID: " + mailId + ", Status: " + newMail.getStatus());
    }

    private void viewPending() {
        System.out.println("\n--- Pending Mails ---");
        boolean found = false;
        
        for (Mail mail : mailQueueManager.getPendingMails()) {
            if (currentUser.isAdmin() || mail.getSender().equals(currentUser.getUsername())) {
                System.out.println("ID: " + mail.getMailId() + 
                                   " | To: " + mail.getRecipient() + 
                                   " | Subject: " + mail.getSubject() + 
                                   " | Status: " + mail.getStatus());
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No pending mails found.");
        }
    }

    private void processNext() {
        Mail processedMail = mailQueueManager.processNextMail();
        if (processedMail != null) {
            System.out.println("Processed Mail ID: " + processedMail.getMailId() + ", Status: " + processedMail.getStatus());
        } else {
            System.out.println("No pending mails.");
        }
    }

    private void viewSent() {
        System.out.println("\n--- Sent Mails ---");
        boolean found = false;
        
        for (Mail mail : mailQueueManager.getMailMap().values()) {
            if (mail.getStatus() == MailStatus.SENT) {
                if (currentUser.isAdmin() || mail.getSender().equals(currentUser.getUsername())) {
                    System.out.println("ID: " + mail.getMailId() + 
                                       " | To: " + mail.getRecipient() + 
                                       " | Subject: " + mail.getSubject() + 
                                       " | Status: " + mail.getStatus());
                    found = true;
                }
            }
        }
        
        if (!found) {
            System.out.println("No sent mails found.");
        }
    }

    private void removeMail() {
        System.out.print("Enter Mail ID to remove: ");
        String mailId = scanner.nextLine();
        
        Mail mail = mailQueueManager.searchMail(mailId);
        if (mail == null) {
            System.out.println("Error: Mail not found.");
            return;
        }

        if (!currentUser.isAdmin() && !mail.getSender().equals(currentUser.getUsername())) {
            System.out.println("Error: Unauthorized to remove this mail.");
            return;
        }

        if (mail.getStatus() != MailStatus.PENDING) {
            System.out.println("Error: Only PENDING mails can be removed.");
            return;
        }

        boolean success = mailQueueManager.removeMail(mailId);
        if (success) {
            System.out.println("Mail " + mailId + " removed successfully.");
        } else {
            System.out.println("Error removing mail.");
        }
    }

    private void searchMail() {
        System.out.print("Enter Mail ID to search: ");
        String mailId = scanner.nextLine();
        
        Mail mail = mailQueueManager.searchMail(mailId);
        if (mail == null) {
            System.out.println("Mail not found.");
            return;
        }

        if (!currentUser.isAdmin() && !mail.getSender().equals(currentUser.getUsername())) {
            System.out.println("Error: Unauthorized to view this mail.");
            return;
        }

        System.out.println("\n--- Mail Details ---");
        System.out.println("ID: " + mail.getMailId());
        System.out.println("From: " + mail.getSender());
        System.out.println("To: " + mail.getRecipient());
        System.out.println("Subject: " + mail.getSubject());
        System.out.println("Status: " + mail.getStatus());
    }
}
