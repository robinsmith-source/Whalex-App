package org.example.media.interfaces;

import java.io.File;

public interface ISound {

    /**
     * @return Title of the sound
     */
    public String getTitle();

    /**
     * @return Filepath to the sound
     */
    public File filePath();

    /**
     * @return Duration of the sound in seconds
     */
    public int getDuration();
}
