package com.example.authentication.Objects;

public class Grid {
    private char[][] template; // The template of the grid (e.g., '/', '-', '*')
    private int[][] clues;     // The clue values for the grid
    private int[][] userInput; // The user's input for editable cells

    public Grid(char[][] template, int[][] clues, int[][] userInput) {
        this.template = template;
        this.clues = clues;
        this.userInput = userInput;
    }

    // Getters and setters
    public char[][] getTemplate() {
        return template;
    }

    public int[][] getClues() {
        return clues;
    }

    public int[][] getUserInput() {
        return userInput;
    }

    public void setUserInput(int row, int col, int value) {
        if (template[row][col] == '-') {
            userInput[row][col] = value;
        }
    }

    // Check if the grid is fully filled
    public boolean isFullyFilled() {
        for (int row = 0; row < template.length; row++) {
            for (int col = 0; col < template[row].length; col++) {
                if (template[row][col] == '-' && userInput[row][col] == 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
