public class Mail {
    private String mailId;
    private String sender;
    private String recipient;
    private String subject;
    private String status; // "PENDING", "SENT", "REMOVED"

    public Mail(String mailId, String sender, String recipient, String subject) {
        this.mailId = mailId;
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
        this.status = "PENDING";
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
