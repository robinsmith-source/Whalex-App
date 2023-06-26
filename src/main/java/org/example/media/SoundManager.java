package org.example.media;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.data.DataOperation;
import org.example.data.DataThread;
import org.example.data.DataType;
import org.example.data.SoundSerializer;
import org.example.exceptions.ReadDataException;
import org.example.exceptions.WriteDataException;
import org.example.media.extensions.SoundFactory;
import org.example.media.interfaces.ISound;
import org.example.profile.User;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

/**
 * SoundManager manages all sounds
 */
//TODO: Write tests for the SoundManager
public class SoundManager {
    private static final Logger log = LogManager.getLogger(SoundManager.class);

    /**
     * Instance of the SoundManager
     */
    private static final SoundManager INSTANCE = new SoundManager();

    /**
     * Gson object to convert JSON to Java and Java to JSON
     *
     * @see SoundManager#soundsToJSON()
     * @see SoundManager#soundsFromJSON()
     */
    private static final Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(ISound.class, new SoundSerializer())
            .setPrettyPrinting()
            .create();

    /**
     * Path to the folder where all sound files are stored
     */
    private Path SOUNDS_PATH = Path.of("src/main/resources/data/sounds/");

    /**
     * Path to the file where all sound data is stored
     */
    private File SAVE_FILE = new File("src/main/resources/data/saves/sounds.json");

    /**
     * Path to the file where the default sound cover is stored
     */
    private final File DEFAULT_COVER = new File("src/main/resources/data/sounds/default/defaultSoundCover.jpg");

    /**
     * Arraylist of all sounds
     */
    private final HashSet<ISound> SOUNDS = new HashSet<>();

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

    public void setSAVE_FILE(File SAVE_FILE) {
        this.SAVE_FILE = SAVE_FILE;
    }

    public void setSOUNDS_PATH(Path SOUNDS_PATH) {
        this.SOUNDS_PATH = SOUNDS_PATH;
    }

    /**
     * Method to get the default sound cover
     *
     * @return File of the default sound cover
     */
    public File getDEFAULT_COVER() {
        return new File(DEFAULT_COVER.toURI());
    }

    /**
     * Method to get a sound by its ID
     *
     * @param soundID ID of the sound
     * @return sound with the given ID
     * @throws IllegalArgumentException if sound with the given ID could not be found
     */
    //Todo: Check if log states are correct -> Should be
    public ISound getSoundByID(String soundID) throws IllegalArgumentException {
        log.debug("Getting sound with ID {}.", soundID);
        return SOUNDS.stream().filter(sound -> sound.getSoundID().equals(soundID)).findFirst().orElseThrow(() -> {
            log.error("Sound with ID {} could not be found.", soundID);
            return new IllegalArgumentException("Sound with ID " + soundID + " could not be found.");
        });
    }

    /**
     * Method to get all sounds uploaded by the user
     *
     * @param user User whose sounds should be returned
     * @return ArrayList of all sounds uploaded by the user
     */
    public ArrayList<ISound> getAllSoundsByUser(User user) {
        ArrayList<ISound> soundsByUser = this.SOUNDS.stream().filter(sound -> sound.getUploadedBy().equals(user)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        log.debug("{} Sounds have been found for user {}.", soundsByUser.size(), user.getUsername());
        return soundsByUser;
    }

    /**
     * Method to get all sounds
     *
     * @return Arraylist of all sounds
     */
    public ArrayList<ISound> getAllSounds() {
        return new ArrayList<>(this.SOUNDS);
    }


    /**
     * Method to add a new sound with a given title and path
     *
     * @param currentUser User who wants to add the sound
     * @param title       Title of the sound
     * @param choosenPath Path to the soundfile
     * @throws IllegalArgumentException if title is null or empty, if path is null, if sound with the same title already exists, if path does not exist, if path is not a file, if path cannot be read
     */
    public void addSound(User currentUser, String title, File choosenPath) throws IllegalArgumentException {
        if (title == null || title.isEmpty()) {
            log.warn("Title is null or empty");
            throw new IllegalArgumentException("Title cannot be null or empty");
        } else if (choosenPath == null) {
            log.warn("Path is null");
            throw new IllegalArgumentException("Path cannot be null");
        } else if (currentUser == null) {
            log.warn("User is null");
            throw new IllegalArgumentException("User cannot be null");
        } else if (SOUNDS.stream().anyMatch(sound -> sound.getTitle().equals(title))) {
            log.warn("Sound {} already exists.", title);
            throw new IllegalArgumentException("Sound " + title + " already exists.");
        }
        try {
            if (!SOUNDS_PATH.toFile().exists()) {
                Files.createDirectories(SOUNDS_PATH);
                log.info("Directory {} has been created.", SOUNDS_PATH);
            }

            String uuid = UUID.randomUUID().toString();
            String extension = choosenPath.getName().substring(choosenPath.getName().lastIndexOf("."));
            Path targetFile = SOUNDS_PATH.resolve(uuid + extension);
            File soundFile = targetFile.toFile();

            Files.copy(choosenPath.toPath(), targetFile);
            log.info("File {} has been copied to {}", choosenPath, targetFile);

            SOUNDS.add(SoundFactory.getInstance().createSound(uuid, title, soundFile, currentUser));
            log.debug("Sound {} has been added.", title);
            new DataThread(DataType.SOUND_PLAYLIST, DataOperation.WRITE).start();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Sound " + title + " could not be added");
        }
    }

    /**
     * Method to delete a sound by its ID
     *
     * @param currentUser User who wants to delete the sound
     * @param soundID     ID of the sound to be deleted by the user
     * @throws IllegalArgumentException if sound with the given ID could not be found or if the sound was not uploaded by the active user
     */
    public void deleteSoundByID(User currentUser, String soundID) throws IllegalArgumentException {
        ISound matchedSound = this.SOUNDS.parallelStream().filter(sound -> sound.getSoundID().equals(soundID)).findFirst().orElseThrow(() -> {
            log.error("Sound {} could not be found.", soundID);
            return new IllegalArgumentException("Sound " + soundID + " could not be found.");
        });

        if (!matchedSound.getUploadedBy().equals(currentUser)) {
            log.error("Sound {} wasn't uploaded by the active user.", matchedSound.getTitle());
            throw new IllegalArgumentException("Sound " + matchedSound.getSoundID() + " could not be deleted because it was not uploaded by the activeUser");
        } else {
            String extension = matchedSound.getMedia().getSource().substring(matchedSound.getMedia().getSource().lastIndexOf("."));
            Path targetFile = SOUNDS_PATH.resolve(soundID + extension);
            log.trace("Deleting sound file " + targetFile);

            try {
                Files.deleteIfExists(targetFile);
                this.SOUNDS.remove(matchedSound);
                log.info("Sound file {} has been deleted.", targetFile);
                new DataThread(DataType.SOUND_PLAYLIST, DataOperation.WRITE).start();
            } catch (Exception e) {
                log.error("Sound file {} couldn't be deleted because {}.", soundID, e.getMessage());
                throw new RuntimeException("Sound file " + soundID + " couldn't be deleted because " + e.getMessage());
            }
        }
    }

    /**
     * Method to write to a JSON file via Gson
     *
     * @throws WriteDataException if the sounds could not be saved to the JSON file
     * @see SoundSerializer
     */
    public synchronized void soundsToJSON() throws WriteDataException {
        try (FileWriter fileWriter = new FileWriter(this.SAVE_FILE)){
            gson.toJson(this.SOUNDS, fileWriter);
            log.info("{} Sounds have been saved to JSON file {}.", this.SOUNDS.size(), this.SAVE_FILE);
        } catch (Exception e) {
            log.fatal("Sounds have not been saved to JSON file {}.", this.SAVE_FILE);
            throw new WriteDataException("Error while saving users to JSON.");
        }
    }

    /**
     * Method to read a JSON file via Gson
     *
     * @throws ReadDataException if the sounds could not be loaded from the JSON file
     * @see SoundSerializer
     */
    public synchronized void soundsFromJSON() throws ReadDataException {
        try (FileReader fileReader = new FileReader(this.SAVE_FILE)){
            this.SOUNDS.addAll(gson.fromJson(fileReader, new TypeToken<ArrayList<ISound>>() {
            }.getType()));
            log.info("{} Sounds have been loaded from JSON file {}.", this.SOUNDS.size(), this.SAVE_FILE);
        } catch (Exception e) {
            log.fatal("Sounds have not been loaded from JSON file {}.", this.SAVE_FILE);
            throw new ReadDataException("Error while loading Sounds from JSON.");
        }
    }
}
