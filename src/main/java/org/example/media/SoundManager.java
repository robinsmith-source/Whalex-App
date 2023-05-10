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
    private static final File SOUNDS_PATH = new File("src/main/resources/data/sounds/");

    /**
     * Path to the file where all sound data is stored
     */
    private static final File SAVE_FILE = new File("src/main/resources/data/saves/sounds.json");

    /**
     * Map of all sounds (key = title, value = sound object)
     */
    private static final Map<String, ISound> sounds = new HashMap<>();

    /**
     * User who currently uses this object
     */
    private final User activeUser;

    /**
     * Constructor for the SoundManager
     *
     * @param activeUser User who currently uses this object
     */
    public SoundManager(User activeUser) {
        this.activeUser = activeUser;
    }

    /**
     * Method to get a sound by its ID
     *
     * @param soundID ID of the sound
     * @return sound with the given ID
     */
    //Todo: Check if log states are correct -> Should be
    public static ISound getSoundByID(String soundID) throws IllegalArgumentException {
        if (soundID == null || soundID.isEmpty()) {
            log.warn("soundID is null or empty");
            throw new IllegalArgumentException("soundID cannot be null or empty");
        }
        for (ISound sound : sounds.values()) {
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
    public static ArrayList<ISound> getAllSoundsByUser(User user) {
        ArrayList<ISound> soundList = new ArrayList<>();
        for (ISound sound : sounds.values()) {
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
    public static Map<String, ISound> getAllSounds() {
        return sounds;
    }

    /**
     * Method to add a sound to the SoundManager
     *
     * @param title Title of the sound
     * @param path  Path to the soundfile
     * @return True if sound was added successfully, false if not
     * TODO: Check if Exception handling is correct
     */
    public boolean addSound(String title, File path) {

        try {
            sounds.put(title, SoundFactory.createSound(UUID.randomUUID().toString(), title, path, activeUser));
            log.debug("Sound {} has been added.", title);
        } catch (Exception e) {
            log.error("Sound could not be added", e);
            return false;
        }
        return sounds.containsKey(title);
    }

    /**
     * Method to remove a sound by its title from the SoundManager
     *
     * @param title Title of the sound
     * @return True if sound was removed successfully, false if not
     */
    public boolean removeSoundByTitle(String title) {
        if (!sounds.containsKey(title)) {
            log.error("Playlist {} could not be found.", title);
            return false;
        } else if (!sounds.get(title).getUploadedBy().equals(activeUser)) {
            log.error("Playlist {} could not be deleted because it was uploaded by {}, active user was {}.", title, sounds.get(title).getUploadedBy(), activeUser);
            return false;
        }
        sounds.remove(title);
        log.info("Playlist {} has been deleted by {}.", title, sounds.get(title).getUploadedBy());
        return true;
    }

    /**
     * Method to write to a JSON file via Gson
     *
     * @link SoundSerializer (Custom serializer for the ISound interface)
     * @see FileWriter
     * @see Gson
     * @see GsonBuilder
     */
    //Todo: Check if the log states are correct -> Should be
    //Todo: Check if exception handling is correct
    public static void soundsToJSON() throws WriteDataException {
        try {
            FileWriter fileWriter = new FileWriter(SAVE_FILE);
            gson.toJson(sounds, fileWriter);
            log.debug("{} Sounds have been saved to JSON file {}.", sounds.size(), SAVE_FILE);
            fileWriter.close();
        } catch (Exception e) {
            log.fatal("Sounds have not been saved to JSON file {}.", SAVE_FILE);
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
     */
    //Todo: Check if the log states are correct -> Should be
    //Todo: Check if exception handling is correct
    public static void soundsFromJSON() throws ReadDataException {
        try {
            FileReader fileReader = new FileReader(SAVE_FILE);
            sounds.putAll(gson.fromJson(fileReader, new TypeToken<Map<String, ISound>>() {
            }.getType()));
            log.debug("{} Sounds have been loaded from JSON file {}.", sounds.size(), SAVE_FILE);
            fileReader.close();
        } catch (Exception e) {
            log.fatal("Sounds have not been loaded from JSON file {}.", SAVE_FILE);
            throw new ReadDataException("Error while loading Sounds from JSON." + e.getMessage());
        }
    }
}
