package org.example.controller;

import org.example.media.interfaces.ISound;
import org.example.playlist.Playlist;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class SoundQueue {

    //TODO : Implement soundQueue as LinkedList (Last in First Out)
    //soundQueue of Sounds to be played
    private final Queue<ISound> soundQueue;

    //TODO : Implement soundHistory as Stack (First in First Out)
    //soundHistory of Sounds that have been played
    private final Stack<ISound> soundHistory;

    //Order of the soundQueue : ORDERED, SHUFFLED
    private Order order;

    /**
     * Constructor of the SoundQueue
     * Default order is ORDERED
     * TODO : Connect with Player
     */
    public SoundQueue() {
        this.soundQueue = new LinkedList<>();
        this.soundHistory = new Stack<>();
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
