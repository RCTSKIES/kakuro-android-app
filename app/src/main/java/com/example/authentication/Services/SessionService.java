package com.example.authentication.Services;

import com.example.authentication.Interfaces.AuthenticationListener;
import com.example.authentication.Interfaces.UIListener;

public final class SessionService {
    private SessionService() {}

    public static void authenticate(String email, String password, UIListener listener){

        AuthService.authenticate(email, password, new AuthenticationListener() {
            @Override
            public void onSuccess() {
//                DBService.storeAccountLocally(email, password, new SearchListener() {
//                    @Override
//                    public void found(String message) {
//
//                    }
//
//                    @Override
//                    public void notFound() {
//                        listener.onFailure("Could not store account in memory");
//                    }
//
//                    @Override
//                    public void onError(String error) {
//                        listener.onFailure(error);
//                    }
//                });
                listener.onSuccess();
            }
            @Override
            public void onFail() {
                listener.onFailure("Authentication failed");
            }
            @Override
            public void onError(String error) {
                listener.onFailure(error);
            }
        });
    }
}
