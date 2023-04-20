package org.example.profile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.playlist.PlaylistManager;

import java.io.File;

/**
 * User class displays the user's profile with his/her username, password, profile picture and playlist manager.
 * @link PlaylistManager (1 to 1 relation : Each user has one PlaylistManager to manage his/her playlists)
 * TODO: Write tests for the User
 */
public class User {
    private static final Logger log = LogManager.getLogger(User.class);

    /**
     * counter of the userID
     */
    private static int counterID = 20000;

    /**
     * ID of the user
     */
    private final int userID;

    /**
     * Profile picture of the user
     */
    private File profilePicture;

    /**
     * Username of the user
     */
    private String username;

    /**
     * Password of the user
     */
    private String password;

    /**
     * Playlist manager of the user
     * @link PlaylistManager
     */
    private final PlaylistManager playlistManager;

    /**
     * @param username Username of the user
     * @param password Password of the user
     */
    public User(String username, String password) {
        this.userID = counterID++;
        this.username = username;
        this.password = password;
        this.playlistManager = new PlaylistManager(this);
    }

    /**
     * @return userID of the user
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @return username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return PlaylistManager of the user
     */
    public PlaylistManager getPlaylistManager() {
        return playlistManager;
    }

    /**
     * @param profilePicture Profile picture of the user
     * @return true if the profile picture is set, false if not
     */
    public boolean setProfilePicture(File profilePicture) {
        if (profilePicture == null) {
            log.error("Profile picture is null.");
            return false;
        }
        this.profilePicture = profilePicture;
        return true;
    }

    /**
     * @param oldPassword Old password of the user
     * @param newPassword New password of the user
     * @return true if the password is changed, false if not
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (!this.password.equals(oldPassword)) {
            log.error("Old password is incorrect.");
            return false;
        } else if (newPassword == null || newPassword.isEmpty()) {
            log.error("New password is null or empty.");
            return false;
        }
        this.password = newPassword;
        return true;
    }
}
