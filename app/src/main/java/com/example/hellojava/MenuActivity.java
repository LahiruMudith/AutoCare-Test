package com.example.hellojava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu); // use your menu.xml file

        ImageView btnHome = findViewById(R.id.btnHome);
        ImageView btnFuelEfficiency = findViewById(R.id.imgFuel_effi);
        ImageView btnNotification = findViewById(R.id.imgNotification);
        ImageView btnSetting = findViewById(R.id.imgSetting);
        Button btnEngine = findViewById(R.id.btnEngine);
        Button btnBattery = findViewById(R.id.btnBattery);
        Button btnFuel_effi = findViewById(R.id.btnFuel_effi);
        Button btnNotification2 = findViewById(R.id.btnNotification);
        Button btnHomePage = findViewById(R.id.btnHomePage);
        Button btnSetting2 = findViewById(R.id.btnSetting);
        Button btnLogOut = findViewById(R.id.btnLogOut);
        Button btnSpeed = findViewById(R.id.btnSpeed);

        btnHome.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(MenuActivity.this, HomeActivity.class);
            startActivity(intent);
        });
        btnHomePage.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(MenuActivity.this, HomeActivity.class);
            startActivity(intent);
        });
        btnLogOut.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        btnFuelEfficiency.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(MenuActivity.this, FuelEfficiencyActivity.class);
            startActivity(intent);
        });
        btnNotification.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(MenuActivity.this, FuelNotificationActivity.class);
            startActivity(intent);
        });
        btnSetting.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(MenuActivity.this, FuelSettingActivity.class);
            startActivity(intent);
        });
        btnSetting2.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(MenuActivity.this, FuelSettingActivity.class);
            startActivity(intent);
        });
        btnFuel_effi.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(MenuActivity.this, FuelEfficiencyActivity.class);
            startActivity(intent);
        });
        btnNotification2.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(MenuActivity.this, FuelNotificationActivity.class);
            startActivity(intent);
        });
        btnSpeed.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(MenuActivity.this, SpeedActivity.class);
            startActivity(intent);
        });
        btnEngine.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(MenuActivity.this, EngineActivity.class);
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                intent.putExtra("rpm", extras.getString("rpm"));
                intent.putExtra("coolant", extras.getString("coolant"));
                intent.putExtra("vehicleName", extras.getString("vehicleName"));
                intent.putExtra("modelYear", extras.getString("modelYear"));
                intent.putExtra("engineLoad", extras.getString("engineLoad"));
                intent.putExtra("batteryVoltage", extras.getString("batteryVoltage"));
            }
            startActivity(intent);
        });
        btnBattery.setOnClickListener(v -> {
            // Go to MenuActivity
            Intent intent = new Intent(MenuActivity.this, NewHomeActivity.class);
            startActivity(intent);
        });
    }
}


