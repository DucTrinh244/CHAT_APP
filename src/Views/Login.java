package Views;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Window.Type;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.mysql.cj.protocol.Resultset;

import Pattern.PasswordChecker;
import Services.ConnectMySql;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JSeparator;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;


public class Login{

	public JFrame frmMyComputer;
	public JTextField User;
	private JButton btnNewButton;
	private JSeparator separator_1;
	private JSeparator separator_2;
	private JFrame jFrame_Exit;
	public JPasswordField Pass;
	private JButton btnngK;
	private ConnectMySql dao_ConnectMySql= new ConnectMySql();
	private JSpinner adip_1;
	private JSpinner adip_2;
	private JSpinner adip_3;
	private JSpinner adip_4;
	private JSpinner Port;

	private String User_main,Pass_main,FullName_main,Email_main;
	private PasswordChecker checkPass;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frmMyComputer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}});}
	
	public  Login() {
		initialize();
	}


	public void initialize() {
		frmMyComputer = new JFrame();
		frmMyComputer.getContentPane().setBackground(new Color(240, 248, 255));
		frmMyComputer.setTitle("My Computer");
		frmMyComputer.setType(Type.UTILITY);
		frmMyComputer.setBounds(100, 100, 900, 600);
		frmMyComputer.setLocationRelativeTo(null);
		frmMyComputer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMyComputer.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("ĐĂNG NHẬP");
		lblNewLabel.setForeground(new Color(15, 96, 43));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblNewLabel.setBounds(513, 91, 235, 56);
		frmMyComputer.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Tên Tài Khoản:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_1.setBounds(513, 157, 124, 44);
		frmMyComputer.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Mật Khẩu:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_2.setBounds(513, 239, 84, 44);
		frmMyComputer.getContentPane().add(lblNewLabel_2);
		
		User = new JTextField();
		User.setFont(new Font("Tahoma", Font.BOLD, 13));
		User.setBounds(513, 197, 335, 38);
		frmMyComputer.getContentPane().add(User);
		User.setColumns(10);
		
		
		btnNewButton =new JButton("Đăng Nhập");
		btnNewButton.addActionListener(event-> CheckAccout());
		btnNewButton.setBackground(new Color(15, 96, 43));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setBounds(513, 356, 158, 44);
		frmMyComputer.getContentPane().add(btnNewButton);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(149, 184, 277, -4);
		frmMyComputer.getContentPane().add(separator);
		
		separator_1 = new JSeparator();
		separator_1.setBounds(513, 330, 335, 2);
		frmMyComputer.getContentPane().add(separator_1);
		
		separator_2 = new JSeparator();
		separator_2.setBounds(513, 157, 335, 2);
		frmMyComputer.getContentPane().add(separator_2);
		
		Pass = new JPasswordField();
		Pass.setFont(new Font("Tahoma", Font.BOLD, 13));
		Pass.setBounds(513, 277, 335, 38);
		frmMyComputer.getContentPane().add(Pass);
		
		JLabel lblNewLabel_4 = new JLabel();
		lblNewLabel_4.setIcon(new ImageIcon("D:\\PictureForMe\\dddd.png"));
		lblNewLabel_4.setBounds(0, 0, 490, 563);
		frmMyComputer.getContentPane().add(lblNewLabel_4);
		
		btnngK = new JButton("Tạo tài khoản");
		btnngK.addActionListener(event->{
			frmMyComputer.setVisible(false);
			Login_dky login_dky= new Login_dky();
			login_dky.initialize();
		});
		btnngK.setForeground(Color.WHITE);
		btnngK.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnngK.setBackground(new Color(15, 96, 43));
		btnngK.setBounds(513, 509, 124, 44);
		frmMyComputer.getContentPane().add(btnngK);
		
		SpinnerNumberModel ip_1= new SpinnerNumberModel(127,0,500,1);
		 adip_1 = new JSpinner(ip_1);
		adip_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		adip_1.setBounds(561, 427, 60, 31);
		frmMyComputer.getContentPane().add(adip_1);
		
		SpinnerNumberModel ip_2= new SpinnerNumberModel(0,0,500,1);
		 adip_2 = new JSpinner(ip_2);
		adip_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		adip_2.setBounds(647, 427, 60, 31);
		frmMyComputer.getContentPane().add(adip_2);
		
		SpinnerNumberModel ip_3= new SpinnerNumberModel(0,0,500,1);
		 adip_3 = new JSpinner(ip_3);
		adip_3.setFont(new Font("Tahoma", Font.BOLD, 13));
		adip_3.setBounds(733, 427, 60, 31);
		frmMyComputer.getContentPane().add(adip_3);
		
		SpinnerNumberModel ip_4= new SpinnerNumberModel(1,0,500,1);
		 adip_4 = new JSpinner(ip_4);
		adip_4.setFont(new Font("Tahoma", Font.BOLD, 13));
		adip_4.setBounds(819, 427, 57, 31);
		frmMyComputer.getContentPane().add(adip_4);
		
		JLabel lblNewLabel_3 = new JLabel("PORT :");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_3.setBounds(500, 474, 60, 19);
		frmMyComputer.getContentPane().add(lblNewLabel_3);
		
		SpinnerNumberModel model = new SpinnerNumberModel(4000,0,10000,1);   
		 Port = new JSpinner(model);
		Port.setFont(new Font("Tahoma", Font.BOLD, 13));
		Port.setBounds(561, 468, 111, 31);
		frmMyComputer.getContentPane().add(Port);
		
		JLabel lblIp = new JLabel("IP :");
		lblIp.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblIp.setBounds(500, 430, 60, 19);
		frmMyComputer.getContentPane().add(lblIp);
		frmMyComputer.setVisible(true);

	}
	private void CheckAccout() {
		if(User.getText().equals("")||Pass.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "VUI LÒNG ĐIỀN ĐỦ THÔNG TIN");
		}else {
			if(check(User.getText(),Pass.getText())==true) {
				
//				System.out.println("ĐĂNG NHẬP THÀNH CÔNG !");
				// THỰC THI CHUYỂN ĐỔI
				login(User.getText(),
					  checkPass.hashPassword(Pass.getText()),
					  getPort(Port),
					  getIPAddress(adip_1, adip_2, adip_3, adip_4));
//				new Thread(new ChatClient(null))
			}else {
				JOptionPane.showMessageDialog(null, "ĐĂNG NHẬP THẤT BẠI! VUI LÒNG THỬ LẠI ");
			}
		}
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
	private boolean check(String user, String pass) {
		pass=checkPass.hashPassword(pass);
	    String sql = "SELECT * FROM user WHERE username = ?";

	    try (Connection conn = dao_ConnectMySql.getConnection();
	         PreparedStatement statement = conn.prepareStatement(sql)) {

	        statement.setString(1, user);
	        ResultSet resultSet = statement.executeQuery();

	        if (resultSet.next()) {
	            String password = resultSet.getString("password");
	            // Thực hiện kiểm tra mật khẩu ở đây, bạn nên sử dụng phương thức so sánh mật khẩu đã băm
	            // Ví dụ: if (BCrypt.checkpw(pass, password)) { ... }
	            if (password != null && password.equals(pass)) {
	                User_main = user;
	                Pass_main = pass;
	                FullName_main = resultSet.getString("FullName");
	                Email_main = resultSet.getString("email");
	                return true;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	private void login(String name,String password,int port,String IPAdress) {
        try {
            
            Tab_Swing_2 runclient = new Tab_Swing_2(IPAdress,port,name);
//            runclient.setVisible(true);
            
            frmMyComputer.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "FAILED TO CONNECT TO SERVER", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
}
