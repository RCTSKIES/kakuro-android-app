package com.example.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.authentication.Services.LevelService;

public class PuzzleListActivity extends AppCompatActivity {

    Button btnlvl1, btnlvl2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_puzzle_list);

        btnlvl1 = findViewById(R.id.btnlvl1);
        btnlvl2 = findViewById(R.id.btnlvl2);

        btnlvl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent receivedIntent = getIntent();
                String difficulty = receivedIntent.getStringExtra("DIFFICULTY"); // Get difficulty level

                Intent intent;
                if ("Medium".equals(difficulty)) {
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                    intent.putExtra("difficulty", "Medium");
                    intent.putExtra("levelId", 1);
                }
                else if ("Easy".equals(difficulty)) {
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                    intent.putExtra("difficulty", "Easy");
                    intent.putExtra("levelId", 1);
                }
//                else if ("Hard".equals(difficulty)) {
//                    intent = new Intent(PuzzleListActivity.this, PuzzleHardGameplayActivity.class);
//                }
                else {
                    // Default case if difficulty is not set
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                }

                startActivity(intent);
            }
        });

        btnlvl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent receivedIntent = getIntent();
                String difficulty = receivedIntent.getStringExtra("DIFFICULTY"); // Get difficulty level

                Intent intent;
                if ("Medium".equals(difficulty)) {
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                    intent.putExtra("difficulty", "Medium");
                    intent.putExtra("levelId", 2);
                }
                else if ("Easy".equals(difficulty)) {
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                    intent.putExtra("difficulty", "Easy");
                    intent.putExtra("levelId", 2);
                }
//                else if ("Hard".equals(difficulty)) {
//                    intent = new Intent(PuzzleListActivity.this, PuzzleHardGameplayActivity.class);
//                }
                else {
                    // Default case if difficulty is not set
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                }

                startActivity(intent);
            }
        });
    }
}