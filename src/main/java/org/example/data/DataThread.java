package org.example.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controller.SceneManager;
import org.example.exceptions.ReadDataException;
import org.example.exceptions.WriteDataException;
import org.example.media.SoundManager;
import org.example.playlist.PlaylistManager;
import org.example.profile.UserManager;

public class DataThread extends Thread {
    private static final Logger log = LogManager.getLogger(SceneManager.class);
    private final DataOperation readOrWrite;
    private final DataType dataType;

    public DataThread(DataType dataType, DataOperation readOrWrite) {
        log.info("SaveData was created");
        this.dataType = dataType;
        this.readOrWrite = readOrWrite;
    }

    @Override
    public void run() {
        try {
            switch (readOrWrite) {
                case READ:
                    switch (dataType) {
                        case USER_SOUND_PLAYLIST:
                            UserManager.getInstance().usersFromJSON();
                        case SOUND_PLAYLIST:
                            SoundManager.getInstance().soundsFromJSON();
                        case PLAYLIST:
                            PlaylistManager.getInstance().playlistsFromJSON();
                            break;
                    }
                    break;
                case WRITE:
                    switch (dataType) {
                        case USER_SOUND_PLAYLIST:
                            UserManager.getInstance().usersToJSON();
                        case SOUND_PLAYLIST:
                            SoundManager.getInstance().soundsToJSON();
                        case PLAYLIST:
                            PlaylistManager.getInstance().playlistsToJSON();
                            break;
                    }
                    break;
            }
        } catch (ReadDataException e) {
            log.fatal("Error while reading data", e);
            Thread.currentThread().interrupt();
            System.exit(1);
        } catch (WriteDataException e) {
            log.fatal("Error while writing data", e);
            Thread.currentThread().interrupt();
            System.exit(1);
        }
    }
}
