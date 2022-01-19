package ticket;

import java.util.*;
import java.io.*;

public class User {
    public static final String userFileName = "userInfo.csv";

    private final String name;
    private final String password;
    private CreditCard card;
    private UserType userType;

    public User(String name, String password, CreditCard card, UserType userType){
        this.name = name;
        this.password = password;
        this.card = card;
        this.userType = userType;
    }

    public String getName(){
        return this.name;
    }

    public CreditCard getCard(){
        return card;
    }

    public String getPassword() {
        return password;
    }

    public boolean validateCred(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        if (this.name.equals(username) && this.password.equals(password)) {
            return true;
        }
        return false;
    }

    public static boolean usernameExist(List<User> users, String username) {
        if (users == null || username == null) {
            return false;
        }
        for (User user : users) {
            if (username.equals(user.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add the new user to the list of users and write to the underlying file.
     * DOES NOT check for existing username!!
     * @param users     List of all users in system
     * @param newUser   The new user to be added
     * @return true if everything went well, false otherwise
     */
    public static boolean addUser(List<User> users, User newUser) {
        users.add(newUser);
        if (User.updateUsers(App.resourcesDir, User.userFileName, users)) { return true; }
        return false;
    }


    @Override
    public boolean equals(Object u){
        if (!(u instanceof User)){
            return false;
        }

        User user = (User) u;

        boolean cardIsEqual;
        if (this.card == null){
            if (user.getCard() != null){
                return false;
            } else{
                cardIsEqual = true;
            }
        } else{
            cardIsEqual = this.card.equals(user.getCard());
        }

        return this.name.equalsIgnoreCase(user.getName()) && this.password.equalsIgnoreCase(user.getPassword()) && cardIsEqual;
    }

    /**
     * Reads userInfo.csv to get all registered users
     * @return A list of all the users
     */
    public static ArrayList<User> initialiseUsers(String filePath, String fileName){
        //Initialise list
        ArrayList<User> userList = new ArrayList<>();

        //Get file path
        File file = new File(fileName);
        String pathAndFile= file.getAbsolutePath().replace(fileName, filePath + File.separator + fileName);
        // System.out.println(pathAndFile);

        //Start reading file
        try (BufferedReader br = new BufferedReader(new FileReader(pathAndFile))) {
            String line;

            //For each line in the file, add an ATM
            while ((line = br.readLine()) != null) {
                //Get each entry
                String[] atmValues = line.split(",");
                List<String> currentValues = Arrays.asList(atmValues);

                if (currentValues.size() < 3){
                    //If the current line is not valid
                    System.out.println("A line in user file has an error");
                } else{
                    //Get the card
                    CreditCard card;
                    if (currentValues.size() == 5){
                        card = new CreditCard(currentValues.get(3),Integer.parseInt(currentValues.get(4)));
                    } else{
                        card = null;
                    }

                    UserType userType;
                    //get user type
                    if (currentValues.get(2).equalsIgnoreCase("MANAGER")){
                        userType = UserType.MANAGER;
                    } else if (currentValues.get(2).equalsIgnoreCase("STAFF")){
                        userType = UserType.STAFF;
                    } else{
                        userType = UserType.CUSTOMER;
                    }

                    //And add our user to the list
                    userList.add(new User(currentValues.get(0), currentValues.get(1), card, userType));

                }
            }
        } catch(Exception e){
            System.out.println("Error reading userInfo.csv");
            e.printStackTrace();
            System.exit(-1);
        }
        return userList;
    }

    public UserType getUserType(){
        return this.userType;
    }

    /**
     * Updates the user file to all have all users in users param
     * @param filePath path of the file
     * @param fileName name of the file
     * @param users    list of users to write to file
     * @return true on success, false otherwise
     */
    public static boolean updateUsers(String filePath, String fileName, List<User> users){
        //Get file path to read it
        File file = new File(fileName);
        String path = file.getAbsolutePath().replace(fileName, filePath + File.separator + fileName);

        ArrayList<String> fileLines = new ArrayList<>();
        for (User u : users){

            String cardString;
            if (u.getCard() == null){
                cardString = "null";
            } else{
                cardString = u.getCard().getName() + "," + u.getCard().getNumber();
            }

            String userType;
            if (u.getUserType().equals(UserType.MANAGER)){
                userType = "MANAGER";
            } else if (u.getUserType().equals(UserType.STAFF)){
                userType = "STAFF";
            } else{
                userType = "CUSTOMER";
            }

            String s = u.getName() + "," + u.getPassword() + "," + userType + "," + cardString;
            fileLines.add(s);
        }

        //Now write to file
        try {
            FileWriter myWriter = new FileWriter(path);
            for (int j = 0; j < fileLines.size(); j++){
                myWriter.write(fileLines.get(j));
                if (j != fileLines.size()-1){
                    myWriter.write('\n');
                }
            }
            myWriter.close();

        } catch (Exception e) {
            System.out.println("Error writing to user file");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    enum UserType{
        CUSTOMER,
        STAFF,
        MANAGER
    }


}
