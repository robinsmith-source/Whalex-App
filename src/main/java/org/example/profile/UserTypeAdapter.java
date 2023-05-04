package org.example.profile;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * UserTypeAdapter for the JSON-Serialization of the User-Object
 */
public class UserTypeAdapter extends TypeAdapter<User> {

    @Override
    public User read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }

        int userID = -1;
        String username = null, password = null;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("userID")) {
                userID = reader.nextInt();
            } else if (name.equals("username")) {
                username = reader.nextString();
            } else if (name.equals("password")) {
                password = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        return new User(userID, username, password);
    }

    @Override
    public void write(JsonWriter writer, User user) throws IOException {
        if (user == null) {
            writer.nullValue();
            return;
        }

        writer.beginObject();
        writer.name("userID").value(user.getUserID());
        //writer.name("profilePicture").value(user.getProfilePicture().getAbsolutePath());
        writer.name("username").value(user.getUsername());
        writer.name("password").value(user.getPassword());
        writer.endObject();
    }
}
