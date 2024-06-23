package Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler_1 implements Runnable {
    private Socket clientSocket;
    private String clientName;
    private BufferedReader in;
    private PrintWriter out;
    private static final Map<String, ClientHandler_1> clients = new HashMap<>();
    private final ChatServer server;

    public ClientHandler_1(Socket socket, ChatServer server) {
        this.clientSocket = socket;
        this.server = server;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            // Read client's username
            this.clientName = in.readLine();
            
            synchronized (clients) {
                if (clients.containsKey(clientName)) {
                    out.println("CLIENT EXISTS! Connection closing.");
                    return;
                }
                clients.put(clientName, this);
            }

            out.println("Welcome " + clientName + "! You can now send messages.");
            server.log("User " + clientName + " has connected.");

        } catch (IOException e) {
            server.log("Client connection error: " + e.getMessage());
        }
    }

    // Rest of the class remains the same...

    @Override
    public void run() {
        String receivedMessage;
        try {
            while ((receivedMessage = in.readLine()) != null) {
//                System.out.println("Received from " + clientName + ": " + receivedMessage);
                // Assuming message format: "recipientID:message"
                String[] parts = receivedMessage.split(":", 2);
                if (parts.length == 2) {
                    String recipientID = parts[0];
                    String message = parts[1];
                    sendMessage(recipientID, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized (clients) {
                clients.remove(clientName);
            }
            server.log("User " + clientName + " has disconnected.");
        }
    }
    private void sendMessage(String recipientID, String message) {
        if (recipientID.equals("CHAT GLOBAL")) {
            // Send message to all clients
            synchronized (clients) {
                for (ClientHandler_1 handler : clients.values()) {
                    handler.out.println(clientName + ": " + message);
                }
            }
        } else {
            ClientHandler_1 recipient = clients.get(recipientID);
            if (recipient != null) {
                recipient.out.println(clientName + ": " + message);
            }
        }
    }
    public void stopClient() {
        try {
            synchronized (clients) {
                clients.remove(clientName);
            }
            server.log("User " + clientName + " has disconnected.");
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            server.log("Error closing client socket: " + e.getMessage());
        }
    }


}
