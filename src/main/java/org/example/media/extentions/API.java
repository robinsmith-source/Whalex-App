package org.example.media.extentions;

import org.example.media.interfaces.ISound;

import javafx.scene.media.Media;

public class API implements ISound {
    /**
     * @return Title of the sound
     */
    @Override
    public String getTitle() {
        return null;
    }

    /**
     * @return Filepath to the sound
     */
    @Override
    public Media mediaPath() {
        return null;
    }

    /**
     * @return Duration of the sound in seconds
     */
    @Override
    public int getDuration() {
        return 0;
    }
}
