package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import Models.User;
import Services.ConnectMySql;

public class UserDAO {
    private Connection connection;
    private ConnectMySql connectMySql= new ConnectMySql();;


    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM user";

        try (Connection con = connectMySql.getConnection()){
        	PreparedStatement selectPstmt = con.prepareStatement(query);
             ResultSet rs = selectPstmt.executeQuery(query) ;
            
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String fullName = rs.getString("FullName");
                String email = rs.getString("Email");
                
                User user = new User( username, password, fullName, email);
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }
    public int GetNumberUser() {
    	int numberUser=0;
    	String query = "SELECT COUNT(*) AS NUMBER\r\n"
    			+ "FROM user";

        try (Connection con = connectMySql.getConnection()){
        	PreparedStatement selectPstmt = con.prepareStatement(query);
             ResultSet rs = selectPstmt.executeQuery(query); 
            
            while (rs.next()) {
            
                numberUser= rs.getInt("NUMBER");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	return numberUser;
    }
    public List<User> getAllUsersExcept(String selectedUsername, String selectedPassword) {
        List<User> userList = new ArrayList<>();
        String query = "SELECT username,FullName,password, Email FROM user WHERE NOT (username = ? AND password = ?)";

        try (Connection con = connectMySql.getConnection();
             PreparedStatement selectPstmt = con.prepareStatement(query)) {
            
            selectPstmt.setString(1, selectedUsername);
            selectPstmt.setString(2, selectedPassword);
            
            ResultSet rs = selectPstmt.executeQuery();
            
            while (rs.next()) {
                String fullName = rs.getString("FullName");
                String username= rs.getString("username");
                String Password= rs.getString("password");
                String email = rs.getString("Email");
                
                User user = new User( username, Password, fullName, email);
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userList;
    }
    	
    public static void main(String[] args) {
    	ConnectMySql connection= new ConnectMySql();
		UserDAO userDAO= new UserDAO();
		List<User> listUsers= userDAO.getAllUsersExcept("admin","1");
		for (User user : listUsers) {
			System.out.println(user.toString());
			
		}
		System.out.println(userDAO.GetNumberUser());
	}
   
}


