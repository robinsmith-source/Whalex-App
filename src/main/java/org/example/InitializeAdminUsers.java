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

public class InitializeAdminUsers {
    private static final Logger log = LogManager.getLogger(InitializeAdminUsers.class);

    private static final UserManager um = UserManager.getInstance();
    private static final PlaylistManager pm = PlaylistManager.getInstance();
    private static final SoundManager sm = SoundManager.getINSTANCE();

    public static void main(String[] args) throws ReadDataException, WriteDataException, IOException {
        writeData();
        //readData();
        //showDataFromUser("Robin");
        //showDataFromUser("Marvin");
    }

    public static void writeData() {
        //User 1 - Marvin
        um.createUser(null, "Marvin", "123", "123");
        User Marvin = um.getUserByName("Marvin");

        sm.addSound(Marvin, "Pottwal Pop", new File("src/main/resources/data/sounds/9750300N.wav"));
        sm.addSound(Marvin, "Glattschweinswal Gesang", new File("src/main/resources/data/sounds/89405023.wav"));
        pm.createPlaylist(Marvin, null,"Ocean Groove");
        pm.getPlaylistByName("Ocean Groove").addAllSounds(Marvin, sm.getAllSoundsByUser(Marvin));

        //User 2 - Robin
        um.createUser(null, "Robin", "123", "123");
        User rootUser = um.getUserByName("Robin");

        sm.addSound(rootUser, "Blauwal Ballade", new File("src/main/resources/data/sounds/72021005.wav"));
        sm.addSound(rootUser, "Rauzahldelfin Rap", new File("src/main/resources/data/sounds/78018003.wav"));
        pm.createPlaylist(rootUser, null, "Deepsea Dive");
        pm.getPlaylistByName("Deepsea Dive").addAllSounds(rootUser, sm.getAllSoundsByUser(rootUser));

        try {
            um.usersToJSON();
            sm.soundsToJSON();
            pm.playlistsToJSON();
        } catch (WriteDataException e) {
            log.error("Error while writing data to JSON files", e);
        }
    }

    public static void readData() throws ReadDataException {
        try {
            um.usersFromJSON();
            sm.soundsFromJSON();
            pm.playlistsFromJSON();
        } catch (ReadDataException e) {
            log.error("Error while loading data from JSON files", e);
        }
    }

    public static void showDataFromUser(String username) {
        User user = um.getUserByName(username);
        System.out.println(user.getUsername() + " | " + sm.getAllSoundsByUser(user));
        System.out.println(user.getUsername() + " | " + pm.getPlaylistsByUser(user));
    }
}
