package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.data.DataOperation;
import org.example.data.DataThread;
import org.example.data.DataType;
import org.example.media.SoundManager;
import org.example.media.interfaces.ISound;
import org.example.player.Player;
import org.example.playlist.Playlist;
import org.example.playlist.PlaylistManager;
import org.example.profile.UserManager;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EditProfileController implements Initializable {

    private static final Logger log = LogManager.getLogger(EditProfileController.class);
    @FXML
    private PasswordField deleteProfileConfirmPasswordField;
    @FXML
    private Label errorMessageLabel3;
    @FXML
    private PasswordField deleteProfilePasswordField;

    @FXML
    private ImageView userProfilePicture;
    @FXML
    private Label userUsername;
    @FXML
    private Label errorMessageLabel1;

    @FXML
    private TextField oldPasswordTextField;
    @FXML
    private TextField newPasswordTextField;
    @FXML
    private TextField confirmNewPasswordTextField;
    @FXML
    private Label errorMessageLabel2;

    private final FileChooser fileChooser = new FileChooser();

    private File chosenImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image(UserManager.getInstance().getActiveUser().getProfilePicture().toURI().toString());
        userProfilePicture.setImage(image);
        userUsername.setText(UserManager.getInstance().getActiveUser().getUsername());
    }

    public void handleSubmitButton() {
        try {
            UserManager.getInstance().getActiveUser().setProfilePicture(chosenImage);
            log.info("Attempting to change profile Picture to {}", chosenImage);
        } catch (Exception e) {
            errorMessageLabel1.setText(e.getMessage());
        }
        try {
            UserManager.getInstance().getActiveUser().changePassword(oldPasswordTextField.getText(), newPasswordTextField.getText(), confirmNewPasswordTextField.getText());
            log.info("Attempting to change password of user {}", UserManager.getInstance().getActiveUser().getUsername());
        } catch (Exception e) {
            errorMessageLabel2.setText(e.getMessage());
        }
        try {
            for (ISound sound : SoundManager.getInstance().getAllSoundsByUser(UserManager.getInstance().getActiveUser())) {
                SoundManager.getInstance().deleteSoundByID(UserManager.getInstance().getActiveUser(), sound.getSoundID());
            }
            for (Playlist playlist : PlaylistManager.getInstance().getPlaylistsByUser(UserManager.getInstance().getActiveUser())) {
                PlaylistManager.getInstance().deletePlaylistByID(UserManager.getInstance().getActiveUser(), playlist.getPlaylistID());
            }
            UserManager.getInstance().deleteUser(deleteProfilePasswordField.getText(), deleteProfileConfirmPasswordField.getText());
            log.debug("Attempting to delete user {}.", UserManager.getInstance().getActiveUser().getUsername());
            SceneManager.EDIT_PROFILE.closeSecondaryStage();
            new DataThread(DataType.USER_SOUND_PLAYLIST, DataOperation.WRITE).start();
            Player.getInstance().clearQueue();
            Player.getInstance().clearHistory();
            SceneManager.LOGIN.changeScene();
            return;
        } catch (Exception e) {
            errorMessageLabel3.setText(e.getMessage());
        }
        SceneManager.EDIT_PROFILE.closeSecondaryStage();
        SceneManager.MAIN.getController().updateUserContent();
        SceneManager.MAIN.getController().updateView();
    }

    public void handleCancelButton() {
        SceneManager.EDIT_PROFILE.closeSecondaryStage();
        log.trace("Cancel Button pressed.");
    }

    public void handleFileChooserButton() {
        Stage fileChooserWindow = new Stage();
        fileChooserWindow.setTitle("Choose a file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        chosenImage = fileChooser.showOpenDialog(fileChooserWindow);
    }
}
