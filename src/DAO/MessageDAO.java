package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

}
