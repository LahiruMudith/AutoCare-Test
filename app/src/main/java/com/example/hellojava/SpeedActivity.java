package com.example.hellojava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.example.hellojava.command.OBDConnectionManager;
import com.github.pires.obd.commands.SpeedCommand;

public class SpeedActivity extends AppCompatActivity {

    private int maxSpeed = 0; // to store top speed

    private TextView txtCurrentSpeed;
    private TextView txtTopSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speed); // your XML above

        txtCurrentSpeed = findViewById(R.id.currentSpeed);
        txtTopSpeed = findViewById(R.id.topSpeed);

        ImageView imgHome = findViewById(R.id.imgHome);
        ImageView imgFuel_effi = findViewById(R.id.imgFuel_effi);
        ImageView imgNotification = findViewById(R.id.imgNotification);
        ImageView imgMenu = findViewById(R.id.imageView33);

        imgHome.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
        imgFuel_effi.setOnClickListener(v -> startActivity(new Intent(this, FuelEfficiencyActivity.class)));
        imgNotification.setOnClickListener(v -> startActivity(new Intent(this, FuelNotificationActivity.class)));
        imgMenu.setOnClickListener(v -> startActivity(new Intent(this, MenuActivity.class)));

        // ✅ Start real-time speed reading
        startSpeedUpdates();
    }

    private void startSpeedUpdates() {
        new Thread(() -> {
            try {
                while (OBDConnectionManager.getSocket() != null && OBDConnectionManager.getSocket().isConnected()) {
                    SpeedCommand speedCommand = new SpeedCommand();
                    speedCommand.run(
                            OBDConnectionManager.getSocket().getInputStream(),
                            OBDConnectionManager.getSocket().getOutputStream()
                    );

                    final int currentSpeed = speedCommand.getMetricSpeed(); // km/h

                    if (currentSpeed > maxSpeed) maxSpeed = currentSpeed;

                    runOnUiThread(() -> {
                        txtCurrentSpeed.setText(currentSpeed + " km/h");
                        txtTopSpeed.setText("Top Speed: " + maxSpeed + " km/h");
                    });

                    Thread.sleep(1000); // update every second
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    txtCurrentSpeed.setText("-- km/h");
                    txtTopSpeed.setText("Top Speed: " + maxSpeed + " km/h");
                });
            }
        }).start();
    }
}
