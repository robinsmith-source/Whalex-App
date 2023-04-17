package org.example.media.extentions;

import org.example.media.interfaces.ISound;
import org.example.profile.User;

import javafx.scene.media.Media;

/**
 * Extention for ISound Interface
 * Thought as uploadable soundfile from a user
 */
public class Soundfile implements ISound {

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

    /**
     * @return Duration of the sound in seconds
     */
    @Override
    public int getDuration() {
        return 0;
    }
}
