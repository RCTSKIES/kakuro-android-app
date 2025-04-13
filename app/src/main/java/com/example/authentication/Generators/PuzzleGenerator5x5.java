package com.example.authentication.Generators;

import android.util.Log;

import com.example.authentication.Interfaces.PuzzleGenerator;
import com.example.authentication.Objects.Grid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class PuzzleGenerator5x5 implements PuzzleGenerator {
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
                    {'*', '*', '*', '/', '/'},
                    {'*', '*', '=', '-', '-'},
                    {'*', '=', '-', '-', '-'},
                    {'/', '-', '-', '-', '*'},
                    {'/', '-', '-', '*', '*'}
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

        // Array of sets to track used numbers for each column
        HashSet<Integer>[] usedNumbersCol = new HashSet[5];
        for (int i = 0; i < 5; i++) {
            usedNumbersCol[i] = new HashSet<>();
        }

        for (int row = 0; row < 5; row++) {
            HashSet<Integer> usedNumbersRow = new HashSet<>(); // Track numbers used in the current row

            for (int col = 0; col < 5; col++) {
                if (template[row][col] == '-') {
                    int num;
                    do {
                        num = rand.nextInt(9) + 1; // Generate numbers 1-9
                    } while (usedNumbersRow.contains(num) || usedNumbersCol[col].contains(num));

                    // Add number to tracking sets
                    usedNumbersRow.add(num);
                    usedNumbersCol[col].add(num);

                    numbers[row][col] = num;
                    Log.e("PuzzleGenerator5x5", "Generated number: " + num);
                }
            }
        }
        return numbers;
    }


    // Generates clues for the clue cells ('/' and '=')
    private int[][] generateClues(char[][] template, int[][] numbers) {
        int[][] clues = new int[5][5];

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (template[row][col] == '/') {
                    clues[row][col] = calculateClue(numbers, template, row, col);
                } else if (template[row][col] == '=') {
                    clues[row][col] = combineClues(numbers, template, row, col);
                }
            }
        }
        return clues;
    }

    // Computes single clues ('/') by summing either row or column numbers
    private int calculateClue(int[][] numbers, char[][] template, int row, int col) {
        int sum = 0;

        // Check if the clue is for a row sum
        if (col + 1 < 5 && template[row][col + 1] == '-') {
            for (int c = col + 1; c < 5 && template[row][c] == '-'; c++) {
                sum += numbers[row][c];
            }
        }
        // Check if the clue is for a column sum
        else if (row + 1 < 5 && template[row + 1][col] == '-') {
            for (int r = row + 1; r < 5 && template[r][col] == '-'; r++) {
                sum += numbers[r][col];
            }
        }

        return sum;
    }

    // Computes double clues ('=') by summing both row and column numbers
    private int combineClues(int[][] numbers, char[][] template, int row, int col) {
        int rowSum = 0, colSum = 0;

        // Get row sum
        for (int c = col + 1; c < 5 && template[row][c] == '-'; c++) {
            rowSum += numbers[row][c];
        }

        // Get column sum
        for (int r = row + 1; r < 5 && template[r][col] == '-'; r++) {
            colSum += numbers[r][col];
        }

        return rowSum * 100 + colSum; // Store as "rowSum/colSum"
    }
}
