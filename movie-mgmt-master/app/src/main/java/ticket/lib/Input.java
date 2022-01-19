package ticket.lib;

import java.util.Scanner;
import java.io.Console;

public class Input {
    
    public static boolean stdInEOF = false;
    public static boolean error = false;
    private static Scanner scanner = new Scanner(System.in);

    public static String getStrInput() {
        Input.scanner = new Scanner(System.in); // Reset scanner
        Input.error = false;
        if (scanner.hasNextLine()) {
            String str = Input.scanner.nextLine();
            return str;
        } else if (Input.scanner.hasNext()){
            Input.scanner.next(); // Clear whatever invalid input there is
        } else {
            Input.stdInEOF = true; // EOF
            UI.exit = true;
        }
        Input.error = true;
        return "";
    }

    public static int getIntInput() {
        Input.scanner = new Scanner(System.in); // Reset scanner
        Input.error = false;
        if (Input.scanner.hasNextInt()) {
            int num = Input.scanner.nextInt();
            return num;
        } else if (Input.scanner.hasNext()){
            Input.scanner.next(); // Clear whatever invalid input there is
        } else {
            Input.stdInEOF = true; // EOF
            UI.exit = true;
        }
        Input.error = true;
        return -1;
    }

    public static double getDoubleInput() {
        Input.scanner = new Scanner(System.in); // Reset scanner
        Input.error = false;
        if (Input.scanner.hasNextDouble()) {
            double num = Input.scanner.nextDouble();
            return num;
        } else if (Input.scanner.hasNext()){
            Input.scanner.next(); // Clear whatever invalid input there is
        } else {
            Input.stdInEOF = true; // EOF
            UI.exit = true;
        }
        Input.error = true;
        return -1;
    }

    public static String getPassword() {
        Console cons;
        char[] passwd;
        if ((cons = System.console()) != null) {
            if ((passwd = cons.readPassword("Password:")) != null) {
                // java.util.Arrays.fill(passwd, ' ');
                return new String(passwd);
            } else {
                Input.stdInEOF = true;
                UI.exit = true;
                Input.error = true;
                return "";
            }
        } else {
            System.out.print("Password: ");
            return Input.getStrInput();
        }
    }
}
