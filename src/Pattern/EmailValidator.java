package Pattern;

import java.util.regex.Pattern;

import javax.swing.JOptionPane;

public class EmailValidator {
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
        Pattern.CASE_INSENSITIVE
    );

    // Method to validate email
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }else if(EMAIL_PATTERN.matcher(email).matches()) {
        	return true;
        }
    	JOptionPane.showMessageDialog(null, "LỖI ĐỊNH DẠNG EMAIL !. VUI LÒNG KIỂM TRA LẠI !");
        return false;
    }

    public static void main(String[] args) {
        // Test cases
        String[] emails = {
            "test@example.com",
            "invalid-email",
            "another.test@domain.co",
            "wrong@domain@domain.com"
        };

        for (String email : emails) {
            System.out.println(email + ": " + isValidEmail(email));
        }
    }
}
