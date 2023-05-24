package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.ReadDataException;
import org.example.exceptions.WriteDataException;
import org.example.media.SoundManager;
import org.example.playlist.PlaylistManager;
import org.example.profile.User;
import org.example.profile.UserManager;

import java.io.File;
import java.io.IOException;

public class TestRootFile {
    private static final Logger log = LogManager.getLogger(TestRootFile.class);

    private static final UserManager um = UserManager.getInstance();
    private static final PlaylistManager pm = PlaylistManager.getInstance();
    private static final SoundManager sm = SoundManager.getInstance();

    public static void main(String[] args) throws ReadDataException, WriteDataException, IOException {
        /*loadAll();
        readUser("Robin");
        readUser("Marvin");

         */
        writeMarvin();
        writeRobin();
    }

    public static void writeMarvin() throws WriteDataException {
        //User 1 - Marvin
        um.createUser(null, "Marvin", "123", "123");
        User rootUser = um.getUserByName("Marvin");

        sm.addSound(rootUser, "Pottwal Pop", new File("src/main/resources/data/sounds/9750300N.wav"));
        sm.addSound(rootUser, "Glattschweinswal Gesang", new File("src/main/resources/data/sounds/89405023.wav"));
        pm.createPlaylist(rootUser, null,"Ocean Groove");
        pm.getPlaylistByName("Ocean Groove").addAllSounds(sm.getAllSoundsByUser(rootUser));

        um.userToJSON();
        sm.soundsToJSON();
        pm.playlistsToJSON();
    }

    public static void writeRobin() throws WriteDataException {
        //User 1 - Robin
        um.createUser(null, "Robin", "test4321", "test4321");
        User rootUser = um.getUserByName("Robin");

        sm.addSound(rootUser, "Blauwal Ballade", new File("src/main/resources/data/sounds/72021005.wav"));
        sm.addSound(rootUser, "Rauzahldelfin Rap", new File("src/main/resources/data/sounds/78018003.wav"));
        pm.createPlaylist(rootUser, null, "Deepsea Dive");
        pm.getPlaylistByName("Deepsea Dive").addAllSounds(sm.getAllSoundsByUser(rootUser));

        um.userToJSON();
        sm.soundsToJSON();
        pm.playlistsToJSON();
    }

    public static void readUser(String username) {
        User user = um.getUserByName(username);
        System.out.println(user.getUsername() + " | " + sm.getAllSoundsByUser(user));
        System.out.println(user.getUsername() + " | " + pm.getPlaylistsByUser(user));
    }

    public static void loadAll() {
        try {
            um.usersFromJSON();
            sm.soundsFromJSON();
            pm.playlistsFromJSON();
        } catch (ReadDataException e) {
            log.error("Error while loading data from JSON files", e);
        }
    }
}
