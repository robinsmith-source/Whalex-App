package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.SoundManager;
import org.example.media.interfaces.ISound;
import org.example.playlist.Playlist;
import org.example.playlist.PlaylistManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class viewController implements Initializable {

    private static final Logger log = LogManager.getLogger(viewController.class);

    @FXML
    private HBox soundPane;
    @FXML
    private HBox playlistPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ArrayList<ISound> sounds = new ArrayList<>(SoundManager.getInstance().getAllSounds().values());
        ArrayList<Playlist> playlists = new ArrayList<>(PlaylistManager.getInstance().getAllPlaylists().values());
        for (int i = 0; i < 50; i++) {

            for (ISound sound : sounds) {
                VBox soundBox;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sounds.fxml"));
                    soundBox = loader.load();
                    SoundController controller = loader.getController();
                    controller.setData(sound);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                soundPane.getChildren().add(soundBox);
            }
        }
        for (Playlist playlist : playlists) {
            VBox playlistBox;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/playlists.fxml"));
                playlistBox = loader.load();
                PlaylistController controller = loader.getController();
                controller.setData(playlist);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            playlistPane.getChildren().add(playlistBox);
        }
    }
}
