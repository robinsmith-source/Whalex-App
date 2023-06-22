package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.data.DataOperation;
import org.example.data.DataThread;
import org.example.data.DataType;
import org.example.media.SoundManager;
import org.example.playlist.PlaylistManager;
import org.example.profile.User;
import org.example.profile.UserManager;

import java.io.File;

public class InitializeAdminUsers {
    private static final Logger log = LogManager.getLogger(InitializeAdminUsers.class);

    private static final UserManager um = UserManager.getInstance();
    private static final PlaylistManager pm = PlaylistManager.getInstance();
    private static final SoundManager sm = SoundManager.getInstance();

    public static void main(String[] args) throws Exception {
        writeData();
        //readData();
        //showDataFromUser("Robin");
        //showDataFromUser("Marvin");
    }

    public static void writeData() throws Exception {
        //User 1 - Marvin
        um.createUser(null, "Marvin", "123", "123");
        User Marvin = um.getUserByName("Marvin");

        sm.addSound(Marvin, "Pottwal Pop", new File("src/main/resources/data/sounds/example/9750300N.wav"));
        sm.addSound(Marvin, "Glattschweinswal Gesang", new File("src/main/resources/data/sounds/example/89405023.wav"));
        sm.addSound(Marvin, "Geht nicht", new File("src/main/resources/data/sounds/example/3e745b2e-9025-4e1d-8d4b-353ff019af36.mp3"));
        pm.createPlaylist(Marvin, null,"Ocean Groove");
        pm.getPlaylistByName("Ocean Groove").addAllSounds(Marvin, sm.getAllSoundsByUser(Marvin));

        //User 2 - Robin
        um.createUser(null, "Robin", "123", "123");
        User rootUser = um.getUserByName("Robin");

        sm.addSound(rootUser, "Blauwal Ballade", new File("src/main/resources/data/sounds/example/72021005.wav"));
        sm.addSound(rootUser, "Rauzahldelfin Rap", new File("src/main/resources/data/sounds/example/78018003.wav"));
        pm.createPlaylist(rootUser, null, "Deepsea Dive");
        pm.getPlaylistByName("Deepsea Dive").addAllSounds(rootUser, sm.getAllSoundsByUser(rootUser));

        new DataThread(DataType.USER_SOUND_PLAYLIST, DataOperation.WRITE).start();
    }

    public static void readData() {
        new DataThread(DataType.USER_SOUND_PLAYLIST, DataOperation.READ).start();
    }

    public static void showDataFromUser(String username) {
        User user = um.getUserByName(username);
        System.out.println(user.getUsername() + " | " + sm.getAllSoundsByUser(user));
        System.out.println(user.getUsername() + " | " + pm.getPlaylistsByUser(user));
    }
}
