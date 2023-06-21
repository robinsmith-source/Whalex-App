package org.example;

import org.example.profile.User;
import org.example.profile.UserManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserManagerTest {

    final UserManager um = UserManager.getInstance();

    User Marvin;

    //TODO: Remove and add a relative resource path to the Managers
    @BeforeEach
    public void setup() {

    }

    @Test
    public void testCreateUser() {
        //positive test
        Assertions.assertDoesNotThrow(() -> um.createUser(null, "Marvin", "123", "123"));
        Marvin = um.getUserByName("Marvin");

        Assertions.assertEquals(um.getUserByName("Marvin"), Marvin);
        Assertions.assertEquals(um.getUserById(Marvin.getUserID()), Marvin);

        Assertions.assertEquals("src\\main\\resources\\data\\defaultImages\\users\\defaultUserProfilePicture.jpg", Marvin.getProfilePicture().getPath());
        //negative tests
        //Username null or empty
        Assertions.assertThrows(IllegalArgumentException.class, () -> um.createUser(null, "", "123", "123"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> um.createUser(null, null, "123", "123"));

        //Password null or empty
        Assertions.assertThrows(IllegalArgumentException.class, () -> um.createUser(null, "Marvin", "", "123"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> um.createUser(null, "Marvin", null, "123"));

        //Wrong password confirmation
        Assertions.assertThrows(IllegalArgumentException.class, () -> um.createUser(null, "Marvin", "123", "12"));

        //Username already exists
        Assertions.assertThrows(IllegalArgumentException.class, () -> um.createUser(null, "Marvin", "123", "123"));
    }

    @Test
    public void testLogin() {
        Assertions.assertDoesNotThrow(() -> um.login("Marvin", "123"));
        Assertions.assertEquals(um.getActiveUser(), Marvin);
    }

    @Test
    public void testDeleteUser() {
        //positive test
        Assertions.assertDoesNotThrow(() -> um.deleteUser("123","123"));
        Assertions.assertNull(um.getUserByName("Marvin"));
        Assertions.assertNull(um.getUserById(Marvin.getUserID()));

        //negative test
    }
}
