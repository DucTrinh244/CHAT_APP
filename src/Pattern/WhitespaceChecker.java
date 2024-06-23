package Pattern;

import javax.swing.JOptionPane;

public class WhitespaceChecker {

    public static boolean containsWhitespace(String input) {
        if (input == null) {
            return true;
        }
        for (int i = 0; i < input.length(); i++) {
            if (Character.isWhitespace(input.charAt(i))) {
            	JOptionPane.showMessageDialog(null,"KHÔNG ĐƯỢC PHÉP CÓ KHOẢNG TRỐNG !!");
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // Test cases
        String[] testStrings = {
            "NoSpacesHere",
            "Some spaces here",
            "   ",
            "LeadingSpace ",
            "TrailingSpace"
        };

        for (String testString : testStrings) {
            System.out.println("'" + testString + "': " + containsWhitespace(testString));
        }
    }
}
