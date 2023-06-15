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
    private String username;

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

    public void setUsername(String username) {
        if (username == null || username.isEmpty()) {
            log.error("Username is null or empty.");
            throw new IllegalArgumentException("Username is null or empty.");
        } else if (UserManager.getInstance().getAllUsers().stream().anyMatch(user -> user.getUsername().equals(username))) {
            log.error("User {} already exists.", username);
            throw new IllegalArgumentException("User already exists.");
        }
        this.username = username;
    }

    /**
     * Method to set the profile picture of the user
     *
     * @param profilePicture Profile picture of the user
     */
    public void setProfilePicture(File profilePicture) {
        if (profilePicture == null) {
            log.debug("Path of new profilePicture is null.");
            throw new IllegalArgumentException("Path of new profilePicture is null.");
        }
        this.profilePicture = profilePicture;
    }

    /**
     * Method to change the password of the user
     *
     * @param oldPassword             Old password of the user
     * @param newPassword             New password of the user
     * @param newPasswordConfirmation New password confirmation of the user
     */
    public void changePassword(String oldPassword, String newPassword, String newPasswordConfirmation) {
        if (!this.password.equals(oldPassword)) {
            log.debug("Old password is incorrect.");
            throw new IllegalArgumentException("Old password is incorrect.");
        } else if (newPassword == null || newPassword.isEmpty()) {
            log.debug("New password is null or empty.");
            throw new IllegalArgumentException("New password is null or empty.");
        } else if (!newPassword.equals(newPasswordConfirmation)) {
            log.debug("New password and new password confirmation are different.");
            throw new IllegalArgumentException("New password and new password confirmation are different.");
        }
        this.password = newPassword;
    }
}
