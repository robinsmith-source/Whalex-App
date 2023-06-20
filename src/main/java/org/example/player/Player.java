package org.example.player;

import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controller.ExceptionPopup;
import org.example.media.interfaces.ISound;
import org.example.playlist.Playlist;

import java.util.LinkedList;

public class Player extends ExceptionPopup {
    private static final Logger log = LogManager.getLogger(Player.class);
    private static final Player INSTANCE = new Player();
    private final LinkedList<ISound> soundQueueOrdered;
    private final LinkedList<ISound> soundHistory;
    private MediaPlayer mediaPlayer;

    ISound currentSound;

    private Player() {
        soundQueueOrdered = new LinkedList<>();
        soundHistory = new LinkedList<>();
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


    private void setMediaplayer() {
        if (currentSound == null) {
            throw new IllegalStateException("Current sound is null");
        }
        mediaPlayer = new MediaPlayer(currentSound.getMedia());
        mediaPlayer.setOnEndOfMedia(() -> {
            if (!soundQueueOrdered.isEmpty()) {
                next();
            }

            if (onNextSongListener != null) {
                onNextSongListener.onEvent();
            }

        });
        mediaPlayer.play();
        log.info("Playing sound: " + currentSound.getTitle());
    }

    public void playPause() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            log.debug("Sound {} has been paused", currentSound.getTitle());
            mediaPlayer.pause();
            return;
        } else if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            log.debug("Sound {} has been resumed", currentSound.getTitle());
            mediaPlayer.play();
            return;
        }

        if (this.soundQueueOrdered.isEmpty()) {
            log.debug("Sound queue is empty");
        } else {
            this.currentSound = this.soundQueueOrdered.peek();
            setMediaplayer();
        }
    }


    public void next() {
        if (mediaPlayer != null) {
            soundHistory.addFirst(soundQueueOrdered.pop());
            mediaPlayer.dispose();
            mediaPlayer = null;
            if (onNextSongListener != null) {
                onNextSongListener.onEvent();
            }
        }

        if (soundQueueOrdered.isEmpty()) {
            log.debug("Sound queue is empty");
            return;
        }

        this.currentSound = this.soundQueueOrdered.peek();
        setMediaplayer();
    }

    public void previous() {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
            mediaPlayer = null;
            if (onNextSongListener != null) {
                onNextSongListener.onEvent();
            }
        }

        if (soundHistory.isEmpty()) {
            log.debug("Sound history is empty");
            return;
        }

        soundQueueOrdered.addFirst(soundHistory.pop());
        this.currentSound = this.soundQueueOrdered.peek();
        setMediaplayer();
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

    public LinkedList<ISound> getSoundQueue() {
        return soundQueueOrdered;
    }

    public LinkedList<ISound> getSoundHistory() {
        return soundHistory;
    }

    public void setVolume(double value) {
        mediaPlayer.setVolume(value);
        System.out.println("Mediaplayer vol: " + mediaPlayer.getVolume());
    }
}
