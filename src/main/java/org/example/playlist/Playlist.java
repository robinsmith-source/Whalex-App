package org.example.playlist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;
import org.example.profile.User;

import java.util.*;

/**
 * Playlist class displays the playlist with its ID, name, list of songs, number of songs, user who created the playlist and date when the playlist was created.
 * @link ISound (1 to n relation : Each playlist can have many sounds)
 * @link User (1 to 1 relation : Each playlist is created by one user via their PlaylistManager)
 * TODO: Write tests for the Playlist
 */
public class Playlist {
    private static final Logger log = LogManager.getLogger(Playlist.class);

    /**
     * ID of the playlist
     */
    private final int playlistID;

    /**
     * Name of the playlist
     */
    private String name;

    /**
     * List of songs in the playlist
     * @see ISound
     */
    private final Set<ISound> sounds;

    /**
     * Number of songs in the playlist
     */
    private int numberOfSongs;

    /**
     * User who created the playlist
     * @see User
     */
    private final User createdBy;

    /**
     * Date when the playlist was created
     * @see Date
     */
    private final Date createdAt;


    /**
     * Constructor of the playlist
     * @param name Name of the playlist
     * @param createdBy User who created the playlist
     */
    public Playlist(int playlistID, String name, User createdBy) {
        this.playlistID = playlistID;
        this.name = name;
        this.sounds = new HashSet<>();
        this.createdBy = createdBy;
        this.createdAt = new Date();
    }

    /**
     * Method to set the name of the playlist
     * @param name Name of the playlist
     * @return true if the name of the playlist was set successfully, false if not
     */
    public boolean setName(String name) {
        if (name == null || name.isEmpty()) {
            log.error("Name of the playlist cannot be null or empty");
            return false;
        }
        this.name = name;
        return true;
    }

    /**
     * Method to get the ID of the playlist
     * @return ID of the playlist
     * TODO: Check if this method is necessary (Maybe it is better to use a second map to iterate through playlists via the ID)
     */
    public int getPlaylistID() {
        return this.playlistID;
    }

    /**
     * Method to get the name of the playlist
     * @return Name of the playlist
     * TODO: Check if this method is necessary (Maybe it is better to use the Map from the PlaylistManager to iterate through playlists via the name)
     */
    public String getName() {
        return this.name;
    }

    /**
     * Method to get the map of sounds in the playlist
     * @return Map of sounds in the playlist (key = title, value = sound)
     * @see ISound
     */
    public Map<String, ISound> getSounds() {
        Map<String, ISound> soundMap = new HashMap<>();
        for (ISound sound : this.sounds) {
            soundMap.put(sound.getTitle(), sound);
        }
        return soundMap;
    }

    /**
     * Method to get the number of sounds in the playlist
     * @return Number of sounds in the playlist
     */
    public int getNumberOfSounds() {
        return this.sounds.size();
    }

    /**
     * Method to get the user who created the playlist
     * @return User who created the playlist
     * @see User
     */
    public User getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Method to get the date when the playlist was created
     * @return Date when the playlist was created
     * @see Date
     */
    public Date getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Method to add a sound to the playlist
     * @param sound Sound to be added to the playlist
     * @return true if the sound was added successfully, false if not
     * @see ISound
     */
    public boolean addSound(ISound sound) {
        if (sound == null) {
            log.error("Sound cannot be null");
            return false;
        }
        this.sounds.add(sound);
        log.info("Sound " + sound.getTitle() + " has been added to playlist " + this.name);
        return true;
    }

    /**
     * Method to remove a sound from the playlist
     * @param sound Sound to be removed from the playlist
     * @return true if the sound was removed successfully, false if not
     * @see ISound
     */
    public boolean removeSound(ISound sound) {
        if (sound == null) {
            log.error("Sound cannot be null");
            return false;
        }
        this.sounds.remove(sound);
        log.info("Sound " + sound.getTitle() + " has been removed from playlist " + this.name);
        return true;
    }

}
