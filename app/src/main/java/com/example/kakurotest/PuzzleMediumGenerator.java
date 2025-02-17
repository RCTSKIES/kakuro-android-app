package com.example.kakurotest;

import java.util.Random;

public class PuzzleMediumGenerator {
    private char[][] selectedTemplate;
    private int[][] clueGrid;

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
        generateClueValues();
    }

    private void selectRandomTemplate() {
        Random rand = new Random();
        int index = rand.nextInt(templates.length); // Pick a random template
        selectedTemplate = templates[index];
    }

    private void generateClueValues() {
        clueGrid = new int[5][5];

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                if (selectedTemplate[row][col] == '/') {
                    clueGrid[row][col] = generateRandomClue();
                }
            }
        }
    }

    private int generateRandomClue() {
        return (new Random().nextInt(15) + 10); // Generates sum clues between 10-25
    }

    public char[][] getSelectedTemplate() {
        return selectedTemplate;
    }

    public int[][] getClueGrid() {
        return clueGrid;
    }
}
