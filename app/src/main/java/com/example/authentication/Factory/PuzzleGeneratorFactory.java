package com.example.authentication.Factory;

import com.example.authentication.Generators.PuzzleGenerator4x4;
import com.example.authentication.Generators.PuzzleGenerator5x5;
import com.example.authentication.Generators.PuzzleGenerator9x9;
import com.example.authentication.Interfaces.PuzzleGenerator;

public class PuzzleGeneratorFactory {
    public static PuzzleGenerator getGenerator(int size) {
        switch (size) {
            case 4:
                return new PuzzleGenerator4x4();
            case 5:
                return new PuzzleGenerator5x5();
            case 9:
                return new PuzzleGenerator9x9();
            default:
                throw new IllegalArgumentException("Unsupported puzzle size: " + size);
        }
    }
}
