import java.time.LocalDateTime;

public class Mail {
    private String mailId;
    private String sender;
    private String recipient;
    private String subject;
    private MailStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private LocalDateTime removedAt;
    private String removedBy;

    public Mail(String mailId, String sender, String recipient, String subject) {
        this.mailId = mailId;
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
        this.status = MailStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.sentAt = null;
        this.removedAt = null;
        this.removedBy = null;
    }

    public Mail(String mailId, String sender, String recipient, String subject, MailStatus status,
                LocalDateTime createdAt, LocalDateTime sentAt, LocalDateTime removedAt, String removedBy) {
        this.mailId = mailId;
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
        this.status = status;
        this.createdAt = createdAt;
        this.sentAt = sentAt;
        this.removedAt = removedAt;
        this.removedBy = removedBy;
    }

    public String getMailId() {
        return mailId;
    }

    public void setMailId(String mailId) {
        this.mailId = mailId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public LocalDateTime getRemovedAt() {
        return removedAt;
    }

    public void setRemovedAt(LocalDateTime removedAt) {
        this.removedAt = removedAt;
    }

    public String getRemovedBy() {
        return removedBy;
    }

    public void setRemovedBy(String removedBy) {
        this.removedBy = removedBy;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "mailId='" + mailId + '\'' +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + '\'' +
                ", subject='" + subject + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", sentAt=" + sentAt +
                ", removedAt=" + removedAt +
                ", removedBy='" + removedBy + '\'' +
                '}';
    }
}
