package Controllers;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ChatServer implements Runnable {
    private final int PORT;
    private final String IP_ADDRESS;
    private final JTextArea logArea;
    private ServerSocket serverSocket;
    private volatile boolean isRunning = true;
    private final List<ClientHandler> clientHandlers = new ArrayList<>();

    public ChatServer(JTextArea logArea, String IP_ADDRESS, int PORT) {
        this.logArea = logArea;
        this.IP_ADDRESS = IP_ADDRESS;
        this.PORT = PORT;
    }

    @Override
    public void run() {
        log("Server started...");
        try {
            InetAddress inetAddress = InetAddress.getByName(IP_ADDRESS);
            serverSocket = new ServerSocket(PORT, 50, inetAddress);
            while (isRunning) {
                try {
                    Socket socket = serverSocket.accept();
                    if (!isRunning) {
                        socket.close();
                        break;
                    }
                    log("New Client connected: " + socket.getInetAddress().getHostAddress());
                    ClientHandler clientHandler = new ClientHandler(socket, this);
                    synchronized (clientHandlers) {
                        clientHandlers.add(clientHandler);
                    }
                    new Thread(clientHandler).start();
                } catch (IOException e) {
                    if (isRunning) {
                        log("Error accepting connection: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            if (isRunning) {
                log("Server error: " + e.getMessage());
            }
        } finally {
            stopServer();
        }
    }

    void log(String message) {
        SwingUtilities.invokeLater(() -> logArea.append("[Server] : IP: " + IP_ADDRESS + ", Port: " + PORT + ", " + message + "\n"));
    }

    public void stopServer() {
        isRunning = false;
        log("Stopping server...");
        synchronized (clientHandlers) {
            for (ClientHandler clientHandler : clientHandlers) {
//                clientHandler.sendMessage("Server is shutting down. Please disconnect.");
//                clientHandler.stopClient();
            }
            clientHandlers.clear();
        }
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            log("Error stopping server: " + e.getMessage());
        }
        log("Server stopped.");
    }

    void removeClientHandler(ClientHandler clientHandler) {
        synchronized (clientHandlers) {
            clientHandlers.remove(clientHandler);
        }
    }
}
