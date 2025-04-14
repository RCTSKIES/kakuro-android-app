package com.example.authentication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.authentication.Managers.LocalDatabaseManager;
import com.example.authentication.R;

public class HomeFragment extends Fragment {

    private TextView xpTextView;
    private LocalDatabaseManager dbManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        xpTextView = view.findViewById(R.id.xpTextView);
        Button btnEasy = view.findViewById(R.id.btnEasy);
        Button btnMedium = view.findViewById(R.id.btnMedium);
        Button btnHard = view.findViewById(R.id.btnHard);
        // Button btnOnline = view.findViewById(R.id.btnOnline);
        PuzzleListFragment listFragment = new PuzzleListFragment();
        Bundle bundle = new Bundle();

        // Initialize XP bar
        dbManager = new LocalDatabaseManager(view.getContext());
        xpTextView.setText("Total XP: " + dbManager.getTotalXP());

        btnEasy.setOnClickListener(v -> {
            bundle.putString("DIFFICULTY", "Easy");
            listFragment.setArguments(bundle);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, listFragment)
                    .addToBackStack(null) // optional if you want to allow back navigation
                    .commit();
        });

        btnMedium.setOnClickListener(v -> {
            bundle.putString("DIFFICULTY", "Medium");
            listFragment.setArguments(bundle);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, listFragment)
                    .addToBackStack(null) // optional if you want to allow back navigation
                    .commit();
        });

        btnHard.setOnClickListener(v -> {
            bundle.putString("DIFFICULTY", "Hard");
            listFragment.setArguments(bundle);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, listFragment)
                    .addToBackStack(null) // optional if you want to allow back navigation
                    .commit();
        });


        return view;
    }
}