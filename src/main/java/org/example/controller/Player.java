package org.example.controller;

import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;

/**
 * Player class is used to handle all media player actions
 *
 * @link SoundQueue (1 to 1 : Each Player can only have one SoundQueue)
 * @link SoundHistory (1 to 1 : Each Player can only have one SoundHistory)
 * TODO : Write tests for the Player
 * @see MediaPlayer
 * @see ISound
 */
public class Player {
    private static final Logger log = LogManager.getLogger(Player.class);

    /**
     * isPlaying is true if the player is playing, false if it is paused
     */
    private boolean isPlaying;

    /**
     * isShuffle is true if the player is shuffling, false if it is not
     */
    private boolean isShuffle;

    /**
     * isRepeat is true if the player is repeating, false if it is not
     */
    private boolean isRepeat;

    /**
     * isMute is true if the player is muted, false if it is not
     */
    private boolean isMute;

    /**
     * volume of the player (between 0 and 1)
     */
    private double volume;


    /**
     * current Sound playing
     */
    private ISound currentSound;

    /**
     * previous Sound played
     */
    private ISound previousSound;


    /**
     * next Sound to play
     */
    private ISound nextSound;


    /**
     * SoundQueue of the player
     *
     * @link SoundQueue
     */
    private final SoundQueue soundQueue;

    /**
     * SoundHistory of the player
     *
     * @link SoundHistory
     */
    private final SoundHistory soundHistory;

    /**
     * MediaPlayer object from JavaFX Media Library
     * TODO : Connect with MediaPlayer
     * @see MediaPlayer
     */
    private MediaPlayer mediaPlayer;

    /**
     * Constructor of the Player
     */
    public Player() {
        this.soundQueue = new SoundQueue();
        this.soundHistory = new SoundHistory();
        this.mediaPlayer = new MediaPlayer(soundQueue.getCurrentSound().getMedia());
        this.isPlaying = false;
        this.isShuffle = soundQueue.getOrder() == Order.SHUFFLED;
        this.isRepeat = false;
        this.isMute = false;
        this.volume = 0.5f;
        this.currentSound = null;
        this.previousSound = null;
        this.nextSound = null;
    }

    /**
     * @return true if the player is playing, false if it is not
     */
    public boolean isPlaying() {
        return this.isPlaying;
    }

    /**
     * @return true if the player is in shuffle mode, false if it is not
     */
    public boolean isShuffle() {
        return this.isShuffle;
    }

    /**
     * @return true if the player is in repeat mode, false if it is not
     */
    public boolean isRepeat() {
        return this.isRepeat;
    }

    /**
     * @return true if the player is muted, false if it is not
     */
    public boolean isMute() {
        return this.isMute;
    }

    /**
     * @return volume value between 0 and 1
     */
    public double getVolume() {
        return this.volume;
    }

    /**
     * @return current sound playing
     */
    public ISound getCurrentSound() {
        return this.currentSound;
    }

    /**
     * @return previous sound played
     */
    public ISound getPreviousSound() {
        return this.previousSound;
    }

    /**
     * @return next sound to play
     */
    public ISound getNextSound() {
        return this.nextSound;
    }

    //Todo : Implement methods to play, pause, next, previous, seekTo
    public void play() {
    }

    public void pause() {
    }

    public void next() {
    }

    public void previous() {
    }

    public void seekTo(float time) {
    }
}