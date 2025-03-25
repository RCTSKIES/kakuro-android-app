package com.example.authentication.Objects;


import android.util.Log;

import java.util.HashSet;

public class GameState {
    public enum State {
        UNSTARTED,
        ONGOING,
        COMPLETED
    }

    private State currentState;
    private Grid grid;

    public GameState(Grid grid, State state) {
        this.grid = grid;
        this.currentState = state;
    }
    public State getCurrentState() {
        return currentState;
    }

    // Setter for the current state
    public void setCurrentState(State state) {
        Log.d("GameState", "Setting current state to: " + state);
        this.currentState = state;
    }

    // Getter for the grid
    public Grid getGrid() {
        return grid;
    }

    // Check if the puzzle is solved correctly
    public boolean isSolved() {
        Log.d("GameState", "Checking if puzzle is solved...");
        boolean isValid = validateGrid();
        Log.d("GameState", "Validation result: " + isValid);
        if (isValid) {
            setCurrentState(State.COMPLETED); // Set state to COMPLETED if the grid is valid
        }
        return isValid;
    }

    private boolean validateGrid() {
        Log.d("GameState", "Validating grid...");
        char[][] template = grid.getTemplate();
        int[][] userInput = grid.getUserInput();
        int[][] clues = grid.getClues();

        // Check rows
        for (int row = 0; row < template.length; row++) {
            boolean hasEditableCells = false; // Track if the row has editable cells
            int sum = 0;
            HashSet<Integer> usedNumbers = new HashSet<>();
            for (int col = 0; col < template[row].length; col++) {
                if (template[row][col] == '-') {
                    hasEditableCells = true; // Row has editable cells
                    int value = userInput[row][col];
                    if (value == 0) {
                        Log.d("GameState", "Empty cell at row=" + row + ", col=" + col);
                        return false; // Cell is empty
                    }
                    if (usedNumbers.contains(value)) {
                        Log.d("GameState", "Duplicate number " + value + " in row=" + row);
                        return false; // Duplicate number in row
                    }
                    usedNumbers.add(value);
                    sum += value;
                }
            }

            // Skip rows without editable cells
            if (!hasEditableCells) {
                Log.d("GameState", "Skipping row " + row + " (no editable cells)");
                continue;
            }

            // Check if the row sum matches the clue
            int expectedSum = getRowClue(row);
            if (sum != expectedSum) {
                Log.d("GameState", "Row sum mismatch at row=" + row + ": expected=" + expectedSum + ", actual=" + sum);
                return false;
            }
        }

        // Check columns
        for (int col = 0; col < template[0].length; col++) {
            boolean hasEditableCells = false; // Track if the column has editable cells
            int sum = 0;
            HashSet<Integer> usedNumbers = new HashSet<>();
            for (int row = 0; row < template.length; row++) {
                if (template[row][col] == '-') {
                    hasEditableCells = true; // Column has editable cells
                    int value = userInput[row][col];
                    if (value == 0) {
                        Log.d("GameState", "Empty cell at row=" + row + ", col=" + col);
                        return false; // Cell is empty
                    }
                    if (usedNumbers.contains(value)) {
                        Log.d("GameState", "Duplicate number " + value + " in col=" + col);
                        return false; // Duplicate number in column
                    }
                    usedNumbers.add(value);
                    sum += value;
                }
            }

            // Skip columns without editable cells
            if (!hasEditableCells) {
                Log.d("GameState", "Skipping column " + col + " (no editable cells)");
                continue;
            }

            // Check if the column sum matches the clue
            int expectedSum = getColumnClue(col);
            if (sum != expectedSum) {
                Log.d("GameState", "Column sum mismatch at col=" + col + ": expected=" + expectedSum + ", actual=" + sum);
                return false;
            }
        }

        Log.d("GameState", "Grid is valid!");
        return true; // Grid is valid
    }

    private int getRowClue(int row) {
        char[][] template = grid.getTemplate();
        int[][] clues = grid.getClues();

        for (int col = 0; col < template[row].length; col++) {
            if (template[row][col] == '/' || template[row][col] == '=') {
                int clueValue = clues[row][col];
                if (template[row][col] == '=') {
                    // For '=' cells, the row clue is the first part of the combined value
                    clueValue = clueValue / 100;
                }
                Log.d("getRowClue", "Row clue at row=" + row + ": " + clueValue);
                return clueValue;
            }
        }
        Log.d("getRowClue", "No row clue found for row=" + row);
        return 0; // No clue found
    }

    private int getColumnClue(int col) {
        char[][] template = grid.getTemplate();
        int[][] clues = grid.getClues();

        for (int row = 0; row < template.length; row++) {
            if (template[row][col] == '/' || template[row][col] == '=') {
                int clueValue = clues[row][col];
                if (template[row][col] == '=') {
                    // For '=' cells, the column clue is the second part of the combined value
                    clueValue = clueValue % 100;
                }
                Log.d("getColumnClue", "Column clue at col=" + col + ": " + clueValue);
                return clueValue;
            }
        }
        Log.d("getColumnClue", "No column clue found for col=" + col);
        return 0; // No clue found
    }
}