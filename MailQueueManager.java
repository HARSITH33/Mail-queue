import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

public class MailQueueManager {
    private Queue<Mail> pendingMails;
    private HashMap<String, Mail> mailMap;

    public MailQueueManager() {
        this.pendingMails = new ArrayDeque<>();
        this.mailMap = new HashMap<>();
    }

    public void addMail(Mail mail) {
        pendingMails.add(mail);
        mailMap.put(mail.getMailId(), mail);
    }

    public Mail processNextMail() {
        Mail nextMail = pendingMails.poll();
        if (nextMail != null) {
            nextMail.setStatus("SENT");
            return nextMail;
        }
        return null;
    }

    public Mail searchMail(String mailId) {
        return mailMap.get(mailId);
    }

    public boolean removeMail(String mailId) {
        Mail mail = mailMap.get(mailId);
        if (mail != null && "PENDING".equals(mail.getStatus())) {
            pendingMails.remove(mail);
            mail.setStatus("REMOVED");
            return true;
        }
        return false;
    }

    public Queue<Mail> getPendingMails() {
        return pendingMails;
    }

    public HashMap<String, Mail> getMailMap() {
        return mailMap;
    }
}
