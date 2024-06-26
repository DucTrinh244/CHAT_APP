package Views;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private ServerSocket serverSocket;

    public ChatServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) throws IOException {
            this.clientSocket = socket;
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        public void run() {
            try {
                String request = in.readLine();
                String response = handleRequest(request);
                out.println(response);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String handleRequest(String request) {
            String[] parts = request.split(":");
            String requestType = parts[0];
            String userId = parts.length > 1 ? parts[1] : "";
            String response;
            switch (requestType) {
                case "GET_CHAT_HISTORY":
                    // Xử lý yêu cầu lấy lịch sử chat
                    response = "CHAT_HISTORY:" + getChatHistory(userId);
                    break;
                case "SEND_MESSAGE":
                    // Xử lý yêu cầu gửi tin nhắn
                    String message = parts.length > 2 ? parts[2] : "";
                    boolean success = sendMessage(userId, message);
                    response = success ? "SUCCESS:Message sent" : "FAIL:Message sending failed";
                    break;
                default:
                    response = "FAIL:Unknown request type";
                    break;
            }
            return response;
        }

        private String getChatHistory(String userId) {
            // Lấy lịch sử chat từ database hoặc bộ nhớ
        	System.out.println();
        	for(int i=1;i<100;i++) {
        		System.out.println(" chat:"+i);
        	}
            return "Chat history for user " + userId;
        }

        private boolean sendMessage(String userId, String message) {
            // Gửi tin nhắn và lưu vào database hoặc bộ nhớ
            return true;
        }
    }

    public static void main(String[] args) {
        try {
            ChatServer server = new ChatServer(12345);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
