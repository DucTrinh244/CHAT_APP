package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import Models.Message;
import Models.User;
import Services.ConnectMySql;

public class MessageDAO {
    private Connection connection;
    private ConnectMySql connectMySql= new ConnectMySql();;

    public void AddMessage(Message mes) {
    	String sql = "INSERT INTO message (sender_username, receiver_username, message_content) VALUES (?, ?, ?)";

    	// Tạo PreparedStatement
        try {
        Connection con = connectMySql.getConnection();
		PreparedStatement stmt = con.prepareStatement(sql);
		
		// Thiết lập các tham số
		stmt.setString(1, mes.getSenderUsername());
		stmt.setString(2, mes.getReceiverUsername());
		stmt.setString(3, mes.getMessageContent());
//		stmt.setTimestamp(4, mes.getSentTimestamp());
		// Thực thi câu lệnh SQL
           stmt.executeUpdate();
//			JOptionPane.showMessageDialog(null, "THÊM THIẾT BỊ THÀNH CÔNG !");
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "LỖI THỰC THI! VUI LÒNG KIỂM TRA LẠI!");
//			System.err.println("Lỗi khi thêm dữ liệu vào bảng gói tập: " + e.getMessage());
		}
    }

    // Lấy lịch sử đoạn chat về 

    public List<Message> getChatMessages(String user1, String user2) {
    List<Message> messages = new ArrayList<>();
    
    String query = "SELECT m.message_id, m.sender_username, m.receiver_username, m.message_content, m.sent_timestamp " +
                   "FROM message m " +
                   "WHERE (m.sender_username = ? AND m.receiver_username = ?) " +
                   "   OR (m.sender_username = ? AND m.receiver_username = ?) " +
                   "ORDER BY m.sent_timestamp ASC";
                   
    try (Connection conn = connectMySql.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, user1);
        stmt.setString(2, user2);
        stmt.setString(3, user2);
        stmt.setString(4, user1);

        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            int messageId = resultSet.getInt("message_id");
            String senderUsername = resultSet.getString("sender_username");
            String receiverUsername = resultSet.getString("receiver_username");
            String messageContent = resultSet.getString("message_content");
            Timestamp sentTimestamp = resultSet.getTimestamp("sent_timestamp");

            Message message = new Message(messageId, senderUsername, receiverUsername, messageContent, sentTimestamp);
            messages.add(message);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return messages;
}
    
}
