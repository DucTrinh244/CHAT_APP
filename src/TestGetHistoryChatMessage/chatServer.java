package TestGetHistoryChatMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class chatServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(4222)) {
            System.out.println("Server is listening on port 4000");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

