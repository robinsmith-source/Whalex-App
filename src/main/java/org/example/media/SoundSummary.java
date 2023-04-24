package org.example.media;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;
import org.example.profile.User;
import org.example.profile.UserManager;

import java.util.ArrayList;

/**
 * Class to get all sounds from all users and summarize them
 *
 * @link UserManager (1 to 1 relation : Each SoundSummary has one UserManager to iterate through all users)
 * @link SoundManager (1 to n relation : Each SoundSummary has many SoundManagers to iterate through all sounds created by a user)
 * TODO : Optionally add other methods to summarize ALL sounds e.g. search for a sound by name
 */
public class SoundSummary {
    private static final Logger log = LogManager.getLogger(SoundSummary.class);

    /**
     * @return ArrayList of all sounds existing from all users
     */
    public static ArrayList<ISound> getAllSounds() {
        ArrayList<ISound> allSounds = new ArrayList<>();
        for (User user : UserManager.getAllUsers()) {
            allSounds.addAll(user.getSoundManager().getSoundsByUser());
        }
        return allSounds;
    }
}
