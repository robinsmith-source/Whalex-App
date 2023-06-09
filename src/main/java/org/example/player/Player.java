package org.example.player;

import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;
import org.example.playlist.Playlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

public class Player {
    private static final Logger log = LogManager.getLogger(Player.class);
    private static final Player INSTANCE = new Player();
    private final LinkedList<ISound> soundQueueOrdered;
    private final LinkedList<ISound> soundQueueShuffled;
    private final Stack<ISound> soundHistory;
    private MediaPlayer mediaPlayer;

    private Order queueOrder = Order.ORDERED;


    ISound currentSound;

    private Player() {
        soundQueueOrdered = new LinkedList<>();
        soundQueueShuffled = new LinkedList<>();
        soundHistory = new Stack<>();
    }

    public static Player getInstance() {
        return INSTANCE;
    }

    public void addSoundToQueue(ISound sound) {
        this.soundQueueOrdered.add(sound);
    }

    public void removeSoundFromQueue(ISound sound) {
        this.soundQueueOrdered.remove(sound);
        log.info("Removed sound {} from queue", sound.getTitle());
    }

    public void addPlaylistToQueue(Playlist playlist) {
        this.soundQueueOrdered.addAll(playlist.getSounds());
    }


    private void setMediaPlayer() {
        if (currentSound == null) {
            throw new IllegalStateException("Current sound is null");
        }
        mediaPlayer = new MediaPlayer(currentSound.getMedia());
        mediaPlayer.setOnEndOfMedia(() -> {


            log.info("Refreshed main scene");
            mediaPlayer.dispose();
            mediaPlayer = null;
            if (!soundQueueOrdered.isEmpty()) {
                next();
            }
            /*
            if (onNextSongListener != null) {
                onNextSongListener.onEvent();
            }

             */
        });
        mediaPlayer.play();
        log.info("Playing sound: " + currentSound.getTitle());
    }

    public void playPause() {
        if (this.soundQueueOrdered.isEmpty()) {
            log.info("Sound queue is empty");
            return;
        }

        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            log.info("Sound {} has been paused", currentSound.getTitle());
            mediaPlayer.pause();
            return;
        } else if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            log.info("Sound {} has been resumed", currentSound.getTitle());
            mediaPlayer.play();
            return;
        }

        this.currentSound = this.soundQueueOrdered.peek();
        setMediaPlayer();
    }


    public void next() {
        soundQueueOrdered.remove(currentSound);
        soundHistory.add(currentSound);


        if (mediaPlayer != null) {
            mediaPlayer.dispose();
            mediaPlayer = null;
        }

        if (soundQueueOrdered.isEmpty()) {
            log.info("Sound queue is empty");
            return;
        }
        this.currentSound = this.soundQueueOrdered.peek();
        setMediaPlayer();
    }

    public void previous() {
        if (this.soundHistory.isEmpty()) {
            log.info("Sound history is empty");
            return;
        }

        if (mediaPlayer != null) {
            mediaPlayer.dispose();
            mediaPlayer = null;
        }

        this.soundQueueOrdered.addFirst(soundHistory.pop()); // Füge den abgespielten Sound zur History hinzu
        this.currentSound = this.soundQueueOrdered.peek(); // Nimm den ersten Sound aus der Warteschlange
        setMediaPlayer();
    }

    public double getCurrentTime() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentTime().toSeconds();
        }
        return 0;
    }

    public double getTotalTime() {
        if (mediaPlayer != null) {
            return mediaPlayer.getTotalDuration().toSeconds();
        }
        return 0;
    }

    public void clearQueue() {
        this.soundQueueOrdered.clear();
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
    }

    private OnNextSongListener onNextSongListener;

    public void registerOnNextSongEvent(OnNextSongListener listener) {
        this.onNextSongListener = listener;
    }


    public ISound getCurrentSound() {
        return this.currentSound;
    }

    public LinkedList<ISound> getSoundQueue() {
        switch (queueOrder) {
            case ORDERED:
                return soundQueueOrdered;
            case SHUFFLED:
                return soundQueueShuffled;
            default:
                return soundQueueOrdered;
        }
    }

    public ArrayList<ISound> getSoundHistory() {
        return new ArrayList<>(soundHistory);
    }

    public void setVolume(double value) {
        mediaPlayer.setVolume(value);
        System.out.println("Mediaplayer vol: " + mediaPlayer.getVolume());
    }

    public MediaPlayer.Status getStatus() {
        return mediaPlayer.getStatus();
    }

    public void shuffle() {
        if (queueOrder == Order.SHUFFLED) {
            queueOrder = Order.ORDERED;
        } else {
            queueOrder = Order.SHUFFLED;
        soundQueueShuffled.clear();
        soundQueueShuffled.addAll(soundQueueOrdered);
        Collections.shuffle(soundQueueShuffled);
            System.out.println(soundQueueOrdered);
            System.out.println(soundQueueShuffled);

        }
    }
}
