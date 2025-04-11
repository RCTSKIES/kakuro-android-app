package com.example.authentication.Objects;

public class Account {
    private final String username;
    private final String email;
    private final String password;

    public Account(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Account (String username, String email) {
        this.username = username;
        this.email = email;
        this.password = "";
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
