package com.example.authentication.Services.Authentication;

import com.example.authentication.Interfaces.AuthenticationListener;
import com.example.authentication.Interfaces.SearchListener;
import com.example.authentication.Interfaces.UIListener;
import com.example.authentication.MyApp;
import com.example.authentication.Objects.Account;
import com.example.authentication.Objects.User;

public final class RegistrationService {
    private RegistrationService() {}

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
                        DBService.saveAccount(newAccount);
                        MyApp.getInstance().getCurrentUser().setAcc(newAccount);
                        uiListener.onSuccess();
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
