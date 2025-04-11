package com.example.authentication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.authentication.Adapters.PlayerAdapter;
import com.example.authentication.Interfaces.UIListener;
import com.example.authentication.MyApp;
import com.example.authentication.Objects.Player;
import com.example.authentication.R;
import com.example.authentication.Services.RealtimeDBService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DailyChallengesFragment extends Fragment {

    private RecyclerView recyclerLeaderboard;
    private PlayerAdapter adapter;

    public DailyChallengesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_challenges, container, false);

        recyclerLeaderboard = view.findViewById(R.id.recyclerLeaderboard);
        recyclerLeaderboard.setLayoutManager(new LinearLayoutManager(getContext()));

        RealtimeDBService.loadLeaderboardData(new UIListener() {
              @Override
              public void onSuccess() {
                  adapter = new PlayerAdapter(MyApp.getInstance().getPlayerList());
                  recyclerLeaderboard.setAdapter(adapter);
              }

              @Override
              public void onFailure(String errorMessage) {
                  Toast.makeText(getContext(), "Error loading leaderboard: " + errorMessage, Toast.LENGTH_SHORT).show();
              }
          });

        Button btnStart = view.findViewById(R.id.btnStartOnlineGame);
        btnStart.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), OnlineGameActivity.class);
//            startActivity(intent);
        });

        return view;
    }



}