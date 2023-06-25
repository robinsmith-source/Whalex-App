package org.example;

import jdk.jfr.Description;
import org.example.media.SoundManager;
import org.example.profile.User;
import org.example.profile.UserManager;
import org.junit.jupiter.api.*;

import java.io.File;

public class SoundTest {

    static final UserManager um = UserManager.getInstance();
    static final SoundManager sm = SoundManager.getInstance();

    static User Marvin;

    @BeforeAll
    @Description("Sets up the save files for the tests")
    public static void setup() {
        sm.setSAVE_FILE(new File("src/test/resources/saves/sounds.json"));
        sm.setSOUNDS_PATH(new File("src/test/resources/data/sounds/").toPath());

        um.setSAVE_FILE(new File("src/test/resources/saves/users.json"));
    }

    @BeforeAll
    @Description("Sets up the Owner for the sounds to be tested")
    public static void setupOwner() {
        Assertions.assertDoesNotThrow(() -> um.createUser(null, "Marvin", "123", "123"));
        Marvin = um.getUserByName("Marvin");
    }

    @Test
    @Description("Tests the creation of a sound")
    public void testCreateSound() {
        Assertions.assertDoesNotThrow(() -> sm.addSound(Marvin, "test", new File("src/main/resources/data/sounds/example/9750300N.wav")));
    }

    @Disabled
    @Test
    @AfterEach
    @Description("Deletes the test sounds after each test")
    @DisplayName("Disabled because the problem with process allocating the file is not fixed yet")
    public void teardownSounds() {
        //Assertions.assertDoesNotThrow(() -> sm.getAllSounds().forEach(sound -> sm.deleteSoundByID(sound.getUploadedBy(), sound.getSoundID())));
    }


    @AfterAll
    @Description("Deletes the test users after all tests")
    public static void teardownUsers() {
        Assertions.assertDoesNotThrow(() -> um.getAllUsers().forEach(user -> um.deleteUser(user.getPassword(), user.getPassword())));
    }
}
