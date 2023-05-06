package org.example.media.extensions;

import javafx.scene.media.Media;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;
import org.example.profile.User;

import java.io.File;


/**
 * Extension for ISound Interface
 *
 * @apiNote Thought as uploadable soundfile from a user
 * @link User (n to 1 : Many sounds can be uploaded by one user)
 * @see Media
 */
public class Soundfile implements ISound {
    private static final Logger log = LogManager.getLogger(Soundfile.class);

    /**
     * ID of the sound
     */
    private final String soundID;

    /**
     * title of the sound
     */
    private final String title;

    /**
     * media object of the soundfile
     */
    private final Media media;

    /**
     * User who uploaded the sound
     */
    private final User uploadedBy;


    /**
     * Constructor for Soundfile
     *
     * @param title      Title of the sound
     * @param path       Path to the soundfile
     * @param uploadedBy User who uploaded the sound
     */
    /* TODO: Check if Exception handling is correct. --> Should be done */
    public Soundfile(String soundID, String title, File path, User uploadedBy) throws Exception {
        if (soundID == null || soundID.isEmpty()) {
            log.error("SoundID cannot be null or empty");
            throw new IllegalArgumentException("SoundID cannot be null or empty");
        }

        if (title == null || title.isEmpty()) {
            log.error("Title cannot be null or empty");
            throw new IllegalArgumentException("Title cannot be null or empty");
        }

        if (uploadedBy == null) {
            log.error("User cannot be null");
            throw new IllegalArgumentException("User cannot be null");
        }

        try {
            media = new Media(path.toURI().toString());
        } catch (Exception e) {
            log.error("Soundfile could not be created: " + e.getMessage());
            throw new Exception("Soundfile could not be created");
        }

        this.soundID = soundID;
        this.title = title;
        this.uploadedBy = uploadedBy;
        log.info("Soundfile " + title + " created by " + uploadedBy.getUsername());
    }


    /**
     * Method to get the soundID of the sound
     *
     * @return soundID of the sound
     */
    @Override
    public String getSoundID() {
        return this.soundID;
    }

    /**
     * Method to get the title of the sound
     *
     * @return Title of the sound
     */
    @Override
    public String getTitle() {
        return this.title;
    }

    /**
     * Method to get the Media object of the soundfile
     *
     * @return Filepath to the sound
     */
    @Override
    public Media getMedia() {
        return this.media;
    }

    /**
     * Method to get the user who uploaded the sound
     *
     * @return User who uploaded the sound
     */
    public User getUploadedBy() {
        return this.uploadedBy;
    }
}
