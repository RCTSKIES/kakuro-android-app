package com.example.authentication.Interfaces;

public interface AuthenticationListener {
    void onSuccess();
    void onFail();
    void onError(String error);
}
