package org.example.media.extentions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;
import org.example.profile.User;

/**
 * Factory for Soundfile class
 *
 * @link ISound (creates a Soundfile driven by a users SoundManager)
 */
public class SoundFactory {
    private static final Logger log = LogManager.getLogger(SoundFactory.class);

    /**
     * Creates an implementation of the ISound interface. Included in this project is the Soundfile class.
     *
     * @param title Title of the sound
     * @param path  Path to the soundfile
     * @param uploadedBy User who uploaded the sound
     * @return Soundfile
     * @see Soundfile
     */
    /* TODO: Check if Exception handling is correct. --> Should be done */
    public static ISound createSound(String title, String path, User uploadedBy) {
        try {
            return new Soundfile(title, path, uploadedBy);
        } catch (Exception e) {
            return null;
        }
    }
}
