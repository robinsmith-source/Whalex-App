package org.example.controller;

import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;

public class Player {
    private static final Logger log = LogManager.getLogger(Player.class);

    //true : playing | false : paused
    private boolean isPlaying;

    //true : shuffle | false : no shuffle
    private boolean isShuffle;

    //true : repeat | false : no repeat
    private boolean isRepeat;

    //true : mute | false : no mute
    private boolean isMute;

    //volume value between 0 and 1
    private float volume;

    //CurrentSound playing
    private ISound currentSound;

    //PreviousSound played
    private ISound previousSound;

    //NextSound to play
    private ISound nextSound;

    //SoundQueue of sounds to play
    private final SoundQueue soundQueue;

    //SoundHistory of sounds that have been played
    private final SoundHistory soundHistory;

    //MediaPlayer Object from JavaFX Media Library
    //TODO : Connect with MediaPlayer
    private MediaPlayer mediaPlayer;

    /**
     * Constructor of the Player
     * TODO : Connect with SoundQueue
     */
    public Player() {
        this.isPlaying = false;
        this.isShuffle = false;
        this.isRepeat = false;
        this.isMute = false;
        this.volume = 0.5f;
        this.currentSound = null;
        this.previousSound = null;
        this.nextSound = null;
        this.soundQueue = new SoundQueue();
        this.soundHistory = new SoundHistory();
    }

    /**
     * @return true if the player is playing, false if it is paused
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * @return true if the player is in shuffle mode, false if it is not
     */
    public boolean isShuffle() {
        return isShuffle;
    }

    /**
     * @return true if the player is in repeat mode, false if it is not
     */
    public boolean isRepeat() {
        return isRepeat;
    }

    /**
     * @return true if the player is muted, false if it is not
     */
    public boolean isMute() {
        return isMute;
    }

    /**
     * @return volume value between 0 and 1
     */
    public float getVolume() {
        return volume;
    }

    /**
     * @return current sound playing
     */
    public ISound getCurrentSound() {
        return currentSound;
    }

    /**
     * @return previous sound played
     */
    public ISound getPreviousSound() {
        return previousSound;
    }

    /**
     * @return next sound to play
     */
    public ISound getNextSound() {
        return nextSound;
    }

    //Todo : Implement methods to play, pause, next, previous, seekTo
    public void play() {}
    public void pause() {}
    public void next() {}
    public void previous() {}
    public void seekTo(float time) {}
}
