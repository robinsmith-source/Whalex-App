package org.example.media.interfaces;

import javafx.scene.media.Media;
import org.example.profile.User;

public interface ISound {
    /**
     * Method to get the soundID of the sound
     *
     * @return soundID of the sound
     */
    String getSoundID();

    /**
     * Method to get the title of the sound
     *
     * @return Title of the sound
     */
    String getTitle();

    /**
     * Method to get the media object of the sound
     *
     * @return Media object of the sound
     */
    Media getMedia();

    /**
     * Method to get the user who uploaded the sound
     *
     * @return User who uploaded the sound
     */
    User getUploadedBy();

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
