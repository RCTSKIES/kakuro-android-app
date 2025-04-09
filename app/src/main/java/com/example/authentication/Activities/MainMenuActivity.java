package com.example.authentication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.authentication.R;
import com.example.authentication.Services.LevelService;

public class MainMenuActivity extends NavbarTopActivity {

    Button btnEasy, btnMedium, btnHard, btnOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main_menu, findViewById(R.id.content_frame));
        EdgeToEdge.enable(this);

        btnEasy = findViewById(R.id.btnEasy);
        btnMedium = findViewById(R.id.btnMedium);
        btnHard = findViewById(R.id.btnHard);
//        btnOnline = findViewById(R.id.btnOnline);


        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, PuzzleListActivity.class);
                intent.putExtra("DIFFICULTY", "Easy");
                startActivity(intent);
            }
        });

        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, PuzzleListActivity.class);
                intent.putExtra("DIFFICULTY", "Medium");
                startActivity(intent);
            }
        });

        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, PuzzleListActivity.class);
                intent.putExtra("DIFFICULTY", "Hard");
                startActivity(intent);
            }
        });

//        btnOnline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainMenuActivity.this, LevelService.class);
//                intent.putExtra("difficulty", "Online");
//                intent.putExtra("levelId", 1);
//                startActivity(intent);
//            }
//        });
    }

}