package com.example.authentication;

import android.app.Application;

import com.example.authentication.Objects.User;

public class MyApp extends Application {
    private static MyApp instance;
    private User currentUser;

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
}
