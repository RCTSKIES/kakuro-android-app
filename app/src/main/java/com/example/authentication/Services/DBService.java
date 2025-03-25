package com.example.authentication.Services;

import androidx.annotation.NonNull;

import com.example.authentication.Helpers.StringHelper;
import com.example.authentication.Interfaces.SearchListener;
import com.example.authentication.Objects.Account;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DBService {

    private DBService() {}

    public static void findAccount(String username, String email, SearchListener listener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        usersRef.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String existingEmail = snapshot.child("email").getValue(String.class);
                    if (existingEmail != null && existingEmail.equals(email)) {
                        listener.found("This username and email belong to the same account.");
                    } else {
                        listener.found("Account already exists with a different email.");
                    }
                } else {
                    DatabaseReference emailRef = FirebaseDatabase.getInstance().getReference().child("emails");

                    // Encode the email before using it as a path
                    String encodedEmail = StringHelper.encodeEmail(email);

                    emailRef.child(encodedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                listener.found("Email is already being used.");
                            } else {
                                listener.notFound();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            listener.onError(error.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.getMessage());
            }
        });
    }


    public static void saveAccount(Account acc) {
        // save account in users node
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
        userRef.child(acc.getUsername()).child("email").setValue(acc.getEmail());
        userRef.child(acc.getUsername()).child("password").setValue(acc.getPassword());
        // save email in emails node
        DatabaseReference emailRef = FirebaseDatabase.getInstance().getReference().child("emails");
        emailRef.child(StringHelper.encodeEmail(acc.getEmail())).setValue(acc.getUsername());
    }
}

