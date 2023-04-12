package org.example.playlist;

import org.example.profile.User;

public class Playlist {

    private String name;
    private int numberOfSongs;
    private User createdBy;
    private String createdAt;

    public Playlist(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

}
