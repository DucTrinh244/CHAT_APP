package Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DAO.GroupDAO;
import DAO.MessageDAO;
import DAO.UserDAO;
import Models.GroupMessage;
import Models.Message;
import Models.User;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private static Map<String, ClientHandler> clientHandlers = new HashMap<>();
    private String clientID;
    private final ChatServer server;
    private MessageDAO messageDAO = new MessageDAO();
    private GroupDAO groupdao = new GroupDAO();
    private UserDAO userDAO = new UserDAO();
    
    public ClientHandler(Socket socket, ChatServer server) {
        this.clientSocket = socket;
        this.server = server;
        
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read client's username
            this.clientID = in.readLine();

            synchronized (clientHandlers) {
                if (clientHandlers.containsKey(clientID)) {
                    out.println("CLIENT EXISTS! Connection closing.");
                    return;
                }
                clientHandlers.put(clientID, this);
            }

            out.println("Welcome " + clientID + "! You can now send messages.");
            server.log("User " + clientID + " has connected.");

        } catch (IOException e) {
            server.log("Client connection error: " + e.getMessage());
        }
    }

    public void run() {
        try {
            // Handle commands from the client
            handleCommands();
            // Handle messages from the client
            handleMessageProcessing();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanup(); // Ensure resources are cleaned up when done
        }
    }

    private void handleCommands() throws IOException {
        String request;
        while ((request = in.readLine()) != null) {
        	System.out.println("Request :"+request);
            if (request.startsWith("GET_GLOBAL_CHAT")) {
                List<GroupMessage> messages = groupdao.retrieveGroupMessages(1);
                for (GroupMessage message : messages) {
                    out.println("|" + formatLocalDateTime(message.getTimestamp()) + " | " + message.getSenderUsername() + ":" + message.getContent());
                }
                out.println("END_OF_DATA");
            } else if (request.startsWith("GET_PRIVATE_CHAT")) {
                String[] parts = request.split(" ");
                if (parts.length == 3) {
                    String sender = parts[1];
                    String receiver = parts[2];
                    List<Message> messages = messageDAO.getChatMessages(sender, receiver);
                    for (Message message : messages) {
                        out.println("|" + formatTimestamp(message.getSentTimestamp()) + " | " + message.getSenderUsername() + ": " + message.getMessageContent());
                    }
                    out.println("END_OF_DATA");
                }
            } else if (request.startsWith("GET_NUMBER_USER")) {
                List<User> listUserFriend = userDAO.getAllUsers();
                for (User user : listUserFriend) {
                    out.println("" + user.getUsername());
                }
                out.println("END_OF_DATA");
            }
            
        }
    }
    

    private void handleMessageProcessing()  {
    		String receivedMessage;
        
        try {
            while ((receivedMessage = in.readLine()) != null) {
//                System.out.println("Received from " + clientID + ": " + receivedMessage);
                if (receivedMessage.startsWith("Client_Closed:")) {
                    String closingClientID = receivedMessage.substring(14);
                    server.log("User " + closingClientID + " has disconnected.");
                    // Optionally notify other clients
                    notifyClients(closingClientID + " has left the chat.");
                    break;
                }
                // Assume message format: "recipientID:message"
                String[] parts = receivedMessage.split(";", 2);
                if (parts.length == 2) {
                    String recipientID = parts[0];
                    String message = parts[1];
//                    System.out.println(recipientID + "-" + message);
                    sendMessage(recipientID, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
//        sendMessagesToClients();
    }

    private void sendMessage(String recipientID, String message) {
        if (recipientID.equals("Global_Chatting")) {
            // Send message to all clients
            synchronized (clientHandlers) {
                for (ClientHandler handler : clientHandlers.values()) {
                    handler.out.println("Global_Chatting;" + message);
                }
            }
        } else {
            // Send message to the client with recipientID
            ClientHandler recipient = clientHandlers.get(recipientID);
            if (recipient != null) {
                recipient.out.println(message);
            } else {
                System.out.println("Recipient not found: " + recipientID);
            }
        }
    }
    private void notifyClients(String message) {
        synchronized (clientHandlers) {
            for (ClientHandler handler : clientHandlers.values()) {
                handler.out.println("Global_Chatting;" + message);
            }
        }
    }
    public static String getTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return now.format(formatter);
    }
    private String formatTimestamp(Timestamp timestamp) {
        LocalTime time = timestamp.toLocalDateTime().toLocalTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return time.format(formatter);
    }

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return dateTime.format(formatter);
    }

    private void cleanup() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (clientHandlers) {
            clientHandlers.remove(this.clientID);
        }
    }
}
