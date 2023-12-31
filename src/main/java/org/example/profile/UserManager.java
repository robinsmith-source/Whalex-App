package org.example.profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.data.DataOperation;
import org.example.data.DataThread;
import org.example.data.DataType;
import org.example.data.UserSerializer;
import org.example.exceptions.ReadDataException;
import org.example.exceptions.WriteDataException;

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
 * UserManager class manages all users.
 */
//TODO : Write tests for the UserManager
public class UserManager {
    private static final Logger log = LogManager.getLogger(UserManager.class);

    /**
     * UserManager instance
     */
    private static final UserManager INSTANCE = new UserManager();

    /**
     * Gson object to convert JSON to Java and Java to JSON
     *
     * @see UserManager#usersToJSON()
     * @see UserManager#usersFromJSON()
     */
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(User.class, new UserSerializer())
            .setPrettyPrinting()
            .create();

    /**
     * Path to the file where all user data is stored
     */
    private final Path PROFILE_PICTURES = Path.of("src/main/resources/data/profilePictures");

    /**
     * Path to the file where all user data is stored
     */
    private File SAVE_FILE = new File("src/main/resources/data/saves/users.json");

    /**
     * Path to the file where the default profile picture is stored
     */
    private final File DEFAULT_PICTURE = new File("src/main/resources/data/profilePictures/default/defaultUserProfilePicture.jpg");

    /**
     * Map of all users (key = username, value = user)
     */
    private final HashSet<User> USERS = new HashSet<>();

    /**
     * User who is currently logged in
     */
    private User activeUser;

    /**
     * Constructor for UserManager
     */
    private UserManager() {
    }

    /**
     * Method to get the UserManager instance
     *
     * @return UserManager instance
     */
    public static UserManager getInstance() {
        return INSTANCE;
    }

    public void setSAVE_FILE(File SAVE_FILE) {
        this.SAVE_FILE = SAVE_FILE;
    }

    public File getDEFAULT_PICTURE() {
        return DEFAULT_PICTURE;
    }

    public Path getPROFILE_PICTURES() {
        return PROFILE_PICTURES;
    }

    /**
     * Method to get the current user
     *
     * @return Current user
     * @throws IllegalArgumentException if no user is logged in
     */
    public User getActiveUser() throws IllegalStateException {
        if (activeUser == null) {
            log.error("No user is logged in.");
            throw new IllegalStateException("No user is logged in.");
        }
        log.debug("Current user is {}.", activeUser.getUsername());
        return activeUser;
    }

    /**
     * Method to get a user by its ID
     *
     * @param userID ID of the user
     * @return User object with the given ID
     * @throws IllegalArgumentException if the user with the given ID could not be found
     */
    public User getUserById(String userID) throws IllegalArgumentException {
        log.debug("Getting user with ID {}.", userID);
        return this.USERS.parallelStream().filter(user -> user.getUserID().equals(userID)).findAny().orElseThrow(() -> {
            log.error("User with ID {} could not be found.", userID);
            return new IllegalArgumentException("User with ID " + userID + " could not be found.");
        });
    }

    /**
     * Method to get a user by its username
     *
     * @param username Username of the user
     * @return User object with the given username
     * @throws IllegalArgumentException if the user with the given name could not be found
     */
    public User getUserByName(String username) throws IllegalArgumentException {
        log.debug("Getting user {}.", username);
        return this.USERS.parallelStream().filter(user -> user.getUsername().equals(username)).findAny().orElseThrow(() -> {
            log.debug("User {} could not be found.", username);
            return new IllegalArgumentException("User " + username + " could not be found.");
        });
    }

    /**
     * Method to get all users
     *
     * @return Arraylist of all users objects
     */
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(USERS);
    }

    /**
     * Method to create a user by its profilePicture, username and password
     *
     * @param chosenImage Profile picture of the user
     * @param username    Username of the user
     * @param password    Password of the user
     * @throws IllegalArgumentException if the profile picture is null, the username is null or empty or the password is null or empty or the user already exists
     */
    public void createUser(File chosenImage, String username, String password, String passwordConfirmation) throws Exception {
        if (username == null || username.isEmpty()) {
            log.warn("Username is null or empty.");
            throw new IllegalArgumentException("Username cannot be null or empty.");
        } else if (password == null || password.isEmpty()) {
            log.warn("Password is null or empty.");
            throw new IllegalArgumentException("Password cannot be null or empty.");
        } else if (!password.equals(passwordConfirmation)) {
            log.error("Password confirmation is incorrect.");
            throw new IllegalArgumentException("Password confirmation is incorrect.");
        } else if (this.USERS.parallelStream().anyMatch(user -> user.getUsername().equals(username))) {
            log.error("User {} already exists.", username);
            throw new IllegalArgumentException("User already exists.");
        } else if (chosenImage == null) {
            log.debug("Profile picture is null.");
            this.USERS.add(new User(UUID.randomUUID().toString(), DEFAULT_PICTURE, username, password));
        } else {
            if (!Files.exists(PROFILE_PICTURES)) {
                Files.createDirectories(PROFILE_PICTURES);
                log.info("Directory {} has been created.", PROFILE_PICTURES);
            }

            String uuid = UUID.randomUUID().toString();

            String extension = chosenImage.getName().substring(chosenImage.getName().lastIndexOf("."));
            Path targetFile = PROFILE_PICTURES.resolve(uuid + extension);

            Files.copy(chosenImage.toPath(), targetFile);
            File profilePicture = targetFile.toFile();

            this.USERS.add(new User(uuid, profilePicture, username, password));
        }
        log.debug("User {} has been created.", username);
        activeUser = getUserByName(username);
        log.debug("User {} is now logged in.", username);
        new DataThread(DataType.USER_SOUND_PLAYLIST, DataOperation.WRITE).start();
    }

    /**
     * Method to delete a user by its username
     *
     * @param password             Password of the user
     * @param passwordConfirmation Password confirmation of the user
     * @throws IllegalArgumentException if the user does not exist or the password is incorrect or the password confirmation is incorrect
     */
    public void deleteUser(String password, String passwordConfirmation) throws IllegalArgumentException {
        if (this.USERS.parallelStream().noneMatch(user -> user.getUsername().equals(activeUser.getUsername()))) {
            log.error("User {} does not exist.", activeUser.getUsername());
            throw new IllegalArgumentException("User does not exist.");
        } else if (!activeUser.getPassword().equals(password)) {
            log.error("Password is incorrect.");
            throw new IllegalArgumentException("Password is incorrect.");
        } else if (!password.equals(passwordConfirmation)) {
            log.error("Password confirmation is incorrect.");
            throw new IllegalArgumentException("Password confirmation is incorrect.");
        }
        log.debug("User {} has been deleted.", activeUser.getUsername());
        USERS.remove(activeUser);

        File profilePicture = activeUser.getProfilePicture();
        String extension = profilePicture.getName().substring(profilePicture.getName().lastIndexOf("."));
        Path targetFile = PROFILE_PICTURES.resolve(activeUser.getUserID() + extension);

        if (targetFile != UserManager.getInstance().getDEFAULT_PICTURE().toPath()) {
            new Thread(() -> {
                try {
                    Files.deleteIfExists(targetFile);
                    log.info("User profile picture {} has been deleted.", targetFile);
                } catch (IOException e) {
                    log.error("User profile picture {} couldn't be deleted", targetFile);
                }
            }).start();
        }
        logout();
        new DataThread(DataType.USER_SOUND_PLAYLIST, DataOperation.WRITE).start();
    }

    /**
     * Method to log in a user by its username and password
     *
     * @param username Username of the user
     * @param password Password of the user
     * @throws IllegalArgumentException if the username is null or empty or the password is null or empty or the user does not exist or the password is incorrect
     */
    public void login(String username, String password) throws IllegalArgumentException {
        User matchedUser = this.USERS.parallelStream().filter(user -> user.getUsername().equals(username)).findAny().orElseThrow(() -> {
                    log.error("User {} does not exist.", username);
                    return new IllegalArgumentException("User does not exist.");
                });
        if (password == null || password.isEmpty()) {
            log.warn("Password is null or empty.");
            throw new IllegalArgumentException("Password cannot be null or empty.");
        } else if (!matchedUser.getPassword().equals(password)) {
            log.warn("Password is incorrect.");
            throw new IllegalArgumentException("Password is incorrect.");
        }
        log.info("User {} has been logged in.", username);
        activeUser = getUserByName(username);
    }

    /**
     * Method to log out the current user
     */
    public void logout() {
        log.info("User {} has been logged out.", activeUser.getUsername());
        activeUser = null;
    }

    /**
     * Method to write to a JSON file via Gson
     *
     * @throws WriteDataException if the users could not be saved from the JSON file
     * @see UserSerializer
     */
    public synchronized void usersToJSON() throws WriteDataException {
        try (FileWriter fileWriter = new FileWriter(SAVE_FILE)) {
            gson.toJson(USERS, fileWriter);
            log.info("{} Users have been saved to JSON file {}.", USERS.size(), SAVE_FILE);
        } catch (Exception e) {
            log.fatal("Users have not been saved to JSON file {}.", SAVE_FILE);
            throw new WriteDataException("Error while saving users to JSON file.");
        }
    }

    /**
     * Method to read a JSON file via Gson
     *
     * @throws ReadDataException if the users could not be loaded from the JSON file
     * @see UserSerializer
     */
    public synchronized void usersFromJSON() throws ReadDataException {
        try (FileReader fileReader = new FileReader(SAVE_FILE)) {
            USERS.addAll(gson.fromJson(fileReader, new TypeToken<ArrayList<User>>() {
            }.getType()));
            log.info("{} Users have been loaded from JSON file {}.", USERS.size(), SAVE_FILE);
        } catch (IOException e) {
            log.fatal("Users have not been loaded from JSON file {}.", SAVE_FILE);
            throw new ReadDataException("Error while loading users from JSON file.");
        }
    }
}