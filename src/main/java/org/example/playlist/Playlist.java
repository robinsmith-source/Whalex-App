package org.example.playlist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;
import org.example.profile.User;

import java.io.File;
import java.util.*;

/**
 * Playlist class displays the playlist with its ID, name, list of songs, number of songs, user who created the playlist and date when the playlist was created.
 * TODO: Write tests for the Playlist
 * TODO: Write JavaDoc for the Playlist
 * TODO: Add Exception handling + logging
 */
public class Playlist {
    private static final Logger log = LogManager.getLogger(Playlist.class);

    /**
     * ID of the playlist
     */
    private final String playlistID;

    /**
     * Cover of the playlist
     */
    private final File playlistCover;

    /**
     * Name of the playlist
     */
    private final String name;

    /**
     * List of songs in the playlist
     *
     * @see ISound
     */
    private final Set<ISound> SOUNDS;

    /**
     * User who created the playlist
     *
     * @see User
     */
    private final User createdBy;

    /**
     * Constructor of the playlist
     *
     * @param playlistID    ID of the playlist
     * @param playlistCover Cover of the playlist
     * @param name          Name of the playlist
     * @param createdBy     User who created the playlist
     */
    public Playlist(String playlistID, File playlistCover, String name, User createdBy) {
        this.playlistID = playlistID;
        this.playlistCover = playlistCover;
        this.name = name;
        this.SOUNDS = new HashSet<>();
        this.createdBy = createdBy;
    }

    /**
     * Method to get the ID of the playlist
     *
     * @return ID of the playlist
     * TODO: Check if this method is necessary (Maybe it is better to use a second map to iterate through playlists via the ID)
     */
    public String getPlaylistID() {
        return this.playlistID;
    }


    /**
     * Method to get the cover of the playlist
     *
     * @return Cover of the playlist
     */
    public File getPlaylistCover() {
        return this.playlistCover;
    }

    /**
     * Method to get the name of the playlist
     *
     * @return Name of the playlist
     */
    public String getName() {
        return this.name;
    }

    /**
     * Method to get the Arraylist of sounds in the playlist
     *
     * @return Arraylist of sounds in the playlist
     */
    public ArrayList<ISound> getSounds() {
        return new ArrayList<>(this.SOUNDS);
    }

    /**
     * Method to get the number of sounds in the playlist
     * Used to display number of Sounds in TableView
     * @return Number of sounds in the playlist
     */
    public int getNumberOfSounds() {
        return this.SOUNDS.size();
    }

    /**
     * Method to get the user who created the playlist
     *
     * @return User who created the playlist
     * @see User
     */
    public User getCreatedBy() {
        return this.createdBy;
    }

    /**
     * Method to add a sound to the playlist
     *
     * @param activeUser User who is currently logged in
     * @param sound      Sound to be added to the playlist
     * @throws IllegalArgumentException if the sound or the user is null or the active user is not the creator of the playlist
     */
    public void addSound(User activeUser, ISound sound) throws IllegalArgumentException {
        if (sound == null) {
            log.error("Sound cannot be null");
            throw new IllegalArgumentException("Sound cannot be null");
        } else if (this.SOUNDS.contains(sound)) {
            log.error("Sound is already in the playlist");
            throw new IllegalArgumentException("Sound is already in the playlist");
        } else if (activeUser == null) {
            log.error("Active user cannot be null");
            throw new IllegalArgumentException("Active user cannot be null");
        } else if (!activeUser.equals(this.createdBy)) {
            log.error("Active user is not the creator of the playlist");
            throw new IllegalArgumentException("Active user is not the creator of the playlist");
        }
        this.SOUNDS.add(sound);
        log.debug("Sound " + sound.getTitle() + " has been added to playlist " + this.name);
    }

    /**
     * Method to add a list of sounds to the playlist
     *
     * @param activeUser User who is currently logged in
     * @param sounds     List of sounds to be added to the playlist
     * @throws IllegalArgumentException if the list of sounds or the user is null or the active user is not the creator of the playlist
     */
    public void addAllSounds(User activeUser, List<ISound> sounds) throws IllegalArgumentException {
        if (sounds == null) {
            log.error("Sounds cannot be null");
            throw new IllegalArgumentException("Sounds cannot be null");
        } else if (activeUser == null) {
            log.error("Active user cannot be null");
            throw new IllegalArgumentException("Active user cannot be null");
        } else if (!activeUser.equals(this.createdBy)) {
            log.error("Active user is not the creator of the playlist");
            throw new IllegalArgumentException("Active user is not the creator of the playlist");
        }
        this.SOUNDS.addAll(sounds);
        log.info("{} Sounds have been added to playlist {}.", sounds.size(), this.name);
    }

    /**
     * Method to remove a sound from the playlist
     *
     * @param sound Sound to be removed from the playlist
     * @throws IllegalArgumentException if the sound is null
     */
    public void removeSound(ISound sound) throws IllegalArgumentException {
        if (sound == null) {
            log.error("Sound cannot be null");
            throw new IllegalArgumentException("Sound cannot be null");
        }
        this.SOUNDS.remove(sound);
        log.info("Sound {} has been removed from playlist {}.", sound.getTitle(), this.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Playlist otherPlaylist = (Playlist) obj;
        return name.equals(otherPlaylist.name) && createdBy.equals(otherPlaylist.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
