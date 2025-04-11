package com.example.authentication;

import android.app.Application;

import com.example.authentication.Objects.Player;
import com.example.authentication.Objects.User;

import java.util.List;

public class MyApp extends Application {
    private static MyApp instance;
    private User currentUser;
    private List<Player> playerList;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        currentUser = new User();
    }

    public static MyApp getInstance() {
        return instance;  // Access the application instance
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public List<Player> getPlayerList() {return playerList;}
    public void setPlayerList(List<Player> playerList) {this.playerList = playerList;}
}
