package com.example.authentication.Objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "kakuroGame.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    private static final String TABLE_LEVELS = "levels";
    private static final String COLUMN_LEVEL_ID = "level_id";
    private static final String COLUMN_DIFFICULTY = "difficulty";
    private static final String COLUMN_GRID_SIZE = "grid_size";
    private static final String COLUMN_TEMPLATE = "template";
    private static final String COLUMN_CLUES = "clues";
    private static final String COLUMN_USER_INPUT = "user_input";
    private static final String COLUMN_STATE = "state";
    private static final String COLUMN_XP = "xp" ;
    private static final String COLUMN_TIME_ELAPSED = "time";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the levels table with a composite primary key
        String createLevelsTable = "CREATE TABLE " + TABLE_LEVELS + " ("
                + COLUMN_LEVEL_ID + " INTEGER, "
                + COLUMN_DIFFICULTY + " TEXT, "
                + COLUMN_GRID_SIZE + " INTEGER, "
                + COLUMN_TEMPLATE + " TEXT, "
                + COLUMN_CLUES + " TEXT, "
                + COLUMN_USER_INPUT + " TEXT, "
                + COLUMN_STATE + " TEXT, "
                + COLUMN_XP + " INTEGER, "
                + COLUMN_TIME_ELAPSED + " TEXT, "
                + "PRIMARY KEY (" + COLUMN_LEVEL_ID + ", " + COLUMN_DIFFICULTY + "))";
        db.execSQL(createLevelsTable);

        Log.d("DatabaseManager", "Database created with table: " + TABLE_LEVELS);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEVELS);
        onCreate(db);
    }

    // Load a level from the database
    public void insertLevel(int levelId, String difficulty, int gridSize, Grid grid, GameState.State state, long timeElapsed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LEVEL_ID, levelId);
        values.put(COLUMN_DIFFICULTY, difficulty);
        values.put(COLUMN_GRID_SIZE, gridSize);
        values.put(COLUMN_TEMPLATE, serializeTemplate(grid.getTemplate()));
        values.put(COLUMN_CLUES, serializeClues(grid.getClues()));
        values.put(COLUMN_USER_INPUT, serializeClues(new int[gridSize][gridSize])); // Empty user input
        values.put(COLUMN_STATE, state.toString());
        values.put(COLUMN_TIME_ELAPSED, timeElapsed); // Add time elapsed to the database

        db.insertWithOnConflict(TABLE_LEVELS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public Level loadLevel(int levelId, String difficulty) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_LEVELS,
                null, // Columns (null means all columns)
                COLUMN_LEVEL_ID + "=? AND " + COLUMN_DIFFICULTY + "=?",
                new String[]{String.valueOf(levelId), difficulty},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            // Get column indices
            int gridSizeIndex = cursor.getColumnIndex(COLUMN_GRID_SIZE);
            int templateIndex = cursor.getColumnIndex(COLUMN_TEMPLATE);
            int cluesIndex = cursor.getColumnIndex(COLUMN_CLUES);
            int userInputIndex = cursor.getColumnIndex(COLUMN_USER_INPUT);
            int stateIndex = cursor.getColumnIndex(COLUMN_STATE);
            int timeElapsedIndex = cursor.getColumnIndex(COLUMN_TIME_ELAPSED);

            // Check if any column index is -1
            if (gridSizeIndex == -1 || templateIndex == -1 || cluesIndex == -1 || userInputIndex == -1 || stateIndex == -1 || timeElapsedIndex == -1) {
                Log.e("DatabaseManager", "Column not found: "
                        + (gridSizeIndex == -1 ? COLUMN_GRID_SIZE : "")
                        + (templateIndex == -1 ? COLUMN_TEMPLATE : "")
                        + (cluesIndex == -1 ? COLUMN_CLUES : "")
                        + (userInputIndex == -1 ? COLUMN_USER_INPUT : "")
                        + (stateIndex == -1 ? COLUMN_STATE : "")
                        + (timeElapsedIndex == -1 ? COLUMN_TIME_ELAPSED : ""));
                cursor.close();
                db.close();
                return null;
            }

            // Deserialize template, clues, and user input
            int gridSize = cursor.getInt(gridSizeIndex);
            char[][] template = deserializeTemplate(cursor.getString(templateIndex));
            int[][] clues = deserializeClues(cursor.getString(cluesIndex));
            int[][] userInput = deserializeClues(cursor.getString(userInputIndex));
            GameState.State state = GameState.State.valueOf(cursor.getString(stateIndex));
            long timeElapsed = cursor.getLong(timeElapsedIndex); // Load time elapsed

            cursor.close();
            db.close();

            // Create and return the Level object
            Grid grid = new Grid(template, clues, userInput);
            GameState gameState = new GameState(grid, state); // Ensure gameState is initialized
            return new Level(levelId, difficulty, grid, gameState, timeElapsed);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return null;
    }

    public void saveLevel(int levelId, Grid grid, GameState.State state, long timeElapsed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_INPUT, serializeClues(grid.getUserInput()));
        values.put(COLUMN_STATE, state.toString());
        values.put(COLUMN_TIME_ELAPSED, timeElapsed); // Add time elapsed to the database

        db.update(TABLE_LEVELS, values, COLUMN_LEVEL_ID + "=?", new String[]{String.valueOf(levelId)});
        db.close();
    }

    public void clearDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
        Log.d("DatabaseManager", "Database cleared");
    }

    public boolean levelsExist() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LEVELS, null, null, null, null, null, null);
        boolean levelsExist = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return levelsExist;
    }

    // Serialization and deserialization methods
    private String serializeTemplate(char[][] template) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : template) {
            for (char cell : row) {
                sb.append(cell).append(",");
            }
            sb.append(";"); // Use semicolon to separate rows
        }
        return sb.toString();
    }

    private char[][] deserializeTemplate(String serialized) {
        // Split the string into rows using the semicolon delimiter
        String[] rows = serialized.split(";");

        // Determine the number of rows and columns
        int numRows = rows.length;
        int numCols = rows[0].split(",").length;

        // Initialize the 2D array to store the template
        char[][] template = new char[numRows][numCols];

        // Parse each row and column
        for (int i = 0; i < numRows; i++) {
            String[] cols = rows[i].split(",");
            for (int j = 0; j < numCols; j++) {
                template[i][j] = cols[j].trim().charAt(0); // Trim and get the first character
            }
        }

        return template;
    }

    private String serializeClues(int[][] clues) {
        StringBuilder sb = new StringBuilder();

        for (int[] row : clues) {
            for (int value : row) {
                sb.append(value).append(",");
            }
            sb.append(";"); // Add semicolon to separate rows
        }

        return sb.toString();
    }

    private int[][] deserializeClues(String serialized) {
        // Split the string into rows using the semicolon delimiter
        String[] rows = serialized.split(";");

        // Determine the number of rows and columns
        int numRows = rows.length;
        int numCols = rows[0].split(",").length;

        // Initialize the 2D array to store clues
        int[][] clues = new int[numRows][numCols];

        // Parse each row and column
        for (int i = 0; i < numRows; i++) {
            String[] cols = rows[i].split(",");
            for (int j = 0; j < numCols; j++) {
                try {
                    // Parse the clue value (ignore empty strings)
                    if (!cols[j].isEmpty()) {
                        clues[i][j] = Integer.parseInt(cols[j].trim());
                    } else {
                        clues[i][j] = 0; // Default value for empty cells
                    }
                } catch (NumberFormatException e) {
                    // Handle invalid numbers (e.g., ";")
                    clues[i][j] = 0; // Default value for invalid cells
                    Log.e("DatabaseManager", "Failed to parse clue value: " + cols[j], e);
                }
            }
        }

        return clues;
    }
}