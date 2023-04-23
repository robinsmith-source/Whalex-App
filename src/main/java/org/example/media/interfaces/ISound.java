package org.example.media.interfaces;

import javafx.scene.media.Media;
import org.example.profile.User;

public interface ISound {
    /**
     * @return Title of the sound
     */
    String getTitle();

    /**
     * @return Media object of the sound
     */
    Media getMedia();

    /**
     * @return User who uploaded the sound
     */
    User uploadedBy();
}
