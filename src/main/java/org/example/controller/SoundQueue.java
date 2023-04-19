package org.example.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;
import org.example.playlist.Playlist;

import java.util.Deque;
import java.util.LinkedList;

public class SoundQueue {
    private static final Logger log = LogManager.getLogger(SoundQueue.class);

    //TODO : Implement soundQueue as ArrayDeque (Last in First Out)
    //soundQueue of Sounds to be played
    private final Deque<ISound> soundQueue;


    //Order of the soundQueue : ORDERED, SHUFFLED
    private Order order;

    /**
     * Constructor of the SoundQueue
     * Default order is ORDERED
     * TODO : Connect with Player
     */
    public SoundQueue() {
        this.soundQueue = new LinkedList<>();
        this.order = Order.ORDERED;
    }

    /**
     * @param order Order of the soundQueue : ORDERED, SHUFFLED
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * @return Order of the soundQueue : ORDERED, SHUFFLED
     */
    public Order getOrder() {
        return this.order;
    }

    /**
     * @return Current sound in the soundQueue
     */
    public ISound getCurrentSound() {
        return null;
    }

    /**
     * @return Next sound in the soundQueue
     */
    public ISound getNextSound() {
        return null;
    }

    /**
     * @param sound Sound to be added to the soundQueue
     */
    public void addSound(ISound sound) {}

    /**
     * @param sound Sound to be removed from the soundQueue
     */
    public void removeSound(ISound sound) {}

    /**
     * @param playlist Playlist to be added to the soundQueue
     */
    public void addPlaylist(Playlist playlist) {}

    /**
     * @param playlist Playlist to be removed from the soundQueue
     */
    public void removePlaylist(Playlist playlist) {}

    /**
     * @param playlist Playlist to override the soundQueue
     */
    public void overrideWithPlaylist(Playlist playlist) {}

    /**
     * Clears the soundQueue
     */
    public void clear() {}
}
