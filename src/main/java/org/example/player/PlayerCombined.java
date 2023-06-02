package org.example.player;

import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;
import org.example.playlist.Playlist;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

public class PlayerCombined {
    private static final Logger log = LogManager.getLogger(PlayerCombined.class);
    private static final PlayerCombined INSTANCE = new PlayerCombined();
    private final LinkedList<ISound> soundQueue;
    private final Stack<ISound> soundHistory;
    private MediaPlayer mediaPlayer;


    ISound currentSound;

    private PlayerCombined() {
        soundQueue = new LinkedList<>();
        soundHistory = new Stack<>();
    }

    public static PlayerCombined getInstance() {
        return INSTANCE;
    }

    public void addSoundToQueue(ISound sound) {
        this.soundQueue.add(sound);
    }

    public void removeSoundFromQueue(ISound sound) {
        this.soundQueue.remove(sound);
    }

    public void addPlaylistToQueue(Playlist playlist) {
        this.soundQueue.addAll(playlist.getSounds());
    }

    private void setMediaPlayer() {
        if (currentSound == null) {
            throw new IllegalStateException("Current sound is null");
        }
        mediaPlayer = new MediaPlayer(currentSound.getMedia());
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.dispose();
            mediaPlayer = null;
            if (!soundQueue.isEmpty()) {
                next();
            }
        });
        mediaPlayer.play();
        log.info("Playing sound: " + currentSound.getTitle());
    }

    public void playPause() {
        if (this.soundQueue.isEmpty()) {
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

        this.currentSound = this.soundQueue.peek();
        setMediaPlayer();
    }

    public void next() {
        if (this.soundQueue.isEmpty()) {
            log.info("Sound queue is empty");
            return;
        }
        soundQueue.remove(currentSound);
        soundHistory.add(currentSound);

        if (mediaPlayer != null) {
            mediaPlayer.dispose();
            mediaPlayer = null;
        }

        this.currentSound = this.soundQueue.peek();
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

        this.soundQueue.addFirst(soundHistory.pop()); // Füge den abgespielten Sound zur History hinzu
        this.currentSound = this.soundQueue.peek(); // Nimm den ersten Sound aus der Warteschlange
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
        this.soundQueue.clear();
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
    }

    public ISound getCurrentSound() {
        return this.currentSound;
    }

    public LinkedList<ISound> getSoundQueue() {
        return soundQueue;
    }

    public ArrayList<ISound> getSoundHistory() {
        return new ArrayList<>(soundHistory);
    }
}
