package com.example.hellojava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Slide01Activity extends AppCompatActivity {
    private ImageView imgSlide;
    private Button btnNext;

    // Your slider images
    private int[] images = {R.drawable.slider1__1_, R.drawable.slider2, R.drawable.slider3};
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide01);

        imgSlide = findViewById(R.id.imgSlide01);
        btnNext = findViewById(R.id.btnNext);

        // Show first image
        imgSlide.setImageResource(images[currentIndex]);

        btnNext.setOnClickListener(v -> {
            currentIndex++;
            if (currentIndex < images.length) {
                imgSlide.setImageResource(images[currentIndex]);
            } else {
                // When slides finish → move to Login page
                Intent intent = new Intent(Slide01Activity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // close slider so user can’t go back
            }
        });

    }
}
