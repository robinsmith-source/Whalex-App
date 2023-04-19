package org.example.media.extentions;

import javafx.scene.media.Media;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.interfaces.ISound;

public class Database implements ISound {
    private static final Logger log = LogManager.getLogger(Database.class);

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
}
