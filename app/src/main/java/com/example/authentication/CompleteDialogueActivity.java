package com.example.authentication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CompleteDialogueActivity extends Dialog {

    private final String timeTaken;
    private final int xpEarned;

    public CompleteDialogueActivity(@NonNull Context context, String timeTaken, int xpEarned) {
        super(context);
        this.timeTaken = timeTaken;
        this.xpEarned = xpEarned;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_level_complete);

        // Initialize views
        TextView congratsMessage = findViewById(R.id.congratsMessage);
        TextView timeTakenView = findViewById(R.id.timeTaken);
        TextView xpEarnedView = findViewById(R.id.XpEarned);
        Button okButton = findViewById(R.id.okButton);

        // Set the time and XP values
        timeTakenView.setText("Time: " + timeTaken);
        xpEarnedView.setText("XP Earned: " + xpEarned);

        // Handle OK button click
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }


}