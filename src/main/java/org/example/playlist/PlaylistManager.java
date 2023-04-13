package org.example.playlist;

import java.util.HashMap;
import java.util.Map;

public class PlaylistManager {
    private final Map<String, Playlist> playlists;

    /**
     * Constructor for the PlaylistManager
     */
    public PlaylistManager() {
        playlists = new HashMap<>();
    }

    /**
     * @param playlist Playlist to be added to the PlaylistManager
     * TODO : Connect with User (Playlists are created by different Users and are displayed in/with the Users Profile)
     */
    public void createPlaylist(Playlist playlist) {}

    /**
     * @param playlist Playlist to be removed from the PlaylistManager
     * TODO : Connect with User (Only the playlist creator can delete the playlist)
     */
    public void deletePlaylist(Playlist playlist) {}

    /**
     * @return Map of all playlists in the PlaylistManager
     */
    public Map<String, Playlist> getPlaylists() {
        return playlists;
    }
}
