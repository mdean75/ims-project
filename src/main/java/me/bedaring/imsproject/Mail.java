package me.bedaring.imsproject;

public class Mail {
    private String from;
    private String replyTo;

    // used when sending to a single address
    private String to;

    // used when sending to multiple addresses
    private String[] toMultiple;

    private String subject;
    private String content;

    public Mail() {
    }

    public Mail(String from, String to, String subject, String content) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String[] getToMultiple() {
        return toMultiple;
    }

    public void setToMultiple(String[] toMultiple) {
        this.toMultiple = toMultiple;
    }
}
