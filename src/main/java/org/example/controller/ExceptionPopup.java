package org.example.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ExceptionPopup {

    private static final Logger log = LogManager.getLogger(ExceptionPopup.class);

    protected void showPopup(Exception e) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(5),
                ae -> alert.close()));
        alert.setTitle("Whalex - Exception");
        alert.setHeaderText(e.getClass().getSimpleName());
        alert.setContentText(e.getMessage());
        alert.show();
        log.info("Exception: {} - {}", e.getClass().getSimpleName(), e.getMessage());
        timeline.play();
    }
}
