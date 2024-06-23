package Views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Window.Type;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


import Services.ConnectMySql;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JSeparator;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import Pattern.*;

public class Login_dky {

	public JFrame frmMyComputer;
	public JTextField UserName;
	private JButton btnNewButton;
	private JSeparator separator_1;
	private JSeparator separator_2;
	public JPasswordField Pasword;
	private JTextField FullName;
	private JPasswordField PasswordAgain;
	private JButton btnQuayLing;
	private ConnectMySql dao_ConnectMySql= new ConnectMySql();
	private JTextField Email;
	private EmailValidator emailValidator= new EmailValidator();
	private WhitespaceChecker whitespaceChecker= new WhitespaceChecker();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login_dky window = new Login_dky();
					window.frmMyComputer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}});}
	
	public  Login_dky() {
		initialize();
	}


	public void initialize() {
		frmMyComputer = new JFrame();
		frmMyComputer.getContentPane().setBackground(new Color(240, 248, 255));
		frmMyComputer.setTitle("My Computer");
		frmMyComputer.setType(Type.UTILITY);
		frmMyComputer.setBounds(100, 100, 900, 656);
		frmMyComputer.setLocationRelativeTo(null);
		frmMyComputer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMyComputer.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("ĐĂNG KÝ TÀI KHOẢN");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(new Color(15, 96, 43));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblNewLabel.setBounds(219, 20, 470, 56);
		frmMyComputer.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Tên Tài Khoản:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_1.setBounds(137, 109, 124, 44);
		frmMyComputer.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Mật Khẩu:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_2.setBounds(137, 259, 84, 44);
		frmMyComputer.getContentPane().add(lblNewLabel_2);
		
		UserName = new JTextField();
		UserName.setFont(new Font("Tahoma", Font.BOLD, 13));
		UserName.setBounds(137, 149, 574, 38);
		frmMyComputer.getContentPane().add(UserName);
		UserName.setColumns(10);
		
		
		btnNewButton =new JButton("Đăng Ký");
		btnNewButton.addActionListener(event ->{
			DangKy();
		});
		btnNewButton.setBackground(new Color(15, 96, 43));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setBounds(137, 509, 277, 44);
		frmMyComputer.getContentPane().add(btnNewButton);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(149, 184, 277, -4);
		frmMyComputer.getContentPane().add(separator);
		
		separator_1 = new JSeparator();
		separator_1.setBounds(68, 497, 741, 2);
		frmMyComputer.getContentPane().add(separator_1);
		
		separator_2 = new JSeparator();
		separator_2.setBounds(68, 86, 741, 2);
		frmMyComputer.getContentPane().add(separator_2);
		
		Pasword = new JPasswordField();
		Pasword.setFont(new Font("Tahoma", Font.BOLD, 13));
		Pasword.setBounds(137, 297, 574, 38);
		frmMyComputer.getContentPane().add(Pasword);
		
		FullName = new JTextField();
		FullName.setFont(new Font("Tahoma", Font.BOLD, 13));
		FullName.setColumns(10);
		FullName.setBounds(137, 224, 574, 38);
		frmMyComputer.getContentPane().add(FullName);
		
		JLabel lblNewLabel_1_1 = new JLabel("Họ và tên :");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_1_1.setBounds(137, 184, 124, 44);
		frmMyComputer.getContentPane().add(lblNewLabel_1_1);
		
		JLabel lblNewLabel_2_1 = new JLabel("Nhập lại mật khẩu :");
		lblNewLabel_2_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_2_1.setBounds(137, 334, 277, 44);
		frmMyComputer.getContentPane().add(lblNewLabel_2_1);
		
		PasswordAgain = new JPasswordField();
		PasswordAgain.setFont(new Font("Tahoma", Font.BOLD, 13));
		PasswordAgain.setBounds(137, 372, 574, 38);
		frmMyComputer.getContentPane().add(PasswordAgain);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("Email:");
		lblNewLabel_2_1_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_2_1_1.setBounds(137, 411, 277, 44);
		frmMyComputer.getContentPane().add(lblNewLabel_2_1_1);
		
		btnQuayLing = new JButton("Quay lại đăng nhập");
		btnQuayLing.addActionListener(event ->{
			frmMyComputer.setVisible(false);
			Login login= new Login();
			login.initialize();
		});
		btnQuayLing.setForeground(Color.WHITE);
		btnQuayLing.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnQuayLing.setBackground(new Color(15, 96, 43));
		btnQuayLing.setBounds(436, 509, 277, 44);
		frmMyComputer.getContentPane().add(btnQuayLing);
		
		Email = new JTextField();
		Email.setFont(new Font("Tahoma", Font.BOLD, 13));
		Email.setColumns(10);
		Email.setBounds(137, 449, 574, 38);
		frmMyComputer.getContentPane().add(Email);
		frmMyComputer.setVisible(true);

	}
	private void DangKy() {
		if(UserName.getText().equals("")||
	       FullName.getText().equals("")||
	       PasswordAgain.getText().equals("")||
	       Pasword.getText().equals("")||
	       Email.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "VUI LÒNG ĐIỀN ĐỦ THÔNG TIN CẦN THIẾT");
			
		}else if(!emailValidator.isValidEmail(Email.getText())||
				 !whitespaceChecker.containsWhitespace(UserName.getText())) {
		}
		else {
			if(Pasword.getText().equals(PasswordAgain.getText())) {
				// THỰC THI ĐĂNG KÝ

				AddAccount(UserName.getText(), FullName.getText(), Pasword.getText(), Email.getText());
				addUserforgroup(1,UserName.getText());
				UserName.setText("");
				FullName.setText("");
				Pasword.setText("");
				PasswordAgain.setText("");
				Email.setText("");

			}
			else {
				JOptionPane.showMessageDialog(null, "VUI LÒNG KIỂM TRA LẠI MẬT KHẨU");

			}
		}
	}
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
	
	
	private void AddAccount(String user, String name, String pass, String Email) {
		String sql = "INSERT INTO user (username, password, FullName, Email) VALUES (?, ?, ?, ?)";

        String hashedPassword = hashPassword(Pasword.getText());

        try {
        	Connection conn = dao_ConnectMySql.getConnection();

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, Email);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "ĐĂNG KÝ TÀI KHOẢN THÀNH CÔNG !");
            }
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	private void addUserforgroup(int id_room,String user) {
		String insertSQL = "INSERT INTO roommember (room_id, username) VALUES (?, ?)";

        try (Connection connection = dao_ConnectMySql.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1, id_room);
            preparedStatement.setString(2, user);

            int rowsInserted = preparedStatement.executeUpdate();
            
            if (rowsInserted > 0) {
                System.out.println("A new member was inserted successfully!");
            }

        } catch (SQLException e) {
            System.err.println("SQL exception occurred: " + e.getMessage());
        }
    
	}
}
