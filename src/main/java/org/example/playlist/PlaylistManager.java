package org.example.playlist;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.data.PlaylistSerializer;
import org.example.exceptions.ReadDataException;
import org.example.exceptions.WriteDataException;
import org.example.profile.User;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 * PlaylistManager manages all playlists of the user and provides methods to e.g. create, delete and get playlists in different ways.
 */
//TODO: Write tests for the PlaylistManager
public class PlaylistManager {
    private static final Logger log = LogManager.getLogger(PlaylistManager.class);

    private static final PlaylistManager INSTANCE = new PlaylistManager();
    /**
     * Gson object to convert JSON to Java and Java to JSON
     *
     * @link usersToJSON() (Method to convert Java to JSON)
     * @link usersFromJSON() (Method to convert JSON to Java)
     * @see Gson
     */
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Playlist.class, new PlaylistSerializer())
            .setPrettyPrinting()
            .create();

    /**
     * Path to the file where all playlist data is stored
     */
    private static final File SAVE_FILE = new File("src/main/resources/data/saves/playlists.json");

    /**
     * Map of all playlists of the user (key = playlistName, value = playlist)
     */
    private static final Map<String, Playlist> playlists = new HashMap<>();

    private PlaylistManager() {
    }

    public static PlaylistManager getInstance() {
        return INSTANCE;
    }
    /**
     * Method to get a playlist by its ID
     *
     * @param playlistID ID of the playlist to be returned
     * @return Playlist with the given ID or null if no playlist with the given ID exists
     */
    //Todo: Check if log states are correct -> Should be
    public static Playlist getPlaylistByID(String playlistID) {
        if (playlistID == null || playlistID.isEmpty()) {
            log.warn("PlaylistID is null or empty");
            throw new IllegalArgumentException("PlaylistID cannot be null or empty");
        }
        for (Playlist playlist : playlists.values()) {
            if (playlist.getPlaylistID().equals(playlistID)) {
                log.debug("Playlist with ID {} has been found", playlistID);
                return playlist;
            }
        }
        log.error("Playlist with ID {} could not be found.", playlistID);
        throw new IllegalArgumentException("Playlist with ID " + playlistID + " could not be found.");
    }

    /**
     * Method to get a playlist by its name
     *
     * @param playlistName Name of the playlist to be returned
     * @return Playlist with the given name
     */
    //TODO: Check if this method is necessary
    public static Playlist getPlaylistByName(String playlistName) {
        return playlists.get(playlistName);
    }

    /**
     * Method to get all playlists created by the User
     *
     * @param user User whose playlists should be returned
     * @return ArrayList of all playlists created by the User
     */
    public static ArrayList<Playlist> getPlaylistsByUser(User user) {
        ArrayList<Playlist> playlistsByUser = new ArrayList<>();
        for (Playlist playlist : playlists.values()) {
            if (playlist.getCreatedBy().equals(user)) {
                playlistsByUser.add(playlist);
            }
        }
        return playlistsByUser;
    }

    /**
     * Method to get all playlists
     * @return Map of all playlists (key = playlistName, value = playlist)
     */
    public static Map<String, Playlist> getAllPlaylists() {
        return playlists;
    }

    /**
     * Method to create a new playlist with a given name
     *
     * @param playlistName Name of the playlist to be returned
     * @return true if the playlist has been created, false if not
     */
    //Todo: Exception handling
    public boolean createPlaylist(User currentuser, String playlistName) {
        playlists.put(playlistName, new Playlist(UUID.randomUUID().toString(), playlistName, currentuser, new Date()));
        return playlists.containsKey(playlistName);
    }

    /**
     * Method to delete a playlist by its ID
     *
     * @param playlistID ID of the playlist to be removed from the PlaylistManager
     * @return true if the playlist has been deleted, false if not
     */
    public boolean deletePlaylistByID(User currentUser, String playlistID) {
        if (!playlists.containsValue(getPlaylistByID(playlistID))) {
            log.error("Playlist " + playlistID + " could not be found.");
            return false;
        } else if (!getPlaylistByID(playlistID).getCreatedBy().equals(currentUser)) {
            log.error("Playlist " + playlistID + " could not be deleted because it was not created by the active user.");
            return false;
        }
        playlists.remove(getPlaylistByID(playlistID).getName());
        log.info("Playlist " + playlistID + " has been deleted.");
        return true;
    }

    /**
     * Method to delete a playlist by its name
     *
     * @param playlistName Name of the playlist to be removed from the PlaylistManager
     * @return true if the playlist has been deleted, false if not
     */
    public boolean deletePlaylistByName(User currentUser, String playlistName) {
        if (!playlists.containsKey(playlistName)) {
            log.error("Playlist " + playlistName + " could not be found.");
            return false;
        } else if (!getPlaylistByName(playlistName).getCreatedBy().equals(currentUser)) {
            log.error("Playlist " + playlistName + " could not be deleted because it was not created by the active user.");
            return false;
        }
        playlists.remove(playlistName);
        log.info("Playlist " + playlistName + " has been deleted.");
        return true;
    }

    /**
     * Method to write to a JSON file via Gson
     *
     * @link UserTypeAdapter
     * @see FileWriter
     * @see Gson
     * @see GsonBuilder
     */
    //Todo: Check if the log states are correct -> Should be
    //Todo: Check if exception handling is correct
    public static void playlistsToJSON() throws WriteDataException {
        try {
            FileWriter fileWriter = new FileWriter(SAVE_FILE);
            gson.toJson(playlists, fileWriter);
            log.debug("{} Playlists have been saved to JSON file {}.", playlists.size(), SAVE_FILE);
            fileWriter.close();
        } catch (Exception e) {
            log.fatal("Playlists have not been saved to JSON file {}.", SAVE_FILE);
            throw new WriteDataException("Error while saving playlists to JSON file." + e.getMessage());
        }
    }

    /**
     * Method to read a JSON file via Gson
     *
     * @link UserSerializer (Custom serializer for User objects)
     * @see FileReader
     * @see Gson
     * @see GsonBuilder
     */
    //Todo: Check if the log state is correct
    //Todo: Check if exception handling is correct
    public static void playlistsFromJSON() throws ReadDataException {
        try {
            FileReader fileReader = new FileReader(SAVE_FILE);
            playlists.putAll(gson.fromJson(fileReader, new TypeToken<Map<String, Playlist>>() {
            }.getType()));
            log.debug("{} Playlists have been loaded from JSON file {}.", playlists.size(), SAVE_FILE);
            fileReader.close();
        } catch (Exception e) {
            log.fatal("Playlists have not been loaded from JSON file {}.", SAVE_FILE);
            throw new ReadDataException("Error while loading playlists from JSON file." + e.getMessage());
        }
    }
}

