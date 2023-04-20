package org.example.playlist;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.profile.User;
import org.example.profile.UserManager;

import java.util.ArrayList;

/**
 * Class to get all playlists from all users and summarize them
 * @see PlaylistManager only manages playlists of the user who owns the playlist manager
 * TODO : Optionally add other methods to summarize ALL playlists e.g. search for a playlist by name
 */
public class PlaylistSummary {

    private static final Logger log = LogManager.getLogger(PlaylistSummary.class);

     /**
     * @return ArrayList of all playlists existing from all users
     */
    public static ArrayList<Playlist> getAllPlaylists() {
        ArrayList<Playlist> allPlaylists = new ArrayList<>();
        for (User user : UserManager.getAllUsers()) {
            allPlaylists.addAll(user.getPlaylistManager().getPlaylistsByUser());
        }
        return allPlaylists;
    }
}
