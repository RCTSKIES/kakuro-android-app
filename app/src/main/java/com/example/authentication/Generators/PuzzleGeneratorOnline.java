package com.example.authentication.Generators;

import android.util.Log;

import com.example.authentication.Interfaces.PuzzleGenerator;
import com.example.authentication.Objects.Grid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class PuzzleGeneratorOnline implements PuzzleGenerator {
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

        for (int row = 0; row < 15; row++) {
            for (int col = 0; col < 15; col++) {
                if (templates[row][col] == '-') {
                    List<int[]> run = getRunCells(row, col); // get cells in same clue run
                    Set<Integer> usedInRun = new HashSet<>();

                    // Gather used numbers in the current clue run
                    for (int[] cell : run) {
                        int r = cell[0], c = cell[1];
                        if (numbers[r][c] != 0) {
                            usedInRun.add(numbers[r][c]);
                        }
                    }

                    // Try generating a number not used in the same clue run
                    int num;
                    int attempts = 0;
                    do {
                        num = rand.nextInt(9) + 1; // 1–9
                        attempts++;
                        if (attempts > 20) {
                            num = 0; // fallback if too many retries (shouldn’t happen often)
                            break;
                        }
                    } while (usedInRun.contains(num));

                    numbers[row][col] = num;
                    Log.d("Generator", "Placed " + num + " at [" + row + "][" + col + "]");
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
                    clues[row][col] = calculateClue(numbers, row, col);
                    Log.e("PuzzleGeneratorOnline", "Generated clue: / " + Arrays.deepToString(clues));
                } else if (templates[row][col] == '=') {
                    clues[row][col] = combineClues(numbers, row, col);
                    Log.e("PuzzleGeneratorOnline", "Generated clue: = " + Arrays.deepToString(clues));
                }
            }
        }
        return clues;
    }

    // Computes single clues ('/') by summing either row or column numbers
    private int calculateClue(int[][] numbers, int row, int col) {
        int sum = 0;

        // Check if the clue is for a row sum
        if (col + 1 < 15 && templates[row][col + 1] == '-') {
            for (int c = col + 1; c < 15 && templates[row][c] == '-'; c++) {
                sum += numbers[row][c];
            }
        }
        // Check if the clue is for a column sum
        else if (row + 1 < 15 && templates[row + 1][col] == '-') {
            for (int r = row + 1; r < 15 && templates[r][col] == '-'; r++) {
                sum += numbers[r][col];
            }
        }

        return sum;
    }

    // Computes double clues ('=') by summing both row and column numbers
    private int combineClues(int[][] numbers, int row, int col) {
        int rowSum = 0, colSum = 0;

        // Get row sum
        for (int c = col + 1; c < 15 && templates[row][c] == '-'; c++) {
            rowSum += numbers[row][c];
        }

        // Get column sum
        for (int r = row + 1; r < 15 && templates[r][col] == '-'; r++) {
            colSum += numbers[r][col];
        }

        return rowSum * 100 + colSum; // Store as "rowSum/colSum"
    }

    private List<int[]> getRunCells(int row, int col) {
        List<int[]> run = new ArrayList<>();

        // Check for horizontal run
        for (int c = col - 1; c >= 0; c--) {
            if (templates[row][c] == '/' || templates[row][c] == '=') {
                for (int cc = c + 1; cc < 15 && templates[row][cc] == '-'; cc++) {
                    run.add(new int[]{row, cc});
                }
                break;
            } else if (templates[row][c] != '-') break;
        }

        // Check for vertical run
        for (int r = row - 1; r >= 0; r--) {
            if (templates[r][col] == '/' || templates[r][col] == '=') {
                for (int rr = r + 1; rr < 15 && templates[rr][col] == '-'; rr++) {
                    run.add(new int[]{rr, col});
                }
                break;
            } else if (templates[r][col] != '-') break;
        }

        return run;
    }

}
