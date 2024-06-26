package TestGetHistoryChatMessage;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ViewClient implements Runnable {
    private JTextArea globalChatArea;
    private JTextArea privateChatArea;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ViewClient() {
        JFrame frame = new JFrame("Chat Client");
        globalChatArea = new JTextArea(10, 30);
        privateChatArea = new JTextArea(10, 30);
        globalChatArea.setEditable(false);
        privateChatArea.setEditable(false);

        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(globalChatArea), BorderLayout.NORTH);
        frame.add(new JScrollPane(privateChatArea), BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        try {
            socket = new Socket("localhost", 4000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            
            out.println("1");
            // Start a new thread to handle message fetching
            Thread thread = new Thread(this);
            thread.start();

        } catch (Exception e) {
            e.printStackTrace();
            closeResources();
        }
    }

    @Override
    public void run() {
        try {
            // Fetch and display global chat messages
        	
        	
            out.println("GET_GLOBAL_CHAT");
            readAndDisplayMessages(globalChatArea);

            // Fetch and display private chat messages
            out.println("GET_PRIVATE_CHAT 1 3");
            readAndDisplayMessages(privateChatArea);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    private void readAndDisplayMessages(JTextArea chatArea) throws Exception {
        String response;
        while ((response = in.readLine()) != null) {
            if (response.equals("END_OF_DATA")) {
                break;
            }
            appendToChatArea(chatArea, response);
        }
    }

    private void appendToChatArea(final JTextArea chatArea, final String message) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
        });
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ViewClient();
        });
    }
}
