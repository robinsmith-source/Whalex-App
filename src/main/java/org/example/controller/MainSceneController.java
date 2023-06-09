package org.example.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.player.Player;
import org.example.profile.UserManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MainSceneController implements Initializable {

    private static final Logger log = LogManager.getLogger(MainSceneController.class);
    @FXML
    private Slider volumeSlider;
    @FXML
    private Label totalSoundTime;
    @FXML
    private Label currentSoundTime;

    @FXML
    private BorderPane border;

    @FXML
    private Label username;

    @FXML
    private ImageView userProfilePicture;

    private ViewController viewController;

    @FXML
    private Label currentSoundTitle;
    @FXML
    private Label currentSoundUploadedBy;

    @FXML
    private ProgressBar soundProgress;

    @FXML
    private TextField searchBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText(UserManager.getInstance().getActiveUser().getUsername());
        Image image = new Image(UserManager.getInstance().getActiveUser().getProfilePicture().toURI().toString());
        userProfilePicture.setImage(image);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view.fxml"));
            loader.load();
            this.viewController = loader.getController();
            this.border.setCenter(loader.getRoot());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.debug("MainSceneController initialized");

        //TODO: FIX THIS!!
        Timer updateTimer = new Timer();
        TimerTask updateTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> updatePlayerContent());
            }
        };
        updateTimer.scheduleAtFixedRate(updateTask, 0, 100);
        volumeSlider.valueProperty().addListener((ov, old_val, new_val) -> Platform.runLater(() -> {
            Player.getInstance().setVolume(volumeSlider.getValue());
            log.info("Volume changed to: " + volumeSlider.getValue());

        }));
    }

    /**
     * Method to show all content by the current user
     */
    public void showAllContentByUser() {

        viewController.setView(ViewType.USER);
    }

    /**
     * Method to show all content
     */
    public void showAllContent() {
        viewController.setView(ViewType.ALL);
    }

    public void handlePlayButton() {
        Player.getInstance().playPause();
    }

    public void handlePreviousButton() {
        Player.getInstance().previous();
        log.info("Previous sound");
    }

    public void handleNextButton() {
        Player.getInstance().next();
        log.info("Next sound");
    }

    //TODO: Only for debugging --> Implement correctly
    private void updatePlayerContent() {
        if (Player.getInstance().getCurrentSound() != null) {
            currentSoundTitle.setText(Player.getInstance().getCurrentSound().getTitle());
            currentSoundUploadedBy.setText(Player.getInstance().getCurrentSound().getUploadedBy().getUsername());
            double progress = Player.getInstance().getCurrentTime() / Player.getInstance().getTotalTime();
            soundProgress.setProgress(progress);
            int totalTimeInt = (int) Player.getInstance().getTotalTime();
            int currentTimeInt = (int) Player.getInstance().getCurrentTime();
            totalSoundTime.setText(String.format("%02d:%02d",totalTimeInt / 60, totalTimeInt % 60));
            currentSoundTime.setText(String.format("%02d:%02d",currentTimeInt / 60, currentTimeInt % 60));
        } else {
            soundProgress.setProgress(0);
            totalSoundTime.setText("00:00");
            currentSoundTime.setText("00:00");
            currentSoundTitle.setText("");
            currentSoundUploadedBy.setText("");
        }
    }

    public void checkSearchQuery() {
        log.debug("Checking search query {}.", searchBar.getText());
        viewController.handleSearchBar(searchBar.getText());
    }

    public void handleShuffleButton(ActionEvent actionEvent) {
        Player.getInstance().shuffle();
    }

    public void handleRepeatButton(ActionEvent actionEvent) {
    }
}
