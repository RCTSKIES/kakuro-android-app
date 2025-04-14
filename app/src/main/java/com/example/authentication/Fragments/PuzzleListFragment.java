package com.example.authentication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.authentication.R;
import com.example.authentication.Services.LevelService;

public class PuzzleListFragment extends Fragment {

    public PuzzleListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_puzzle_list, container, false);

        Button btnlvl1 = view.findViewById(R.id.btnlvl1);
        Button btnlvl2 = view.findViewById(R.id.btnlvl2);
        Button btnlvl3 = view.findViewById(R.id.btnlvl3);
        Button btnlvl4 = view.findViewById(R.id.btnlvl4);
        Button btnlvl5 = view.findViewById(R.id.btnlvl5);

        Bundle args = getArguments();
        if (args != null) {
            String difficulty = args.getString("DIFFICULTY");

            btnlvl1.setOnClickListener(v -> {
                Intent intent;
                if ("Medium".equals(difficulty)) {
                    intent = new Intent(requireContext(), LevelService.class);
                    intent.putExtra("difficulty", "Medium");
                    intent.putExtra("levelId", 1);
                }
                else if ("Easy".equals(difficulty)) {
                    intent = new Intent(requireContext(), LevelService.class);
                    intent.putExtra("difficulty", "Easy");
                    intent.putExtra("levelId", 1);
                }
                else if ("Hard".equals(difficulty)) {
                    intent = new Intent(requireContext(), LevelService.class);
                    intent.putExtra("difficulty", "Hard");
                    intent.putExtra("levelId", 1);
                }
                else {
                    // Default case if difficulty is not set
                    intent = new Intent(requireContext(), LevelService.class);
                }

                startActivity(intent);
            });

            btnlvl2.setOnClickListener(v -> {
                Intent intent;
                if ("Medium".equals(difficulty)) {
                    intent = new Intent(requireContext(), LevelService.class);
                    intent.putExtra("difficulty", "Medium");
                    intent.putExtra("levelId", 2);
                }
                else if ("Easy".equals(difficulty)) {
                    intent = new Intent(requireContext(), LevelService.class);
                    intent.putExtra("difficulty", "Easy");
                    intent.putExtra("levelId", 2);
                }
                else if ("Hard".equals(difficulty)) {
                    intent = new Intent(requireContext(), LevelService.class);
                    intent.putExtra("difficulty", "Hard");
                    intent.putExtra("levelId", 2);
                }
                else {
                    // Default case if difficulty is not set
                    intent = new Intent(requireContext(), LevelService.class);
                }

                startActivity(intent);
            });

            btnlvl3.setOnClickListener(v -> {
                Intent intent;
                if ("Medium".equals(difficulty)) {
                    intent = new Intent(requireContext(), LevelService.class);
                    intent.putExtra("difficulty", "Medium");
                    intent.putExtra("levelId", 3);
                }
                else if ("Easy".equals(difficulty)) {
                    intent = new Intent(requireContext(), LevelService.class);
                    intent.putExtra("difficulty", "Easy");
                    intent.putExtra("levelId", 3);
                }
                else if ("Hard".equals(difficulty)) {
                    intent = new Intent(requireContext(), LevelService.class);
                    intent.putExtra("difficulty", "Hard");
                    intent.putExtra("levelId", 3);
                }
                else {
                    // Default case if difficulty is not set
                    intent = new Intent(requireContext(), LevelService.class);
                }

                startActivity(intent);
            });

            btnlvl4.setOnClickListener(v -> {
                Intent intent;
                if ("Medium".equals(difficulty)) {
                    intent = new Intent(requireContext(), LevelService.class);
                    intent.putExtra("difficulty", "Medium");
                    intent.putExtra("levelId", 4);
                }
                else if ("Easy".equals(difficulty)) {
                    intent = new Intent(requireContext(), LevelService.class);
                    intent.putExtra("difficulty", "Easy");
                    intent.putExtra("levelId", 4);
                }
                else if ("Hard".equals(difficulty)) {
                    intent = new Intent(requireContext(), LevelService.class);
                    intent.putExtra("difficulty", "Hard");
                    intent.putExtra("levelId", 4);
                }
                else {
                    // Default case if difficulty is not set
                    intent = new Intent(requireContext(), LevelService.class);
                }

                startActivity(intent);
            });

            btnlvl5.setOnClickListener(v -> {
                Intent intent;
                if ("Medium".equals(difficulty)) {
                    intent = new Intent(requireContext(), LevelService.class);
                    intent.putExtra("difficulty", "Medium");
                    intent.putExtra("levelId", 5);
                }
                else if ("Easy".equals(difficulty)) {
                    intent = new Intent(requireContext(), LevelService.class);
                    intent.putExtra("difficulty", "Easy");
                    intent.putExtra("levelId", 5);
                }
                else if ("Hard".equals(difficulty)) {
                    intent = new Intent(requireContext(), LevelService.class);
                    intent.putExtra("difficulty", "Hard");
                    intent.putExtra("levelId", 5);
                }
                else {
                    // Default case if difficulty is not set
                    intent = new Intent(requireContext(), LevelService.class);
                }

                startActivity(intent);
            });
        }

        return view;
    }

}