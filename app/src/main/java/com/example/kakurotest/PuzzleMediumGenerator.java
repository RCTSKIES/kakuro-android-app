package com.example.kakurotest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PuzzleMediumGenerator {
    private char[][] selectedTemplate;
    private int[][] solutionGrid;
    private int[][] clueGrid;
    private int[] rowClues;  // Added rowClues
    private int[] colClues;  // Added colClues

    private final char[][][] templates = {
            // First template
            {
                    {'*', '*', '*', '*', '*'},
                    {'*', '/', '-', '-', '*'},
                    {'*', '-', '-', '-', '-'},
                    {'*', '-', '-', '-', '-'},
                    {'*', '/', '-', '-', '*'}
            },
            // Second template
            {
                    {'*', '/', '/', '*', '*'},
                    {'/', '-', '-', '/', '*'},
                    {'/', '-', '-', '-', '/'},
                    {'*', '/', '-', '-', '-'},
                    {'*', '*', '/', '-', '-'}

            },
            // Third template
            {
                    {'*', '*', '/', '/', '/'},
                    {'*', '/', '-', '-', '-'},
                    {'*', '/', '-', '-', '-'},
                    {'/', '-', '-', '-', '*'},
                    {'/', '-', '-', '-', '*'}
            }
    };

    public PuzzleMediumGenerator() {
        selectRandomTemplate();
        generatePuzzleGrid();
        generateClueValues();
    }

    private void selectRandomTemplate() {
        Random rand = new Random();
        int index = rand.nextInt(templates.length); // Pick a random template
        selectedTemplate = templates[index];
    }

    private void generatePuzzleGrid() {
        solutionGrid = new int[5][5];  // 5x5 grid
        rowClues = new int[5];          // Store row clues
        colClues = new int[5];          // Store column clues

        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            numbers.add(i);  // Fill list with numbers 1 to 9
        }

        Collections.shuffle(numbers);  // Shuffle numbers to randomize

        int numIndex = 0;

        // Fill the grid with random numbers from the shuffled list
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (selectedTemplate[row][col] == '/') {
                    solutionGrid[row][col] = numbers.get(numIndex++);
                }
            }
        }

        // Calculate row and column clues (sums)
        calculateClues();
    }

    // Calculate the row and column clues (sums)
    private void calculateClues() {
        // Row clues
        for (int row = 0; row < 5; row++) {
            int sum = 0;
            for (int col = 0; col < 5; col++) {
                sum += solutionGrid[row][col];
            }
            rowClues[row] = sum;  // Store sum in rowClues
        }

        // Column clues
        for (int col = 0; col < 5; col++) {
            int sum = 0;
            for (int row = 0; row < 5; row++) {
                sum += solutionGrid[row][col];
            }
            colClues[col] = sum;  // Store sum in colClues
        }
    }


    private boolean checkColumnsValid() {
        for (int col = 0; col < 5; col++) {
            for (int row = 0; row < 4; row++) {
                if (solutionGrid[row][col] == solutionGrid[row + 1][col]) {
                    return false;  // Prevent duplicate numbers in columns
                }
            }
        }
        return true;
    }

    private void generateClueValues() {
        clueGrid = new int[5][5];

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (selectedTemplate[row][col] == '/') {
                    clueGrid[row][col] = generateRandomClue();
                    rowClues[row] += clueGrid[row][col]; // Summing for row clues
                    colClues[col] += clueGrid[row][col]; // Summing for column clues
                }
            }
        }
    }

    private int generateRandomClue() {
        return (new Random().nextInt(15) + 10); // Generates sum clues between 10-25
    }

    // Getters
    public char[][] getSelectedTemplate() {
        return selectedTemplate;
    }

    public int[][] getClueGrid() {
        return clueGrid;
    }

    public int[][] getSolution() {
        return solutionGrid;
    }

    // Setters
    public void setRowClues(int[] rowClues) {
        this.rowClues = rowClues;
    }

    public void setColClues(int[] colClues) {
        this.colClues = colClues;
    }

    // Getters for row and column clues
    public int[] getRowClues() {
        return rowClues;
    }

    public int[] getColClues() {
        return colClues;
    }
}
