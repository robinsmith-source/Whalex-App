package org.example;

import jdk.jfr.Description;
import org.example.profile.User;
import org.example.profile.UserManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

public class UserTest {

    static final UserManager um = UserManager.getInstance();


    @BeforeAll
    @Description("Sets up the save files for the tests")
    public static void setup() {
        um.setSAVE_FILE(new File("src/test/resources/saves/users.json"));
    }

    @Test
    @Description("Tests the creation of a user")
    public void testCreateUser() {
        Assertions.assertDoesNotThrow(() -> um.createUser(null, "Marvin", "123", "123"));
        User Marvin = Assertions.assertDoesNotThrow(() -> um.getUserByName("Marvin"));

        //Check if the user is created
        Assertions.assertEquals(um.getUserByName("Marvin"), Marvin);
        Assertions.assertEquals(um.getUserById(Marvin.getUserID()), Marvin);

        //Check if the default profile picture is set
        Assertions.assertEquals(UserManager.getInstance().getDEFAULT_PICTURE().getPath(), Marvin.getProfilePicture().getPath());

        //negative tests
        //Username null or empty
        Assertions.assertThrows(IllegalArgumentException.class, () -> um.createUser(null, "", "123", "123"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> um.createUser(null, null, "123", "123"));

        //Password null or empty
        Assertions.assertThrows(IllegalArgumentException.class, () -> um.createUser(null, "Robin", "", "123"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> um.createUser(null, "Robin", null, "123"));

        //Wrong password confirmation
        Assertions.assertThrows(IllegalArgumentException.class, () -> um.createUser(null, "Robin", "123", "12"));

        //Username already exists
        Assertions.assertThrows(IllegalArgumentException.class, () -> um.createUser(null, "Marvin", "123", "123"));
    }

    @Test
    @Description("Test if the user is logged in correctly")
    public void testLogin() {
        //login after creation
        Assertions.assertDoesNotThrow(() -> um.createUser(null, "Marvin", "123", "123"));
        User Marvin = Assertions.assertDoesNotThrow(() -> um.getUserByName("Marvin"));

        Assertions.assertEquals(um.getActiveUser(), Marvin);

        //logout
        Assertions.assertDoesNotThrow(um::logout);
        Assertions.assertThrows(IllegalStateException.class, um::getActiveUser);

        //login with null or empty username
        Assertions.assertThrows(IllegalArgumentException.class, () -> um.login(null, "123"));

        //login with wrong username
        Assertions.assertThrows(IllegalArgumentException.class, () -> um.login("Marv", "123"));

        //login with wrong password
        Assertions.assertThrows(IllegalArgumentException.class, () -> um.login("Marvin", "1234"));

        //login with null password
        Assertions.assertThrows(IllegalArgumentException.class, () -> um.login("Marvin", null));

        //login with empty password
        Assertions.assertThrows(IllegalArgumentException.class, () -> um.login("Marvin", ""));

        //login after logout
        Assertions.assertDoesNotThrow(() -> um.login("Marvin", "123"));
    }

    @Test
    @Description("Test if the user changes his password correctly")
    public void testPasswordChange() {
        Assertions.assertDoesNotThrow(() -> um.createUser(null, "Marvin", "oldPassword", "oldPassword"));

        //change password
        Assertions.assertDoesNotThrow(() -> um.getActiveUser().changePassword("oldPassword", "newPassword", "newPassword"));

        //logout
        Assertions.assertDoesNotThrow(um::logout);
        Assertions.assertThrows(IllegalStateException.class, um::getActiveUser);

        //login after logout
        Assertions.assertDoesNotThrow(() -> um.login("Marvin", "newPassword"));
    }

    @Test
    @AfterEach
    @Description("Deletes the test users after each test")
    public void teardownUsers() {
        Assertions.assertDoesNotThrow(() -> um.getAllUsers().forEach(user -> um.deleteUser(user.getPassword(), user.getPassword())));
    }
}
