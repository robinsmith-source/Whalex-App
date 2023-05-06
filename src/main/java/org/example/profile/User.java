package org.example.profile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.UUID;

/**
 * User class displays the user's profile with UUID, username, password, profile picture.
 *
 * @see UUID
 * @see File
 */
// TODO : Write tests for the User
public class User {
    private static final Logger log = LogManager.getLogger(User.class);

    /**
     * ID of the user
     */
    private final String userID;

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
     * Constructor of the User class
     *
     * @param username Username of the user
     * @param password Password of the user
     */
    public User(String userID, File profilePicture, String username, String password) {
        this.userID = userID;
        this.profilePicture = profilePicture;
        this.username = username;
        this.password = password;
    }

    /**
     * Method to get the ID of the user
     *
     * @return userID of the user
     */
    public String getUserID() {
        return this.userID;
    }

    /**
     * Method to get the profile picture of the user
     *
     * @return profilePicture of the user
     */
    public File getProfilePicture() {
        return this.profilePicture;
    }

    /**
     * Method to get the username of the user
     *
     * @return username of the user
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Method to get the password of the user
     *
     * @return password of the user
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Method to set the profile picture of the user
     *
     * @param profilePicture Profile picture of the user
     * @return true if the profile picture is set, false if not
     */
    public boolean setProfilePicture(File profilePicture) {
        if (profilePicture == null) {
            log.error("Path of new profilePicture is null.");
            return false;
        }
        this.profilePicture = profilePicture;
        return true;
    }

    /**
     * Method to change the password of the user
     *
     * @param oldPassword             Old password of the user
     * @param newPassword             New password of the user
     * @param newPasswordConfirmation New password confirmation of the user
     * @return true if the password is changed, false if not
     */
    public boolean changePassword(String oldPassword, String newPassword, String newPasswordConfirmation) {
        if (!this.password.equals(oldPassword)) {
            log.error("Old password is incorrect.");
            return false;
        } else if (newPassword == null || newPassword.isEmpty()) {
            log.error("New password is null or empty.");
            return false;
        } else if (!newPassword.equals(newPasswordConfirmation)) {
            log.error("New password and new password confirmation are different.");
            return false;
        }
        this.password = newPassword;
        return true;
    }

}
