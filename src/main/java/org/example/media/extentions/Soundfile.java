package org.example.media.extentions;

import javafx.scene.media.Media;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;
import org.example.profile.User;

import java.io.File;


/**
 * Extention for ISound Interface
 * Thought as uploadable soundfile from a user
 */
public class Soundfile implements ISound {
    private static final Logger log = LogManager.getLogger(Soundfile.class);

    /**
     * title of the sound
     */
    private final String title;

    /**
     * media object of the soundfile
     */
    private Media media;

    /**
     * User who uploaded the sound
     */
    private final User uploadedBy;

    /**
     * Constructor for Soundfile
     * @param title Title of the sound
     * @param path Path to the soundfile
     * @param uploadedBy User who uploaded the sound
     * TODO: Check if Exception handling is correct.
     */
    public Soundfile(String title, String path, User uploadedBy) {
        if (title == null) {
            log.error("Title is null");
            throw new NullPointerException("Title is null");
        } else {
            this.title = title;
        }
        try {
            media = new Media(new File(path).toURI().toString());
        } catch (NullPointerException e) {
            log.error("Path is null");
        } catch (Exception e) {
            log.error("Soundfile could not be created");
        }
        log.info("Soundfile created");
        this.uploadedBy = uploadedBy;
    }

    /**
     * @return Title of the sound
     */
    @Override
    public String getTitle() {
        return this.title;
    }

    /**
     * @return Filepath to the sound
     */
    @Override
    public Media getMedia() {
        return this.media;
    }

    /**
     * @return User who uploaded the sound
     */
    public User uploadedBy() {
        return uploadedBy;
    }
}
