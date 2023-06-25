package org.example;

import org.example.media.SoundManager;
import org.example.media.interfaces.ISound;
import org.example.profile.User;
import org.example.profile.UserManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class UserManagerTest {

    final UserManager um = UserManager.getInstance();

    ISound sound;

    /**
     * Positive Tests
     */
    @Test
    public void testCreateUser() {
        Assertions.assertDoesNotThrow(() -> um.createUser(null, "Marvin", "123", "123"));
        User Marvin = um.getUserByName("Marvin");
        Assertions.assertEquals(Marvin.getProfilePicture().toString(), (UserManager.getInstance().getPROFILE_PICTURES() + "\\default\\defaultUserProfilePicture.jpg"));
    }

    @Test
    public void createSound() {
        //positive test
        Assertions.assertDoesNotThrow(() -> um.createUser(null, "Marvin", "123", "123"));
        User Marvin = um.getUserByName("Marvin");
        Assertions.assertDoesNotThrow(() -> SoundManager.getInstance().addSound(Marvin, "Pottwal Pop", new File("src/main/resources/data/sounds/example/72021005.wav")));
        sound = SoundManager.getInstance().getAllSoundsByUser(Marvin).get(0);
        Assertions.assertDoesNotThrow(() -> SoundManager.getInstance().deleteSoundByID(Marvin, sound.getSoundID()));
    }

    @Test
    public void deleteSound() {

    }

    public void t() {
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
        Assertions.assertDoesNotThrow(() -> um.createUser(null, "Marvin", "123", "123"));
        User Marvin = um.getUserByName("Marvin");
        Assertions.assertDoesNotThrow(() -> um.login("Marvin", "123"));
        Assertions.assertEquals(um.getActiveUser(), Marvin);
    }

    @Test
    public void testDeleteUser() {
        //positive test
        Assertions.assertDoesNotThrow(() -> um.createUser(null, "Marvin", "123", "123"));
        User Marvin = um.getUserByName("Marvin");
        Assertions.assertDoesNotThrow(() -> um.deleteUser("123", "123"));
        Assertions.assertNull(um.getUserByName("Marvin"));
        Assertions.assertNull(um.getUserById(Marvin.getUserID()));

        //negative test
    }
}
