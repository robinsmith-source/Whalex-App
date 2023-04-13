package org.example.profile;

import java.io.File;

public class User {

    //Profile picture of the user
    private File profilePicture;

    //Username of the user
    private String username;

    //Password of the user
    private String password;

    /**
     * @param username Username of the user
     * @param password Password of the user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
