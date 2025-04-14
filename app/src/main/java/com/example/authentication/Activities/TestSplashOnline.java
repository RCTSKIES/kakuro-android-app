package com.example.authentication.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.TextView;

import com.example.authentication.Generators.PuzzleGenerator4x4;
import com.example.authentication.Generators.PuzzleGenerator5x5;
import com.example.authentication.Generators.PuzzleGenerator9x9;
import com.example.authentication.Generators.PuzzleGeneratorOnline;
import com.example.authentication.Objects.DatabaseManager;
import com.example.authentication.Objects.FirebaseManager;
import com.example.authentication.Objects.GameState;
import com.example.authentication.Objects.Grid;
import com.example.authentication.R;
import com.example.authentication.Services.LevelService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestSplashOnline extends AppCompatActivity {

    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView appTitle = findViewById(R.id.text_splash);
        appTitle.setText("Kakuro");

//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE, 1); // Move to tomorrow
//        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());


        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Create a background thread for the online level generation
        new Thread(() -> {

            FirebaseManager.getDailyChallengeDate(storedDate -> {
                if (storedDate == null || !storedDate.equals(today)) {
                    // If no date is stored or the stored Date mismatch — regenerate daily challenge
                    regenerateDailyChallenge(today);
                }
            });

            generateOnlineLevel(TestSplashOnline.this); // Run online level generation in separate thread
            //         Log.d("SplashActivity", "Starting MainActivity...");
            runOnUiThread(() -> {
                // Proceed to the next activity after initialization
                Intent intent = new Intent(TestSplashOnline.this, LevelService.class);
                intent.putExtra("difficulty", "Online");
                intent.putExtra("levelId", 1);
                startActivity(intent);
                finish();
            });
        }).start();
    }

    private void generateOnlineLevel(Context context) {
        //DatabaseManager dbManager = new DatabaseManager(context);
        FirebaseManager fbManager = new FirebaseManager();



        // Check if the level exists before generating
        FirebaseManager.levelExists(1, "Online", exists -> {
            if (exists) {
                Log.d("App", "Online level already exists.");
                return;
            }

            // Generate 1 Hard level (ONLINE TEST)
            for (int levelId = 1; levelId == 1; levelId++) {
                PuzzleGeneratorOnline generator = new PuzzleGeneratorOnline();
                Grid grid = generator.generateGrid();
                int gridSize = 15;

                // Log the generated grid
                Log.d("App", "Generated Online level " + levelId);
                Log.d("App", "Grid Size: " + gridSize);
                Log.d("App", "Template: " + Arrays.deepToString(grid.getTemplate()));
                Log.d("App", "Clues: " + Arrays.deepToString(grid.getClues()));

                // Insert the level into the database
                fbManager.insertLevel(levelId, "Online", 15, grid);
                //dbManager.insertLevel(levelId, "Online", 15, grid, GameState.State.UNSTARTED, 0);
                Log.d("App", "Inserted ONLINE TEST level " + levelId + " grid size: " + gridSize);
            }

            Log.d("App", "Initialized ONLINE TEST level");

        });
    }

    private void regenerateDailyChallenge(String today) {
        PuzzleGeneratorOnline generator = new PuzzleGeneratorOnline();
        Grid newGrid = generator.generateGrid();

        int levelId = 1; // Always reusing the same "daily challenge" level ID
        String difficulty = "Online";

        // Replace level in Firebase
        FirebaseManager.insertLevel(levelId, difficulty, newGrid.getTemplate().length, newGrid);

        // Clear all user-specific inputs
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DataSnapshot user : task.getResult().getChildren()) {
                    usersRef.child(user.getKey())
                            .child("levels")
                            .child(String.valueOf(levelId))
                            .child(difficulty)
                            .removeValue();
                }

                //Update the date after regeneration
                FirebaseManager.updateDailyChallengeDate(today);
            }
        });
    }
}
