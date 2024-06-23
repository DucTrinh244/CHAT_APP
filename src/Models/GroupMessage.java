package Models;

import java.time.LocalDateTime;

public class GroupMessage {
    private int messageId;
    private int roomId;
    private String senderUsername;
    private String content;
    private LocalDateTime timestamp;

    public GroupMessage(int messageId, int roomId, String senderUsername, String content, LocalDateTime timestamp) {
        this.messageId = messageId;
        this.roomId = roomId;
        this.senderUsername = senderUsername;
        this.content = content;
        this.timestamp = timestamp;
    }

    public GroupMessage() {
	}

	public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "GroupMessage{" +
                "messageId=" + messageId +
                ", roomId=" + roomId +
                ", senderUsername='" + senderUsername + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
