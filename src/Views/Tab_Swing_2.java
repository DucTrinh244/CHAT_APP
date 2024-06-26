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
import java.util.ArrayList;
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

public class Tab_Swing_2 implements Runnable{
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
    private ConnectMySql connectMySql= new ConnectMySql();
	private Thread thread_1;;

    public Tab_Swing_2(String address, int Port, String UserName) {
    	this.UserName= UserName;
    	JFrame frame= new JFrame();
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT); // Enable scrollable tabs
        textAreas = new HashMap<>();

        frame.setLayout(new BorderLayout());
        frame.add(tabbedPane, BorderLayout.CENTER);

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
//        setTextInTab("Global_Chatting", "Bạn có thể nhắn tin ở đây !");
        // DUYỆT PHẦN TỪ CHO CHO CLIENT HIỂN THỊ
        List<User> listUserFriend = userDAO.getAllUsers();
        for (User user : listUserFriend) {
        	if(user.getUsername().equals(UserName)){

            }else {
                addTab(user.getUsername());
            }
        }

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("");
        sendButton.setIcon(new ImageIcon(Tab_Swing_2.class.getResource("/Assets/Client/send.png")));


        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        sendButton.addActionListener(event ->{
            ActionButtonSendMessage(NameTabCurrent, inputField.getText(),UserName);
          MessageDAO messageDAO=  new MessageDAO();
          messageDAO.AddMessage(GetDataMessage());
        });
        inputField.addActionListener(event ->{
            ActionButtonSendMessage(NameTabCurrent, inputField.getText(),UserName);
        });
        
        // ĐÓNG CLIENT VÀ GỬI LÊN SERVER 
        frame.addWindowListener(new WindowAdapter() {
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
        frame.add(inputPanel, BorderLayout.SOUTH);
        frame.setTitle("View - "+UserName);
//        setType(Type.UTILITY);
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
      	try {
            socket = new Socket(address, Port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Gửi ID của client đến server
            out.println(UserName);        

             new Thread(this).start();
            
    		new Thread(new Tab_Swing_2.IncomingReader()).start();


        } catch (IOException e) {
            e.printStackTrace();
        	closeResources();
        }
    }
	@Override
	public void run() {
	      
		try {
            // Fetch and display global chat messages
            out.println("GET_GLOBAL_CHAT");
            readAndDisplayMessages("Global_Chatting");

            out.println("GET_NUMBER_USER");
            List<String> ListUser= new ArrayList<>();
            String response;
            while ((response = in.readLine()) != null) {
                if (response.equals("END_OF_DATA")) {
                    break;
                }
                ListUser.add(response);
            }
            for(String list:ListUser) {
            	if(list.equals(UserName)) {
            	}else {
            	out.println("GET_PRIVATE_CHAT "+UserName+" "+list);
                 readAndDisplayMessages(list);
            	}
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
	}
    private void closeResources() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readAndDisplayMessages(String nametab) throws Exception {
        String response;
        while ((response = in.readLine()) != null) {
            if (response.equals("END_OF_DATA")) {
                break;
            }
            getTextInTab(nametab, response);
        }
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
    private String formatTimestamp(Timestamp timestamp) {
        LocalTime time = timestamp.toLocalDateTime().toLocalTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return time.format(formatter);
    }
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Tab_Swing_2 example = new Tab_Swing_2("127.0.0.1",4000,"1");
//            example.setVisible(true);
        });
    }



}
