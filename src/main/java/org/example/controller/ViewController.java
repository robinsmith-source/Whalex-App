package org.example.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import org.example.player.PlayerCombined;
import org.example.playlist.Playlist;
import org.example.playlist.PlaylistManager;
import org.example.profile.User;
import org.example.profile.UserManager;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ViewController implements Initializable {
    private static final Logger log = LogManager.getLogger(ViewController.class);

    @FXML
    private Label soundTableLabel;
    @FXML
    private TableView<ISound> soundTable;
    @FXML
    private TableColumn<ISound, ImageView> soundCover;
    @FXML
    private TableColumn<ISound, String> soundTitle;
    @FXML
    private TableColumn<ISound, HBox> soundUploadedBy;

    @FXML
    private Label playlistTableLabel;
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


    private ObservableList<Playlist> playlistObjectList;
    private ObservableList<ISound> soundObjectList;

    private final ObservableList<ISound> queueObjectList = FXCollections.observableList(PlayerCombined.getInstance().getSoundQueue());
    private ViewType view;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Sound table
        soundCover.setCellValueFactory(cellData -> {
            Image image = new Image(SoundManager.getINSTANCE().getDEFAULT_COVER().toURI().toString());
            if (cellData.getValue().getMedia().getMetadata().get("image") != null) {
                image = ((Image) cellData.getValue().getMedia().getMetadata().get("image"));
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
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
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.getChildren().addAll(label, imageView);
            return new ReadOnlyObjectWrapper<>(hBox);
        });
        soundTable.setPlaceholder(new Label("No sounds found"));

        soundCover.prefWidthProperty().bind(soundTable.widthProperty().multiply(0.2));
        soundTitle.prefWidthProperty().bind(soundTable.widthProperty().multiply(0.5));
        soundUploadedBy.prefWidthProperty().bind(soundTable.widthProperty().multiply(0.3));

        soundTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        //Playlist table
        playlistCover.setCellValueFactory(cellData -> {
            Image image = new Image(cellData.getValue().getPlaylistCover().toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
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
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.getChildren().addAll(label, imageView);
            return new ReadOnlyObjectWrapper<>(hBox);
        });
        playlistTable.setPlaceholder(new Label("No playlists found"));

        playlistCover.prefWidthProperty().bind(playlistTable.widthProperty().multiply(0.2));
        numberOfSounds.prefWidthProperty().bind(playlistTable.widthProperty().multiply(0.2));
        playlistName.prefWidthProperty().bind(playlistTable.widthProperty().multiply(0.3));
        playlistCreatedBy.prefWidthProperty().bind(playlistTable.widthProperty().multiply(0.3));


        //Queue table
        queueSoundCover.setCellValueFactory(cellData -> {
            Image image = new Image(SoundManager.getINSTANCE().getDEFAULT_COVER().toURI().toString());
            if (cellData.getValue().getMedia().getMetadata().get("image") != null) {
                image = ((Image) cellData.getValue().getMedia().getMetadata().get("image"));
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            imageView.setId("soundCover");
            return new ReadOnlyObjectWrapper<>(imageView);
        });
        queueSoundTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        queueTable.setPlaceholder(new Label("Queue is empty"));

        queueSoundCover.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.4));
        queueSoundTitle.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.6));
        setView(ViewType.ALL);

        queueTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        queueTable.setItems(queueObjectList);

        //TODO: FIX THIS!!
        Timer updateTimer = new Timer();
        TimerTask updateTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    queueTable.refresh();
                    System.out.println("Queue: " + queueObjectList.size());
                    System.out.println("Sounds: " + soundObjectList.size());
                    System.out.println("Playlists: " + playlistObjectList.size());
                });
                //initializeQueueContent(PlayerCombined.getInstance().getSoundQueue());
            }
        };
        updateTimer.schedule(updateTask, 0, 500);

    }

    public void setView(ViewType viewType) {
        view = viewType;
        updateView();
    }

    private void updateView() {
        switch (view) {
            case ALL:
                soundTableLabel.setText("All Sounds");
                initializeSoundContent(SoundManager.getINSTANCE().getAllSounds());
                playlistTableLabel.setText("All Playlists");
                initializePlaylistContent(PlaylistManager.getInstance().getAllPlaylists());
                break;
            case USER:
                soundTableLabel.setText("My Sounds");
                initializeSoundContent(SoundManager.getINSTANCE().getAllSoundsByUser(UserManager.getInstance().getActiveUser()));
                playlistTableLabel.setText("My Playlists");
                initializePlaylistContent(PlaylistManager.getInstance().getPlaylistsByUser(UserManager.getInstance().getActiveUser()));
                break;
        }
        soundTable.selectionModelProperty().get().clearSelection();
        playlistTable.selectionModelProperty().get().clearSelection();
        queueTable.selectionModelProperty().get().clearSelection();
    }

    private void initializeSoundContent(ArrayList<ISound> sounds) {
        soundObjectList = FXCollections.observableArrayList();
        soundObjectList.setAll(sounds);
        FXCollections.sort(soundObjectList, Comparator.comparing(ISound::getTitle));
        soundTable.setItems(soundObjectList);
        soundTable.refresh();
    }

    private void initializePlaylistContent(ArrayList<Playlist> playlists) {
        playlistObjectList = FXCollections.observableArrayList();
        playlistObjectList.setAll(playlists);
        FXCollections.sort(playlistObjectList, Comparator.comparing(Playlist::getName));
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
     * @see org.example.player.Player
     */
    //TODO: implement correcly to play sounds
    @FXML
    private void deleteSound() {
        ISound sound = soundTable.getSelectionModel().getSelectedItem();
        SoundManager.getINSTANCE().deleteSoundByID(UserManager.getInstance().getActiveUser(), sound.getSoundID());
        for (Playlist playlist : PlaylistManager.getInstance().getAllPlaylists()) {
            playlist.removeSound(sound);
        }
        Player.getInstance().getSoundQueue().removeSound(sound);
        //soundTable.refresh();
        //playlistTable.refresh();
        updateView();
    }

    @FXML
    private void deletePlaylist() {
        Playlist playlist = playlistTable.getSelectionModel().getSelectedItem();
        PlaylistManager.getInstance().deletePlaylistByID(UserManager.getInstance().getActiveUser(), playlist.getPlaylistID());
        //playlistTable.refresh();
        updateView();
    }

    @FXML
    private void addToPlaylist() {
        ISound sound = soundTable.getSelectionModel().getSelectedItem();
        Playlist playlist = playlistTable.getSelectionModel().getSelectedItem();
        playlist.addSound(UserManager.getInstance().getActiveUser(), sound);
        //soundTable.refresh();
        //playlistTable.refresh();
        updateView();
    }

    @FXML
    private void addSoundToQueue() {
        ISound sound = soundTable.getSelectionModel().getSelectedItem();
        PlayerCombined.getInstance().addSoundToQueue(sound);
    }

    @FXML
    private void removeSoundFromQueue() {
        ISound sound = queueTable.getSelectionModel().getSelectedItem();
        PlayerCombined.getInstance().removeSoundFromQueue(sound);
    }

    @FXML
    private void addPlaylistToQueue() {
        Playlist playlist = playlistTable.getSelectionModel().getSelectedItem();
        PlayerCombined.getInstance().addPlaylistToQueue(playlist);
    }

    /**
     * Method was only a test
     *
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


    /**
     * Probably the method to set the sound / playlist in queues first place
     *
     * @param mouseEvent mouse event
     * @see org.example.player.Player
     */
    //TODO: implement correcly to play sounds
    @FXML
    private void handleOnSoundClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            try {
                ISound sound = soundTable.getSelectionModel().getSelectedItem();
                PlayerCombined.getInstance().clearQueue();
                PlayerCombined.getInstance().addSoundToQueue(sound);
                PlayerCombined.getInstance().playPause();
            } catch (NullPointerException e) {
                log.error("No sound selected");
            }
        }
    }

    @FXML
    private void handleOnPlaylistClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            try {
                Playlist playlist = playlistTable.getSelectionModel().getSelectedItem();
                soundTableLabel.setText("Sounds in Playlist: " + playlist.getName());
                initializeSoundContent(playlist.getSounds());
            } catch (NullPointerException e) {
                log.error("No playlist selected");
            }
        }
        //TODO: Implement with button hover effect
        /*
        if (mouseEvent.getClickCount() > 1) {
            try {
                Playlist playlist = playlistTable.getSelectionModel().getSelectedItem();
                PlayerCombined.getInstance().clearQueue();
                PlayerCombined.getInstance().addPlaylistToQueue(playlist);
                PlayerCombined.getInstance().playPause();
            } catch (NullPointerException e) {
                log.error("No playlist selected");
            }
        }
         */
    }

    void handleSearchBar(String search) {
        if (search.isEmpty()) {
            updateView();
        } else if (search.startsWith("user:")) {
            ArrayList<ISound> soundsInSearch = SoundManager.getINSTANCE().getAllSoundsByUser(UserManager.getInstance().getUserByName(search.substring(5)));
            ArrayList<Playlist> playlistsInSearch = PlaylistManager.getInstance().getPlaylistsByUser(UserManager.getInstance().getUserByName(search.substring(5)));
            initializeSoundContent(soundsInSearch);
            initializePlaylistContent(playlistsInSearch);
        } else if (search.startsWith("sound:")) {
            ArrayList<ISound> soundsInSearch = SoundManager.getINSTANCE().getAllSounds().stream().filter(sound -> sound.getTitle().toLowerCase().contains(search.substring(6).toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
            initializeSoundContent(soundsInSearch);
        } else if (search.startsWith("playlist:")) {
            ArrayList<Playlist> playlistsInSearch = PlaylistManager.getInstance().getAllPlaylists().stream().filter(playlist -> playlist.getName().toLowerCase().contains(search.substring(9).toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
            initializePlaylistContent(playlistsInSearch);
        } else {
            ArrayList<ISound> soundsInSearch = SoundManager.getINSTANCE().getAllSounds().stream().filter(sound -> sound.getTitle().toLowerCase().contains(search.toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
            ArrayList<Playlist> playlistsInSearch = PlaylistManager.getInstance().getAllPlaylists().stream().filter(playlist -> playlist.getName().toLowerCase().contains(search.toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
            initializeSoundContent(soundsInSearch);
            initializePlaylistContent(playlistsInSearch);
        }
    }
}

