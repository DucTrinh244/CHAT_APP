package Models;

import java.sql.Timestamp;

public class Message {
    private int messageId;
    private String senderUsername;
    private String receiverUsername;
    private String messageContent;
    private Timestamp sentTimestamp;

    // Constructors
    public Message() {}

    public Message(String senderUsername, String receiverUsername, String messageContent) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.messageContent = messageContent;
        this.sentTimestamp = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters
    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public Timestamp getSentTimestamp() {
        return sentTimestamp;
    }

    public void setSentTimestamp(Timestamp sentTimestamp) {
        this.sentTimestamp = sentTimestamp;
    }

	@Override
	public String toString() {
		return "Message [messageId=" + messageId + ", senderUsername=" + senderUsername + ", receiverUsername="
				+ receiverUsername + ", messageContent=" + messageContent + ", sentTimestamp=" + sentTimestamp + "]";
	}
    public static void main(String[] args) {
    	Message message = new Message();
	}
}
