package Pattern;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordChecker {

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

    public static boolean checkPassword(String inputPassword, String hashedPassword) {
        String hashedInputPassword = hashPassword(inputPassword);
        return hashedInputPassword.equals(hashedPassword);
    }

    public static void main(String[] args) {
        String storedPassword = "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8"; // Mật khẩu đã được mã hóa
        String userInputPassword = "password"; // Mật khẩu nhập vào từ người dùng

        if (checkPassword(userInputPassword, storedPassword)) {
            System.out.println("Mật khẩu chính xác.");
        } else {
            System.out.println("Mật khẩu không chính xác.");
        }
    }
}
