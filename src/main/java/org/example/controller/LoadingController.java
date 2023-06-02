package org.example.controller;

import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.exceptions.ReadDataException;
import org.example.media.SoundManager;
import org.example.playlist.PlaylistManager;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable {

    private static final Logger log = LogManager.getLogger(LoadingController.class);

    @FXML
    ImageView logo;

    @FXML
    ProgressBar loadingBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Thread loadingThread = new Thread(loadingTask);
        loadingBar.progressProperty().bind(loadingTask.progressProperty());
        loadingThread.start();

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), logo);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        fadeIn.setOnFinished(actionEvent -> {
            log.info("Loading screen finished loading.");
            try {
                SceneManager.MAIN.changeScene();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Task to load sound and playlist data from JSON file while loading animation.
     */
    Task<Void> loadingTask = new Task<>() {
        @Override
        public Void call() {
            try {
                SoundManager.getINSTANCE().soundsFromJSON();
                PlaylistManager.getInstance().playlistsFromJSON();
            } catch (ReadDataException e) {
                log.fatal("Failed to read data sound and playlist from JSON file.");
                System.exit(1);
            }
            return null;
        }
    };
}
