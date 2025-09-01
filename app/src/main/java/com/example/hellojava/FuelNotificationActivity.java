package com.example.hellojava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FuelNotificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuel_notification); // use your menu.xml file

        ImageView btnHome = findViewById(R.id.imgHome);
        ImageView btnFuelEfficiency = findViewById(R.id.imgFuel_effi);
        ImageView btnMenu = findViewById(R.id.imageView33);
//
        btnHome.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(FuelNotificationActivity.this, HomeActivity.class);
            startActivity(intent);
        });
        btnFuelEfficiency.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(FuelNotificationActivity.this, FuelEfficiencyActivity.class);
            startActivity(intent);
        });
        btnMenu.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(FuelNotificationActivity.this, MenuActivity.class);
            startActivity(intent);
        });
    }
}


