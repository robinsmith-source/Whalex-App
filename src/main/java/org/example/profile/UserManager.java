package org.example.profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.data.UserSerializer;
import org.example.exceptions.ReadDataException;
import org.example.exceptions.WriteDataException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * UserManager class manages all users.
 */
// TODO : Write tests for the UserManager
public class UserManager {
    private static final Logger log = LogManager.getLogger(UserManager.class);

    /**
     * UserManager instance
     */
    private static final UserManager INSTANCE = new UserManager();

    /**
     * Gson object to convert JSON to Java and Java to JSON
     *
     * @link usersToJSON() (Method to convert Java to JSON)
     * @link usersFromJSON() (Method to convert JSON to Java)
     * @see Gson
     */
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(User.class, new UserSerializer()).setPrettyPrinting().create();

    /**
     * Path to the file where all user data is stored
     */
    private final File SAVE_FILE = new File("src/main/resources/data/saves/users.json");

    /**
     * Path to the file where the default profile picture is stored
     */
    private final File DEFAULT_PICTURE = new File("src/main/resources/data/defaultImages/users/defaultUserProfilePicture.jpg");

    /**
     * Map of all users (key = username, value = user)
     */
    private final Map<String, User> users = new HashMap<>();

    /**
     * User who is currently logged in
     */
    private User currentUser;

    /**
     * Constructor for UserManager
     */
    private UserManager() {
        this.currentUser = null;
    }

    /**
     * Method to get the UserManager instance
     *
     * @return UserManager instance
     */
    public static UserManager getInstance() {
        return INSTANCE;
    }

    /**
     * Method to create a user by its profilePicture, username and password
     *
     * @param profilePicture Profile picture of the user
     * @param username       Username of the user
     * @param password       Password of the user
     * @throws IllegalArgumentException if the profile picture is null, the username is null or empty or the password is null or empty or the user already exists
     */
    public void createUser(File profilePicture, String username, String password, String passwordConfirmation) throws IllegalArgumentException {
        if (username == null || username.isEmpty()) {
            log.error("Username is null or empty.");
            throw new IllegalArgumentException("Username cannot be null or empty.");
        } else if (password == null || password.isEmpty()) {
            log.error("Password is null or empty.");
            throw new IllegalArgumentException("Password cannot be null or empty.");
        } else if (!password.equals(passwordConfirmation)) {
            log.error("Password confirmation is incorrect.");
            throw new IllegalArgumentException("Password confirmation is incorrect.");
        } else if (users.containsKey(username)) {
            log.error("User {} already exists with ID {}.", username, users.get(username).getUserID());
            throw new IllegalArgumentException("User already exists.");
        } else if (profilePicture == null) {
            log.warn("Profile picture is null.");
            users.put(username, new User(UUID.randomUUID().toString(), DEFAULT_PICTURE, username, password));
        } else {
            users.put(username, new User(UUID.randomUUID().toString(), profilePicture, username, password));
        }
        log.info("User {} has been created.", username);
    }

    /**
     * Method to delete a user by its username
     *
     * @param currentUser          User who wants to be deleted
     * @param password             Password of the user
     * @param passwordConfirmation Password confirmation of the user
     * @throws IllegalArgumentException if the user does not exist or the password is incorrect or the password confirmation is incorrect
     */
    //TODO: Check if the log states are correct -> Should be
    //Todo: Add Custom Exception + Exception handling
    public void deleteUser(User currentUser, String password, String passwordConfirmation) throws IllegalArgumentException {
        if (!this.users.containsValue(currentUser)) {
            log.error("User {} does not exist.", currentUser.getUsername());
            throw new IllegalArgumentException("User does not exist.");
        } else if (!users.get(currentUser.getUsername()).getPassword().equals(password)) {
            log.error("Password is incorrect.");
            throw new IllegalArgumentException("Password is incorrect.");
        } else if (!password.equals(passwordConfirmation)) {
            log.error("Password confirmation is incorrect.");
            throw new IllegalArgumentException("Password confirmation is incorrect.");
        }
        users.remove(currentUser.getUsername());
        log.info("User {} has been deleted.", currentUser.getUsername());
    }

    /**
     * Method to log in a user by its username and password
     *
     * @param username Username of the user
     * @param password Password of the user
     * @throws IllegalArgumentException if the username is null or empty or the password is null or empty or the user does not exist or the password is incorrect
     */
    public void login(String username, String password) throws IllegalArgumentException {
        if (username == null || username.isEmpty()) {
            log.warn("Username is null or empty.");
            throw new IllegalArgumentException("Username cannot be null or empty.");
        } else if (password == null || password.isEmpty()) {
            log.warn("Password is null or empty.");
            throw new IllegalArgumentException("Password cannot be null or empty.");
        } else if (!users.containsKey(username)) {
            log.error("User {} does not exist.", username);
            throw new IllegalArgumentException("User does not exist.");
        } else if (!users.get(username).getPassword().equals(password)) {
            log.warn("Password is incorrect.");
            throw new IllegalArgumentException("Password is incorrect.");
        }
        log.info("User {} has been logged in.", username);
        currentUser = users.get(username);
    }

    /**
     * Method to get the current user
     *
     * @return Current user
     * @throws IllegalArgumentException if no user is logged in
     */
    public User getCurrentUser() throws IllegalArgumentException {
        if (currentUser == null) {
            log.error("No user is logged in.");
            throw new IllegalArgumentException("No user is logged in.");
        }
        log.debug("Current user is {}.", currentUser.getUsername());
        return currentUser;
    }

    /**
     * Method to get a user by its username
     *
     * @param username Username of the user
     * @return User object with the given username
     * @throws IllegalArgumentException if the username is null, empty or could not be found
     */
    public User getUserByName(String username) throws IllegalArgumentException {
        if (username == null || username.isEmpty()) {
            log.warn("Username is null or empty.");
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (!users.containsKey(username)) {
            log.error("Username {} could not be found.", username);
            throw new IllegalArgumentException("Username " + username + " could not be found.");
        }
        log.debug("User with username {} has been found.", username);
        return users.get(username);
    }

    /**
     * Method to get a user by its ID
     *
     * @param userID ID of the user
     * @return User object with the given ID
     * @throws IllegalArgumentException if the userID is null, empty or could not be found
     */
    public User getUserById(String userID) throws IllegalArgumentException {
        if (userID == null || userID.isEmpty()) {
            log.warn("User ID is null or empty.");
            throw new IllegalArgumentException("User ID cannot be null or empty.");
        }
        for (User user : users.values()) {
            if (user.getUserID().equals(userID)) {
                log.debug("User with ID {} has been found.", userID);
                return user;
            }
        }
        log.error("User with ID {} could not be found.", userID);
        throw new IllegalArgumentException("Sound with ID " + userID + " could not be found.");
    }

    /**
     * Method to get all users
     *
     * @return Arraylist of all users objects
     */
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Method to write to a JSON file via Gson
     *
     * @throws WriteDataException if the file could not be found or the file could not be written
     * @link UserSerializer (Custom serializer for the User class)
     * @see FileWriter
     * @see Gson
     * @see GsonBuilder
     */
    //Todo: Check if the log states are correct -> Should be
    //Todo: Check if exception handling is correct
    public void userToJSON() throws WriteDataException {
        try {
            FileWriter fileWriter = new FileWriter(SAVE_FILE);
            gson.toJson(users, fileWriter);
            log.info("{} Users have been saved to JSON file {}.", users.size(), SAVE_FILE);
            fileWriter.close();
        } catch (Exception e) {
            log.fatal("Users have not been saved to JSON file {}.", SAVE_FILE);
            throw new WriteDataException("Error while saving users to JSON file." + e.getMessage());
        }
    }

    /**
     * Method to read a JSON file via Gson
     *
     * @throws ReadDataException if the file could not be found or the file could not be read
     * @link UserSerializer (Custom serializer for the User class)
     * @see FileReader
     * @see Gson
     * @see GsonBuilder
     */
    //Todo: Check if the log states are correct -> Should be
    //Todo: Check if exception handling is correct
    public void usersFromJSON() throws ReadDataException {
        try {
            FileReader fileReader = new FileReader(SAVE_FILE);
            users.putAll(gson.fromJson(fileReader, new TypeToken<Map<String, User>>() {
            }.getType()));
            log.info("{} Users have been loaded from JSON file {}.", users.size(), SAVE_FILE);
            fileReader.close();
        } catch (IOException e) {
            log.fatal("Users have not been loaded from JSON file {}.", SAVE_FILE);
            throw new ReadDataException("Error while loading users from JSON file." + e.getMessage());
        }
    }
}