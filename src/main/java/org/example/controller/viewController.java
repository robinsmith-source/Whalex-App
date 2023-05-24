package org.example.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.SoundManager;
import org.example.media.interfaces.ISound;
import org.example.playlist.Playlist;
import org.example.playlist.PlaylistManager;
import org.example.profile.User;
import org.example.profile.UserManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class viewController implements Initializable {

    private static final Logger log = LogManager.getLogger(viewController.class);

    @FXML
    private TableView soundTable;

    @FXML
    private TableColumn<ISound, ImageView> soundCover;

    @FXML
    private TableColumn<ISound, String> soundTitle;
    @FXML
    private TableColumn<ISound, HBox> soundUploadedBy;


    @FXML
    private TableView playlistTable;

    @FXML
    private TableColumn<Playlist, ImageView> playlistCover;

    @FXML
    private TableColumn<Playlist, String> playlistName;
    @FXML
    private TableColumn<Playlist, User> playlistCreatedBy;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ArrayList<ISound> sounds = new ArrayList<>(SoundManager.getInstance().getAllSounds().values());
        ArrayList<Playlist> playlists = new ArrayList<>(PlaylistManager.getInstance().getAllPlaylists().values());
        sounds.sort((o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()));
        playlists.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));


        // soundCover.setCellValueFactory(new PropertyValueFactory<>("Media"));

        soundCover.setCellValueFactory(cellData -> {
            Image image = new Image(SoundManager.getInstance().getDEFAULT_COVER().toURI().toString());
            if (cellData.getValue().getMedia().getMetadata().get("image") != null) {
                image = ((Image) cellData.getValue().getMedia().getMetadata().get("image"));
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(80);
            imageView.setFitWidth(80);
            imageView.setId("soundCover");
            return new ReadOnlyObjectWrapper<>(imageView);
        });

        soundTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        //soundUploadedBy.setCellValueFactory(new PropertyValueFactory<>("UploadedBy"));
        soundUploadedBy.setCellValueFactory(cellData -> {
            User user = UserManager.getInstance().getUserByName(cellData.getValue().getUploadedBy().getUsername());
            Label label = new Label(user.getUsername());
            ImageView imageView = new ImageView(new Image(cellData.getValue().getUploadedBy().getProfilePicture().toURI().toString()));
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setAlignment(javafx.geometry.Pos.CENTER);
            hBox.getChildren().addAll(label,imageView);
            return new ReadOnlyObjectWrapper<>(hBox);
        });

        soundCover.prefWidthProperty().bind(soundTable.widthProperty().multiply(0.2));
        soundTitle.prefWidthProperty().bind(soundTable.widthProperty().multiply(0.5));
        soundUploadedBy.prefWidthProperty().bind(soundTable.widthProperty().multiply(0.3));


        //playlistCover.setCellValueFactory(new PropertyValueFactory<>("PlaylistCover"));
        playlistCover.setCellValueFactory(cellData -> {
            Image image = new Image(cellData.getValue().getPlaylistCover().toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(80);
            imageView.setFitWidth(80);
            imageView.setId("playlistCover");
            return new ReadOnlyObjectWrapper<>(imageView);
        });
        playlistName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        playlistCreatedBy.setCellValueFactory(new PropertyValueFactory<>("CreatedBy"));

        playlistCover.prefWidthProperty().bind(playlistTable.widthProperty().multiply(0.2));
        playlistName.prefWidthProperty().bind(playlistTable.widthProperty().multiply(0.5));
        playlistCreatedBy.prefWidthProperty().bind(playlistTable.widthProperty().multiply(0.3));

        soundTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        initializeSoundContent();
        initializePlaylistContent();
    }

    private void initializeSoundContent() {
        ObservableList<ISound> objectList = FXCollections.observableArrayList();
        ArrayList<ISound> soundsList = new ArrayList<>(SoundManager.getInstance().getAllSounds().values());
        soundsList.sort((o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()));
        objectList.addAll(soundsList);
        soundTable.setItems(objectList);
        soundTable.refresh();
    }

    private void initializePlaylistContent() {
        ObservableList<Playlist> objectList = FXCollections.observableArrayList();
        ArrayList<Playlist> playlistList = new ArrayList<>(PlaylistManager.getInstance().getAllPlaylists().values());
        playlistList.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        objectList.addAll(playlistList);
        playlistTable.setItems(objectList);
        playlistTable.refresh();
    }

    @FXML
    public void selectRow(MouseEvent event) {
        if (event.getClickCount() == 2) {
            ISound sound = (ISound) soundTable.getSelectionModel().getSelectedItem();
            System.out.println(sound.getTitle());
        }
    }

    @FXML
    public void deleteSound() {
        ObservableList<ISound> selectedSounds = soundTable.getSelectionModel().getSelectedItems();
        for (ISound sound : selectedSounds) {
            try {

                SoundManager.getInstance().removeSoundByID(UserManager.getInstance().getCurrentUser(), sound.getSoundID());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        initializeSoundContent();
    }

    @FXML
    public void deletePlaylist() {
        Playlist playlist = (Playlist) playlistTable.getSelectionModel().getSelectedItem();
        PlaylistManager.getInstance().deletePlaylistByID(UserManager.getInstance().getCurrentUser(), playlist.getPlaylistID());
        initializePlaylistContent();
    }

    public void addToPlaylist(ActionEvent actionEvent) {
        ISound sound = (ISound) soundTable.getSelectionModel().getSelectedItem();
        Playlist playlist = (Playlist) playlistTable.getSelectionModel().getSelectedItem();
        playlist.addSound(sound);
    }
}

