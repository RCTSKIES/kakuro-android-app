package com.example.authentication.Objects;

public class XPCalculator {
    private static final int BASE_XP_EASY = 10; // Base XP for easy levels
    private static final int BASE_XP_MEDIUM = 20; // Base XP for medium levels
    private static final int BONUS_XP_FAST = 5; // Bonus XP for completing in under 3 minutes
    private static final int BONUS_XP_MEDIUM = 2; // Bonus XP for completing in under 6 minutes

    private long timeElapsed; // Time taken to complete the level (in milliseconds)
    private String difficulty; // Difficulty of the level

    public XPCalculator(long timeElapsed, String difficulty) {
        this.timeElapsed = timeElapsed;
        this.difficulty = difficulty;
    }

    // Calculate the total XP earned
    public int calculateTotalXP() {
        int baseXP = getBaseXP();
        int bonusXP = calculateBonusXP();
        return baseXP + bonusXP;
    }

    // Get the base XP based on difficulty
    private int getBaseXP() {
        switch (difficulty.toLowerCase()) {
            case "easy":
                return BASE_XP_EASY;
            case "medium":
                return BASE_XP_MEDIUM;
            default:
                return 0; // Default case (should not happen)
        }
    }

    // Calculate the bonus XP based on the time of completion
    private int calculateBonusXP() {
        long timeInMinutes = timeElapsed / (60 * 1000); // Convert milliseconds to minutes

        if (timeInMinutes < 3) {
            return BONUS_XP_FAST; // Bonus XP for completing in under 3 minutes
        } else if (timeInMinutes < 6) {
            return BONUS_XP_MEDIUM; // Bonus XP for completing in under 6 minutes
        } else {
            return 0; // No bonus XP after 6 minutes
        }
    }

    // Getters and setters
    public long getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}