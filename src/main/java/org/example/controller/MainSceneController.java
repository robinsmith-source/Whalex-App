package org.example.controller;

import javafx.application.Platform;
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
        log.info("Initializing MainSceneController");
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
        updateTimer.scheduleAtFixedRate(updateTask, 0, 1000);
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
        viewController.updateView();
        log.info("Showing all content by user");
    }

    /**
     * Method to show all content
     */
    public void showAllContent() {
        viewController.setView(ViewType.ALL);
        viewController.updateView();
        log.info("Showing all content");
    }

    public void handlePlayButton() {
        Player.getInstance().playPause();
        log.debug("Trying to play/pause sound");
    }

    public void handlePreviousButton() {
        Player.getInstance().previous();
        log.debug("Trying to play previous sound");
    }

    public void handleNextButton() {
        Player.getInstance().next();
        log.debug("Trying to play next sound");
    }

    //TODO: Only for debugging --> Implement correctly -- should be done
    private void updatePlayerContent() {
        if (Player.getInstance().getSoundQueue().isEmpty()) {
            soundProgress.setProgress(0);
            totalSoundTime.setText("00:00");
            currentSoundTime.setText("00:00");
            currentSoundTitle.setText("");
            currentSoundUploadedBy.setText("");
        } else {
            currentSoundTitle.setText(Player.getInstance().getSoundQueue().getFirst().getTitle());
            currentSoundUploadedBy.setText(Player.getInstance().getSoundQueue().getFirst().getUploadedBy().getUsername());
            double progress = Player.getInstance().getCurrentTime() / Player.getInstance().getTotalTime();
            soundProgress.setProgress(progress);
            int totalTimeInt = (int) Player.getInstance().getTotalTime();
            int currentTimeInt = (int) Player.getInstance().getCurrentTime();
            totalSoundTime.setText(String.format("%02d:%02d", totalTimeInt / 60, totalTimeInt % 60));
            currentSoundTime.setText(String.format("%02d:%02d", currentTimeInt / 60, currentTimeInt % 60));
        }
    }

    public void checkSearchQuery() {
        log.debug("Checking search query {}.", searchBar.getText());
        viewController.handleSearchBar(searchBar.getText());
    }

    public void handleShuffleButton() {
        Player.getInstance().shuffle();
    }

    public void handleRepeatButton() {
    }

    public void updateView() {
        viewController.updateView();
        log.info("View updated access from MainSceneController (Added Playlist)");
    }

    public void handleSettingsButton() {
        SceneManager.EDIT_PROFILE.showSecondaryStage();
    }

    public void handleLogoutButton() {
        UserManager.getInstance().logout();
        SceneManager.LOGIN.changeScene();
    }
}
