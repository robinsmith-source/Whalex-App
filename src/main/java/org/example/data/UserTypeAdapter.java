package org.example.data;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.example.profile.User;

import java.io.File;
import java.io.IOException;

/**
 * UserTypeAdapter for the JSON-Serialization of the User-Object
 * Todo : Add logging / Exception handling
 */
public class UserTypeAdapter extends TypeAdapter<User> {

    @Override
    public User read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        User user = null;
        String userID = null, username = null, password = null;
        File profilePicture = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String userKey = reader.nextName();
            switch (userKey) {
                case "userID":
                    userID = reader.nextString();
                    break;
                case "profilePicture":
                    profilePicture = new File(reader.nextString());
                    break;
                case "username":
                    username = reader.nextString();
                    break;
                case "password":
                    password = reader.nextString();
                    user = new User(userID, profilePicture, username, password);
                    break;
                case "soundfiles":
                    reader.beginArray();
                    while (reader.hasNext()) {
                        reader.beginObject();
                        String title = null;
                        File path = null;
                        while (reader.hasNext()) {
                            String soundkey = reader.nextName();
                            if (soundkey.equals("title")) {
                                title = reader.nextString();
                            } else if (soundkey.equals("path")) {
                                path = new File(reader.nextString());
                            } else {
                                reader.skipValue();
                            }
                        }
                        reader.endObject();
                        if (user != null) {
                            user.getSoundManager().addSound(title, path);
                        }
                    }
                    reader.endArray();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return user;
    }

    @Override
    public void write(JsonWriter writer, User user) throws IOException {
        if (user == null) {
            writer.nullValue();
            return;
        }

        writer.beginObject();
        writer.name("userID").value(user.getUserID());
        writer.name("profilePicture").value(user.getProfilePicture().getPath());
        writer.name("username").value(user.getUsername());
        writer.name("password").value(user.getPassword());
        writer.name("soundfiles");
        writer.beginArray();
        for (String soundKey : user.getSoundManager().getAllSoundsByUser().keySet()) {
            writer.beginObject();
            String absolutePath = user.getSoundManager().getAllSoundsByUser().get(soundKey).getMedia().getSource();
            writer.name("title").value(user.getSoundManager().getAllSoundsByUser().get(soundKey).getTitle());
            writer.name("path").value("src/main/resources/sounds" + absolutePath.substring(absolutePath.lastIndexOf('/')));
            writer.endObject();
        }
        writer.endArray();

        writer.name("playlists");
        writer.beginArray();
        for (String playlistKey : user.getPlaylistManager().getAllPlaylistsByUser().keySet()) {
            writer.beginObject();
            writer.name(playlistKey);
            writer.beginArray();
            for (String soundKey : user.getPlaylistManager().getAllPlaylistsByUser().get(playlistKey).getSounds().keySet()) {
                writer.beginObject();
                String absolutePath = user.getSoundManager().getAllSoundsByUser().get(soundKey).getMedia().getSource();
                writer.name("title").value(user.getSoundManager().getAllSoundsByUser().get(soundKey).getTitle());
                writer.name(soundKey).value("src/main/resources/sounds" + absolutePath.substring(absolutePath.lastIndexOf('/')));
                writer.endObject();
            }
            writer.endArray();
            writer.endObject();
        }
        writer.endArray();
        writer.endObject();
    }
}
