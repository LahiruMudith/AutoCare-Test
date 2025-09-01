package com.example.hellojava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FuelSettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fuel_setting); // use your menu.xml file

        ImageView btnHome = findViewById(R.id.imageView55);
//        ImageView btnFuelEfficiency = findViewById(R.id.imgFuel_effi);
//        ImageView btnNotification = findViewById(R.id.imgNotification);
//
//        btnHome.setOnClickListener(v -> {
//            // Go to MenuActivity
//            Intent intent = new Intent(MenuActivity.this, HomeActivity.class);
//            startActivity(intent);
//        });
//        btnFuelEfficiency.setOnClickListener(v -> {
//            // Go to MenuActivity
//            Intent intent = new Intent(MenuActivity.this, FuelEfficiencyActivity.class);
//            startActivity(intent);
//        });
        btnHome.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(FuelSettingActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
}


