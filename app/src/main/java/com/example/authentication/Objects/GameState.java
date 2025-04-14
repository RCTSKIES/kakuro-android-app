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
        int size = template.length;

        // Validate all horizontal runs
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (template[row][col] == '-' && (col == 0 || template[row][col - 1] != '-')) {
                    int sum = 0;
                    HashSet<Integer> seen = new HashSet<>();
                    int c = col;
                    while (c < size && template[row][c] == '-') {
                        int val = userInput[row][c];
                        if (val == 0 || seen.contains(val)) return false;
                        sum += val;
                        seen.add(val);
                        c++;
                    }
                    int expected = getRowClue(row, col);
                    if (sum != expected) return false;
                }
            }
        }

        // Validate all vertical runs
        for (int col = 0; col < size; col++) {
            for (int row = 0; row < size; row++) {
                if (template[row][col] == '-' && (row == 0 || template[row - 1][col] != '-')) {
                    int sum = 0;
                    HashSet<Integer> seen = new HashSet<>();
                    int r = row;
                    while (r < size && template[r][col] == '-') {
                        int val = userInput[r][col];
                        if (val == 0 || seen.contains(val)) return false;
                        sum += val;
                        seen.add(val);
                        r++;
                    }
                    int expected = getColumnClue(row, col);
                    if (sum != expected) return false;
                }
            }
        }

        Log.d("GameState", "Grid is valid!");
        return true;
    }


    private int getRowClue(int row, int col) {
        char[][] template = grid.getTemplate();
        int[][] clues = grid.getClues();

        for (int c = col - 1; c >= 0; c--) {
            char type = template[row][c];
            if (type == '/' || type == '=') {
                int clue = clues[row][c];
                if (type == '=') {
                    clue = clue / 100;
                }
                return clue;
            } else if (type != '-') {
                break; // no clue before run
            }
        }

        return 0; // no valid clue found
    }


    private int getColumnClue(int row, int col) {
        char[][] template = grid.getTemplate();
        int[][] clues = grid.getClues();

        for (int r = row - 1; r >= 0; r--) {
            char type = template[r][col];
            if (type == '/' || type == '=') {
                int clue = clues[r][col];
                if (type == '=') {
                    clue = clue % 100;
                }
                return clue;
            } else if (type != '-') {
                break;
            }
        }

        return 0;
    }



}