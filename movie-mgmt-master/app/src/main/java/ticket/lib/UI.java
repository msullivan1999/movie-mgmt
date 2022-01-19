package ticket.lib;

import java.util.List;
import ticket.User;

public class UI {
    
    private static final char boxCorner = '+';
    private static final char boxVEdge = '-';
    private static final char boxHEdge = '|';

    public static boolean exit = false;

    public static void drawBox(String str) {
        if (str == null || str.equals("")) {
            return;
        }
        // Top layer
        System.out.print(UI.boxCorner);

        for (int i = 0; i < str.length() + 2; i++) {
            System.out.print(UI.boxVEdge);
        }
        System.out.println(UI.boxCorner);

        // Center layer
        System.out.printf("%c %s %c\n", UI.boxHEdge, str, UI.boxHEdge);

        // Bottom layer
        System.out.print(UI.boxCorner);
        for (int i = 0; i < str.length() + 2; i++) {
            System.out.print(UI.boxVEdge);
        }
        System.out.println(UI.boxCorner);
    }

    /**
     * Print choose option screen, please check the UI.exit flag after every call!!!
     * exit flag will be set to true if EOF on stdin.
     * @param pageName Optional, Page name to print in box above option
     * @param user Optional, defaults to guest
     * @param options Options to print, 0th index is 1
     * @param validInt Optional, valid integer to accept besides 1 to n options
     * @return -1 for error, user input on success
     */
    public static int chooseOption(String pageName, User user, List<String> options, int[] validInt) {
        boolean valid = false;
        int ret = -1;
        while (!valid) {
            if (pageName != null) {
                if (!pageName.equals("")) {
                    UI.drawBox(pageName);
                }
            }
            System.out.print("Logged in as: ");
            if (user == null) {
                System.out.print("None ");
                System.out.println("(GUEST)");
            } else {
                System.out.print(user.getName());
                System.out.println(" (" + user.getUserType() + ")");
            }


            int i = 1;
            for (String option : options) {
                System.out.printf("%d) %s\n", i, option);
                i += 1;
            }
            System.out.print("\nChoose an option: ");
            int userInput = Input.getIntInput();
    
            if (Input.stdInEOF) {
                UI.exit = true;
                ret = -1;
                break;
            } 
            
            if (Input.error) {
                UI.error("Invalid input, please try again.", true);
                continue;
            }
            
            // Check user input in validInt array
            if (validInt != null) {
                for (int validNum : validInt) {
                    if (userInput == validNum) {
                        ret = userInput;
                        valid = true;
                        break;
                    }
                }
            }

            // Check user input in range
            if (!valid && userInput > 0 && userInput <= options.size()) {
                ret = userInput;
                valid = true;
            }

            if (!valid) {
                UI.error("Invalid input, please try again.", true);
            }
        }
        return ret;
    }
    
    /**
     * Print str to stdout in bold and red colour.
     * @param str String to print to stdout
     * @param newLine Print a new line at end of string
     */
    public static void error(String str, boolean newLine) {
        if (str == null) { return; }
        System.out.printf("\033[1;31m%s\033[0m", str);
        if (newLine) { System.out.println(); }
    }
    
    /**
     * Print str to stdout in bold and green colour.
     * @param str String to print to stdout
     * @param newLine Print a new line at end of string
     */
    public static void success(String str, boolean newLine) {
        if (str == null) { return; }
        System.out.printf("\033[1;32m%s\033[0m", str);
        if (newLine) { System.out.println(); }
    }
}