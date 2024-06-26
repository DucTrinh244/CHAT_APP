package Views;
import java.io.*;
import java.net.Socket;

public class ChatClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ChatClient(String serverAddress, int serverPort) throws IOException {
        socket = new Socket(serverAddress, serverPort);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendRequest(String request) {
        out.println(request);
    }

    public String getResponse() throws IOException {
        return in.readLine();
    }

    public static void main(String[] args) {
        try {
            ChatClient client = new ChatClient("localhost", 12345);
            
            // Gửi yêu cầu lấy lịch sử chat
            String request = "GET_CHAT_HISTORY:user123";
            client.sendRequest(request);
            
            String response = client.getResponse();
//            System.out.println("Response: " + response);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
