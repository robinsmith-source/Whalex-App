package org.example;

import org.example.media.SoundManager;
import org.example.playlist.PlaylistManager;
import org.example.profile.User;
import org.example.profile.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

public class UserManagerTest {

    final UserManager um = UserManager.getInstance();
    final SoundManager sm = SoundManager.getINSTANCE();
    final PlaylistManager pm = PlaylistManager.getInstance();

    //TODO: Remove and add a relative resource path to the Managers
    @BeforeEach
    public void setup() {
        um.setSAVE_FILE(new File("src/test/resources/saves/users.json"));
        sm.setSAVE_FILE(new File("src/test/resources/saves/sounds.json"));
        pm.setSAVE_FILE(new File("src/test/resources/saves/playlists.json"));
    }

    @Test
    public void testCreateUser() throws Exception {
        //User 1 - Marvin
        um.createUser(null, "Marvin", "123", "123");
        User Marvin = um.getUserByName("Marvin");

        sm.addSound(Marvin, "Pottwal Pop13454645654", new File("src/main/resources/data/sounds/9750300N.wav"));
        sm.addSound(Marvin, "Glattschweinswal Gesang", new File("src/main/resources/data/sounds/89405023.wav"));
        pm.createPlaylist(Marvin, null, "Ocean Groove");
        pm.getPlaylistByName("Ocean Groove").addAllSounds(Marvin, sm.getAllSoundsByUser(Marvin));

        //User 2 - Robin
        um.createUser(null, "Robin", "123", "123");
        User rootUser = um.getUserByName("Robin");

        sm.addSound(rootUser, "Blauwal Ballade", new File("src/main/resources/data/sounds/72021005.wav"));
        sm.addSound(rootUser, "Rauzahldelfin Rap", new File("src/main/resources/data/sounds/78018003.wav"));
        pm.createPlaylist(rootUser, null, "Deepsea Dive");
        pm.getPlaylistByName("Deepsea Dive").addAllSounds(rootUser, sm.getAllSoundsByUser(rootUser));

        um.usersToJSON();
        sm.soundsToJSON();
        pm.playlistsToJSON();
    }
}
