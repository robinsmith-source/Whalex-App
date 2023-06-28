package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.SoundManager;
import org.example.playlist.PlaylistManager;
import org.example.profile.User;
import org.example.profile.UserManager;

import java.io.File;

public abstract class InitializeAdminUsers {
    private static final Logger log = LogManager.getLogger(InitializeAdminUsers.class);

    private static final UserManager um = UserManager.getInstance();
    private static final PlaylistManager pm = PlaylistManager.getInstance();
    private static final SoundManager sm = SoundManager.getInstance();

    public static void main(String[] args) throws Exception {
        deleteExisitingFiles();
        writeData();
    }

    private static void deleteExisitingFiles() {
        File file = new File("src/main/resources/data/sounds/");
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isFile() && f.delete()) {
                    log.debug("Deleted file {}.", f.getName());
                }
            }
        }
    }

    public static void writeData() throws Exception {
        //User 1 - Marvin
        um.createUser(null, "Marvin", "123", "123");
        User Marvin = um.getUserByName("Marvin");

        sm.addSound(Marvin, "Pottwal Pop", new File("src/main/resources/data/sounds/example/PottwalPop.wav"));
        sm.addSound(Marvin, "Glattschweinswal Gesang", new File("src/main/resources/data/sounds/example/GlattschweinswalGesang.wav"));
        pm.createPlaylist(Marvin, null, "Ocean Groove");
        pm.getPlaylistByName("Ocean Groove").addAllSounds(Marvin, sm.getAllSoundsByUser(Marvin));

        //User 2 - Robin
        um.createUser(null, "Robin", "123", "123");
        User rootUser = um.getUserByName("Robin");

        sm.addSound(rootUser, "Blauwal Ballade", new File("src/main/resources/data/sounds/example/BlauwalBallade.wav"));
        sm.addSound(rootUser, "Rauzahldelfin Rap", new File("src/main/resources/data/sounds/example/RauzahndelfinRap.wav"));
        pm.createPlaylist(rootUser, null, "Deepsea Dive");
        pm.getPlaylistByName("Deepsea Dive").addAllSounds(rootUser, sm.getAllSoundsByUser(rootUser));
    }
}
