package org.example.profile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * User class displays the user's profile with his/her username, password, profile picture, PlaylistManager and SoundManager.
 *
 * @link SoundManager (1 to 1 relation : Each User has one SoundManager to manage his/her sounds)
 * @link PlaylistManager (1 to 1 relation : Each User has one PlaylistManager to manage his/her playlists)
 * TODO : Write tests for the User
 * @see File
 */
public class User {
    private static final Logger log = LogManager.getLogger(User.class);

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
    private final String username;

    /**
     * Password of the user
     */
    private String password;

    /**
     * PlaylistManager of the user
     *
     * @link PlaylistManager
     */
    private final PlaylistManager playlistManager;

    /**
     * SoundManager of the user
     *
     * @link SoundManager
     */
    private final SoundManager soundManager;

    /**
     * Constructor of the User class
     *
     * @param username Username of the user
     * @param password Password of the user
     */
    public User(int userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.playlistManager = new PlaylistManager(this);
        this.soundManager = new SoundManager(this);
    }

    /**
     * Method to get the ID of the user
     *
     * @return userID of the user
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Method to get the username of the user
     *
     * @return username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Method to get the PlaylistManager of the user
     *
     * @return PlaylistManager of the user
     */
    public PlaylistManager getPlaylistManager() {
        return playlistManager;
    }

    /**
     * Method to get the SoundManager of the user
     *
     * @return SoundManager of the user
     */
    public SoundManager getSoundManager() {
        return soundManager;
    }

    /**
     * Method to set the profile picture of the user
     *
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
     * Method to change the password of the user
     *
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

    /**
     * Method to get the profile picture of the user
     *
     * @return profilePicture of the user
     */
    public File getProfilePicture() {
        return this.profilePicture;
    }
}
