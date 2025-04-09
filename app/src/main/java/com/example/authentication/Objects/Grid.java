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
}
