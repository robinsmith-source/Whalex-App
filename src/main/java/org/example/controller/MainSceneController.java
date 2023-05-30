package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.profile.UserManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

    private static final Logger log = LogManager.getLogger(MainSceneController.class);

    @FXML
    private BorderPane border;

    @FXML
    private Label username;

    @FXML
    private ImageView userProfilePicture;

    private ViewController viewController;

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
}
