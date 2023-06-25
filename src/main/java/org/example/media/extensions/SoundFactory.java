package org.example.media.extensions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;
import org.example.profile.User;

import java.io.File;

/**
 * Factory for Soundfile class
 *
 * @link ISound (creates a Soundfile driven by a users SoundManager)
 */
public class SoundFactory {
    private static final Logger log = LogManager.getLogger(SoundFactory.class);

    /**
     * SoundFactory instance
     */
    private static final SoundFactory soundFactory = new SoundFactory();

    /**
     * Constructor of the SoundFactory
     */
    private SoundFactory() {
    }

    /**
     * Returns the SoundFactory instance
     * @return SoundFactory
     */
    public static SoundFactory getInstance() {
        return soundFactory;
    }

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
    public ISound createSound(String soundID, String title, File path, User uploadedBy) {
        try {
            log.debug("Sound {} created by user {} with path {}", soundID, uploadedBy.getUsername(), path);
            return new Soundfile(soundID, title, path, uploadedBy);
        } catch (Exception e) {
            log.error("Sound {} could not be created by user {} with path {}", soundID, uploadedBy.getUsername(), path);
            throw new IllegalArgumentException("Sound " + soundID + " could not be created by user " + uploadedBy.getUsername() + " with path " + path);
        }
    }
}
