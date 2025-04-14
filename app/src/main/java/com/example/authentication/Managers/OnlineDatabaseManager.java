package com.example.authentication.Managers;

import com.example.authentication.Objects.GameState;
import com.example.authentication.Objects.Grid;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class OnlineDatabaseManager {

    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference levelsRef = database.getReference("levels");
    private static final DatabaseReference usersRef = database.getReference("users");

    public OnlineDatabaseManager() {

    }
    // Insert level data into the 'levels' node (this will be the same for all users)
    public static void insertLevel(int levelId, String difficulty, int gridSize, Grid grid) {
        DatabaseReference levelRef = levelsRef.child(String.valueOf(levelId)).child(difficulty);

        // Convert 2D arrays to List<List> before saving
        List<List<Integer>> cluesList = convertIntArrayToList(grid.getClues());
        List<List<String>> templateList = convertCharArrayToList(grid.getTemplate());

        // Save the level properties
        levelRef.child("grid_size").setValue(gridSize);
        levelRef.child("template").setValue(templateList);
        levelRef.child("clues").setValue(cluesList);
    }

    // Load the level data (shared among users)
    public static void loadLevel(int levelId, String difficulty, final FirebaseCallback callback) {
        DatabaseReference levelRef = levelsRef.child(String.valueOf(levelId)).child(difficulty);
        levelRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    // Get data from Firebase
                    List<List<String>> templateList = (List<List<String>>) snapshot.child("template").getValue();
                    List<List<Object>> cluesList = (List<List<Object>>) snapshot.child("clues").getValue();

                    // Convert the lists back to arrays
                    char[][] template = convertListToCharArray(templateList);
                    int[][] clues = convertListToIntArray(cluesList);

                    // Create the grid and pass it to the callback
                    Grid grid = new Grid(template, clues);
                    callback.onCallback(grid);
                } else {
                    callback.onCallback(null); // Level not found
                }
            } else {
                callback.onCallback(null); // Error loading level
            }
        });
    }


    public static void saveUserData(String userId, int levelId, String difficulty, int[][] userInput, GameState.State state, long timeElapsed) {
        DatabaseReference userLevelRef = usersRef.child(userId)
                .child("levels")
                .child(String.valueOf(levelId))
                .child(difficulty);

        userLevelRef.child("user_input").setValue(convertIntArrayToList(userInput));
        userLevelRef.child("state").setValue(state.toString());
        userLevelRef.child("time_elapsed").setValue(timeElapsed);
    }



    // Load user-specific data
    public static void loadUserData(String userId, int levelId, String difficulty, FirebaseUserCallback callback) {
        DatabaseReference userLevelRef = usersRef.child(userId)
                .child("levels")
                .child(String.valueOf(levelId))
                .child(difficulty);

        userLevelRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    List<List<Object>> inputList = (List<List<Object>>) snapshot.child("user_input").getValue();
                    int[][] userInput = convertListToIntArray(inputList);
                    GameState.State state = GameState.State.valueOf(snapshot.child("state").getValue(String.class));
                    long timeElapsed = snapshot.child("time_elapsed").getValue(Long.class);
                    callback.onCallback(userInput, state, timeElapsed);
                } else {
                    callback.onCallback(null, null, -1);
                }
            } else {
                callback.onCallback(null, null, -1);
            }
        });
    }



    // Check if a level exists
    public static void levelExists(int levelId, String difficulty, final FirebaseExistsCallback callback) {
        DatabaseReference levelRef = levelsRef.child(String.valueOf(levelId)).child(difficulty);
        levelRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                callback.onCallback(snapshot.exists());
            } else {
                callback.onCallback(false); // Error checking level existence
            }
        });
    }

    public static void SaveUserXP(String userId, int additionalXP) {
        DatabaseReference xpRef = usersRef.child(userId).child("xp");
        xpRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Long currentXP = task.getResult().getValue(Long.class);
                int updatedXP = (currentXP != null ? currentXP.intValue() : 0) + additionalXP;
                xpRef.setValue(updatedXP);
            }
        });
    }

    // Convert int[][] to List<List<Integer>>
    private static List<List<Integer>> convertIntArrayToList(int[][] array) {
        List<List<Integer>> list = new ArrayList<>();
        for (int[] row : array) {
            List<Integer> rowList = new ArrayList<>();
            for (int element : row) {
                rowList.add(element);
            }
            list.add(rowList);
        }
        return list;
    }

    // Convert char[][] to List<List<String>> (using Strings instead of Characters)
    private static List<List<String>> convertCharArrayToList(char[][] array) {
        List<List<String>> list = new ArrayList<>();
        for (char[] row : array) {
            List<String> rowList = new ArrayList<>();
            for (char element : row) {
                rowList.add(String.valueOf(element));  // Convert char to String
            }
            list.add(rowList);
        }
        return list;
    }

    private static char[][] convertListToCharArray(List<List<String>> list) {
        char[][] array = new char[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            List<String> rowList = list.get(i);
            array[i] = new char[rowList.size()];
            for (int j = 0; j < rowList.size(); j++) {
                array[i][j] = rowList.get(j).charAt(0);  // Convert String back to char
            }
        }
        return array;
    }

    // Convert List<List<Integer>> back to int[][]
    public static int[][] convertListToIntArray(List<List<Object>> list) {
        int[][] array = new int[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            List<Object> rowList = list.get(i);
            array[i] = new int[rowList.size()];
            for (int j = 0; j < rowList.size(); j++) {
                Object value = rowList.get(j);
                if (value instanceof Long) {
                    array[i][j] = ((Long) value).intValue(); // ✅ Proper cast
                } else if (value instanceof String) {
                    array[i][j] = Integer.parseInt((String) value); // ✅ Fallback
                } else {
                    array[i][j] = 0; // Or throw an exception if you want
                }
            }
        }
        return array;
    }

    public static void getDailyChallengeDate(FirebaseDateCallback callback) {
        database.getReference("meta").child("daily_challenge_date").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String date = task.getResult().getValue(String.class);
                callback.onCallback(date);
            } else {
                callback.onCallback(null);
            }
        });
    }

    public static void updateDailyChallengeDate(String newDate) {
        database.getReference("meta").child("daily_challenge_date").setValue(newDate);
    }


    // Callback interfaces
    public interface FirebaseDateCallback {
        void onCallback(String date);
    }


    public interface FirebaseCallback {
        void onCallback(Grid grid);
    }

    public interface FirebaseUserCallback {
        void onCallback(int[][] userInput, GameState.State state, long timeElapsed);
    }

    public interface FirebaseExistsCallback {
        void onCallback(boolean exists);
    }
}
