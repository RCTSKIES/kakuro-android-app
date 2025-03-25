package com.example.authentication.Services;



import android.content.Context;

import com.example.authentication.Interfaces.AuthenticationListener;
import com.example.authentication.Interfaces.UIListener;
import com.example.authentication.Objects.Account;
import com.example.authentication.Interfaces.SearchListener;

public class AccountService {

    private AccountService() {

    }

    public static void authenticate(String email, String password, UIListener listener){

        AuthService.authenticate(email, password, new AuthenticationListener() {
            @Override
            public void onSuccess() {
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

    public static void register(String username, String email, String password, UIListener uiListener) {

        DBService.findAccount(username, email, new SearchListener() {
            @Override
            public void found(String message) {
                uiListener.onFailure(message);
            }

            @Override
            public void notFound() {
                Account newAccount = new Account(username, email, password);
                // creates auth user only after confimation
                AuthService.createUser(newAccount, new AuthenticationListener() {
                    @Override
                    public void onSuccess() {
                        AuthService.authenticate(newAccount.getEmail(), newAccount.getPassword(), new AuthenticationListener() {
                            @Override
                            public void onSuccess() {
                                uiListener.onSuccess();
                            }

                            @Override
                            public void onFail() {
                                uiListener.onFailure("Authentication from signup failed");
                            }

                            @Override
                            public void onError(String error) {
                                uiListener.onFailure(error);
                            }
                        });
                    }

                    @Override
                    public void onFail() {
                        uiListener.onFailure("Account creation failed");
                    }

                    @Override
                    public void onError(String error) {
                        uiListener.onFailure(error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                uiListener.onFailure(error);
            }
        });
    }

}
