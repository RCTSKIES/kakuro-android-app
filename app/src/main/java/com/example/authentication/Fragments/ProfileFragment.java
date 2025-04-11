package com.example.authentication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.authentication.Activities.MainActivity;
import com.example.authentication.MyApp;
import com.example.authentication.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    TextView etUserName, etEmail;
    Button btnLogout;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        etUserName = view.findViewById(R.id.etUserName);
        etEmail = view.findViewById(R.id.etEmail);
        btnLogout = view.findViewById(R.id.btnLogout);

        etUserName.setText("Username: " + MyApp.getInstance().getCurrentUser().getAcc().getUsername());
        etEmail.setText("Email: " + MyApp.getInstance().getCurrentUser().getAcc().getEmail());
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), MainActivity.class); // Or getContext() if needed
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear back stack
            startActivity(intent);
        });
        return view;
    }
}