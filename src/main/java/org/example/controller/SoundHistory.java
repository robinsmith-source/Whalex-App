package org.example.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;

import java.util.Stack;

public class SoundHistory {

    private static final Logger log = LogManager.getLogger(SoundHistory.class);

    //TODO : Implement soundHistory as Stack (First in First Out)
    //soundHistory of Sounds that have been played
    private final Stack<ISound> soundHistory;

    /**
     * Constructor of the SoundHistory
     */
    public SoundHistory() {
        this.soundHistory = new Stack<>();
    }

    /**
     * @return Next sound in the soundHistory (Previous sound of soundQueue)
     * @see SoundQueue
     */
    public ISound getNextSound() {
        return null;
    }

    /**
     * @param sound Sound to be added to the soundHistory
     */
    public void addSound(ISound sound) {}

    /**
     * @param sound Sound to be removed from the soundHistory
     */
    public void removeSound(ISound sound) {}

    /**
     * Clears the soundHistory
     */
    public void clear() {}
}
