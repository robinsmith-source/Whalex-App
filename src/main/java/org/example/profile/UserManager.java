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
     * Gson object to convert JSON to Java and Java to JSON
     *
     * @link usersToJSON() (Method to convert Java to JSON)
     * @link usersFromJSON() (Method to convert JSON to Java)
     * @see Gson
     */
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(User.class, new UserSerializer())
            .setPrettyPrinting()
            .create();

    /**
     * Path to the file where all user data is stored
     */
    private static final File SAVE_FILE = new File("src/main/resources/data/saves/users.json");

    /**
     * Path to the file where the default profile picture is stored
     */
    private static final File defaultProfilePicture = new File("src/main/resources/data/users/default/profilePicture/whale01.png");

    /**
     * Map of all users (key = username, value = user)
     */
    private static final Map<String, User> users = new HashMap<>();


    /**
     *
     */
    private static User currentUser = null;

    /**
     * Method to create a user by its username and password
     *
     * @param username Username of the user
     * @param password Password of the user
     * @return true if the user has been created, false if the user already exists
     */
    public static boolean createUser(String username, String password) {
        if (users.containsKey(username)) {
            log.error("User {} already exists.", username);
            return false;
        }
        users.put(username, new User(UUID.randomUUID().toString(), defaultProfilePicture, username, password));
        log.info("User {} has been created.", username);
        return true;
    }

    /**
     * Method to create a user by its profilePicture, username and password
     *
     * @param profilePicture Profile picture of the user
     * @param username       Username of the user
     * @param password       Password of the user
     * @return true if the user has been created, false if the user already exists
     */
    public static boolean createUser(File profilePicture, String username, String password) {
        if (users.containsKey(username)) {
            log.error("User {} already exists.", username);
            return false;
        }
        users.put(username, new User(UUID.randomUUID().toString(), profilePicture, username, password));
        log.info("User {} has been created.", username);
        return true;
    }

    /**
     * Method to delete a user by its username
     *
     * @param username Username of the user
     * @return true if the user has been deleted, false if the user does not exist
     */
    //TODO: Check if the log states are correct -> Should be
    //Todo: Add Custom Exception + Exception handling
    public static boolean deleteUser(String username, String password, String passwordConfirmation) {
        if (!users.containsKey(username)) {
            log.error("User " + username + " does not exist.");
            return false;
        } else if (!users.get(username).getPassword().equals(password)) {
            log.error("Password is incorrect.");
            return false;
        } else if (!password.equals(passwordConfirmation)) {
            log.error("Password confirmation is incorrect.");
            return false;
        }
        users.remove(username);
        log.info("User " + username + " has been deleted.");
        return true;
    }

    /**
     * Method to log in a user by its username and password
     *
     * @param username Username of the user
     * @param password Password of the user
     * @return true if the user has been logged in, false if the user does not exist or the password is incorrect
     */
    public static boolean login(String username, String password) {
        if (!users.containsKey(username)) {
            log.error("User {} does not exist.", username);
            return false;
        } else if (!users.get(username).getPassword().equals(password)) {
            log.error("Password is incorrect.");
            return false;
        }
        log.info("User {} has been logged in.", username);
        currentUser = users.get(username);
        return true;
    }

    /**
     * Method to get the current user
     * @return Current user
     */
    public static User getCurrentUser() {
        if (currentUser == null) {
            log.error("No user is logged in.");
            throw new IllegalArgumentException("No user is logged in.");
        } else {
            log.info("Current user is {}.", currentUser.getUsername());
            return currentUser;
        }
    }

    /**
     * Method to get a user by its username
     *
     * @param username Username of the user
     * @return User object with the given username
     * @throws IllegalArgumentException if the username is null, empty or could not be found
     */
    public static User getUserByName(String username) throws IllegalArgumentException {
        if (username == null || username.isEmpty()) {
            log.warn("Username is null or empty.");
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        if (!users.containsKey(username)) {
            log.error("Username {} could not be found.", username);
            throw new IllegalArgumentException("Username " + username + " could not be found.");
        }
        return users.get(username);
    }

    /**
     * Method to get a user by its ID
     *
     * @param userID ID of the user
     * @return User object with the given ID
     * @throws IllegalArgumentException if the userID is null, empty or could not be found
     */
    public static User getUserById(String userID) throws IllegalArgumentException {
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
     * @return Map of all users objects (key = username, value = user object)
     */
    public static Map<String, User> getAllUsers() {
        return users;
    }

    /**
     * Method to write to a JSON file via Gson
     *
     * @link UserSerializer (Custom serializer for the User class)
     * @see FileWriter
     * @see Gson
     * @see GsonBuilder
     */
    //Todo: Check if the log states are correct -> Should be
    //Todo: Check if exception handling is correct
    public static void userToJSON() throws WriteDataException {
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
     * @link UserSerializer (Custom serializer for the User class)
     * @see FileReader
     * @see Gson
     * @see GsonBuilder
     */
    //Todo: Check if the log states are correct -> Should be
    //Todo: Check if exception handling is correct
    public static void usersFromJSON() throws ReadDataException {
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