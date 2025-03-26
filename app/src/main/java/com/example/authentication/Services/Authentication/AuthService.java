package com.example.authentication.Services.Authentication;

import com.example.authentication.Interfaces.AuthenticationListener;
import com.example.authentication.Objects.Account;
import com.google.firebase.auth.FirebaseAuth;

public class AuthService {
    private static final FirebaseAuth auth = FirebaseAuth.getInstance();

    public static void createUser(Account acc, AuthenticationListener listener) {
        try{
            auth.createUserWithEmailAndPassword(acc.getEmail(), acc.getPassword()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {// Save the account after successful Firebase Auth
                    listener.onSuccess();
                } else {
                    listener.onFail();
                }
            });
        } catch (Exception e) {
            listener.onError(e.getMessage());
        }

    }

    public static void authenticate(String email, String password, AuthenticationListener listener) {
        try{
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    listener.onSuccess();
                } else {
                    listener.onFail();
                }
            });
        } catch (Error e) {
            listener.onError(e.getMessage());
        }

    }
}

