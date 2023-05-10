package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.ReadDataException;
import org.example.exceptions.WriteDataException;
import org.example.media.SoundManager;
import org.example.playlist.Playlist;
import org.example.playlist.PlaylistManager;
import org.example.profile.User;
import org.example.profile.UserManager;

import java.io.File;
import java.io.IOException;

public class TestRootFile {
    private static final Logger log = LogManager.getLogger(TestRootFile.class);

    public static void main(String[] args) throws ReadDataException, WriteDataException, IOException {
        writeUser1();
        readUser1();

    /*
       User user = UserManager.getUserByName("root");
         PlaylistManager playlistManager1 = new PlaylistManager(user);
            SoundManager soundManager1 = new SoundManager(user);

            soundManager1.soundsFromJSON();
            playlistManager1.playlistsFromJSON();

        System.out.println(SoundManager.getAllSoundsByUser(UserManager.getUserByName("robin")));



        /*
       UserManager.usersFromJSON();
        User user = UserManager.getUserByName("root");
        PlaylistManager playlistManager1 = new PlaylistManager(user);
        SoundManager soundManager1 = new SoundManager(user);

        soundManager1.soundsFromJSON();
        playlistManager1.playlistsFromJSON();
        SoundManager.getAllSounds().forEach((s, iSound) -> System.out.println(iSound.getTitle()));

        */
    }

    public static void writeUser1() throws WriteDataException {
        //User 1 - root
        UserManager.createUser("root", "hallo1234");
        User user = UserManager.getUserByName("root");
        PlaylistManager playlistManager1 = new PlaylistManager(user);
        SoundManager soundManager1 = new SoundManager(user);

        soundManager1.addSound("Walsound 1", new File("src/main/resources/data/sounds/9750300N.wav"));
        soundManager1.addSound("Walsound 2", new File("src/main/resources/data/sounds/89405023.wav"));
        playlistManager1.createPlaylist("Walsounds");
        Playlist p = PlaylistManager.getPlaylistByName("Walsounds");
        p.addSound(SoundManager.getAllSoundsByUser(user).get(0));

        UserManager.userToJSON();
        SoundManager.soundsToJSON();
        PlaylistManager.playlistsToJSON();
    }

    public static void writeUser2() throws WriteDataException {
        //User 1 - root
        UserManager.createUser("robin", "hallo1234");
        User user = UserManager.getUserByName("robin");
        PlaylistManager playlistManager2 = new PlaylistManager(user);
        SoundManager soundManager2 = new SoundManager(user);

        soundManager2.addSound("Walsound 3", new File("src/main/resources/data/sounds/72021005.wav"));
        soundManager2.addSound("Walsound 4", new File("src/main/resources/data/sounds/78018003.wav"));
        playlistManager2.createPlaylist(" by robin");
        Playlist p = PlaylistManager.getPlaylistByName(" by robin");
        p.addSound(SoundManager.getAllSoundsByUser(user).get(1));
        p.addSound(SoundManager.getAllSoundsByUser(user).get(0));

        UserManager.userToJSON();
        SoundManager.soundsToJSON();
        PlaylistManager.playlistsToJSON();
    }

    public static void readUser1() throws ReadDataException {
        UserManager.usersFromJSON();
        User user = UserManager.getUserByName("root");
        show(user);

    }

    public static void readUser2() throws ReadDataException {
        UserManager.usersFromJSON();
        User user = UserManager.getUserByName("robin");
        show(user);
    }

    private static void show(User user) throws ReadDataException {
        SoundManager soundManager = new SoundManager(user);
        PlaylistManager playlistManager = new PlaylistManager(user);

        SoundManager.soundsFromJSON();
        PlaylistManager.playlistsFromJSON();

        System.out.println(user.getUsername() + " | " + SoundManager.getAllSoundsByUser(user));
        System.out.println(user.getUsername() + " | " + PlaylistManager.getPlaylistsByUser(user));
    }
}
