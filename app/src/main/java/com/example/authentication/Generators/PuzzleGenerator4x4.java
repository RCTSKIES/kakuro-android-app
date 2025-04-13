package com.example.authentication.Generators;

import com.example.authentication.Interfaces.PuzzleGenerator;
import com.example.authentication.Objects.Grid;

import java.util.HashSet;
import java.util.Random;

public class PuzzleGenerator4x4 implements PuzzleGenerator {
    // The only template for 4x4 puzzles
    private final char[][] template = {
            {'*', '/', '/', '/'},
            {'/', '-', '-', '-'},
            {'/', '-', '-', '-'},
            {'/', '-', '-', '-'}
    };

    public PuzzleGenerator4x4() {
        // Constructor remains empty
    }

    // Generates a new Grid with the fixed template, numbers, and clues
    public Grid generateGrid() {
        int[][] numbers = generateNumbers();
        int[][] clues = generateClues(numbers);
        int[][] userInput = new int[4][4]; // Empty user input

        return new Grid(template, clues, userInput);
    }

    // Generates unique numbers (1-9) for the editable cells ('-')
    private int[][] generateNumbers() {
        int[][] numbers = new int[4][4];
        Random rand = new Random();

        for (int row = 1; row < 4; row++) { // Rows 1-3 are editable
            HashSet<Integer> usedNumbersRow = new HashSet<>();
            for (int col = 1; col < 4; col++) { // Columns 1-3 are editable
                int num;
                do {
                    num = rand.nextInt(9) + 1; // Generate numbers 1-9
                } while (usedNumbersRow.contains(num));
                usedNumbersRow.add(num);
                numbers[row][col] = num;
            }
        }

        return numbers;
    }

    // Generates clues for the clue cells ('/')
    private int[][] generateClues(int[][] numbers) {
        int[][] clues = new int[4][4];

        // Calculate row sums
        for (int row = 1; row < 4; row++) {
            int rowSum = 0;
            for (int col = 1; col < 4; col++) {
                rowSum += numbers[row][col];
            }
            // Assign row sum to the clue cell in the first row
            clues[0][row] = rowSum;
        }

        // Calculate column sums
        for (int col = 1; col < 4; col++) {
            int colSum = 0;
            for (int row = 1; row < 4; row++) {
                colSum += numbers[row][col];
            }
            // Assign column sum to the clue cell in the first column
            clues[col][0] = colSum;
        }

        return clues;
    }
}
