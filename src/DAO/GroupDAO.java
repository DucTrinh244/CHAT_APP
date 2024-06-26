package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import Models.GroupMessage;
import Models.Message;
import Services.ConnectMySql;

public class GroupDAO {
    private Connection connection;
    private ConnectMySql connectMySql= new ConnectMySql();



    public void insertRoomMember(int roomId, String username) {
        String insertSQL = "INSERT INTO roommember (room_id, username) VALUES (?, ?)";

        try (Connection connection = connectMySql.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1, roomId);
            preparedStatement.setString(2, username);

            int rowsInserted = preparedStatement.executeUpdate();
            
            if (rowsInserted > 0) {
//                System.out.println("A new member was inserted successfully!");
            }

        } catch (SQLException e) {
            System.err.println("SQL exception occurred: " + e.getMessage());
        }
    }
    public void insertGroupMessage(GroupMessage mes) {
        String insertSQL = "INSERT INTO groupmessage (room_id, sender_username, content) VALUES (?, ?, ?)";

        try (Connection connection = connectMySql.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1, mes.getRoomId());
            preparedStatement.setString(2, mes.getSenderUsername());
            preparedStatement.setString(3, mes.getContent());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
//                System.out.println("A new message was inserted successfully!");
            }

        } catch (SQLException e) {
            System.err.println("SQL exception occurred: " + e.getMessage());
        }
    }
    public String getRoomName(int roomId) {
    	String roomname="";
        try (Connection conn =connectMySql.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT room_name FROM chatsroom WHERE room_id = ?")) {

            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
            	roomname = rs.getString("room_name");
            } else {
                System.out.println("Không tìm thấy phòng với room_id = " + roomId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return roomname;
    }
//   
//    public ResultSet retrieveGroupMessages(int roomId) {
//        String query = "SELECT message_id, sender_username, content, timestamp FROM groupmessage WHERE room_id = ? ORDER BY timestamp ASC;";
//        
//        try (Connection connection = connectMySql.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//
//            preparedStatement.setInt(1, roomId);
//
//            return  preparedStatement.executeQuery();
//
//        } catch (SQLException e) {
//            System.err.println("Error SQL: " + e.getMessage());
//        }
//        return null;
//    }

public List<GroupMessage> retrieveGroupMessages(int roomId) {
    String query = "SELECT message_id, sender_username, content, timestamp FROM groupmessage WHERE room_id = ? ORDER BY timestamp ASC;";
    List<GroupMessage> messages = new ArrayList<>();

    try (Connection connection = connectMySql.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

        preparedStatement.setInt(1, roomId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            int messageId = resultSet.getInt("message_id");
            String senderUsername = resultSet.getString("sender_username");
            String content = resultSet.getString("content");
            Timestamp timestamp = resultSet.getTimestamp("timestamp");

            GroupMessage message = new GroupMessage(messageId,roomId, senderUsername, content, convertTimestampToLocalDateTime(timestamp));
            messages.add(message);
        }

    } catch (SQLException e) {
        System.err.println("Error SQL: " + e.getMessage());
    }

    return messages;
}
public static LocalDateTime convertTimestampToLocalDateTime(Timestamp timestamp) {
    if (timestamp != null) {
        return timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    return null;
}

}
