package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
    private VBox soundPane1;
    @FXML
    private VBox soundPane2;

    @FXML
    private VBox playlistPane1;
    @FXML
    private VBox playlistPane2;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ArrayList<ISound> sounds = new ArrayList<>(SoundManager.getInstance().getAllSounds().values());
        ArrayList<Playlist> playlists = new ArrayList<>(PlaylistManager.getInstance().getAllPlaylists().values());
        sounds.sort((o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()));
        playlists.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        int modCount = 0;
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
                if (modCount % 2 == 0) {
                    soundPane1.getChildren().add(soundBox);
                } else {
                    soundPane2.getChildren().add(soundBox);
                }
                modCount++;
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
                if (modCount % 2 == 0) {
                    playlistPane1.getChildren().add(playlistBox);
                } else {
                    playlistPane2.getChildren().add(playlistBox);
                }
                modCount++;
            }
        }
    }
}