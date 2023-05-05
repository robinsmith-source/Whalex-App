package org.example.playlist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.profile.User;
import org.example.profile.UserManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to get all playlists from all users and summarize them
 * @link UserManager (1 to 1 relation : Each PlaylistSummary has one UserManager to iterate through all users)
 * @link PlaylistManager (1 to n relation : Each PlaylistSummary has many PlaylistManagers to iterate through all playlists created by an user)
 * TODO : Optionally add other methods to summarize ALL playlists e.g. search for a playlist by name
 */
public class PlaylistSummary {

    private static final Logger log = LogManager.getLogger(PlaylistSummary.class);

    /**
     * @return ArrayList of all playlists existing from all users
     */
    public static Map<String, Playlist> getAllPlaylists() {
        Map<String, Playlist> allPlaylists = new HashMap<>();
        for (User user : UserManager.getAllUsers()) {
            allPlaylists.putAll(user.getPlaylistManager().getAllPlaylistsByUser());
        }
        return allPlaylists;
    }
}
