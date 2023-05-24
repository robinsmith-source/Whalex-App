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

    /**
     * PlaylistManager instance
     */
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
    private final File SAVE_FILE = new File("src/main/resources/data/saves/playlists.json");

    /**
     * Default playlist cover
     */
    private final File DEFAULT_COVER = new File("src/main/resources/data/defaultImages/playlists/defaultPlaylistCover.jpg");

    /**
     * Map of all playlists of the user (key = playlistName, value = playlist)
     */
    private final Map<String, Playlist> playlists = new HashMap<>();

    /**
     * Constructor of the PlaylistManager
     */
    private PlaylistManager() {
    }

    /**
     * Method to get the PlaylistManager instance
     *
     * @return PlaylistManager instance
     */
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
    public Playlist getPlaylistByID(String playlistID) {
        if (playlistID == null || playlistID.isEmpty()) {
            log.warn("PlaylistID is null or empty");
            throw new IllegalArgumentException("PlaylistID cannot be null or empty");
        }
        for (Playlist playlist : this.playlists.values()) {
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
    public Playlist getPlaylistByName(String playlistName) {
        return this.playlists.get(playlistName);
    }

    /**
     * Method to get all playlists created by the User
     *
     * @param user User whose playlists should be returned
     * @return ArrayList of all playlists created by the User
     */
    public ArrayList<Playlist> getPlaylistsByUser(User user) {
        ArrayList<Playlist> playlistsByUser = new ArrayList<>();
        for (Playlist playlist : this.playlists.values()) {
            if (playlist.getCreatedBy().equals(user)) {
                playlistsByUser.add(playlist);
            }
        }
        return playlistsByUser;
    }

    /**
     * Method to get all playlists
     *
     * @return Arraylist of all playlists
     */
    public ArrayList<Playlist> getAllPlaylists() {
        return new ArrayList<>(this.playlists.values());
    }

    /**
     * Method to create a new playlist with a given name
     *
     * @param currentUser  User who wants to create the playlist
     * @param playlistName Name of the playlist to be returned
     * @throws IllegalArgumentException if the playlistName is null or empty or if a playlist with the given name already exists
     */
    //Todo: Exception handling
    public void createPlaylist(User currentUser, File playlistCover, String playlistName) throws IllegalArgumentException {
        if (playlistName == null || playlistName.isEmpty()) {
            log.warn("PlaylistName is null or empty");
            throw new IllegalArgumentException("PlaylistName cannot be null or empty");
        } else if (currentUser == null) {
            log.warn("User is null");
            throw new IllegalArgumentException("User cannot be null");
        } else if (this.playlists.containsKey(playlistName)) {
            log.error("Playlist {} already exists.", playlistName);
            throw new IllegalArgumentException("Playlist " + playlistName + " already exists.");
        } else if (playlistCover == null) {
            log.warn("PlaylistCover is null");
            this.playlists.put(playlistName, new Playlist(UUID.randomUUID().toString(), DEFAULT_COVER, playlistName, currentUser, new Date()));
        } else {
            this.playlists.put(playlistName, new Playlist(UUID.randomUUID().toString(), playlistCover, playlistName, currentUser, new Date()));
        }
        log.debug("Playlist {} has been created.", playlistName);
    }

    /**
     * Method to delete a playlist by its ID
     *
     * @param currentUser User who wants to delete the playlist
     * @param playlistID  ID of the playlist to be removed from the PlaylistManager
     * @throws IllegalArgumentException if the playlist doesn't exist or if the playlist wasn't created by the active user
     */
    public void deletePlaylistByID(User currentUser, String playlistID) throws IllegalArgumentException {
        if (playlistID == null || playlistID.isEmpty()) {
            log.error("PlaylistID is null or empty");
            throw new IllegalArgumentException("PlaylistID cannot be null or empty");
        } else if (currentUser == null) {
            log.error("User is null");
            throw new IllegalArgumentException("User cannot be null");
        } else if (!this.playlists.containsValue(getPlaylistByID(playlistID))) {
            log.error("Playlist {} is not in the PlaylistManager.", playlistID);
            throw new IllegalArgumentException("Playlist " + playlistID + " could not be found.");
        } else if (!getPlaylistByID(playlistID).getCreatedBy().equals(currentUser)) {
            log.error("Playlist {} wasn't created by the active user.", playlistID);
            throw new IllegalArgumentException("Playlist " + playlistID + " could not be deleted because it was not created by the active user.");
        }
        this.playlists.remove(getPlaylistByID(playlistID).getName());
        log.info("Playlist {} has been deleted by user {}.", playlistID, currentUser.getUsername());
    }

    /**
     * Method to delete a playlist by its name
     *
     * @param currentUser  User who wants to delete the playlist
     * @param playlistName Name of the playlist to be removed from the PlaylistManager
     * @throws IllegalArgumentException if the playlist doesn't exist or if the playlist wasn't created by the active user
     */
    public void deletePlaylistByName(User currentUser, String playlistName) throws IllegalArgumentException {
        if (playlistName == null || playlistName.isEmpty()) {
            log.warn("PlaylistName is null or empty");
            throw new IllegalArgumentException("PlaylistName cannot be null or empty");
        } else if (currentUser == null) {
            log.warn("User is null");
            throw new IllegalArgumentException("User cannot be null");
        } else if (!this.playlists.containsKey(playlistName)) {
            log.error("Playlist {} is not in the PlaylistManager.", playlistName);
            throw new IllegalArgumentException("Playlist " + playlistName + " could not be found.");
        } else if (!getPlaylistByName(playlistName).getCreatedBy().equals(currentUser)) {
            log.error("Playlist {} wasn't created by the active user.", playlistName);
            throw new IllegalArgumentException("Playlist " + playlistName + " could not be deleted because it was not created by the active user.");
        }
        this.playlists.remove(playlistName);
        log.info("Playlist {} has been deleted by user {}.", playlistName, currentUser.getUsername());
    }

    /**
     * Method to delete all playlists of a user
     *
     * @param currentUser User whose playlists should be deleted
     * @throws IllegalArgumentException if the user is null
     */
    public void deletePlaylistsByUser(User currentUser) throws IllegalArgumentException {
        if (currentUser == null) {
            log.warn("User is null.");
            throw new IllegalArgumentException("User cannot be null.");
        }
        int playlistCounter = 0;
        for (Playlist playlist : this.playlists.values()) {
            if (playlist.getCreatedBy().equals(currentUser)) {
                this.playlists.remove(playlist.getName());
                playlistCounter++;
            }
        }
        log.info("{} Playlists of user {} have been deleted.", playlistCounter, currentUser.getUsername());
    }

    /**
     * Method to write to a JSON file via Gson
     *
     * @throws WriteDataException if the playlists could not be saved to the JSON file
     * @throws WriteDataException if the playlists could not be saved to the JSON file
     * @link UserTypeAdapter
     * @see FileWriter
     * @see Gson
     * @see GsonBuilder
     */
    //Todo: Check if the log states are correct -> Should be
    //Todo: Check if exception handling is correct
    public void playlistsToJSON() throws WriteDataException {
        try {
            FileWriter fileWriter = new FileWriter(this.SAVE_FILE);
            gson.toJson(this.playlists, fileWriter);
            log.info("{} Playlists have been saved to JSON file {}.", this.playlists.size(), this.SAVE_FILE);
            fileWriter.close();
        } catch (Exception e) {
            log.fatal("Playlists have not been saved to JSON file {}.", this.SAVE_FILE);
            throw new WriteDataException("Error while saving playlists to JSON file." + e.getMessage());
        }
    }

    /**
     * Method to read a JSON file via Gson
     *
     * @throws ReadDataException if the playlists could not be loaded from the JSON file
     * @throws ReadDataException if the playlists could not be loaded from the JSON file
     * @link UserSerializer (Custom serializer for User objects)
     * @see FileReader
     * @see Gson
     * @see GsonBuilder
     */
    //Todo: Check if the log state is correct
    //Todo: Check if exception handling is correct
    public void playlistsFromJSON() throws ReadDataException {
        try {
            FileReader fileReader = new FileReader(this.SAVE_FILE);
            this.playlists.putAll(gson.fromJson(fileReader, new TypeToken<Map<String, Playlist>>() {
            }.getType()));
            log.info("{} Playlists have been loaded from JSON file {}.", this.playlists.size(), this.SAVE_FILE);
            fileReader.close();
        } catch (Exception e) {
            log.fatal("Playlists have not been loaded from JSON file {}.", this.SAVE_FILE);
            throw new ReadDataException("Error while loading playlists from JSON file." + e.getMessage());
        }
    }
}

