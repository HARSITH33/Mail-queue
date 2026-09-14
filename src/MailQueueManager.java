import java.util.Queue;
import java.util.ArrayDeque;
import java.util.HashMap;

public class MailQueueManager {
    private Queue<Mail> pendingMails = new ArrayDeque<>();
    private HashMap<String, Mail> mailMap = new HashMap<>();

    public void addMail(Mail mail) {
        pendingMails.add(mail);
        mailMap.put(mail.getMailId(), mail);
    }

    public Queue<Mail> getPendingMails() {
        return pendingMails;
    }
}
