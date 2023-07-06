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
import org.example.data.DataOperation;
import org.example.data.DataThread;
import org.example.data.DataType;

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
        loadingBar.progressProperty().bind(loadingTask.progressProperty());
        loadingTask.setOnSucceeded(taskSucceededTask -> {
            FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), logo);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
            fadeIn.setOnFinished(transitionFinishedEvent -> {
                log.info("Loading screen finished loading.");
                try {
                    SceneManager.LOGIN.changeScene();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        });
        loadingTask.run();
    }

    /**
     * Task to load sound and playlist data from JSON file while loading animation.
     */
    final Task<Void> loadingTask = new Task<>() {
        @Override
        public Void call() {
            new DataThread(DataType.USER_SOUND_PLAYLIST, DataOperation.READ).start();
            return null;
        }
    };
}
