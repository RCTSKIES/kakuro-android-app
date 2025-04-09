package com.example.authentication.Services;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.authentication.Activities.CompleteDialogueActivity;
import com.example.authentication.Objects.DatabaseManager;
import com.example.authentication.Objects.GameState;
import com.example.authentication.Objects.Grid;
import com.example.authentication.Objects.Level;
import com.example.authentication.Objects.XPCalculator;
import com.example.authentication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LevelService extends AppCompatActivity {
    private GridLayout gridLayout;
    private Grid grid; // Holds the template, clues, and user input
    private GameState gameState; // Tracks the state of the puzzle
    private DatabaseManager dbManager; // Manages database operations
    private XPCalculator xpCalculator; // Manages XP earned
    private int levelId; // The ID of the current level
    private String difficulty; // The difficulty of the current level

    private TextView timerTextView; // Timer display
    private CountDownTimer timer; // Timer for the puzzle
    private long timeElapsed = 0; // Time elapsed in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medium_gameplay);

        gridLayout = findViewById(R.id.gridLayout);
        timerTextView = findViewById(R.id.timerTextView);

        // Get the level ID and difficulty from the intent
        levelId = getIntent().getIntExtra("levelId", 1);
        difficulty = getIntent().getStringExtra("difficulty");

        // Initialize the database manager
        dbManager = new DatabaseManager(this);

        // Load the level from the database or generate a new one
        loadLevel();
    }

    private void completionMessage() {
        // Calculate XP
        XPCalculator xpCalculator = new XPCalculator(timeElapsed, difficulty);
        int totalXP = xpCalculator.calculateXP();

        // Format the time elapsed
        int minutes = (int) (timeElapsed / 1000) / 60;
        int seconds = (int) (timeElapsed / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        // Show the custom dialog
        CompleteDialogueActivity dialog = new CompleteDialogueActivity(this, timeFormatted, totalXP);
        dialog.show();
    }

    // Start the timer
    private void startTimer() {
        timer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeElapsed += 1000; // Increment time elapsed by 1 second
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                // Timer finished (unlikely to happen since we use Long.MAX_VALUE)
            }
        }.start();
    }

    // Update the timer display
    private void updateTimerDisplay() {
        int minutes = (int) (timeElapsed / 1000) / 60;
        int seconds = (int) (timeElapsed / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerTextView.setText(timeFormatted);
    }

    // Load the level from the database or generate a new one
    private void loadLevel() {
        new Thread(() -> {
            // Load the level from the database
            Level level = dbManager.loadLevel(levelId, difficulty);

            if (level != null) {
                // Level exists in the database
                grid = level.getGrid();
                gameState = level.getGameState();
                timeElapsed = level.getTimeElapsed(); // Load saved time elapsed

                Log.d("PuzzleMediumGameplayActivity", "gameState initialized: " + gameState);

                // Update the UI on the main thread
                runOnUiThread(() -> {
                    updateTimerDisplay(); // Update the timer display
                    setupGrid(); // Set up the grid

                    // Start the timer only if the level is not completed
                    if (gameState.getCurrentState() != GameState.State.COMPLETED) {
                        startTimer();
                    } else {
                        // Show the completion dialog if the level is already solved
                        completionMessage();
                    }
                });
            } else {
                // This should never happen because levels are pre-generated
                Log.e("PuzzleMediumGameplayActivity", "Level not found in the database!");
                runOnUiThread(() -> Toast.makeText(this, "Error: Level not found!", Toast.LENGTH_SHORT).show());
                finish(); // Close the activity
            }
        }).start();
    }

    // Generates the grid dynamically (supports both 4x4 and 5x5)
    private void setupGrid() {
        char[][] template = grid.getTemplate();
        int[][] clues = grid.getClues();
        int[][] userInput = grid.getUserInput();


        // Log the template, clues, and userInput for debugging
        Log.d("PuzzleMediumGameplayActivity", "Template: " + Arrays.deepToString(template));
        Log.d("PuzzleMediumGameplayActivity", "Clues: " + Arrays.deepToString(clues));
        Log.d("PuzzleMediumGameplayActivity", "User Input: " + Arrays.deepToString(userInput));

        // Determine the grid size based on the template
        int gridSize = template.length;

        // Set the number of rows and columns in the GridLayout
        gridLayout.setColumnCount(gridSize);
        gridLayout.setRowCount(gridSize);

        // Clear any existing views in the GridLayout
        gridLayout.removeAllViews();


        // Populate the grid
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                char cellType = template[row][col];
                selectNumber(row, col, cellType, clues[row][col], userInput[row][col]);
            }
        }
    }

    // Creating the UI cells based on the template
    private void selectNumber(int row, int col, char cellType, int clueValue, int userInputValue) {
        View cell;

        if (cellType == '-') {
            // Editable Cell (User input)
            EditText et = new EditText(this);
            et.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            et.setBackgroundResource(R.drawable.editable_cell_bg);
            et.setGravity(Gravity.CENTER);


            // Set the initial value from user input (if any)
            if (userInputValue != 0) {
                et.setText(String.valueOf(userInputValue));
            }

            // Add a TextWatcher to validate input
            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String input = s.toString();
                    if (!input.isEmpty()) {
                        int value = Integer.parseInt(input);
                        // Validate number range
                        if (value < 1 || value > 9) {
                            et.setError("Enter a number between 1 and 9");
                            Toast.makeText(LevelService.this, "Please enter a number between 1 and 9", Toast.LENGTH_SHORT).show();
                            et.setText("");
                            grid.setUserInput(row, col, 0);
                        } else {
                            // Check for duplicates in the row and column
                            int gridSize = grid.getTemplate().length;
                            boolean duplicate = false;

                            // Check current row (skip current column)
                            for (int c = 0; c < gridSize; c++) {
                                if (c == col) continue;
                                if (grid.getUserInput()[row][c] == value) {
                                    duplicate = true;
                                    break;
                                }
                            }

                            // Check current column (skip current row)
                            if (!duplicate) {
                                for (int r = 0; r < gridSize; r++) {
                                    if (r == row) continue;
                                    if (grid.getUserInput()[r][col] == value) {
                                        duplicate = true;
                                        break;
                                    }
                                }
                            }

                            if (duplicate) {
                                et.setError("Number already exists in row/column");
                                Toast.makeText(LevelService.this, "Duplicate number in row or column", Toast.LENGTH_SHORT).show();
                                et.setText("");
                                grid.setUserInput(row, col, 0);
                            } else {
                                // Update the grid with the valid input if not a duplicate
                                grid.setUserInput(row, col, value);
                                onRowColInputCheck(et, row, col); // Additional sum validations
                            }
                        }
                    } else {
                        grid.setUserInput(row, col, 0);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            cell = et;

        } else if (cellType == '/' || cellType == '=') {
            // Clue Cell
            TextView tv = new TextView(this);
            if (cellType == '=') {
                // Split the combined clue value into row and column sums
                int rowSum = clueValue / 100;
                int colSum = clueValue % 100;
                tv.setText(rowSum + "\\" + colSum); // Display both sums
            } else {
                tv.setText(String.valueOf(clueValue)); // Display single sum
            }
            tv.setBackgroundResource(R.drawable.clue_cell_bg);
            tv.setGravity(Gravity.CENTER);
            cell = tv;

        } else {
            // Uneditable Empty Cell (*)
            TextView emptyView = new TextView(this);
            emptyView.setBackgroundResource(R.drawable.clue_cell_bg);
            cell = emptyView;
        }

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(row, 1f);
        params.columnSpec = GridLayout.spec(col, 1f);
        params.width = (int) getResources().getDimension(R.dimen.cell_size);
        params.height = (int) getResources().getDimension(R.dimen.cell_size);
        params.setMargins(2, 2, 2, 2);

        gridLayout.addView(cell, params);


    }

    // Validate user input for rows and columns
    public void onRowColInputCheck(View view, int row, int col) {
        // Update user input in the grid
        boolean allFilled = false;
        if (view instanceof EditText) {
            EditText et = (EditText) view;
            String input = et.getText().toString();
            if (!input.isEmpty()) {
                grid.setUserInput(row, col, Integer.parseInt(input));
            }
        }

        int gridSize = grid.getTemplate().length;

        // Check rows
        for (int r = 0; r < gridSize; r++) {
            for (int c = 0; c < gridSize; c++) {
                char cell = grid.getTemplate()[r][c];

                if (cell == '/' || cell == '=') {
                    int expectedSum = (cell == '=')
                            ? grid.getClues()[r][c] / 100
                            : grid.getClues()[r][c];

                    int sum = 0;
                    allFilled = true;
                    List<EditText> involvedCells = new ArrayList<>();

                    // Traverse right for the run of '-' cells
                    for (int k = c + 1; k < gridSize && grid.getTemplate()[r][k] == '-'; k++) {
                        int value = grid.getUserInput()[r][k];
                        if (value == 0) {
                            allFilled = false;
                            break;
                        }
                        sum += value;

                        EditText et = (EditText) gridLayout.getChildAt(r * gridSize + k);
                        involvedCells.add(et);
                    }

                    if (allFilled && !involvedCells.isEmpty()) {
                        int color = (sum == expectedSum) ? Color.GREEN : Color.RED;
                        for (EditText et : involvedCells) {
                            et.setBackgroundColor(color);
                        }
                    }
                }
            }
        }


        // Similar update for column checks
        for (int c = 0; c < gridSize; c++) {
            for (int r = 0; r < gridSize; r++) {
                char cell = grid.getTemplate()[r][c];

                if (cell == '/' || cell == '=') {
                    int expectedSum = (cell == '=')
                            ? grid.getClues()[r][c] % 100
                            : grid.getClues()[r][c];

                    int sum = 0;
                    allFilled = true;
                    List<EditText> involvedCells = new ArrayList<>();

                    // Traverse downward for the run of '-' cells
                    for (int k = r + 1; k < gridSize && grid.getTemplate()[k][c] == '-'; k++) {
                        int value = grid.getUserInput()[k][c];
                        if (value == 0) {
                            allFilled = false;
                            break;
                        }
                        sum += value;

                        EditText et = (EditText) gridLayout.getChildAt(k * gridSize + c);
                        involvedCells.add(et);
                    }

                    if (allFilled && !involvedCells.isEmpty()) {
                        int color = (sum == expectedSum) ? Color.GREEN : Color.RED;
                        for (EditText et : involvedCells) {
                            et.setBackgroundColor(color);
                        }
                    }
                }
            }
        }

        if (allFilled) {
            isComplete(view);
        }

    }


    // Check if the puzzle is solved
    public void isComplete(View view) {
        if (gameState.isSolved()) {
            // Stop the timer
            if (timer != null) {
                timer.cancel();
            }

            // Calculate XP
            XPCalculator xpCalculator = new XPCalculator(timeElapsed, difficulty);
            int totalXP = xpCalculator.calculateXP();

            // Format the time elapsed
            int minutes = (int) (timeElapsed / 1000) / 60;
            int seconds = (int) (timeElapsed / 1000) % 60;
            String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

            // Show the custom dialog
            CompleteDialogueActivity dialog = new CompleteDialogueActivity(this, timeFormatted, totalXP);
            dialog.show();

            // Save the level state (optional)
            gameState.setCurrentState(GameState.State.COMPLETED);
            dbManager.saveLevel(levelId, difficulty, grid, gameState.getCurrentState(), timeElapsed);
            dbManager.saveXP(levelId, difficulty, totalXP);

        }
    }

    // Handle the Quit button click
    public void onQuit(View view) {
        // Save the current state only if the level is not completed
        if (gameState.getCurrentState() != GameState.State.COMPLETED) {
            gameState.setCurrentState(GameState.State.ONGOING);
            dbManager.saveLevel(levelId, difficulty, grid, gameState.getCurrentState(), timeElapsed);
        }

        // Stop the timer
        if (timer != null) {
            timer.cancel();
        }

        // Close the activity
        finish();
    }

    // Save the level when the player quits
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Save the current state only if the level is not completed
        if (gameState.getCurrentState() != GameState.State.COMPLETED) {
            gameState.setCurrentState(GameState.State.ONGOING);
            dbManager.saveLevel(levelId, difficulty, grid, gameState.getCurrentState(), timeElapsed);
        }

        // Stop the timer
        if (timer != null) {
            timer.cancel();
        }
    }



}