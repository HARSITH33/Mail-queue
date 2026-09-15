import java.time.LocalDateTime;

public class Mail {
    private String mailId;
    private String sender;
    private String recipient;
    private String subject;
    private MailStatus status;
    private LocalDateTime createdAt;

    public Mail(String mailId, String sender, String recipient, String subject) {
        this.mailId = mailId;
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
        this.status = MailStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public String getMailId() {
        return mailId;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSubject() {
        return subject;
    }

    public MailStatus getStatus() {
        return status;
    }

    public void setStatus(MailStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
