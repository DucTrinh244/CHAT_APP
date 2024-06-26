package TestGetHistoryChatMessage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import Services.ConnectMySql;

public class DatabaseManager {
    private Connection connection;
    private ConnectMySql connect=new ConnectMySql();
    
    public ResultSet getGlobalMessages() {
        try {
            Statement stmt = connect.getConnection().createStatement();
            return stmt.executeQuery("SELECT * FROM groupmessage ORDER BY timestamp ASC");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getPrivateMessages(String sender, String receiver) {
        try {
            Statement stmt = connect.getConnection().createStatement();
            return stmt.executeQuery(
                    "SELECT m.message_id, m.sender_username, m.receiver_username, m.message_content, m.sent_timestamp " +
                            "FROM message m " +
                            "WHERE (m.sender_username = '"+sender +"' AND m.receiver_username = '"+receiver+"') " +
                            "   OR (m.sender_username = '"+receiver+"' AND m.receiver_username = '"+sender+"') " +
                            "ORDER BY m.sent_timestamp ASC");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }	
    }
}
