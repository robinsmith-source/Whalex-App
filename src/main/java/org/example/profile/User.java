package org.example.profile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
     */
    public void setProfilePicture(File profilePicture) {
        Path oldProfilePicture = Path.of(this.profilePicture.toURI());
        if (profilePicture == null)
            throw new IllegalArgumentException("Profile picture cannot be null when manually set.");


        String extension = profilePicture.getName().substring(profilePicture.getName().lastIndexOf("."));
        Path targetFile = UserManager.getInstance().getPROFILE_PICTURES().resolve(this.getUserID() + extension);

        System.out.println(oldProfilePicture.toFile());
        if (!oldProfilePicture.toFile().toString().contains("profilePictures\\default\\")) {
            try {
                Files.deleteIfExists(oldProfilePicture);
                log.info("User profile picture {} of user {} has been deleted.", targetFile, this.getUsername());
            } catch (IOException e) {
                log.error("User profile picture {} couldn't be deleted", targetFile);
            }
        }

        try {
            Files.copy(profilePicture.toPath(), targetFile);
            log.info("User profile picture {} of user {} has been copied.", targetFile, this.getUsername());
        } catch (IOException e) {
            log.error("User profile picture {} couldn't be copied", targetFile);
        }

        this.profilePicture = targetFile.toFile();
        log.info("Profile picture successfully changed to {}.", targetFile);
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        User otherUser = (User) obj;
        return username.equals(otherUser.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
