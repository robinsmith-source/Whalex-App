package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.profile.UserManager;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EditProfileController implements Initializable {

    private static final Logger log = LogManager.getLogger(EditProfileController.class);
    @FXML
    private ImageView userProfilePicture;
    @FXML
    private Label userUsername;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;

    @FXML
    private Button fileChooserButton;
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

    private File choosenImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image(UserManager.getInstance().getActiveUser().getProfilePicture().toURI().toString());
        userProfilePicture.setImage(image);
        userUsername.setText(UserManager.getInstance().getActiveUser().getUsername());
    }

    public void handleSubmitButton() {
        try {
            UserManager.getInstance().getActiveUser().setProfilePicture(choosenImage);
        } catch (Exception e) {
            errorMessageLabel1.setText(e.getMessage());
        }
        try {
            UserManager.getInstance().getActiveUser().changePassword(oldPasswordTextField.getText(), newPasswordTextField.getText(), confirmNewPasswordTextField.getText());
        } catch (Exception e) {
            errorMessageLabel2.setText(e.getMessage());
        }
        SceneManager.EDIT_PROFILE.closeSecondaryStage();
    }

    public void handleCancelButton() {
        SceneManager.EDIT_PROFILE.closeSecondaryStage();
    }

    public void handleFileChooserButton() {
        Stage fileChooserWindow = new Stage();
        fileChooserWindow.setTitle("Choose a file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        choosenImage = fileChooser.showOpenDialog(fileChooserWindow);
    }
}