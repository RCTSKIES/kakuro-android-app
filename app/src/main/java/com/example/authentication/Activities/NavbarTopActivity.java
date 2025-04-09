package com.example.authentication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.authentication.Objects.DatabaseManager;
import com.example.authentication.R;

public class NavbarTopActivity extends AppCompatActivity {

    private TextView xpTextView;
    private DatabaseManager dbManager;
    Button loginButton, homeButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar); // Common layout for the nav bar

        // Set up Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        Toolbar buttombar = findViewById(R.id.bottombar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setSupportActionBar(buttombar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize XP bar
        xpTextView = findViewById(R.id.xpTextView);
        dbManager = new DatabaseManager(this);
        xpTextView.setText("Total XP: " + dbManager.getTotalXP());


        loginButton = findViewById(R.id.loginButton);
        homeButton = findViewById(R.id.menuButton);

        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        homeButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        });

    }

}
