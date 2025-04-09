package com.example.authentication.Generators;

import android.util.Log;
import com.example.authentication.Objects.Grid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class PuzzleGeneratorOnline {
    private final char[][] templates = {
                {'*', '*', '/', '/', '*', '/', '/', '*', '*', '*', '*', '*', '*', '*', '*'},

                {'*', '=', '-', '-', '=', '-', '-', '*', '/', '/', '*', '*', '*', '*', '*'},

                {'/', '-', '-', '-', '-', '-', '-', '/', '-', '-', '/', '*', '*', '*', '*'},

                {'/', '-', '-', '=', '-', '-', '/', '=', '-', '-', '-', '/', '*', '*', '*'},

                {'*', '/', '-', '-', '=', '-', '-', '-', '/', '/', '-', '-', '/', '*', '*'},

                {'*', '*', '=', '-', '-', '=', '-', '-', '-', '/', '/', '-', '-', '/', '/'},

                {'*', '/', '-', '-', '-', '-', '-', '=', '-', '-', '/', '/', '-', '-', '-'},

                {'*', '=', '-', '-', '/', '-', '-', '-', '-', '-', '-', '*', '=', '-', '-'},

                {'/', '-', '-', '/', '/', '-', '-', '-', '-', '-', '-', '=', '-', '-', '*'},

                {'/', '-', '-', '-', '/', '/', '-', '-', '=', '-', '-', '-', '-', '-', '*'},

                {'*', '*', '/', '-', '-', '/', '/', '-', '-', '-', '=', '-', '-', '/', '*'},

                {'*', '*', '*', '/', '-', '-', '/', '=', '-', '-', '-', '=', '-', '-', '/'},

                {'*', '*', '*', '*', '/', '-', '-', '-', '*', '=', '-', '-', '=', '-', '-'},

                {'*', '*', '*', '*', '*', '/', '-', '-', '/', '-', '-', '-', '-', '-', '-'},

                {'*', '*', '*', '*', '*', '*', '*', '*', '/', '-', '-', '/', '-', '-', '*'}
    };

    public PuzzleGeneratorOnline() {
        // Constructor remains empty
    }

    // Generates a new Grid with a random template, numbers, and clues
    public Grid generateGrid() {
        int[][] numbers = generateNumbers();
        int[][] clues = generateClues(numbers);
        int[][] userInput = new int[15][15];

        // Log the generated grid and clues
        Log.d("PuzzleGeneratorOnline", "Template: " + Arrays.deepToString(templates));
        Log.d("PuzzleGeneratorOnline", "Numbers: " + Arrays.deepToString(numbers));
        Log.d("PuzzleGeneratorOnline", "Clues: " + Arrays.deepToString(clues));

        return new Grid(templates, clues, userInput);
    }

    // Generates unique numbers (1-9) for the editable cells ('-')
    private int[][] generateNumbers() {
        int[][] numbers = new int[15][15];
        Random rand = new Random();

        // Array of sets to track used numbers for each column
        HashSet<Integer>[] usedNumbersCol = new HashSet[15];
        for (int i = 0; i < 15; i++) {
            usedNumbersCol[i] = new HashSet<>();
        }

        for (int row = 0; row < 15; row++) {
            HashSet<Integer> usedNumbersRow = new HashSet<>(); // Track numbers used in the current row

            for (int col = 0; col < 15; col++) {
                if (templates[row][col] == '-') {
                    int num;
                    do {
                        num = rand.nextInt(9) + 1; // Generate numbers 1-9
                    } while (usedNumbersRow.contains(num) || usedNumbersCol[col].contains(num));

                    // Add number to tracking sets
                    usedNumbersRow.add(num);
                    usedNumbersCol[col].add(num);

                    numbers[row][col] = num;
                    Log.e("PuzzleGeneratorOnline", "Generated number: " + num);
                }
            }
        }
        return numbers;
    }


    // Generates clues for the clue cells ('/' and '=')
    private int[][] generateClues(int[][] numbers) {
        int[][] clues = new int[15][15];

        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                if (templates[row][col] == '/') {
                    clues[row][col] = calculateClue(numbers, templates, row, col);
                } else if (templates[row][col] == '=') {
                    clues[row][col] = combineClues(numbers, templates, row, col);
                }
            }
        }
        return clues;
    }

    // Computes single clues ('/') by summing either row or column numbers
    private int calculateClue(int[][] numbers, char[][] template, int row, int col) {
        int sum = 0;

        // Check if the clue is for a row sum
        if (col + 1 < 15 && template[row][col + 1] == '-') {
            for (int c = col + 1; c < 15 && template[row][c] == '-'; c++) {
                sum += numbers[row][c];
            }
        }
        // Check if the clue is for a column sum
        else if (row + 1 < 15 && template[row + 1][col] == '-') {
            for (int r = row + 1; r < 15 && template[r][col] == '-'; r++) {
                sum += numbers[r][col];
            }
        }

        return sum;
    }

    // Computes double clues ('=') by summing both row and column numbers
    private int combineClues(int[][] numbers, char[][] template, int row, int col) {
        int rowSum = 0, colSum = 0;

        // Get row sum
        for (int c = col + 1; c < 15 && template[row][c] == '-'; c++) {
            rowSum += numbers[row][c];
        }

        // Get column sum
        for (int r = row + 1; r < 15 && template[r][col] == '-'; r++) {
            colSum += numbers[r][col];
        }

        return rowSum * 100 + colSum; // Store as "rowSum/colSum"
    }
}
