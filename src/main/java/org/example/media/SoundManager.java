package org.example.media;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.data.SoundSerializer;
import org.example.exceptions.ReadDataException;
import org.example.exceptions.WriteDataException;
import org.example.media.extensions.SoundFactory;
import org.example.media.interfaces.ISound;
import org.example.profile.User;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * SoundManager manages all Sounds, providing methods to create, delete and get Sounds in different ways.
 * TODO: Write tests for the SoundManager
 */
public class SoundManager {
    private static final Logger log = LogManager.getLogger(SoundManager.class);

    private static final SoundManager INSTANCE = new SoundManager();
    /**
     * Gson object to convert JSON to Java and Java to JSON
     *
     * @link usersToJSON() (Method to convert Java to JSON)
     * @link usersFromJSON() (Method to convert JSON to Java)
     * @see Gson
     */
    private static final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(ISound.class, new SoundSerializer())
            .setPrettyPrinting()
            .create();

    /**
     * Path to the folder where all sound files are stored
     */
    private final File SOUNDS_PATH = new File("src/main/resources/data/sounds/");

    /**
     * Path to the file where all sound data is stored
     */
    private final File SAVE_FILE = new File("src/main/resources/data/saves/sounds.json");

    /**
     * Path to the file where the default sound cover is stored
     */
    private final File DEFAULT_COVER = new File("src/main/resources/data/defaultImages/sounds/defaultSoundCover.jpg");

    /**
     * Map of all sounds (key = title, value = sound object)
     */
    private final Map<String, ISound> sounds = new HashMap<>();

    /**
     * Constructor of the SoundManager
     */
    private SoundManager() {
    }

    /**
     * Method to get the SoundManager instance
     *
     * @return SoundManager instance
     */
    public static SoundManager getInstance() {
        return INSTANCE;
    }

    /**
     * Method to get a sound by its ID
     *
     * @param soundID ID of the sound
     * @return sound with the given ID
     */
    //Todo: Check if log states are correct -> Should be
    public ISound getSoundByID(String soundID) throws IllegalArgumentException {
        if (soundID == null || soundID.isEmpty()) {
            log.warn("soundID is null or empty");
            throw new IllegalArgumentException("soundID cannot be null or empty");
        }
        for (ISound sound : this.sounds.values()) {
            if (sound.getSoundID().equals(soundID)) {
                log.debug("Sound with ID {} has been found.", soundID);
                return sound;
            }
        }
        log.error("Sound with ID {} could not be found.", soundID);
        throw new IllegalArgumentException("Sound with ID " + soundID + " could not be found.");
    }

    /**
     * Method to get all sounds uploaded by the user
     *
     * @param user User whose sounds should be returned
     * @return ArrayList of all sounds of the user
     */
    public ArrayList<ISound> getAllSoundsByUser(User user) {
        ArrayList<ISound> soundList = new ArrayList<>();
        for (ISound sound : this.sounds.values()) {
            if (sound.getUploadedBy().equals(user)) {
                soundList.add(sound);
                log.debug("{} Sounds have been found for user {}.", soundList.size(), user.getUsername());
            }
        }
        return soundList;
    }

    /**
     * Method to get all sounds
     *
     * @return Map of all sounds (key = title, value = sound object)
     */
    public Map<String, ISound> getAllSounds() {
        return this.sounds;
    }

    /**
     * Method to get the default sound cover
     * @return File of the default sound cover
     */
    public File getDEFAULT_COVER() {
        return DEFAULT_COVER;
    }

    /**
     * Method to add a sound to the SoundManager
     *
     * @param title Title of the sound
     * @param path  Path to the soundfile
     * @throws IllegalArgumentException if title is null or empty, if path is null, if sound with the same title already exists, if path does not exist, if path is not a file, if path cannot be read
     *                                                                   TODO: Check if Exception handling is correct
     */
    public void addSound(User currentUser, String title, File path) throws IllegalArgumentException {
        if (title == null || title.isEmpty()) {
            log.warn("Title is null or empty");
            throw new IllegalArgumentException("Title cannot be null or empty");
        } else if (path == null) {
            log.warn("Path is null");
            throw new IllegalArgumentException("Path cannot be null");
        } else if (sounds.containsKey(title)) {
            log.warn("Sound {} already exists.", title);
            throw new IllegalArgumentException("Sound " + title + " already exists.");
        } else if (!path.exists()) {
            log.error("Path {} does not exist.", path);
            throw new IllegalArgumentException("Path " + path + " does not exist.");
        } else if (!path.isFile()) {
            log.error("Path {} is not a file.", path);
            throw new IllegalArgumentException("Path " + path + " is not a file.");
        } else if (!path.canRead()) {
            log.error("Path {} cannot be read.", path);
            throw new IllegalArgumentException("Path " + path + " cannot be read.");
        }
        try {
            sounds.put(title, SoundFactory.getInstance().createSound(UUID.randomUUID().toString(), title, path, currentUser));
            log.debug("Sound {} has been added.", title);
        } catch (Exception e) {
            log.error("Sound could not be added", e);
            throw new IllegalArgumentException("Sound " + title + " not be added", e);
        }
    }

    /**
     * Method to remove a sound by its title
     *
     * @param currentUser User who wants to remove the sound
     * @param soundID     ID of the sound
     * @throws IllegalArgumentException if soundID is null or empty, sound with the given ID does not exist or sound was not uploaded by the active user
     */
    public void removeSoundByID(User currentUser, String soundID) throws IllegalArgumentException {
        if (soundID == null || soundID.isEmpty()) {
            log.warn("SoundID is null or empty");
            throw new IllegalArgumentException("SoundID cannot be null or empty");
        } else if (currentUser == null) {
            log.warn("User is null");
            throw new IllegalArgumentException("User cannot be null");
        } else if (!sounds.containsKey(soundID)) {
            log.error("Sound {} is not in the SoundManager.", soundID);
            throw new IllegalArgumentException("Sound " + soundID + " could not be found.");
        } else if (!sounds.get(soundID).getUploadedBy().equals(currentUser)) {
            log.error("Sound {} wasn't uploaded by the active user.", soundID);
            throw new IllegalArgumentException("Sound " + soundID + " could not be deleted because it was not uploaded by the activeUser");
        }
        sounds.remove(soundID);
        log.debug("Sound {} has been removed.", soundID);
    }

    /**
     * Method to remove a sound by its title from the SoundManager
     *
     * @param title Title of the sound
     * @throws IllegalArgumentException if title is null or empty, if sound with the given title does not exist, if sound was not uploaded by the active user
     */
    public void removeSoundByTitle(User currentUser, String title) throws IllegalArgumentException {
        if (title == null || title.isEmpty()) {
            log.warn("Title is null or empty");
            throw new IllegalArgumentException("Title cannot be null or empty");
        } else if (currentUser == null) {
            log.warn("User is null");
            throw new IllegalArgumentException("User cannot be null");
        } else if (!sounds.containsKey(title)) {
            log.error("Sound {} is not in the SoundManager.", title);
            throw new IllegalArgumentException("Playlist " + title + " could not be found.");
        } else if (!sounds.get(title).getUploadedBy().equals(currentUser)) {
            log.error("Sound {} wasn't uploaded by the active user.", title);
            throw new IllegalArgumentException("Playlist " + title + " could not be deleted because it was not uploaded by the activeUser");
        }
        sounds.remove(title);
        log.debug("Sound {} has been deleted by user {}.", title, currentUser.getUsername());
    }

    /**
     * Method to write to a JSON file via Gson
     *
     * @link SoundSerializer (Custom serializer for the ISound interface)
     * @see FileWriter
     * @see Gson
     * @see GsonBuilder
     * @throws WriteDataException if the sounds could not be saved to the JSON file
     */
    //Todo: Check if the log states are correct -> Should be
    //Todo: Check if exception handling is correct
    public void soundsToJSON() throws WriteDataException {
        try {
            FileWriter fileWriter = new FileWriter(this.SAVE_FILE);
            gson.toJson(this.sounds, fileWriter);
            log.info("{} Sounds have been saved to JSON file {}.", this.sounds.size(), this.SAVE_FILE);
            fileWriter.close();
        } catch (Exception e) {
            log.fatal("Sounds have not been saved to JSON file {}.", this.SAVE_FILE);
            throw new WriteDataException("Error while saving users to JSON." + e.getMessage());
        }
    }

    /**
     * Method to read a JSON file via Gson
     *
     * @link SoundSerializer (Custom serializer for the ISound interface)
     * @see FileReader
     * @see Gson
     * @see GsonBuilder
     * @throws ReadDataException if the sounds could not be loaded from the JSON file
     */
    //Todo: Check if the log states are correct -> Should be
    //Todo: Check if exception handling is correct
    public void soundsFromJSON() throws ReadDataException {
        try {
            FileReader fileReader = new FileReader(this.SAVE_FILE);
            this.sounds.putAll(gson.fromJson(fileReader, new TypeToken<Map<String, ISound>>() {
            }.getType()));
            log.info("{} Sounds have been loaded from JSON file {}.", this.sounds.size(), this.SAVE_FILE);
            fileReader.close();
        } catch (Exception e) {
            log.fatal("Sounds have not been loaded from JSON file {}.", this.SAVE_FILE);
            throw new ReadDataException("Error while loading Sounds from JSON." + e.getMessage());
        }
    }
}
