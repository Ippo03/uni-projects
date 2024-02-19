/**
 * This utility class provides methods for validating user input from a text file. 
 * It includes functions for validating integers, non-empty strings, and other input constraints.
 */
public class UserInputValidator {
    public final static String REGEX_INT = "^[-+]?\\d+$";

    public static int validateInputInt(String input, String label) {
        int var = 0;

        if (input.isEmpty()) {
            System.out.println(label + " must not be empty!");
            System.exit(0);
        }

        if (!input.matches(REGEX_INT)) {
            System.out.println(label + " must be an integer!");
            System.exit(0);
        }

        var = Integer.parseInt(input.trim());

        if (var <= 0) {
            System.out.println(label + " must be greater than 0!");
            System.exit(0);
        }

        return var;
    }

    public static String validateInputString(String input, String label) {
        String var = "";

        if (input.isEmpty()) {
            System.out.println(label + " must not be empty!");
            System.exit(0);
        }

        var = input.trim();

        return var;
    }
}
