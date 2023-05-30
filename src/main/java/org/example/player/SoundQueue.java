package org.example.player;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;
import org.example.playlist.Playlist;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;

/**
 * SoundQueue class is used to handle all soundQueue actions
 * @link Order (1 to 1 : Each SoundQueue can only have one Order)
 */
public class SoundQueue {
    private static final Logger log = LogManager.getLogger(SoundQueue.class);


    /**
     * soundQueue of the player
     */
    private final Deque<ISound> soundQueue = new ArrayDeque<>();

    /**
     * order of the soundQueue : ORDERED, SHUFFLED
     */
    private Order order = Order.ORDERED;

    /**
     * Constructor of the SoundQueue
     * Default order is ORDERED
     */
    SoundQueue() {
    }

    public ArrayList<ISound> getSounds() {
        return new ArrayList<>(soundQueue);
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
        return soundQueue.getFirst();
    }

    /**
     * Method to get the next sound in the soundQueue
     * @return Next sound in the soundQueue
     */
    public ISound getNextSound() {
        Iterator<ISound> iterator = soundQueue.iterator();
        iterator.next();
        return iterator.next();
    }

    /**
     * Method to add a sound to the soundQueue
     * @param sound Sound to be added to the soundQueue
     */
    public void addSound(ISound sound) {
        soundQueue.add(sound);
    }

    /**
     * Method to remove a sound from the soundQueue
     * @param sound Sound to be removed from the soundQueue
     */
    public void removeSound(ISound sound) {
        soundQueue.remove(sound);
    }

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
    public void overrideWithPlaylist(Playlist playlist) {
        soundQueue.clear();

        soundQueue.addAll(playlist.getSounds());
        addPlaylist(playlist);
    }
}
