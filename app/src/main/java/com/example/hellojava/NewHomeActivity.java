package com.example.hellojava;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.example.hellojava.command.EngineLoadCommand;
import com.example.hellojava.command.FuelSystemStatusCommand;
import com.example.hellojava.command.OBDConnectionManager;
import com.example.hellojava.command.*;
import com.github.pires.obd.commands.ObdCommand;
//import com.github.pires.obd.commands.SpeedCommand;
//import com.github.pires.obd.commands.fuel.FuelLevelCommand;
//import com.github.pires.obd.commands.temperature.EngineCoolantTemperatureCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class NewHomeActivity extends AppCompatActivity {

    private int maxSpeed = 0;
    private double totalDistance = 0; // in km
    private double fuelConsumed = 0;  // in liters
    private final double fuelPricePerLiter = 305; // in LKR
    private double tankSizeLiters = 50; // example: 50L tank
    private long travelStartTime = 0; // start timestamp
    private long travelEndTime = 0;   // end timestamp

    private double initialFuelLevelPercent = -1; // trip start fuel level
    private boolean isTraveling = false;


    // Profile & Car Info
    private ImageView imgProfile;
    private TextView userName, userLocation, titleSpeed, txtCarModel, txtCurrentSpeed;

    // OBD Parameters
    private TextView txtFuelStatus2, txtBatteryVoltage2, txtEngineLoad2, txtEngineCoolant2, txtFuel2, txtTopSpeed;

    // OBD Status
    private CardView obdStatusCard;
    private TextView txtOBDStatus;

    // Buttons
    private Button btnConnect2, btnReport;

    // Bluetooth & OBD-II
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket obdSocket;
    private BluetoothDevice obdDevice;
    private boolean isOBDConnected = false;

    // Replace with your OBD-II MAC address
    private static final String OBD_MAC_ADDRESS = "1C:A1:35:69:8D:C5";
    private static final UUID OBD_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN},
                    1001);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_page);

        initViews();
        updateOBDStatus();
        resetOBDData();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        btnConnect2.setOnClickListener(v -> {
            if (bluetoothAdapter == null) {
                Toast.makeText(this, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!bluetoothAdapter.isEnabled()) {
                // Show alert to enable Bluetooth
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Bluetooth Required")
                        .setMessage("Bluetooth is disabled. Please enable it to connect to the OBD-II device.")
                        .setCancelable(false)
                        .setPositiveButton("Enable", (dialog, which) -> {
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            bluetoothAdapter.enable(); // Turn on Bluetooth
                            Toast.makeText(this, "Bluetooth enabling...", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .show();
                return;
            }

            // Connect or disconnect OBD-II
            if (!isOBDConnected) {
                travelStartTime = System.currentTimeMillis();
                isTraveling = true;
                totalDistance = 0;
                maxSpeed = 0;
                fuelConsumed = 0;
                connectOBD();
            } else {
                disconnectOBD();
            }
        });

        btnReport.setOnClickListener(v -> {
            long elapsedTimeMs = System.currentTimeMillis() - travelStartTime;
            long elapsedSeconds = elapsedTimeMs / 1000;
            long hours = elapsedSeconds / 3600;
            long minutes = (elapsedSeconds % 3600) / 60;
            long seconds = elapsedSeconds % 60;

            double fuelCost = fuelConsumed * fuelPricePerLiter;

            String report =
                    "Top Speed: " + maxSpeed + " km/h\n" +
                    "Distance: " + String.format("%.2f", totalDistance) + " km\n" +
                    "Travel Time: " + hours + "h " + minutes + "m " + seconds + "s\n" +
                    "Fuel Consumed: " + String.format("%.2f", fuelConsumed) + " L\n" +
                    "Fuel Cost: " + String.format("%.2f", fuelCost) + " LKR";

            // Show alert
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Trip Report")
                    .setMessage(report)
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }

    private void initViews() {
        imgProfile = findViewById(R.id.imgProfile);
        userName = findViewById(R.id.userName);
        userLocation = findViewById(R.id.userLocation);
        titleSpeed = findViewById(R.id.titleSpeed);
        txtCarModel = findViewById(R.id.carModel);
        txtCurrentSpeed = findViewById(R.id.currentSpeed);

        txtFuelStatus2 = findViewById(R.id.txtFuelStatus2);
        txtBatteryVoltage2 = findViewById(R.id.txtBatteryVoltage2);
        txtEngineLoad2 = findViewById(R.id.txtEngineLoad2);
        txtEngineCoolant2 = findViewById(R.id.txtEngineCoolant2);
        txtFuel2 = findViewById(R.id.txtFuel2);
        txtTopSpeed = findViewById(R.id.txtTopSpeed);

        obdStatusCard = findViewById(R.id.obdStatusCard);
        txtOBDStatus = findViewById(R.id.txtOBDStatus);

        btnConnect2 = findViewById(R.id.btnConnect2);
        btnReport = findViewById(R.id.btnReport);

        imgProfile = findViewById(R.id.imgProfile);
        imgProfile = findViewById(R.id.imgProfile);
    }

    private void connectOBD() {
        obdDevice = bluetoothAdapter.getRemoteDevice(OBD_MAC_ADDRESS);

        new Thread(() -> {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                obdSocket = obdDevice.createRfcommSocketToServiceRecord(OBD_UUID);
                obdSocket.connect();

                isOBDConnected = true;
                runOnUiThread(() -> {
                    updateOBDStatus();
                    Toast.makeText(this, "OBD Connected!", Toast.LENGTH_SHORT).show();
                });

                startReadingOBDData();

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Failed to connect OBD", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void disconnectOBD() {
        try {
            if (obdSocket != null) obdSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        travelEndTime = System.currentTimeMillis();
        isTraveling = false;

        long durationMs = travelEndTime - travelStartTime;
        long seconds = durationMs / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        double fuelCost = fuelConsumed * fuelPricePerLiter;

        String report =
                "Top Speed: " + maxSpeed + " km/h\n" +
                "Total Distance: " + String.format("%.2f", totalDistance) + " km\n" +
                "Total Travel Time: " + hours + "h " + minutes + "m " + remainingSeconds + "s\n" +
                "Fuel Consumed: " + String.format("%.2f", fuelConsumed) + " L\n" +
                "Fuel Cost: " + String.format("%.2f", fuelCost) + " LKR";

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Trip Report")
                .setMessage(report)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();

        isOBDConnected = false;
        updateOBDStatus();
        resetOBDData();
        Toast.makeText(this, "OBD Disconnected", Toast.LENGTH_SHORT).show();
    }

    private void updateOBDStatus() {
        if (isOBDConnected) {
            txtOBDStatus.setText("OBD Device: Connected");
            obdStatusCard.setCardBackgroundColor(Color.parseColor("#4CAF50")); // Green
        } else {
            txtOBDStatus.setText("OBD Device: Disconnected");
            obdStatusCard.setCardBackgroundColor(Color.parseColor("#FFCDD2")); // Red
        }
    }

    private void startReadingOBDData() {
        new Thread(() -> {
            try {
                InputStream in = obdSocket.getInputStream();
                OutputStream out = obdSocket.getOutputStream();

                VehicleInfoCommand vehicleInfo = new VehicleInfoCommand();
                vehicleInfo.run(in, out);
                runOnUiThread(() -> txtCarModel.setText(vehicleInfo.getVin()));

                while (isOBDConnected && obdSocket.isConnected()) {
                    // Create OBD commands
                    SpeedCommand speedCommand = new SpeedCommand();
                    FuelSystemStatusCommand fuelSystem = new FuelSystemStatusCommand();
                    EngineLoadCommand engineLoad = new EngineLoadCommand();
                    EngineCoolantTemperatureCommand coolantTemp = new EngineCoolantTemperatureCommand();
                    FuelLevelCommand fuelLevel = new FuelLevelCommand();
                    BatteryVoltageCommand batteryVoltageCommand = new BatteryVoltageCommand();
                    FuelLevelCommand fuelLevelCommand = new FuelLevelCommand();

                    fuelLevelCommand.run(in, out);
                    // Run commands
                    speedCommand.run(in, out);
                    fuelSystem.run(in, out);
                    engineLoad.run(in, out);
                    coolantTemp.run(in, out);
                    fuelLevel.run(in, out);
                    batteryVoltageCommand.run(in, out);
                    fuelLevelCommand.run(in, out);


                    // Get results
                    final int currentSpeed = speedCommand.getSpeedKmh();
                    final String fuelSystemVal = fuelSystem.getFormattedResult();
                    final String engineLoadVal = engineLoad.getFormattedResult();
                    final String coolantTempVal = coolantTemp.getCoolantTempC();
                    final double fuelLevelPercent = fuelLevel.getFuelLevel();
                    final String fuelLevelVal = String.format("%.1f L", tankSizeLiters * (fuelLevelPercent / 100));
                    final String batteryVoltage = batteryVoltageCommand.getFormattedResult();
                    double currentFuelLevelPercent = fuelLevelCommand.getFuelLevel();

                    // Initialize at start of trip
                    if (initialFuelLevelPercent < 0) {
                        initialFuelLevelPercent = currentFuelLevelPercent;
                    }

                    // Update max speed
                    if (currentSpeed > maxSpeed) maxSpeed = currentSpeed;

                    // Update distance
                    totalDistance += (currentSpeed / 3600.0); // km

                    // Calculate consumed fuel
                    fuelConsumed = tankSizeLiters * (initialFuelLevelPercent - currentFuelLevelPercent) / 100.0;

                    // Update UI
                    runOnUiThread(() -> {
                        txtCurrentSpeed.setText(currentSpeed + " km/h");
                        txtFuelStatus2.setText(fuelSystemVal);
                        txtBatteryVoltage2.setText(batteryVoltage);
                        txtEngineLoad2.setText(engineLoadVal);
                        txtEngineCoolant2.setText(coolantTempVal);
                        txtFuel2.setText(fuelLevelVal);
                        txtTopSpeed.setText(maxSpeed + " km/h");
                    });

                    Thread.sleep(1000); // Update every 1 second
                }

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(this::resetOBDData);
            }
        }).start();
    }



    private void resetOBDData() {
        txtFuelStatus2.setText("--");
        txtBatteryVoltage2.setText("-- V");
        txtEngineLoad2.setText("0%");
        txtEngineCoolant2.setText("--°C");
        txtFuel2.setText("--L");
        txtCurrentSpeed.setText("-- kmph");
        txtTopSpeed.setText("-- kmph");
        txtCarModel.setText("No Model");
    }
}
