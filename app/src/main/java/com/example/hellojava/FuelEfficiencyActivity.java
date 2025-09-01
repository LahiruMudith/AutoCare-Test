package com.example.hellojava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FuelEfficiencyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuel_efficiency); // use your menu.xml file

        ImageView btnHome = findViewById(R.id.imgHome);
        ImageView btnFuelNotification = findViewById(R.id.imgNotification);
        ImageView btnMenu = findViewById(R.id.imgMenu);
//
        btnHome.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(FuelEfficiencyActivity.this, HomeActivity.class);
            startActivity(intent);
        });
        btnFuelNotification.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(FuelEfficiencyActivity.this, FuelNotificationActivity.class);
            startActivity(intent);
        });
        btnMenu.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(FuelEfficiencyActivity.this, MenuActivity.class);
            startActivity(intent);
        });
    }
}


