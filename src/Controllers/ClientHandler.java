package Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import DAO.GroupDAO;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private static Map<String, ClientHandler> clientHandlers = new HashMap<>();
    private String clientID;
    private final ChatServer server;
    private GroupDAO groupDAO= new GroupDAO();

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
        sendMessagesToClients();
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
    public void sendMessagesToClients() {
    	String[] messages=groupDAO.retrieveGroupMessages(1);
        synchronized (clientHandlers) {
            for (ClientHandler handler : clientHandlers.values()) {
                for (String message : messages) {
                	System.out.println(message);
                    handler.out.println("Global_Chatting;" + message);
                }
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
