package com.example.kakurotest;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PuzzleGameplayActivity extends AppCompatActivity {
    private GridLayout gridLayout;
    private PuzzleGenerator puzzle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_gameplay);

        gridLayout = findViewById(R.id.gridLayout);
        generateNewPuzzle();
    }

    // Generates a new puzzle
    private void generateNewPuzzle() {
        gridLayout.removeAllViews();

        new Thread(() -> {
            puzzle = new PuzzleGenerator();
            runOnUiThread(this::setupGrid); // Ensure UI updates happen on the main thread
        }).start();
    }

    // Generating the field logic
    private void setupGrid() {
        gridLayout.setColumnCount(4);
        gridLayout.setRowCount(4);

        // Create empty top-left cell
        addCell(0, 0, "", true);

        // Add column clues (top row)
        int[] colClues = puzzle.getColClues();
        for(int col=1; col<4; col++) {
            addCell(0, col, String.valueOf(colClues[col-1]), true);
        }

        // Add row clues (left column) and editable cells
        int[] rowClues = puzzle.getRowClues();
        for(int row=1; row<4; row++) {
            addCell(row, 0, String.valueOf(rowClues[row-1]), true);

            for(int col=1; col<4; col++) {
                addCell(row, col, "", false);
            }
        }
    }

    // Creating the cells and the overall layout of the 4x4 in the UI
    private void addCell(int row, int col, String text, boolean isClue) {

        View cell;

        // Initializing clue cells(grey cells containing the numbers)
        if(isClue) {
            TextView tv = new TextView(this);
            tv.setText(text);
            tv.setBackgroundResource(R.drawable.clue_cell_bg);
            tv.setGravity(Gravity.CENTER);

            cell = tv;


        // Initializing editable cells where the user will input the numbers
        } else {
            EditText et = new EditText(this);
            et.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            et.setBackgroundResource(R.drawable.editable_cell_bg);
            et.setGravity(Gravity.CENTER);

            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    onRowColInputCheck(et);
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            cell = et;
        }

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(row, 1f);
        params.columnSpec = GridLayout.spec(col, 1f);
        params.width = (int) getResources().getDimension(R.dimen.cell_size);
        params.height = (int) getResources().getDimension(R.dimen.cell_size);

        gridLayout.addView(cell, params);


    }

    public void onRowColInputCheck(View view) {
        // Check rows
        for (int row = 1; row < 4; row++) {
            int sum = 0;
            boolean allFilled = true;
            for (int col = 1; col < 4; col++) {
                EditText et = (EditText) gridLayout.getChildAt(row * 4 + col);
                String input = et.getText().toString();
                if (input.isEmpty()) {
                    allFilled = false;
                    break;
                }
                sum += Integer.parseInt(input);
            }
            // If all cells are filled, color them green or red based on correctness
            if (allFilled) {
                int color = (sum == puzzle.getRowClues()[row - 1]) ? Color.GREEN : Color.RED;
                for (int col = 1; col < 4; col++) {
                    EditText et = (EditText) gridLayout.getChildAt(row * 4 + col);
                    et.setBackgroundColor(color);
                }
            }
        }

        // Check columns
        for (int col = 1; col < 4; col++) {
            int sum = 0;
            boolean allFilled = true;
            for (int row = 1; row < 4; row++) {
                EditText et = (EditText) gridLayout.getChildAt(row * 4 + col);
                String input = et.getText().toString();
                if (input.isEmpty()) {
                    allFilled = false;
                    break;
                }
                sum += Integer.parseInt(input);
            }
            // If all cells are filled, color them green or red based on correctness
            if (allFilled) {
                int color = (sum == puzzle.getColClues()[col - 1]) ? Color.GREEN : Color.RED;
                for (int row = 1; row < 4; row++) {
                    EditText et = (EditText) gridLayout.getChildAt(row * 4 + col);
                    et.setBackgroundColor(color);
                }
            }
        }
    }


    public void onCheckSolution(View view) {
        boolean isValid = true;

        // Check rows
        for(int row=1; row<4; row++) {
            int sum = 0;
            for(int col=1; col<4; col++) {
                EditText et = (EditText) gridLayout.getChildAt(row*4 + col);
                String input = et.getText().toString();
                if(input.isEmpty()) {
                    isValid = false;
                    break;
                }
                sum += Integer.parseInt(input);
            }
            if(sum != puzzle.getRowClues()[row-1]) isValid = false;
        }

        // Check columns
        for(int col=1; col<4; col++) {
            int sum = 0;
            for(int row=1; row<4; row++) {
                EditText et = (EditText) gridLayout.getChildAt(row*4 + col);
                String input = et.getText().toString();
                sum += Integer.parseInt(input);
            }
            if(sum != puzzle.getColClues()[col-1]) isValid = false;
        }

        // Show result
        if (isValid) {
            Toast.makeText(this, "Congratulations! Solution is correct 🎉", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Incorrect solution. Try again!", Toast.LENGTH_SHORT).show();
        }
    }
}