package com.example.authentication.Objects;

public class Level {
    private int levelId;
    private String difficulty;
    private Grid grid;
    private GameState gameState;
    private long timeElapsed; // Time elapsed for this level

    public Level(int levelId, String difficulty, Grid grid, GameState gameState, long timeElapsed) {
        this.levelId = levelId;
        this.difficulty = difficulty;
        this.grid = grid;
        this.gameState = gameState;
        this.timeElapsed = timeElapsed;
    }

    // Getters and setters
    public int getLevelId() {
        return levelId;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public Grid getGrid() {
        return grid;
    }

    public GameState getGameState() {
        return gameState;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }
}