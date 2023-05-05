package org.example.profile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.playlist.Playlist;

import java.util.HashMap;
import java.util.Map;

/**
 * PlaylistManager manages all playlists of the user and provides methods to e.g. create, delete and get playlists with a given ID or name.
 *
 * @link User (1 to 1 relation : Each PlaylistManager is created by one User and is displayed in/with the User's Profile)
 * @link Playlist (1 to n relation : Each PlaylistManager manages many playlists)
 * TODO : Write tests for the PlaylistManager
 */
public class PlaylistManager {
    private static final Logger log = LogManager.getLogger(PlaylistManager.class);

    /**
     * counter of the playlistID
     */
    private static int counterID = 10000;

    /**
     * Map of all playlists of the user (key = playlistName, value = playlist)
     */
    private final Map<String, Playlist> playlists;

    /**
     * User who created the playlists
     */
    private final User createdBy;

    /**
     * Constructor for the PlaylistManager
     */
    public PlaylistManager(User createdBy) {
        this.playlists = new HashMap<>();
        this.createdBy = createdBy;
    }

    /**
     * Method to create a new playlist with a given name
     *
     * @param playlistName Name of the playlist to be returned
     * @return true if the playlist has been created, false if not
     * DONE : Connect with User (Playlists are created by different Users and are displayed in/with the Users Profile)
     */
    public boolean createPlaylist(String playlistName) {
        this.playlists.put(playlistName, new Playlist(++counterID, playlistName, createdBy));
        return playlists.containsKey(playlistName);
    }

    /**
     * Method to delete a playlist by its ID
     *
     * @param playlistID ID of the playlist to be removed from the PlaylistManager
     * @return true if the playlist has been deleted, false if not
     * TODO: Check if this method is necessary
     */
    public boolean deletePlaylistByID(int playlistID) {
        if (this.playlists.remove(getPlaylistbyID(playlistID).getName()) == null) {
            log.error("Playlist " + playlistID + " could not be deleted.");
            return false;
        }
        log.info("Playlist " + playlistID + " has been deleted.");
        return true;
    }

    /**
     * Method to delete a playlist by its name
     *
     * @param playlistName Name of the playlist to be removed from the PlaylistManager
     * @return true if the playlist has been deleted, false if not
     * TODO: Check if this method is necessary
     */
    public boolean deletePlaylistByName(String playlistName) {
        if (this.playlists.remove(playlistName) == null) {
            log.error("Playlist " + playlistName + " could not be deleted.");
            return false;
        }
        log.info("Playlist " + playlistName + " has been deleted.");
        return true;
    }

    /**
     * Method to get a playlist by its ID
     *
     * @param playlistID ID of the playlist to be returned
     * @return Playlist with the given ID or null if no playlist with the given ID exists
     * TODO: Check if this method is necessary
     */
    public Playlist getPlaylistbyID(int playlistID) {
        for (Playlist playlist : playlists.values()) {
            if (playlist.getPlaylistID() == playlistID) {
                return playlist;
            }
        }
        return null;
    }

    /**
     * Method to get a playlist by its name
     *
     * @param playlistName Name of the playlist to be returned
     * @return Playlist with the given name
     * TODO: Check if this method is necessary
     */
    public Playlist getPlaylistByName(String playlistName) {
        return playlists.get(playlistName);
    }

    /**
     * Method to get all playlists created by the user
     *
     * @return ArrayList of all playlists created by the user
     */
    public Map<String, Playlist> getAllPlaylistsByUser() {
        return playlists;
    }
}
