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

import java.util.Random;

public class PuzzleMediumGameplayActivity extends AppCompatActivity {
    private GridLayout gridLayout;
    private char[][] selectedTemplate;
    private int[][] clueValues; // Holds double clue values
    private PuzzleMediumGenerator puzzle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.puzzle_medium_gameplay);

        gridLayout = findViewById(R.id.gridLayout);
        generateNewPuzzle();
    }

    // Generates a new puzzle
    private void generateNewPuzzle() {
        gridLayout.removeAllViews();

        new Thread(() -> {
            puzzle = new PuzzleMediumGenerator();
            selectedTemplate = puzzle.getSelectedTemplate(); // Get a random 5x5 template
            clueValues = puzzle.getClueGrid(); // Fetch clue values
            runOnUiThread(this::setupGrid); // Ensure UI updates happen on the main thread
        }).start();
    }

    // Generates the 5x5 grid dynamically
    private void setupGrid() {
        gridLayout.setColumnCount(5);
        gridLayout.setRowCount(5);

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                char cellType = selectedTemplate[row][col];
                addCell(row, col, cellType);
            }
        }
    }

    // Creating the UI cells based on the template
    private void addCell(int row, int col, char cellType) {
        View cell;

        if (cellType == '-') {
            // Editable Cell (User input)
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

        } else if (cellType == '/') {
            // Double Clue Cell
            TextView tv = new TextView(this);
            tv.setText(String.valueOf(clueValues[row][col])); // Display precomputed clue
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


    public void onRowColInputCheck(View view) {
        // Check rows
        for (int row = 1; row < 5; row++) {
            int sum = 0;
            boolean allFilled = true;
            for (int col = 1; col < 5; col++) {
                EditText et = (EditText) gridLayout.getChildAt(row * 5 + col);
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
                for (int col = 1; col < 5; col++) {
                    EditText et = (EditText) gridLayout.getChildAt(row * 5 + col);
                    et.setBackgroundColor(color);
                }
            }
        }

        // Check columns
        for (int col = 1; col < 5; col++) {
            int sum = 0;
            boolean allFilled = true;
            for (int row = 1; row < 5; row++) {
                EditText et = (EditText) gridLayout.getChildAt(row * 5 + col);
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

}

