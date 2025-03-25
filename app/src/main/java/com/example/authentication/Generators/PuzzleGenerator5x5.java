package com.example.authentication.Generators;

import android.util.Log;

import com.example.authentication.Objects.Grid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class PuzzleGenerator5x5 {
    private final char[][][] templates = {
            // First template
            {
                    {'*', '*', '/', '/', '*'},
                    {'*', '=', '-', '-', '/'},
                    {'/', '-', '-', '-', '-'},
                    {'/', '-', '-', '-', '-'},
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
                    {'*', '/', '-', '-', '-'},
                    {'*', '/', '-', '-', '-'}
            }
    };

    public PuzzleGenerator5x5() {
        // Constructor remains empty
    }

    // Generates a new Grid with a random template, numbers, and clues
    public Grid generateGrid() {
        char[][] selectedTemplate = selectRandomTemplate();
        int[][] numbers = generateNumbers(selectedTemplate);
        int[][] clues = generateClues(selectedTemplate, numbers);
        int[][] userInput = new int[5][5];

        // Log the generated grid and clues
        Log.d("PuzzleGenerator5x5", "Template: " + Arrays.deepToString(selectedTemplate));
        Log.d("PuzzleGenerator5x5", "Numbers: " + Arrays.deepToString(numbers));
        Log.d("PuzzleGenerator5x5", "Clues: " + Arrays.deepToString(clues));

        return new Grid(selectedTemplate, clues, userInput);
    }

    // Selects a random template from the available templates
    private char[][] selectRandomTemplate() {
        Random rand = new Random();
        int index = rand.nextInt(templates.length);
        return templates[index];
    }

    // Generates unique numbers (1-9) for the editable cells ('-')
    private int[][] generateNumbers(char[][] template) {
        int[][] numbers = new int[5][5];
        Random rand = new Random();

        for (int row = 0; row < 5; row++) {
            HashSet<Integer> usedNumbersRow = new HashSet<>();
            for (int col = 0; col < 5; col++) {
                if (template[row][col] == '-') {
                    int num;
                    do {
                        num = rand.nextInt(9) + 1; // Generate numbers 1-9
                    } while (usedNumbersRow.contains(num));
                    usedNumbersRow.add(num);
                    numbers[row][col] = num;
                }
            }
        }

        return numbers;
    }

    // Generates clues for the clue cells ('/')
    private int[][] generateClues(char[][] template, int[][] numbers) {
        int[][] clues = new int[5][5];

        // Calculate row sums and assign to clue cells
        for (int row = 0; row < 5; row++) {
            int rowSum = 0;
            int clueCol = -1; // Column index of the clue cell for this row

            // Find the clue cell for this row and calculate the row sum
            for (int col = 0; col < 5; col++) {
                if (template[row][col] == '-') {
                    rowSum += numbers[row][col];
                } else if (template[row][col] == '/' || template[row][col] == '=') {
                    clueCol = col; // This is the clue cell for the row
                }
            }

            // Assign the row sum to the clue cell (if found)
            if (clueCol != -1) {
                clues[row][clueCol] = rowSum;
            }
        }

        // Calculate column sums and assign to clue cells
        for (int col = 0; col < 5; col++) {
            int colSum = 0;
            int clueRow = -1; // Row index of the clue cell for this column

            // Find the clue cell for this column and calculate the column sum
            for (int row = 0; row < 5; row++) {
                if (template[row][col] == '-') {
                    colSum += numbers[row][col];
                } else if (template[row][col] == '/' || template[row][col] == '=') {
                    clueRow = row; // This is the clue cell for the column
                }
            }

            // Assign the column sum to the clue cell (if found)
            if (clueRow != -1) {
                // If the cell is '=', append the column sum to the existing row sum
                if (template[clueRow][col] == '=') {
                    clues[clueRow][col] = clues[clueRow][col] * 100 + colSum; // Combine row and column sums
                } else {
                    clues[clueRow][col] = colSum;
                }
            }
        }

        return clues;
    }
}
