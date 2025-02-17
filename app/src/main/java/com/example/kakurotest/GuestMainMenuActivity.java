package com.example.kakurotest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class GuestMainMenuActivity extends AppCompatActivity {

    Button btnEasy, btnMedium, btnHard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_guest_main_menu);

        btnEasy = findViewById(R.id.btnEasy);
        btnMedium = findViewById(R.id.btnMedium);
        btnHard = findViewById(R.id.btnHard);

        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuestMainMenuActivity.this, PuzzleListActivity.class);
                intent.putExtra("DIFFICULTY", "Easy");
                startActivity(intent);
            }
        });

        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuestMainMenuActivity.this, PuzzleListActivity.class);
                intent.putExtra("DIFFICULTY", "Medium");
                startActivity(intent);
            }
        });

        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuestMainMenuActivity.this, PuzzleListActivity.class);
                intent.putExtra("DIFFICULTY", "Hard");
                startActivity(intent);
            }
        });
    }

}