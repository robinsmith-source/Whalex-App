package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.playlist.PlaylistManager;
import org.example.profile.UserManager;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class CreatePlaylistController implements Initializable {
    @FXML
    private Label errorMessageLabel;
    @FXML
    private ImageView userProfilePicture;
    @FXML
    private Label userUsername;
    @FXML
    private TextField playlistNameTextField;

    private final FileChooser fileChooser = new FileChooser();

    private File choosenImage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("CreatePlaylistController");
        Image image = new Image(UserManager.getInstance().getActiveUser().getProfilePicture().toURI().toString());
        userProfilePicture.setImage(image);
        userUsername.setText(UserManager.getInstance().getActiveUser().getUsername());
    }

    public void handleChooseCoverButton() {
        System.out.println("Choose Cover Button");
        Stage fileChooserWindow = new Stage();
        fileChooserWindow.setTitle("Choose a file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        choosenImage = fileChooser.showOpenDialog(fileChooserWindow);
    }

    public void handleAddButton() {
        System.out.println("Add Button");
        System.out.println(playlistNameTextField.getText());
        try {
            PlaylistManager.getInstance().createPlaylist(UserManager.getInstance().getActiveUser(), choosenImage, playlistNameTextField.getText()); //braucht Playlistname, (braucht Cover)
        } catch (Exception e) {
            errorMessageLabel.setText(e.getMessage());
        }
        SceneManager.CREATE_PLAYLIST.closeSecondaryStage();
        SceneManager.MAIN.getController().updateView();
        //Name der Playlist abgreifen --> Playlistmanager --> Playlist erstellen (createPlaylist)
        //Todo: Fenster schließen, Tabelle aktualisieren
    }
}
