package com.example.authentication.Objects;

public class Player {
    private String username;
    private long xp;

    public Player() {} // Required for Firebase

    public Player(String username, long xp) {
        this.username = username;
        this.xp = xp;
    }

    public String getUsername() {
        return username;
    }

    public long getXp() {
        return xp;
    }
}
