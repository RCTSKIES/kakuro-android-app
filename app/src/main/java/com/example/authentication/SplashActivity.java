package com.example.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.TextView;

import com.example.authentication.Generators.PuzzleGenerator4x4;
import com.example.authentication.Generators.PuzzleGenerator5x5;
import com.example.authentication.Objects.DatabaseManager;
import com.example.authentication.Objects.GameState;
import com.example.authentication.Objects.Grid;

import java.util.Arrays;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView appTitle = findViewById(R.id.text_splash);
        appTitle.setText("Kakuro"); // Set the text in the center

        new Thread(() -> {
            initializeLevels(SplashActivity.this);
            runOnUiThread(() -> {
                // Proceed to the next activity after initialization
                Intent intent = new Intent(SplashActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            });
        }).start();
    }

    public void initializeLevels(Context context) {
        DatabaseManager dbManager = new DatabaseManager(context);

//        dbManager.clearDatabase(context);
//        Log.d("App", "Database cleared");

        // Check if levels already exist in the database
        if (dbManager.levelsExist()) {
            Log.d("App", "Levels already initialized");
            return;
        }

        // Generate 5 Easy levels (4x4 grid)
        for (int levelId = 1; levelId <= 5; levelId++) {
            PuzzleGenerator4x4 generator = new PuzzleGenerator4x4();
            Grid grid = generator.generateGrid();

            // Log the generated grid
            Log.d("App", "Generated Easy level " + levelId);
            Log.d("App", "Template: " + Arrays.deepToString(grid.getTemplate()));
            Log.d("App", "Clues: " + Arrays.deepToString(grid.getClues()));

            // Insert the level into the database
            dbManager.insertLevel(levelId, "Easy", 4, grid, GameState.State.UNSTARTED, 0);
            Log.d("App", "Inserted Easy level " + levelId);
        }

        // Generate 5 Medium levels (5x5 grid)
        for (int levelId = 1; levelId <= 5; levelId++) {
            PuzzleGenerator5x5 generator = new PuzzleGenerator5x5();
            Grid grid = generator.generateGrid();

            // Log the generated grid
            Log.d("App", "Generated Medium level " + levelId);
            Log.d("App", "Template: " + Arrays.deepToString(grid.getTemplate()));
            Log.d("App", "Clues: " + Arrays.deepToString(grid.getClues()));

            // Insert the level into the database
            dbManager.insertLevel(levelId, "Medium", 5, grid, GameState.State.UNSTARTED, 0);
            Log.d("App", "Inserted Medium level " + levelId);
        }

        Log.d("App", "Initialized 5 Easy and 5 Medium levels");
    }
}
