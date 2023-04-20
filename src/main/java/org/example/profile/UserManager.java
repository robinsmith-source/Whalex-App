package org.example.profile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to manage all users
 * @link User (1 to n relation : Many users are managed by the UserManager)
 */
public class UserManager {

    private static final Logger log = LogManager.getLogger(UserManager.class);

    /**
     * Map of all users (key = username, value = user)
     */
    private static Map<String, User> users;

    /**
     * Constructor of the UserManager
     */
    public UserManager() {
        users = new HashMap<>();
    }

    /**
     * @param username Username of the user
     * @param password Password of the user
     * @return true if the user has been created, false if the user already exists
     */
    public boolean createUser(String username, String password) {
        if (users.containsKey(username)) {
            log.error("User " + username + " already exists.");
            return false;
        }
        users.put(username, new User(username, password));
        log.info("User " + username + " has been created.");
        return true;
    }

    /**
     * @param username Username of the user
     * @return true if the user has been deleted, false if the user does not exist
     * TODO: Check if the log state is correct
     */
    public boolean deleteUser(String username) {
        if (!users.containsKey(username)) {
            log.error("User " + username + " does not exist.");
            return false;
        }
        users.remove(username);
        log.info("User " + username + " has been deleted.");
        return true;
    }

    /**
     * @return ArrayList of all users
     */
    public static ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
