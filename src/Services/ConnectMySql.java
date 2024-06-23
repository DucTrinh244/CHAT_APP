package Services;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectMySql {
    private static final String URL = "jdbc:mysql://localhost:3306/app_chat";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        return connection;
    }


    public static boolean isConnected() {
        return connection != null;
    }
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing the database connection: " + e.getMessage());
            }
        }
    }
    public static void main(String[] args) {
    	 Connection conn = null;
    	ConnectMySql connectMySql= new ConnectMySql();
    	
    	conn =connectMySql.getConnection();
        if (connectMySql.isConnected()) {
            System.out.println("Connected to the database!");
	}
}
    }
