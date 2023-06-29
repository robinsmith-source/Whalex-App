package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.playlist.PlaylistManager;
import org.example.profile.UserManager;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class CreatePlaylistController implements Initializable {
    private static final Logger log = LogManager.getLogger(CreatePlaylistController.class);

    @FXML
    private Label errorMessageLabel;
    @FXML
    private ImageView userProfilePicture;
    @FXML
    private Label userUsername;
    @FXML
    private TextField playlistNameTextField;

    private final FileChooser fileChooser = new FileChooser();

    private File chosenImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(UserManager.getInstance().getActiveUser().getProfilePicture().toURI().toString());
        userProfilePicture.setImage(image);
        userUsername.setText(UserManager.getInstance().getActiveUser().getUsername());
    }

    public void handleChooseCoverButton() {
        Stage fileChooserWindow = new Stage();
        fileChooserWindow.setTitle("Choose a file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        chosenImage = fileChooser.showOpenDialog(fileChooserWindow);
    }

    @FXML
    public void handleAddButton() {
        try {
            PlaylistManager.getInstance().createPlaylist(UserManager.getInstance().getActiveUser(), chosenImage, playlistNameTextField.getText()); //braucht Playlistname, (braucht Cover)
            log.debug("Create Playlist Button pressed with name {} and cover {}.", playlistNameTextField.getText(), chosenImage);
        } catch (Exception e) {
            errorMessageLabel.setText(e.getMessage());
        }
        SceneManager.CREATE_PLAYLIST.closeSecondaryStage();
        SceneManager.MAIN.getController().updateView();
    }

    @FXML
    public void handleCancelButton() {
        SceneManager.CREATE_PLAYLIST.closeSecondaryStage();
        log.trace("Cancel Button pressed.");
    }
}
