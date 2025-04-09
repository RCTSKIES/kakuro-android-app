package com.example.authentication.Interfaces;


public interface SearchListener {
    void found(String message);
    void notFound();
    void onError(String error);
}
