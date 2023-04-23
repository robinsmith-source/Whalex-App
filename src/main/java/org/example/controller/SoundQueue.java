package org.example.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;
import org.example.playlist.Playlist;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * SoundQueue class is used to handle all soundQueue actions
 * @link Order (1 to 1 : Each SoundQueue can only have one Order)
 */
public class SoundQueue {
    private static final Logger log = LogManager.getLogger(SoundQueue.class);

    /**
     * soundQueue of the player
     */
    private final Deque<ISound> soundQueue;

    /**
     * order of the soundQueue : ORDERED, SHUFFLED
     */
    private Order order;

    /**
     * Constructor of the SoundQueue
     * Default order is ORDERED
     */
    public SoundQueue() {
        this.soundQueue = new ArrayDeque<>();
        this.order = Order.ORDERED;
    }

    /**
     * Method to set the order of the soundQueue
     * @param order Order of the soundQueue : ORDERED, SHUFFLED
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * Method to get the order of the soundQueue
     * @return Order of the soundQueue : ORDERED, SHUFFLED
     */
    public Order getOrder() {
        return this.order;
    }

    /**
     * Method to get the current sound in the soundQueue
     * @return Current sound in the soundQueue
     */
    public ISound getCurrentSound() {
        return null;
    }

    /**
     * Method to get the next sound in the soundQueue
     * @return Next sound in the soundQueue
     */
    public ISound getNextSound() {
        return null;
    }

    /**
     * Method to add a sound to the soundQueue
     * @param sound Sound to be added to the soundQueue
     */
    public void addSound(ISound sound) {}

    /**
     * Method to remove a sound from the soundQueue
     * @param sound Sound to be removed from the soundQueue
     */
    public void removeSound(ISound sound) {}

    /**
     * Method to add a playlist to the soundQueue
     * @param playlist Playlist to be added to the soundQueue
     */
    public void addPlaylist(Playlist playlist) {}

    /**
     * Method to remove a playlist from the soundQueue
     * @param playlist Playlist to be removed from the soundQueue
     */
    public void removePlaylist(Playlist playlist) {}

    /**
     * Method to override the soundQueue with a playlist
     * @param playlist Playlist to override the soundQueue
     */
    public void overrideWithPlaylist(Playlist playlist) {}

    /**
     * Method to clear the soundQueue
     */
    public void clear() {}
}
