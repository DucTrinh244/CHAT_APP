package TestGetHistoryChatMessage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;

public class ClientHandler extends Thread {
    private Socket socket;
    private DatabaseManager dbManager;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.dbManager = new DatabaseManager();
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String request;
            while ((request = in.readLine()) != null) {
                if (request.startsWith("GET_GLOBAL_CHAT")) {
                    ResultSet rs = dbManager.getGlobalMessages();
                    while (rs.next()) {
                        out.println(rs.getString("sender_username") + ": " + rs.getString("content"));
                    }
                    out.println("END_OF_DATA");
                } else if (request.startsWith("GET_PRIVATE_CHAT")) {
                    String[] parts = request.split(" ");
                    if (parts.length == 3) {
                        String sender = parts[1];
                        String receiver = parts[2];
                        ResultSet rs = dbManager.getPrivateMessages(sender, receiver);
                        while (rs.next()) {
                            out.println(rs.getString("sender_username") + " to " + rs.getString("receiver_username") + ": " + rs.getString("message_content"));
                        }
                        out.println("END_OF_DATA");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
