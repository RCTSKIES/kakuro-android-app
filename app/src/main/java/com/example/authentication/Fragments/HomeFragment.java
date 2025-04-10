package com.example.authentication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.authentication.R;

public class HomeFragment extends Fragment {

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

        Button btnEasy = view.findViewById(R.id.btnEasy);
        Button btnMedium = view.findViewById(R.id.btnMedium);
        Button btnHard = view.findViewById(R.id.btnHard);
        // Button btnOnline = view.findViewById(R.id.btnOnline);
        PuzzleListFragment listFragment = new PuzzleListFragment();
        Bundle bundle = new Bundle();


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

        // Uncomment if using the online button
        // btnOnline.setOnClickListener(v -> {
        //     Intent intent = new Intent(requireContext(), LevelService.class);
        //     intent.putExtra("difficulty", "Online");
        //     intent.putExtra("levelId", 1);
        //     startActivity(intent);
        // });

        return view;
    }
}