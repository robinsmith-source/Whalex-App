package org.example.playlist;

import org.example.media.interfaces.ISound;
import org.example.profile.User;

import java.util.Date;
import java.util.Map;
import java.util.Set;

public class Playlist {

    //Name of the playlist
    private String name;

    //Soundset of all sounds in the playlist
    Set<ISound> sounds;

    //Number of songs in the playlist
    private int numberOfSongs;

    //User who created the playlist
    private User createdBy;

    //Date when the playlist was created
    private Date createdAt;

    /**
     * @param name Name of the playlist
     */
    public Playlist(String name, Set<ISound> sounds) {
        this.name = name;
        this.sounds = sounds;
        this.createdAt = new Date();
    }

    /**
     * @param name Name of the playlist
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Name of the playlist
     */
    public String getName() {
        return name;
    }

    /**
     * @return Number of songs in the playlist
     */
    public int getNumberOfSongs() {
        return numberOfSongs;
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
}
