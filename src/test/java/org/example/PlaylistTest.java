package org.example;

import jdk.jfr.Description;
import org.example.media.SoundManager;
import org.example.playlist.Playlist;
import org.example.playlist.PlaylistManager;
import org.example.profile.User;
import org.example.profile.UserManager;
import org.junit.jupiter.api.*;

import java.io.File;

public class PlaylistTest {
    static final UserManager um = UserManager.getInstance();
    static final SoundManager sm = SoundManager.getInstance();

    static final PlaylistManager pm = PlaylistManager.getInstance();

    static User Marvin;

    @BeforeAll
    @Description("Sets up the save files for the tests")
    public static void setup() {
        um.setSAVE_FILE(new File("src/test/resources/saves/users.json"));

        sm.setSAVE_FILE(new File("src/test/resources/saves/users.json"));
        sm.setSOUNDS_PATH(new File("src/test/resources/data/sounds/").toPath());

        pm.setSAVE_FILE(new File("src/test/resources/saves/playlists.json"));
        pm.setPLAYLIST_COVERS(new File("src/test/resources/data/playlists/").toPath());
    }

    @BeforeAll
    @Description("Sets up the Owner and the sounds for the playlists to be tested")
    public static void setupOwner() {
        Assertions.assertDoesNotThrow(() -> um.createUser(null, "Marvin", "123", "123"));
        Marvin = um.getUserByName("Marvin");

        Assertions.assertDoesNotThrow(() -> sm.addSound(Marvin, "Blauwal Ballade", new File("src/main/resources/data/sounds/example/9750300N.wav")));
        Assertions.assertDoesNotThrow(() -> sm.addSound(Marvin, "Pottwal Pop", new File("src/main/resources/data/sounds/example/78018003.wav")));
    }


    @Test
    @Description("Tests the creation of a playlist")
    public void testCreatePlaylist() {
        Assertions.assertDoesNotThrow(() -> pm.createPlaylist(Marvin, null, "Ocean Groove"));
        Playlist testPlaylist = Assertions.assertDoesNotThrow(() -> pm.getPlaylistByName("Ocean Groove"));

        //Check if the getters in PlaylistManager work
        Assertions.assertEquals(pm.getPlaylistsByUser(Marvin).get(0), testPlaylist);
        Assertions.assertEquals(pm.getPlaylistByID(testPlaylist.getPlaylistID()), testPlaylist);

        //Check if the default profile picture is set
        Assertions.assertEquals(UserManager.getInstance().getDEFAULT_PICTURE().getPath(), Marvin.getProfilePicture().getPath());
    }

    @Test
    @Description("Tests the adding of sounds to a playlist")
    public void testAddSoundToPlaylist() {
        Assertions.assertDoesNotThrow(() -> pm.createPlaylist(Marvin, null, "Ocean Groove"));
        Playlist testPlaylist = Assertions.assertDoesNotThrow(() -> pm.getPlaylistByName("Ocean Groove"));

        Assertions.assertDoesNotThrow(() -> pm.getPlaylistByName("Ocean Groove").addSound(Marvin, sm.getAllSoundsByUser(Marvin).get(0)));
        Assertions.assertDoesNotThrow(() -> pm.getPlaylistByName("Ocean Groove").addSound(Marvin, sm.getAllSoundsByUser(Marvin).get(1)));

        Assertions.assertEquals(testPlaylist.getSounds().get(0), sm.getAllSoundsByUser(Marvin).get(0));
        Assertions.assertEquals(testPlaylist.getSounds().get(1), sm.getAllSoundsByUser(Marvin).get(1));
    }

    @Test
    @Description("Tests the removal of sounds from a playlist")
    public void removeSoundFromPlaylist() {
        testAddSoundToPlaylist();

        Assertions.assertDoesNotThrow(() -> pm.getPlaylistByName("Ocean Groove").removeSound(sm.getAllSoundsByUser(Marvin).get(0)));
        Assertions.assertDoesNotThrow(() -> pm.getPlaylistByName("Ocean Groove").removeSound(sm.getAllSoundsByUser(Marvin).get(1)));
        Assertions.assertEquals(pm.getPlaylistByName("Ocean Groove").getSounds().size(), 0);
    }

    @Test
    @AfterEach
    @Description("Deletes the playlists after each test")
    public void teardownPlaylists() {
        Assertions.assertDoesNotThrow(() -> pm.getAllPlaylists().forEach(playlist -> pm.deletePlaylistByID(playlist.getCreatedBy(), playlist.getPlaylistID())));
    }

    @Disabled
    @AfterAll
    @Description("Deletes the sounds after all tests")
    @DisplayName("Disabled because the problem with process allocating the file is not fixed yet")
    public static void teardownSounds() {
        //Assertions.assertDoesNotThrow(() -> sm.getAllSounds().forEach(sound -> sm.deleteSoundByID(sound.getUploadedBy(), sound.getSoundID())));
    }

    @AfterAll
    @Description("Deletes the test users after all tests")
    public static void teardownUsers() {
        Assertions.assertDoesNotThrow(() -> um.getAllUsers().forEach(user -> um.deleteUser("123", "123")));
    }
}
