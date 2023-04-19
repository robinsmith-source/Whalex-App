package org.example.media.interfaces;

import javafx.scene.media.Media;

public interface ISound {
    /**
     * @return Title of the sound
     */
    String getTitle();

    /**
     * @return Filepath to the sound
     */
    Media mediaPath();
}
