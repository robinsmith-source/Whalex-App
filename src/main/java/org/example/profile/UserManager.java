package org.example.profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to manage all users
 *
 * @link User (1 to n relation : Many users are managed by the UserManager)
 */
public class UserManager {

    private static final Logger log = LogManager.getLogger(UserManager.class);

    /**
     * counter of the userID
     */
    private static int counterID = 20000;

    /**
     * Map of all users (key = username, value = user)
     */
    private static Map<String, User> users = new HashMap<>();

    /**
     * Method to create a user by its username and password
     *
     * @param username Username of the user
     * @param password Password of the user
     * @return true if the user has been created, false if the user already exists
     */
    public static boolean createUser(String username, String password) {
        if (users.containsKey(username)) {
            log.error("User " + username + " already exists.");
            return false;
        }
        users.put(username, new User(++counterID, username, password));
        log.info("User " + username + " has been created.");
        return true;
    }

    /**
     * Method to delete a user by its username
     *
     * @param username Username of the user
     * @return true if the user has been deleted, false if the user does not exist
     * TODO: Check if the log state is correct
     */
    public static boolean deleteUser(String username) {
        if (!users.containsKey(username)) {
            log.error("User " + username + " does not exist.");
            return false;
        }
        users.remove(username);
        log.info("User " + username + " has been deleted.");
        return true;
    }

    /**
     * Method to get a user by its username
     *
     * @param username Username of the user
     * @return User with the given username
     */
    public static User getUser(String username) {
        return users.get(username);
    }

    /**
     * Method to get all users
     *
     * @return ArrayList of all users
     */
    public static ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Method to write to a JSON file via Gson
     *
     * @link UserTypeAdapter
     * @see Gson
     * @see GsonBuilder
     */
    public static void usersToJSON() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new UserTypeAdapter())
                .setPrettyPrinting()
                .create();
        try (FileWriter fileWriter = new FileWriter("src/main/resources/data/users.json")) {
            gson.toJson(users, fileWriter);
            log.info("Users have been saved to JSON.");
        } catch (IOException e) {
            e.printStackTrace();
            log.fatal("Users have not been saved to JSON.");
        }
    }

    /**
     * Method to read a JSON file via Gson
     *
     * @link UserTypeAdapter
     * @see Gson
     * @see GsonBuilder
     */
    public static void usersFromJSON() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new UserTypeAdapter())
                .setPrettyPrinting()
                .create();
        try (FileReader fileReader = new FileReader("src/main/resources/data/users.json")) {
            users = gson.fromJson(fileReader, new TypeToken<Map<String, User>>(){}.getType());
            log.info("Users have been loaded from JSON.");
        } catch (IOException e) {
            e.printStackTrace();
            log.fatal("Users have not been loaded from JSON.");
        }
        System.out.println(users);
    }
}
