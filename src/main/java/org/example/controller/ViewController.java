package org.example.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.media.SoundManager;
import org.example.media.interfaces.ISound;
import org.example.player.Player;
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
    private TableColumn<Playlist, Integer> numberOfSounds;
    @FXML
    private TableColumn<Playlist, HBox> playlistCreatedBy;

    @FXML
    private TableView<ISound> queueTable;
    @FXML
    private TableColumn<ISound, ImageView> queueSoundCover;
    @FXML
    private TableColumn<ISound, String> queueSoundTitle;

    private final ObservableList<Playlist> playlistObjectList = FXCollections.observableArrayList();
    private final ObservableList<ISound> soundObjectList = FXCollections.observableArrayList();
    private final ObservableList<ISound> queueObjectList = FXCollections.observableArrayList();
    private ViewType view;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        soundCover.setCellValueFactory(cellData -> {
            Image image = new Image(SoundManager.getINSTANCE().getDEFAULT_COVER().toURI().toString());
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


        playlistCover.setCellValueFactory(cellData -> {
            Image image = new Image(cellData.getValue().getPlaylistCover().toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(80);
            imageView.setFitWidth(80);
            imageView.setId("playlistCover");
            return new ReadOnlyObjectWrapper<>(imageView);
        });
        playlistName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        numberOfSounds.setCellValueFactory(new PropertyValueFactory<>("numberOfSounds"));
        playlistCreatedBy.setCellValueFactory(cellData -> {
            User user = UserManager.getInstance().getUserByName(cellData.getValue().getCreatedBy().getUsername());
            Label label = new Label(user.getUsername());
            ImageView imageView = new ImageView(new Image(cellData.getValue().getCreatedBy().getProfilePicture().toURI().toString()));
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            HBox hBox = new HBox();
            hBox.setSpacing(10);
            hBox.setAlignment(javafx.geometry.Pos.CENTER);
            hBox.getChildren().addAll(label, imageView);
            return new ReadOnlyObjectWrapper<>(hBox);
        });

        playlistCover.prefWidthProperty().bind(playlistTable.widthProperty().multiply(0.2));
        numberOfSounds.prefWidthProperty().bind(playlistTable.widthProperty().multiply(0.2));
        playlistName.prefWidthProperty().bind(playlistTable.widthProperty().multiply(0.3));
        playlistCreatedBy.prefWidthProperty().bind(playlistTable.widthProperty().multiply(0.3));


        queueSoundCover.setCellValueFactory(cellData -> {
            Image image = new Image(SoundManager.getINSTANCE().getDEFAULT_COVER().toURI().toString());
            if (cellData.getValue().getMedia().getMetadata().get("image") != null) {
                image = ((Image) cellData.getValue().getMedia().getMetadata().get("image"));
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(80);
            imageView.setFitWidth(80);
            imageView.setId("soundCover");
            return new ReadOnlyObjectWrapper<>(imageView);
        });
        queueSoundTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));

        queueSoundCover.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.4));
        queueSoundTitle.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.6));

        soundTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setView(ViewType.ALL);
    }

    public void setView(ViewType viewType) {
        view = viewType;
        updateView();
    }

    private void updateView() {
        switch (view) {
            case ALL:
                initializeSoundContent(SoundManager.getINSTANCE().getAllSounds());
                initializePlaylistContent(PlaylistManager.getInstance().getAllPlaylists());
                initializeQueueContent(Player.getInstance().getSoundQueue().getSounds());
                break;
            case USER:
                initializeSoundContent(SoundManager.getINSTANCE().getAllSoundsByUser(UserManager.getInstance().getActiveUser()));
                initializePlaylistContent(PlaylistManager.getInstance().getPlaylistsByUser(UserManager.getInstance().getActiveUser()));
                initializeQueueContent(Player.getInstance().getSoundQueue().getSounds());
                break;
        }
    }

    private void initializeSoundContent(ArrayList<ISound> sounds) {
        sounds.sort((o1, o2) -> o1.getTitle().compareToIgnoreCase(o2.getTitle()));
        soundObjectList.setAll(sounds);
        soundTable.setItems(soundObjectList);
        soundTable.refresh();
    }

    private void initializePlaylistContent(ArrayList<Playlist> playlists) {
        playlists.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        playlistObjectList.setAll(playlists);
        playlistTable.setItems(playlistObjectList);
        playlistTable.refresh();
    }

    private void initializeQueueContent(ArrayList<ISound> sounds) {
        queueObjectList.setAll(sounds);
        queueTable.setItems(queueObjectList);
        queueTable.refresh();
    }

    /**
     * Probably the method to set the sound / playlist in queues first place
     *
     * @param event mouse event
     * @see org.example.player.Player
     */
    //TODO: implement correcly to play sounds
    @FXML
    public void selectRow(MouseEvent event) {
        if (event.getClickCount() == 2) {
            try {
                ISound sound = soundTable.getSelectionModel().getSelectedItem();
                System.out.println(sound.getTitle());
            } catch (NullPointerException e) {
                log.error("No sound selected");
            }
        }
    }

    @FXML
    public void deleteSound() {
        ISound sound = soundTable.getSelectionModel().getSelectedItem();
        SoundManager.getINSTANCE().deleteSoundByID(UserManager.getInstance().getActiveUser(), sound.getSoundID());
        for (Playlist playlist : PlaylistManager.getInstance().getAllPlaylists()) {
            playlist.removeSound(sound);
        }
        Player.getInstance().getSoundQueue().removeSound(sound);
        updateView();
    }

    @FXML
    public void deletePlaylist() {
        Playlist playlist = playlistTable.getSelectionModel().getSelectedItem();
        PlaylistManager.getInstance().deletePlaylistByID(UserManager.getInstance().getActiveUser(), playlist.getPlaylistID());
        updateView();
    }

    public void addToPlaylist() {
        ISound sound = soundTable.getSelectionModel().getSelectedItem();
        Playlist playlist = playlistTable.getSelectionModel().getSelectedItem();
        playlist.addSound(UserManager.getInstance().getActiveUser(), sound);
        updateView();
    }

    public void addSoundToQueue() {
        ISound sound = soundTable.getSelectionModel().getSelectedItem();
        Player.getInstance().getSoundQueue().addSound(sound);
        queueObjectList.setAll(Player.getInstance().getSoundQueue().getSounds());
        queueTable.setItems(queueObjectList);
        updateView();
    }

    public void removeSoundFromQueue() {
        ISound sound = queueTable.getSelectionModel().getSelectedItem();
        Player.getInstance().getSoundQueue().removeSound(sound);
        updateView();
    }

    public void addPlaylistToQueue() {
        Playlist playlist = playlistTable.getSelectionModel().getSelectedItem();
        Player.getInstance().getSoundQueue().addPlaylist(playlist);
        updateView();
    }

    /**
     * Method was only a test
     * @param e
     */
    private void showPopup(Exception e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(3000),
                ae -> alert.close()));
        alert.setTitle("Whalex - Information");
        alert.setHeaderText(getClass().getSimpleName());
        alert.setContentText(e.getMessage());
        alert.show();
        timeline.play();
    }
}

