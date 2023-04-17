package org.example.media.interfaces;

import javafx.scene.media.Media;

public interface ISound {

    /**
     * @return Title of the sound
     */
    public String getTitle();

    /**
     * @return Filepath to the sound
     */
    public Media mediaPath();

    /**
     * @return Duration of the sound in seconds
     */
    public int getDuration();
}
