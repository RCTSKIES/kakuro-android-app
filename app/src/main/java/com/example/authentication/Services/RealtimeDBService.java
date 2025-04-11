package com.example.authentication.Services;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.authentication.Helpers.StringHelper;
import com.example.authentication.Interfaces.SearchListener;
import com.example.authentication.Interfaces.UIListener;
import com.example.authentication.MyApp;
import com.example.authentication.Objects.Account;
import com.example.authentication.Objects.Player;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RealtimeDBService {

    private RealtimeDBService() {}

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

    public static void loadLeaderboardData(UIListener listener) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.orderByChild("xp").limitToLast(5) // Top 5 by XP
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        List<Player> playerList = new ArrayList<>();

                        for (DataSnapshot userSnap : snapshot.getChildren()) {
                            String username = userSnap.getKey(); // Username is the key
                            Long xp = userSnap.child("xp").getValue(Long.class);

                            if (username != null && xp != null) {
                                Player player = new Player(username, xp);
                                playerList.add(player);
                            }
                        }

                        // Reverse to show descending order (Firebase returns ascending)
                        Collections.reverse(playerList);
                        MyApp.getInstance().setPlayerList(playerList);
                        listener.onSuccess();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        listener.onFailure(error.getMessage());
                    }
                });
    }

    public static void setUsername(String email) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("emails");

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String username = snapshot.child(email).getValue(String.class);
                    if (username != null) {
                        MyApp.getInstance().getCurrentUser().setAcc(new Account(username, email));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

