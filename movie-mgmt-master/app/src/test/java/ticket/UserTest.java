package ticket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    final String expectedUser = "Bani";
    final String expectedPw = "H3ll0ImBaN1";
    User user = new User(expectedUser, expectedPw, null, User.UserType.CUSTOMER);

    @Test void testConstructor() {
        assertEquals(expectedUser, user.getName());
        assertNull(user.getCard());
    }

    @Test void testCheckCredentialNullUser() {
        assertFalse(user.validateCred(null, expectedPw));
    }

    @Test void testCheckCredentialNullPassword() {
        assertFalse(user.validateCred(expectedUser, null));
    }

    @Test void testCheckCredentialWrongUser() {
        assertFalse(user.validateCred("BAni", expectedPw));
    }

    @Test void testCheckCredentialWrongPassword() {
        assertFalse(user.validateCred(expectedUser, "HelloImBani"));
    }

    @Test void testCheckCredentialEmptyString() {
        assertFalse(user.validateCred("", ""));
    }

    @Test void testCheckCredentialNull() {
        final User u = new User(null, null, null, null);
        assertFalse(u.validateCred(null, null));
    }

    @Test void testCheckCredentialValidCred() {
        assertTrue(user.validateCred(expectedUser, expectedPw));
    }

    @Test void testUserExist() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("user1", "pass", null, User.UserType.CUSTOMER));
        users.add(new User("user2", "pass1", null, User.UserType.CUSTOMER));
        assertTrue(User.usernameExist(users, "user2"));
    }

    @Test void testUserDoesNotExist() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("user1", "pass", null, User.UserType.CUSTOMER));
        users.add(new User("user2", "pass1", null, User.UserType.CUSTOMER));
        assertFalse(User.usernameExist(users, "John Cena"));
    }

    @Test void testUserExistUserNameNull() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("user1", "pass", null, User.UserType.CUSTOMER));
        users.add(new User("user2", "pass1", null, User.UserType.CUSTOMER));
        assertFalse(User.usernameExist(users, null));
    }

    @Test void testUserExistUserListNull() {
        assertFalse(User.usernameExist(null, "someone"));
    }

    @Test
    void readUsersTest(){
        /*
        name1,password1,null
        name4,password1,Charles,40691
         */
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("name1", "password1", null, User.UserType.CUSTOMER));
        users.add(new User("name4", "password1", new CreditCard("Charles", 40691), User.UserType.CUSTOMER));
        ArrayList<User> actualUsers = User.initialiseUsers("src" + File.separator + "test" + File.separator + "resources", "testUserInfo.csv");

        /*
        for (int i = 0; i < users.size(); i++){
            System.out.println(users.get(i) + " " + actualUsers.get(i));
            assertTrue(users.get(i).equals(actualUsers.get(i)));
        }

         */
    }

    @Test
    public void writeUsersTest(){
        String path = "src" + File.separator + "test" + File.separator + "resources";
        String file = "randomTestCSV.csv";
        App.clearFile(path, file);

        //Write data to file
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("name1", "password1", null, User.UserType.CUSTOMER));
        users.add(new User("name4", "password1", new CreditCard("Charles", 40691), User.UserType.CUSTOMER));
        User.updateUsers(path, file, users);

        //Create correct line list
        ArrayList<String> correctLines = new ArrayList<>();
        correctLines.add("name1,password1,null");
        correctLines.add("name4,password1,Charles,40691");

        //Get the actual lines
        ArrayList<String> actualLines = new ArrayList<>();
        File fileFile = new File(file);
        String pathAndFile= fileFile.getAbsolutePath().replace(file, path + File.separator + file);

        //Start reading file
        try (BufferedReader br = new BufferedReader(new FileReader(pathAndFile))) {
            String line;

            //For each line in the file, get it
            while ((line = br.readLine()) != null) {
                actualLines.add(line);
            }

        } catch(Exception e){
            System.out.println("Error reading file");
            System.exit(-1);
        }

        //And finally, confirm correctness
        //assertTrue(correctLines.containsAll(actualLines));
    }
}
