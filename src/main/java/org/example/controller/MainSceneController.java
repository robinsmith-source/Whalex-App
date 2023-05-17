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
    BorderPane border;

    @FXML
    Label username;

    @FXML
    ImageView userProfilePicture;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        username.setText(UserManager.getInstance().getCurrentUser().getUsername());
        Image image = new Image(UserManager.getInstance().getCurrentUser().getProfilePicture().toURI().toString());
        userProfilePicture.setImage(image);
        try {
            border.setCenter(FXMLLoader.load(getClass().getResource("/fxml/view.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
