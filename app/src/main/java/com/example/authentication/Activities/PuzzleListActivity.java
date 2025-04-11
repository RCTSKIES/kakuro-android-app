package com.example.authentication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.authentication.R;
import com.example.authentication.Services.LevelService;

public class PuzzleListActivity extends NavbarTopActivity {

    Button btnlvl1, btnlvl2, btnlvl3, btnlvl4, btnlvl5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getLayoutInflater().inflate(R.layout.activity_puzzle_list, findViewById(R.id.content_frame));

        btnlvl1 = findViewById(R.id.btnlvl1);
        btnlvl2 = findViewById(R.id.btnlvl2);
        btnlvl3 = findViewById(R.id.btnlvl3);
        btnlvl4 = findViewById(R.id.btnlvl4);
        btnlvl5 = findViewById(R.id.btnlvl5);


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
                else if ("Hard".equals(difficulty)) {
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                    intent.putExtra("difficulty", "Hard");
                    intent.putExtra("levelId", 1);
                }
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
                else if ("Hard".equals(difficulty)) {
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                    intent.putExtra("difficulty", "Hard");
                    intent.putExtra("levelId", 2);
                }
                else {
                    // Default case if difficulty is not set
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                }

                startActivity(intent);
            }
        });

        btnlvl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent receivedIntent = getIntent();
                String difficulty = receivedIntent.getStringExtra("DIFFICULTY"); // Get difficulty level

                Intent intent;
                if ("Medium".equals(difficulty)) {
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                    intent.putExtra("difficulty", "Medium");
                    intent.putExtra("levelId", 3);
                }
                else if ("Easy".equals(difficulty)) {
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                    intent.putExtra("difficulty", "Easy");
                    intent.putExtra("levelId", 3);
                }  else if ("Hard".equals(difficulty)) {
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                    intent.putExtra("difficulty", "Hard");
                    intent.putExtra("levelId", 3);
                }
                else {
                    // Default case if difficulty is not set
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                }

                startActivity(intent);
            }
        });

        btnlvl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent receivedIntent = getIntent();
                String difficulty = receivedIntent.getStringExtra("DIFFICULTY"); // Get difficulty level

                Intent intent;
                if ("Medium".equals(difficulty)) {
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                    intent.putExtra("difficulty", "Medium");
                    intent.putExtra("levelId", 4);
                }
                else if ("Easy".equals(difficulty)) {
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                    intent.putExtra("difficulty", "Easy");
                    intent.putExtra("levelId", 4);
                }  else if ("Hard".equals(difficulty)) {
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                    intent.putExtra("difficulty", "Hard");
                    intent.putExtra("levelId", 4);
                }
                else {
                    // Default case if difficulty is not set
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                }

                startActivity(intent);
            }
        });

        btnlvl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent receivedIntent = getIntent();
                String difficulty = receivedIntent.getStringExtra("DIFFICULTY"); // Get difficulty level

                Intent intent;
                if ("Medium".equals(difficulty)) {
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                    intent.putExtra("difficulty", "Medium");
                    intent.putExtra("levelId", 5);
                }
                else if ("Easy".equals(difficulty)) {
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                    intent.putExtra("difficulty", "Easy");
                    intent.putExtra("levelId", 5);
                }
                else if ("Hard".equals(difficulty)) {
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                    intent.putExtra("difficulty", "Hard");
                    intent.putExtra("levelId", 5);
                }
                else {
                    // Default case if difficulty is not set
                    intent = new Intent(PuzzleListActivity.this, LevelService.class);
                }

                startActivity(intent);
            }
        });
    }
}