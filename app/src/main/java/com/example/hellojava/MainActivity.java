package com.example.hellojava;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 1000; // 1 second = 1000 ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Redirect after 1 second
        new Handler().postDelayed(() -> {
//            Intent intent = new Intent(MainActivity.this, NewHomeActivity.class);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // finish splash so user can’t go back
        }, SPLASH_DELAY);
    }
}

