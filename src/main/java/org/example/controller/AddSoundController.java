package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.data.DataOperation;
import org.example.data.DataThread;
import org.example.data.DataType;
import org.example.media.SoundManager;
import org.example.profile.UserManager;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AddSoundController implements Initializable {
    @FXML
    private TextField soundTitleTextField;
    @FXML
    private ImageView userProfilePicture;
    @FXML
    private Label userUsername;
    @FXML
    private Label errorMessageLabel;

    private final FileChooser fileChooser = new FileChooser();

    private File choosenSoundfile;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(UserManager.getInstance().getActiveUser().getProfilePicture().toURI().toString());
        userProfilePicture.setImage(image);
        userUsername.setText(UserManager.getInstance().getActiveUser().getUsername());
    }

    public void handleChooseSoundfileButton() {
        Stage fileChooserWindow = new Stage();
        fileChooserWindow.setTitle("Choose a file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Sound Files", "*.mp3", "*.wav"));
        choosenSoundfile = fileChooser.showOpenDialog(fileChooserWindow);
    }

    public void handleAddButton() {
        try {
            SoundManager.getInstance().addSound(UserManager.getInstance().getActiveUser(),soundTitleTextField.getText(),choosenSoundfile);
        } catch (Exception e) {
            errorMessageLabel.setText(e.getMessage());
        }
        SceneManager.ADD_SOUND.closeSecondaryStage();
        new DataThread(DataType.SOUND_PLAYLIST, DataOperation.WRITE).start();
        SceneManager.MAIN.getController().updateView();
    }

    public void handleCancelButton() {
    }

}
