package org.example.profile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.extentions.SoundFactory;
import org.example.media.interfaces.ISound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * SoundManager manages all sounds of the user and provides methods to e.g. create, delete and get sounds with a given ID or name.
 *
 * @link User (1 to 1 relation : Each Soundmanager is owned by a User)
 * @link ISound (1 to n relation : Each Soundmanager manages many sounds)
 * TODO: Write tests for the PlaylistManager
 * TODO: Implement more methods to get Sounds like in
 */
public class SoundManager {
    private static final Logger log = LogManager.getLogger(SoundManager.class);

    /**
     * Map of all sounds of the user (key = title, value = sound)
     */
    private final Map<String, ISound> sounds;

    /**
     * User who uploaded the sounds
     */
    private final User uploadedBy;

    /**
     * Constructor for the SoundManager
     *
     * @param uploadedBy User who uploaded the sounds
     */
    public SoundManager(User uploadedBy) {
        sounds = new HashMap<>();
        this.uploadedBy = uploadedBy;
    }

    /**
     * Method to add a sound to the SoundManager
     *
     * @param title Title of the sound
     * @param path  Path to the soundfile
     * @return True if sound was added successfully, false if not
     * TODO: Check if Exception handling is correct
     */
    public boolean addSound(String title, String path) {
        try {
            this.sounds.put(title, SoundFactory.createSound(title, path, uploadedBy));
        } catch (Exception e) {
            log.error("Sound could not be added", e);
            return false;
        }
        return sounds.containsKey(title);
    }

    /**
     * Method to remove a sound from the SoundManager
     *
     * @param title Title of the sound
     * @return True if sound was removed successfully, false if not
     */
    public boolean removeSoundByTitle(String title) {
        if (this.sounds.remove(title) == null) {
            log.error("Playlist " + title + " could not be deleted.");
            return false;
        }
        log.info("Playlist " + title + " has been deleted.");
        return true;
    }

    /**
     * Method to get all sounds of the user
     *
     * @return ArrayList of all sounds of the user
     */
    public ArrayList<ISound> getSoundsByUser() {
        return new ArrayList<>(this.sounds.values());
    }
}
