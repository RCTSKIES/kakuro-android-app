package com.example.authentication.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.TextView;

import com.example.authentication.Factory.PuzzleGeneratorFactory;
import com.example.authentication.Helpers.StringHelper;
import com.example.authentication.Interfaces.PuzzleGenerator;
import com.example.authentication.Managers.LocalDatabaseManager;
import com.example.authentication.Objects.GameState;
import com.example.authentication.Objects.Grid;
import com.example.authentication.R;
import com.example.authentication.Services.RealtimeDBService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.Objects;

public class LocalSplashActivity extends AppCompatActivity {

    private PuzzleGeneratorFactory factory;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView appTitle = findViewById(R.id.text_splash);
        appTitle.setText("Kakuro"); // Set the text in the center

        // Create a background thread for the online level generation
        new Thread(() -> {
            initializeLevels(LocalSplashActivity.this);
            fetchCurrentUser();
            //seedXpValues();
            runOnUiThread(() -> {
                // Proceed to the next activity after initialization
                Intent intent = new Intent(LocalSplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }).start();
    }

    public void initializeLevels(Context context) {
        LocalDatabaseManager dbManager = new LocalDatabaseManager(context);
        PuzzleGenerator generator;
        factory = new PuzzleGeneratorFactory();

//        dbManager.clearDatabase(context);
//        Log.d("App", "Database cleared");

        // Check if levels already exist in the database
        if (dbManager.levelsExist()) {
            Log.d("App", "Levels already initialized");
            return;
        }

        // Generate 5 Easy levels (4x4 grid)
        for (int levelId = 1; levelId <= 5; levelId++) {
            Grid grid = factory.getGenerator(4).generateGrid();
            int gridSize = 4;

            // Log the generated grid
            Log.d("App", "Generated Easy level " + levelId);
            Log.d("App", "Grid Size: " + gridSize);
            Log.d("App", "Template: " + Arrays.deepToString(grid.getTemplate()));
            Log.d("App", "Clues: " + Arrays.deepToString(grid.getClues()));

            // Insert the level into the database
            dbManager.insertLevel(levelId, "Easy", 4, grid, GameState.State.UNSTARTED, 0);
            Log.d("App", "Inserted Easy level " + levelId + " grid size: " + gridSize);
        }

        // Generate 5 Medium levels (5x5 grid)
        for (int levelId = 1; levelId <= 5; levelId++) {
            Grid grid = factory.getGenerator(5).generateGrid();
            int gridSize = 5;

            // Log the generated grid
            Log.d("App", "Generated Medium level " + levelId);
            Log.d("App", "Grid Size: " + gridSize);
            Log.d("App", "Template: " + Arrays.deepToString(grid.getTemplate()));
            Log.d("App", "Clues: " + Arrays.deepToString(grid.getClues()));

            // Insert the level into the database
            dbManager.insertLevel(levelId, "Medium", 5, grid, GameState.State.UNSTARTED, 0);
            Log.d("App", "Inserted Medium level " + levelId + " grid size: " + gridSize);
        }

        // Generate 5 Hard levels (9x9 grid)
        for (int levelId = 1; levelId <= 5; levelId++) {
            Grid grid = factory.getGenerator(9).generateGrid();
            int gridSize = 9;

            // Log the generated grid
            Log.d("App", "Generated Hard level " + levelId);
            Log.d("App", "Grid Size: " + gridSize);
            Log.d("App", "Template: " + Arrays.deepToString(grid.getTemplate()));
            Log.d("App", "Clues: " + Arrays.deepToString(grid.getClues()));

            // Insert the level into the database
            dbManager.insertLevel(levelId, "Hard", 9, grid, GameState.State.UNSTARTED, 0);
            Log.d("App", "Inserted Hard level " + levelId + " grid size: " + gridSize);
        }


        Log.d("App", "Initialized 5 Easy, 5 Medium and 5 Hard levels and 1 ONLINE TEST level");
    }

    public void fetchCurrentUser(){
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            String email = StringHelper.encodeEmail(Objects.requireNonNull(auth.getCurrentUser().getEmail()));
            RealtimeDBService.setUsername(email);
        }

    }

}
