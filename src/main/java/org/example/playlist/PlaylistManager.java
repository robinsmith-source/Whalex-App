package org.example.playlist;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.data.DataOperation;
import org.example.data.DataThread;
import org.example.data.DataType;
import org.example.data.PlaylistSerializer;
import org.example.exceptions.ReadDataException;
import org.example.exceptions.WriteDataException;
import org.example.profile.User;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

/**
 * PlaylistManager manages all playlists
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
     * @see PlaylistManager#playlistsToJSON()
     * @see PlaylistManager#playlistsFromJSON()
     */
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Playlist.class, new PlaylistSerializer())
            .setPrettyPrinting()
            .create();


    private final Path PLAYLIST_COVERS = Path.of("src/main/resources/data/playlistCover/");
    /**
     * Path to the file where all playlist data is stored
     */
    private File SAVE_FILE = new File("src/main/resources/data/saves/playlists.json");

    /**
     * Default playlist cover
     */
    private final File DEFAULT_COVER = new File("src/main/resources/data/playlistCover/default/defaultPlaylistCover.jpg");

    /**
     * Arraylist of all playlists of the user
     */
    private final HashSet<Playlist> PLAYLISTS = new HashSet<>();

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

    public void setSAVE_FILE(File SAVE_FILE) {
        this.SAVE_FILE = SAVE_FILE;
    }

    /**
     * Method to get a playlist by its ID
     *
     * @param playlistID ID of the playlist to be returned
     * @return Playlist with the given ID
     * @throws IllegalArgumentException if the playlist with the given ID could not be found or if the name is null or empty
     */
    //TODO: Check if log states are correct -> Should be
    //TODO: Check if this method is necessary
    public Playlist getPlaylistByID(String playlistID) {
        log.debug("Getting playlist with ID {}.", playlistID);
        if (playlistID == null || playlistID.isEmpty()) {
            log.warn("PlaylistID is null or empty");
            throw new IllegalArgumentException("PlaylistID cannot be null or empty");
        }
        for (Playlist playlist : this.PLAYLISTS) {
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
     * @throws IllegalArgumentException if the playlist with the given name could not be found
     */
    //TODO: Check if this method is necessary
    public Playlist getPlaylistByName(String playlistName) {
        log.debug("Getting playlist {}.", playlistName);
        return this.PLAYLISTS.parallelStream().filter(playlist -> playlist.getName().equals(playlistName)).findAny().orElseThrow(() -> {
            log.error("Playlist {} could not be found.", playlistName);
            return new IllegalArgumentException("Playlist " + playlistName + " could not be found.");
        });
    }

    /**
     * Method to get all playlists created by the User
     *
     * @param user User whose playlists should be returned
     * @return ArrayList of all playlists created by the User
     */
    public ArrayList<Playlist> getPlaylistsByUser(User user) {
        ArrayList<Playlist> playlistsByUser = this.PLAYLISTS.parallelStream().filter(playlist -> playlist.getCreatedBy().equals(user)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        log.debug("{} Playlists have been found for user {}.", playlistsByUser.size(), user.getUsername());
        return playlistsByUser;
    }

    /**
     * Method to get all playlists
     *
     * @return Arraylist of all playlists
     */
    public ArrayList<Playlist> getAllPlaylists() {
        return new ArrayList<>(this.PLAYLISTS);
    }

    /**
     * Method to create a new playlist with a given name and cover
     *
     * @param currentUser  User who wants to create the playlist
     * @param playlistName Name of the playlist
     * @throws IllegalArgumentException if the playlistName is null or empty or if a playlist with the given name already exists
     */
    //TODO: Check if log states are correct -> Should be
    //TODO: Exception handling -> Should be
    //TODO: PlaylistCover value is checked at input (FileExplorer)
    public void createPlaylist(User currentUser, File playlistCover, String playlistName) throws IllegalArgumentException {
        if (playlistName == null || playlistName.isEmpty()) {
            log.debug("PlaylistName is null or empty");
            throw new IllegalArgumentException("PlaylistName cannot be null or empty");
        } else if (currentUser == null) {
            log.warn("User is null");
            throw new IllegalArgumentException("User cannot be null");
        } else if (this.PLAYLISTS.parallelStream().anyMatch(playlist -> playlist.getName().equals(playlistName))) {
            log.error("Playlist {} already exists.", playlistName);
            throw new IllegalArgumentException("Playlist " + playlistName + " already exists.");
        } else {
            if (playlistCover == null) {
                log.debug("PlaylistCover is null");
                log.debug("Playlist {} has been created with Default Cover {}.", playlistName, DEFAULT_COVER);
                this.PLAYLISTS.add(new Playlist(UUID.randomUUID().toString(), DEFAULT_COVER, playlistName, currentUser));
            } else {
                try {
                    if (!Files.exists(PLAYLIST_COVERS)) {
                        Files.createDirectories(PLAYLIST_COVERS);
                        log.debug("Directory {} has been created.", PLAYLIST_COVERS);
                    }

                    String uuid = UUID.randomUUID().toString();
                    String extension = playlistCover.getName().substring(playlistCover.getName().lastIndexOf("."));
                    Path targetFile = PLAYLIST_COVERS.resolve(uuid + extension);

                    this.PLAYLISTS.add(new Playlist(uuid, targetFile.toFile(), playlistName, currentUser));
                    log.debug("Playlist {} has been created with custom Cover {}.", playlistName, targetFile.toFile());

                    try {
                        Files.copy(playlistCover.toPath(), targetFile);
                        log.debug("File copy from {} to {} successful", playlistCover, targetFile);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (Exception e) {
                    log.error("Playlist {} could not be created", playlistName);
                }
            }
            new DataThread(DataType.PLAYLIST, DataOperation.WRITE).start();
        }
    }

    /**
     * Method to delete a playlist by its ID
     *
     * @param currentUser User who wants to delete the playlist
     * @param playlistID  ID of the playlist to be removed by the user
     * @throws IllegalArgumentException if the playlist with the given ID could not be found or if the playlist wasn't created by the active user
     */
    public void deletePlaylistByID(User currentUser, String playlistID) throws IllegalArgumentException {
        Playlist matchedPlaylist = this.PLAYLISTS.parallelStream().filter(playlist -> playlist.getPlaylistID().equals(playlistID)).findAny().orElseThrow(() -> {
            log.error("Playlist with ID {} could not be found.", playlistID);
            return new IllegalArgumentException("Playlist with ID " + playlistID + " could not be found.");
        });

        if (!matchedPlaylist.getCreatedBy().equals(currentUser)) {
            log.error("Playlist with ID {} was not created by user {}.", playlistID, currentUser.getUsername());
            throw new IllegalArgumentException("Playlist with ID " + playlistID + " was not created by user " + currentUser.getUsername() + ".");
        } else {
            log.debug("Playlist {} has been deleted.", matchedPlaylist.getName());
            this.PLAYLISTS.remove(matchedPlaylist);

            File playlistCover = matchedPlaylist.getPlaylistCover();
            String extension = playlistCover.getName().substring(playlistCover.getName().lastIndexOf("."));
            Path targetFile = PLAYLIST_COVERS.resolve(playlistID + extension);
            new Thread(() -> {
                try {
                    Files.deleteIfExists(targetFile);
                    log.info("Playlist cover {} of playlist {} has been deleted.", targetFile, playlistID);
                } catch (IOException e) {
                    log.error("Playlist cover from playlist {} couldn't be deleted", playlistID);
                }
            }).start();
            new DataThread(DataType.PLAYLIST, DataOperation.WRITE).start();
        }
    }


    /**
     * Method to write to a JSON file via Gson
     *
     * @throws WriteDataException if the playlists could not be saved to the JSON file
     * @see PlaylistSerializer
     */
    public synchronized void playlistsToJSON() throws WriteDataException {
        try (FileWriter fileWriter = new FileWriter(this.SAVE_FILE)) {
            gson.toJson(this.PLAYLISTS, fileWriter);
            log.info("{} Playlists have been saved to JSON file {}.", this.PLAYLISTS.size(), this.SAVE_FILE);
        } catch (Exception e) {
            log.fatal("Playlists have not been saved to JSON file {}.", this.SAVE_FILE);
            throw new WriteDataException("Error while saving playlists to JSON file.");
        }
    }

    /**
     * Method to read a JSON file via Gson
     *
     * @throws ReadDataException if the playlists could not be loaded from the JSON file
     * @see PlaylistSerializer
     */
    public synchronized void playlistsFromJSON() throws ReadDataException {
        try (FileReader fileReader = new FileReader(this.SAVE_FILE)) {
            this.PLAYLISTS.addAll(gson.fromJson(fileReader, new TypeToken<ArrayList<Playlist>>() {
            }.getType()));
            log.info("{} Playlists have been loaded from JSON file {}.", this.PLAYLISTS.size(), this.SAVE_FILE);
        } catch (Exception e) {
            log.fatal("Playlists have not been loaded from JSON file {}.", this.SAVE_FILE);
            throw new ReadDataException("Error while loading playlists from JSON file.");
        }
    }
}

