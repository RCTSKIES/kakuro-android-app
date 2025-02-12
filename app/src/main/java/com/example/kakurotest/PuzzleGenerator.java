package com.example.kakurotest;

import java.util.Random;

public class PuzzleGenerator {
    private int[][] solutionGrid;
    private int[] rowClues;
    private int[] colClues;

    public PuzzleGenerator() {
        generateNewPuzzle();
    }

    private void generateNewPuzzle() {
        solutionGrid = new int[3][3];
        rowClues = new int[3];
        colClues = new int[3];

        // Generate valid 3x3 grid with unique numbers in rows/columns
        generateValidGrid();

        // Calculate row and column clues
        calculateClues();
    }

    private void generateValidGrid() {
        Random rand = new Random();
        int attempts = 0;
        boolean validGrid = false;

        while (!validGrid && attempts < 100) {  // Limit to 100 attempts to prevent infinite loops
            attempts++;

            // Generate unique values for each row
            int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
            shuffleArray(numbers, rand);  // Shuffle the array

            for (int i = 0; i < 3; i++) {
                solutionGrid[i][0] = numbers[i * 3];
                solutionGrid[i][1] = numbers[i * 3 + 1];
                solutionGrid[i][2] = numbers[i * 3 + 2];
            }

            // Check if columns are unique
            if (checkColumnsValid()) {
                validGrid = true;
            }
        }

        if (!validGrid) {
            throw new RuntimeException("Failed to generate a valid Kakuro grid after 100 attempts");
        }
    }

    // Helper method to shuffle the array
    private void shuffleArray(int[] array, Random rand) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
    }



    private boolean checkColumnsValid() {
        for(int col=0; col<3; col++) {
            if(solutionGrid[0][col] == solutionGrid[1][col] ||
                    solutionGrid[0][col] == solutionGrid[2][col] ||
                    solutionGrid[1][col] == solutionGrid[2][col]) {
                return false;
            }
        }
        return true;
    }

    private void calculateClues() {
        // Row clues (will go in first column)
        for(int i=0; i<3; i++) {
            rowClues[i] = solutionGrid[i][0] + solutionGrid[i][1] + solutionGrid[i][2];
        }

        // Column clues (will go in first row)
        for(int j=0; j<3; j++) {
            colClues[j] = solutionGrid[0][j] + solutionGrid[1][j] + solutionGrid[2][j];
        }
    }

    public int[] getRowClues() { return rowClues; }
    public int[] getColClues() { return colClues; }
    public int[][] getSolution() { return solutionGrid; }
}
