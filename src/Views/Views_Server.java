package Views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Window.Type;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

import java.awt.Font;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SwingConstants;

import Controllers.ChatServer;
//import Controllers.Controller_ServerThread;
//import server.OnlineListThread;
//import server.ServerThread;

public class Views_Server {

	private JFrame frmServer;
	private AbstractButton ButtonStart;
	private JButton ButtonTest;
	private JButton ButtonClose;
	private JLabel lblIp;
	private JSpinner adip_1;
	private JSpinner adip_2;
	private JSpinner adip_3;
	private JSpinner adip_4;
	private JSpinner Port;
	public JTextArea Console_Chat;
	private ChatServer server;
//	private Controller_ServerThread serverThread;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Views_Server window = new Views_Server();
					window.frmServer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Views_Server() {
		initialize();
	}

	private void initialize() {
		frmServer = new JFrame();
		frmServer.setResizable(false);
		frmServer.setTitle("SERVER");
		frmServer.setBounds(100, 100, 647, 758);
		frmServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmServer.getContentPane().setLayout(null);
		frmServer.setLocationRelativeTo(null);
		
		Console_Chat = new JTextArea();
		Console_Chat.setEditable(false);
		Console_Chat.setForeground(new Color(0, 255, 64));
		Console_Chat.setBackground(new Color(0, 0, 0));
		Console_Chat.setLineWrap(true);
		Console_Chat.setFont(new Font("Monospaced", Font.BOLD, 12));
		JScrollPane scrollPane = new JScrollPane(Console_Chat);
		scrollPane.setBounds(23, 39, 585, 248);
		frmServer.getContentPane().add(scrollPane);
		
		ButtonStart = new JButton("Start Server");
		ButtonStart.setBackground(new Color(0, 128, 255));
		ButtonStart.addActionListener(event -> ActionButtonRunServer());
		ButtonStart.setIcon(new ImageIcon(Views_Server.class.getResource("/Assets/Server/start.png")));
		ButtonStart.setFont(new Font("Tahoma", Font.BOLD, 12));
		ButtonStart.setBounds(23, 345, 178, 42);
		frmServer.getContentPane().add(ButtonStart);
		
		ButtonTest = new JButton("Test Server");
		ButtonTest.setBackground(new Color(128, 255, 128));
		ButtonTest.addActionListener(event -> ActionButtonTestServer ());
		ButtonTest.setIcon(new ImageIcon(Views_Server.class.getResource("/Assets/Server/testing.png")));
		ButtonTest.setFont(new Font("Tahoma", Font.BOLD, 12));
		ButtonTest.setBounds(211, 345, 178, 42);
		frmServer.getContentPane().add(ButtonTest);
		
		ButtonClose = new JButton("Close Server");
		ButtonClose.setEnabled(false);
		ButtonClose.setBackground(null);
		ButtonClose.addActionListener(event -> ActionButtonCloseServer());
		ButtonClose.setIcon(new ImageIcon(Views_Server.class.getResource("/Assets/Server/ban.png")));
		ButtonClose.setFont(new Font("Tahoma", Font.BOLD, 12));
		ButtonClose.setBounds(399, 345, 178, 42);
		frmServer.getContentPane().add(ButtonClose);
		
		JLabel lblNewLabel = new JLabel("PORT :");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(23, 303, 60, 19);
		frmServer.getContentPane().add(lblNewLabel);
		
		SpinnerNumberModel model = new SpinnerNumberModel(4000,0,10000,1);   
		 Port = new JSpinner(model);
		Port.setFont(new Font("Tahoma", Font.BOLD, 13));
		Port.setBounds(84, 297, 111, 31);
		frmServer.getContentPane().add(Port);
		
		lblIp = new JLabel("IP :");
		lblIp.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblIp.setBounds(211, 303, 60, 19);
		frmServer.getContentPane().add(lblIp);
		
		SpinnerNumberModel ip_1= new SpinnerNumberModel(127,0,500,1);
		 adip_1 = new JSpinner(ip_1);
		adip_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		adip_1.setBounds(247, 297, 76, 31);
		frmServer.getContentPane().add(adip_1);
		
		SpinnerNumberModel ip_2= new SpinnerNumberModel(0,0,500,1);
		 adip_2 = new JSpinner(ip_2);
		adip_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		adip_2.setBounds(333, 297, 76, 31);
		frmServer.getContentPane().add(adip_2);
		
		SpinnerNumberModel ip_3= new SpinnerNumberModel(0,0,500,1);
		 adip_3 = new JSpinner(ip_3);
		adip_3.setFont(new Font("Tahoma", Font.BOLD, 13));
		adip_3.setBounds(419, 297, 76, 31);
		frmServer.getContentPane().add(adip_3);
		
		SpinnerNumberModel ip_4= new SpinnerNumberModel(1,0,500,1);
		 adip_4 = new JSpinner(ip_4);
		adip_4.setFont(new Font("Tahoma", Font.BOLD, 13));
		adip_4.setBounds(505, 297, 76, 31);
		frmServer.getContentPane().add(adip_4);
		
		JLabel lblNewLabel_1 = new JLabel("CHAT CONTROL ");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(107, 10, 405, 19);
		frmServer.getContentPane().add(lblNewLabel_1);
		
		
	}
	public void AppendMessage(String Text) {
		Console_Chat.append(getCurrentDateTime()+":  "+Text +"\n");
	}
	public int getPort(JSpinner spinner1) {
		return (Integer)spinner1.getValue();
	}
    public  String getIPAddress(JSpinner spinner1, JSpinner spinner2, JSpinner spinner3, JSpinner spinner4) {
        int part1 = (Integer) spinner1.getValue();
        int part2 = (Integer) spinner2.getValue();
        int part3 = (Integer) spinner3.getValue();
        int part4 = (Integer) spinner4.getValue();

        return part1 + "." + part2 + "." + part3 + "." + part4;
    }
	private void ActionButtonRunServer() {
	        
		   server= new ChatServer(Console_Chat,getIPAddress(adip_1, adip_2, adip_3, adip_4),getPort(Port));
		  new Thread(server).start();
//		  serverThread.start();
		if(true) {
			ButtonStart.setEnabled(false);
			ButtonStart.setBackground(null);
			ButtonTest.setEnabled(false);
			ButtonTest.setBackground(null);
			ButtonClose.setEnabled(true);
			ButtonClose.setBackground(new Color(255, 128, 128));
		}else {
			// Thực thi lệnh nếu không được chạy !!!
		}
	
	}

	private void ActionButtonCloseServer() {
			ButtonStart.setEnabled(true);
			ButtonStart.setBackground(new Color(0, 128, 255));
			ButtonTest.setEnabled(true);
			 ButtonTest.setBackground(new Color(128, 255, 128));
			ButtonClose.setEnabled(false);
			ButtonClose.setBackground(null);
		
	      if (server != null) {
	            server.stopServer();
	          
	  }
	}
	private void ActionButtonTestServer() {
		Console_Chat.append(getCurrentDateTime()
							+" [Server] :"
							+" Prepare running IP : "
							+getIPAddress(adip_1, adip_2, adip_3, adip_4)
							+", Port : "
							+getPort(Port)+"\n");
	}
    private String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }
}
