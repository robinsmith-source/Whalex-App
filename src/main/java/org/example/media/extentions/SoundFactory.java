package org.example.media.extentions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;
import org.example.profile.User;

/**
 * Factory for Soundfile class
 * @link ISound (creates a Soundfile driven by a users SoundManager)
 */
public class SoundFactory {
    private static final Logger log = LogManager.getLogger(SoundFactory.class);

    /**
     * Creates an implementation of the ISound interface. Included in this project is the Soundfile class.
     * @see Soundfile
     * @param title Title of the sound
     * @param path Path to the soundfile
     * @return Soundfile
     * TODO: Check if Exception handling is correct.
     */
    public static ISound createSound(String title, String path, User uploadedBy) throws NullPointerException {
        try {
            return new Soundfile(title, path, uploadedBy);
        } catch (Exception e) {
            log.error("Error creating Soundfile", e);
            throw new NullPointerException("Soundfile could not be created");
        }
    }
}
