package org.example.media.extentions;

import javafx.scene.media.Media;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;
import org.example.profile.User;

/**
 * Extention for ISound Interface
 * Thought as uploadable soundfile from a user
 */
public class Soundfile implements ISound {
    private static final Logger log = LogManager.getLogger(Soundfile.class);

    /**
     * @return User who uploaded the sound
     */
    public User uploadedBy() { return null; }

    /**
     * @return Title of the sound
     */
    @Override
    public String getTitle() {
        return null;
    }

    /**
     * @return Filepath to the sound
     */
    @Override
    public Media mediaPath() {
        return null;
    }
}
