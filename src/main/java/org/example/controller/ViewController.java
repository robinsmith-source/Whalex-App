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

public class ViewController implements Initializable {

    private static final Logger log = LogManager.getLogger(ViewController.class);

    @FXML
    private TableView<ISound> soundTable;

    @FXML
    private TableColumn<ISound, ImageView> soundCover;

    @FXML
    private TableColumn<ISound, String> soundTitle;
    @FXML
    private TableColumn<ISound, HBox> soundUploadedBy;


    @FXML
    private TableView<Playlist> playlistTable;

    @FXML
    private TableColumn<Playlist, ImageView> playlistCover;

    @FXML
    private TableColumn<Playlist, String> playlistName;
    @FXML
    private TableColumn<Playlist, User> playlistCreatedBy;

    private final ObservableList<Playlist> playlistObjectList = FXCollections.observableArrayList();
    private final ObservableList<ISound> soundObjectList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
            hBox.getChildren().addAll(label, imageView);
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

        initializeSoundContent(SoundManager.getInstance().getAllSounds());
        initializePlaylistContent(PlaylistManager.getInstance().getAllPlaylists());
    }

    void initializeSoundContent(ArrayList<ISound> sounds) {
        soundObjectList.clear();
        sounds.sort((o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()));
        soundObjectList.addAll(sounds);
        soundTable.setItems(soundObjectList);
        soundTable.refresh();
    }

    void initializePlaylistContent(ArrayList<Playlist> playlists) {
        playlistObjectList.clear();
        playlists.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        playlistObjectList.addAll(playlists);
        playlistTable.setItems(playlistObjectList);
        playlistTable.refresh();
    }

    @FXML
    public void selectRow(MouseEvent event) {
        if (event.getClickCount() == 2) {
            ISound sound = soundTable.getSelectionModel().getSelectedItem();
            System.out.println(sound.getTitle());
        }
    }

    @FXML
    public void deleteSound() {
        ISound sound = soundTable.getSelectionModel().getSelectedItem();
        SoundManager.getInstance().removeSoundByID(UserManager.getInstance().getCurrentUser(), sound.getSoundID());
        for (Playlist playlist : PlaylistManager.getInstance().getAllPlaylists()) {
            playlist.removeSound(sound);
        }
        soundObjectList.remove(sound);
        soundTable.refresh();
    }

    @FXML
    public void deletePlaylist() {
        Playlist playlist = playlistTable.getSelectionModel().getSelectedItem();
        PlaylistManager.getInstance().deletePlaylistByID(UserManager.getInstance().getCurrentUser(), playlist.getPlaylistID());
        playlistObjectList.remove(playlist);
        playlistTable.refresh();
    }

    public void addToPlaylist(ActionEvent actionEvent) {
        ISound sound = soundTable.getSelectionModel().getSelectedItem();
        Playlist playlist = playlistTable.getSelectionModel().getSelectedItem();
        playlist.addSound(sound);
    }
}

