package com.example.authentication.Services.Authentication;

import com.example.authentication.Helpers.StringHelper;
import com.example.authentication.Interfaces.AuthenticationListener;
import com.example.authentication.Interfaces.UIListener;
import com.example.authentication.MyApp;
import com.example.authentication.Objects.Account;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class SessionService {
    private SessionService() {}

    public static String getLoggedInUsername() {
        Account acc = MyApp.getInstance().getCurrentUser().getAcc();
        return (acc != null) ? acc.getUsername() : null;
    }

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
                String encodedEmail = StringHelper.encodeEmail(email);
                DatabaseReference emailRef = FirebaseDatabase.getInstance().getReference("emails");
                emailRef.child(encodedEmail).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String username = task.getResult().getValue(String.class);
                        if (username != null) {
                            Account acc = new Account(username, email, password);
                            MyApp.getInstance().getCurrentUser().setAcc(acc);
                            listener.onSuccess();
                        } else {
                            listener.onFailure("Could not find user account");
                        }
                    } else {
                        listener.onFailure("Failed to fetch user info");
                    }
                });
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
