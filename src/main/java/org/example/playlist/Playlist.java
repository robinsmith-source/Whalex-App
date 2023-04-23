package org.example.playlist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;
import org.example.profile.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Playlist class displays the playlist with its ID, name, list of songs, number of songs, user who created the playlist and date when the playlist was created.
 *
 * @link ISound (1 to many relation : Each playlist can have many sounds)
 * @link User (1 to 1 relation : Each playlist is created by one user via their PlaylistManager)
 * TODO: Write tests for the Playlist
 */
public class Playlist {
    private static final Logger log = LogManager.getLogger(Playlist.class);

    /**
     * counter of the playlistID
     */
    private static int counterID = 10000;

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
     *
     * @link ISound
     */
    private Set<ISound> sounds;

    /**
     * Number of songs in the playlist
     */
    private int numberOfSongs;

    /**
     * User who created the playlist
     */
    private final User createdBy;

    /**
     * Date when the playlist was created
     */
    private final Date createdAt;


    /**
     * @param name Name of the playlist
     */
    public Playlist(String name, User createdBy) {
        this.playlistID = counterID++;
        this.name = name;
        this.sounds = new HashSet<>();
        this.createdBy = createdBy;
        this.createdAt = new Date();
    }

    /**
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
     * @return ID of the playlist
     * TODO: Check if this method is necessary (Maybe it is better to use a second map to iterate through playlists via the ID)
     */
    public int getPlaylistID() {
        return playlistID;
    }

    /**
     * @return Name of the playlist
     * TODO: Check if this method is necessary (Maybe it is better to use the Map from the PlaylistManager to iterate through playlists via the name)
     */
    public String getName() {
        return name;
    }

    /**
     * @return List of sounds in the playlist
     */
    public ArrayList<ISound> getSounds() {
        return new ArrayList<>(sounds);
    }

    /**
     * @return Number of sounds in the playlist
     */
    public int getNumberOfSounds() {
        return sounds.size();
    }

    /**
     * @return User who created the playlist
     */
    public User getCreatedBy() {
        return createdBy;
    }

    /**
     * @return Date when the playlist was created
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param sound Sound to be added to the playlist
     * @return true if the sound was added successfully, false if not
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
     * @param sound Sound to be removed from the playlist
     * @return true if the sound was removed successfully, false if not
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
