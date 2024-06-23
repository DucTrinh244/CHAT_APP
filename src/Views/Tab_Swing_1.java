package Views;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import DAO.GroupDAO;
import DAO.MessageDAO;
import DAO.UserDAO;
import Models.GroupMessage;
import Models.Message;
import Models.User;
import Services.ConnectMySql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.awt.*;
import java.awt.Window.Type;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tab_Swing_1 extends JFrame {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private JTabbedPane tabbedPane;
    private String UserName;
    private Map<String, JTextArea> textAreas;
    private JTextField inputField;
    private JButton sendButton;
    private String recipientID;
    private String NameTabCurrent = "";
    private UserDAO userDAO = new UserDAO();
    private MessageDAO messageDAO= new MessageDAO();
    private GroupDAO groupDAO= new GroupDAO();
    private Connection connection;
    private ConnectMySql connectMySql= new ConnectMySql();;


    public Tab_Swing_1(String address, int Port, String UserName){
        this.UserName= UserName;
        try {
            socket = new Socket(address, Port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Gửi ID của client đến server
            out.println(UserName);

            Views_Tab_Swing_1();

            new Thread(new Tab_Swing_1.IncomingReader()).start();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }


    public void Views_Tab_Swing_1() {
        this.UserName= UserName;
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT); // Enable scrollable tabs
        textAreas = new HashMap<>();

        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);

        // Adding ChangeListener to JTabbedPane
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedTitle = tabbedPane.getTitleAt(selectedIndex);
//                    System.out.println("Current tab: " + selectedTitle);
                    NameTabCurrent = selectedTitle;
                }
            }
        });
        
        addTab("Global_Chatting");
        setTextInTab("Global_Chatting", "Bạn có thể nhắn tin ở đây !");
        // DUYỆT PHẦN TỪ CHO CHO CLIENT HIỂN THỊ
        List<User> listUserFriend = userDAO.getAllUsers();
        for (User user : listUserFriend) {
        	if(user.getUsername().equals(UserName)){

            }else {
                addTab(user.getUsername());
//                setTextInTab(user.getUsername(), "Hello, "+user.getUsername());
            	getChatMessages(UserName,user.getUsername());
            }
        }
        retrieveGroupMessages(1);
//        for (int i = 1; i <= 3; i++) {
//            String nameclient= "Client_"+i;
//            if(nameclient.equals(UserName)){
//
//            }else {
//                addTab(nameclient);
//                setTextInTab(nameclient, "This is the content of Tab " + i + ".");
//            }
//        }
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("");
        sendButton.setIcon(new ImageIcon(Tab_Swing_1.class.getResource("/Assets/Client/send.png")));


        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        sendButton.addActionListener(event ->{
            ActionButtonSendMessage(NameTabCurrent, inputField.getText(),UserName);
                    PrintTabClick(NameTabCurrent);
          MessageDAO messageDAO=  new MessageDAO();
          messageDAO.AddMessage(GetDataMessage());
        });
        inputField.addActionListener(event ->{
            ActionButtonSendMessage(NameTabCurrent, inputField.getText(),UserName);
                    PrintTabClick(NameTabCurrent);
        });
        // ĐÓNG CLIENT VÀ GỬI LÊN SERVER 
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (socket != null && !socket.isClosed()) {
                    out.println("Client_Closed:" + UserName); // Notify server
                    try {
                        socket.close();
                    } catch (IOException ex) {
//                        ex.printStackTrace();
                    }
                }
                System.exit(0);
            }
        });
    
        add(inputPanel, BorderLayout.SOUTH);
        setTitle("View - "+UserName);
//        setType(Type.UTILITY);
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void ActionButtonSendMessage(String ReceiID, String message,String UserName) {
        if (!ReceiID.isEmpty() && textAreas.containsKey(ReceiID)) {
            String fullMessage = ReceiID +";"+UserName+":" + message;
            // ĐẨY TIN NHẮN LÊN SERVER ĐỂ XỬ LÝ
            out.println(fullMessage);
            JTextArea textArea = textAreas.get(ReceiID);
            
            
            if(ReceiID.equals("Global_Chatting")){
            	groupDAO.insertGroupMessage(getDataGroupMessage());
            	
            }else {
                // ADD INFORMATION TO DATABASE
            	messageDAO.AddMessage(GetDataMessage());
            	
                textArea.append("\n"+ "| "+getTime()+" | "+UserName + ": " + message);
            }
            inputField.setText("");
//            System.out.println("full message get to server :"+fullMessage);
        }else if(message.equals("")){
            JOptionPane.showMessageDialog(null,"Please enter your Message !");
        }
        else {
            System.out.println("Tab with title '" + ReceiID + "' does not exist.");
        }
    }

    public void addTab(String title) {
        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        tabbedPane.addTab(title, scrollPane);
        textAreas.put(title, textArea);
    }

    public void PrintTabClick(String tablink){
//        System.out.println("tab currently :"+tablink);
    }
    public void setTextInTab(String title, String text) {
        JTextArea textArea = textAreas.get(title);
        if (textArea != null) {
            textArea.append("\n"+"| "+getTime()+" | "+text);
        } else {
            System.out.println("Tab with title '" + title + "' does not exist.");
        }
    }
    public void getTextInTab(String title, String text) {
        JTextArea textArea = textAreas.get(title);
        if (textArea != null) {
            textArea.append("\n"+text);
        } else {
            System.out.println("Tab with title '" + title + "' does not exist.");
        }
    }
    // LẤY DỮ LIỆU TỪ SERVER VỂ VÀ SET VÀO TAB
    private class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = in.readLine()) != null) {
                	
                    if (message.startsWith("Global_Chatting;")) {
                        String globalMessage = message.substring(16);
                        setTextInTab("Global_Chatting",globalMessage);
                    }
                    else{
                        List<User> listUserFriend = userDAO.getAllUsers();
                        for (User user : listUserFriend) {
                        	if(user.getUsername().equals(UserName)){

                            }
                        	else {
                            	if(message.startsWith(user.getUsername()+":")) {
                            		 setTextInTab(user.getUsername(),message);
                            	}
                            }
                        }
//                        System.out.println("Đéo kiếm ra dữ liệu nha em ");
                    }
                }
            } catch (IOException e) {
//                e.printStackTrace();
            }
        }
    }
    public static String getTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return now.format(formatter);
    }
    private Message GetDataMessage() {
	    try {
	        if (inputField.getText().equals("")) {
	            JOptionPane.showMessageDialog(null, "Vui Lòng Nhập tin nhắn", "LỖI", JOptionPane.ERROR_MESSAGE);
	            return null; // Trả về null khi dữ liệu không đủ
	        } else {
	        	 Date currentDate = new Date();
	             Timestamp time= new Timestamp(currentDate.getTime());

	            String Usersender = UserName;
	            String UserReceider = NameTabCurrent;
	            String text=inputField.getText();
	

	         Message messs= new Message();
	         messs.setSenderUsername(Usersender);
	         messs.setReceiverUsername(UserReceider);
	         messs.setMessageContent(text);
	         messs.setSentTimestamp(time);


	            return messs; 
	        }
	    } catch (NumberFormatException ex) {
	        JOptionPane.showMessageDialog(null, "GIÁ TRỊ KHÔNG HỢP LỆ", "LỖI", JOptionPane.ERROR_MESSAGE);
	        return null; // Trả về null khi giá trị giá không hợp lệ
	    }
	}
    private GroupMessage getDataGroupMessage() {
    	try {
	        if (inputField.getText().equals("")) {
	            JOptionPane.showMessageDialog(null, "Vui Lòng Nhập tin nhắn", "LỖI", JOptionPane.ERROR_MESSAGE);
	            return null; // Trả về null khi dữ liệu không đủ
	        } else {
	        	 Date currentDate = new Date();
	             Timestamp time= new Timestamp(currentDate.getTime());

	            String Usersender = UserName;
	            String UserReceider = NameTabCurrent;
	            String text=inputField.getText();
	

	         GroupMessage messs= new GroupMessage();
	         messs.setSenderUsername(Usersender);
	         messs.setRoomId(1);
	         messs.setSenderUsername(Usersender);
	         messs.setContent(text);
//	         messs.setTimestamp((LocalDateTime)time);


	            return messs; // Trả về đối tượng customer khi dữ liệu đủ
	        }
	    } catch (NumberFormatException ex) {
	        JOptionPane.showMessageDialog(null, "GIÁ TRỊ KHÔNG HỢP LỆ", "LỖI", JOptionPane.ERROR_MESSAGE);
	        return null; // Trả về null khi giá trị giá không hợp lệ
	    }
    }
    public void getChatMessages(String user1, String user2) {

        try (Connection conn = connectMySql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT m.message_id, m.sender_username, m.receiver_username, m.message_content, m.sent_timestamp " +
                 "FROM message m " +
                 "WHERE (m.sender_username = ? AND m.receiver_username = ?) " +
                 "   OR (m.sender_username = ? AND m.receiver_username = ?) " +
                 "ORDER BY m.sent_timestamp ASC")) {
            stmt.setString(1, user1);
            stmt.setString(2, user2);
            stmt.setString(3, user2);
            stmt.setString(4, user1);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int messageId = rs.getInt("message_id");
                    String senderUsername = rs.getString("sender_username");
                    String receiverUsername = rs.getString("receiver_username");
                    String messageContent = rs.getString("message_content");
                    java.sql.Timestamp sentTimestamp = rs.getTimestamp("sent_timestamp");
//                    System.out.println(receiverUsername+"--");
                    getTextInTab(user2, "| "+formatTimestamp(sentTimestamp)+" | "+senderUsername+": "  +messageContent);
//                    setTextInTab(senderUsername, "| "+formatTimestamp(sentTimestamp)+" | "+messageContent+"Cạc");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void retrieveGroupMessages(int roomId) {
        String query = "SELECT message_id, sender_username, content, timestamp FROM groupmessage WHERE room_id = ? ORDER BY timestamp ASC;";
        
        try (Connection connection = connectMySql.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, roomId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int messageId = resultSet.getInt("message_id");
                    String senderUsername = resultSet.getString("sender_username");
                    String content = resultSet.getString("content");
                    String timestamp = resultSet.getString("timestamp");
                    java.sql.Timestamp sentTimestamp = resultSet.getTimestamp("timestamp");
                    groupDAO.getRoomName(roomId);
                    getTextInTab(groupDAO.getRoomName(roomId), "| "+formatTimestamp(sentTimestamp)+" | "+senderUsername+": "  +content);

                }
            }

        } catch (SQLException e) {
            System.err.println("SQL exception occurred: " + e.getMessage());
        }
    }
    
    private String formatTimestamp(Timestamp timestamp) {
        LocalTime time = timestamp.toLocalDateTime().toLocalTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return time.format(formatter);
    }
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Tab_Swing_1 example = new Tab_Swing_1("127.0.0.1",4000,"3");
            example.setVisible(true);
        });
    }
}
