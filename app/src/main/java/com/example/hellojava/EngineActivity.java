package com.example.hellojava;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hellojava.command.EngineLoadCommand;
import com.example.hellojava.command.FuelSystemStatusCommand;
import com.example.hellojava.command.OBDConnectionManager;
import com.github.pires.obd.commands.control.ModuleVoltageCommand;
import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.commands.temperature.EngineCoolantTemperatureCommand;

public class EngineActivity extends AppCompatActivity {

    private TextView txtVehicleName, txtRPM, txtEngineCoolant, txtEngineLoad,
            txtBatteryVoltage, txtFuelStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.engin);

        // Initialize views
        txtVehicleName = findViewById(R.id.txtVehicleName);
        txtRPM = findViewById(R.id.txtRPM);
        txtEngineCoolant = findViewById(R.id.txtEngineCoolant);
        txtEngineLoad = findViewById(R.id.txtEngineLoad);
        txtBatteryVoltage = findViewById(R.id.txtBatteryVoltage);
        txtFuelStatus = findViewById(R.id.txtFuelStatus);

        ImageView btnSpeed = findViewById(R.id.imageView55);
        btnSpeed.setOnClickListener(v -> {
            Intent intent = new Intent(EngineActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        // Start fetching OBD-II data
        readOBDData();
    }

    private void readOBDData() {
        new Thread(() -> {
            try {
                if (OBDConnectionManager.getSocket() == null) {
                    runOnUiThread(() -> txtVehicleName.setText("Not Connected"));
                    return;
                }

                // RPM
                RPMCommand rpmCmd = new RPMCommand();
                rpmCmd.run(
                        OBDConnectionManager.getSocket().getInputStream(),
                        OBDConnectionManager.getSocket().getOutputStream()
                );
                final String rpm = rpmCmd.getFormattedResult();

                // Engine Load
                EngineLoadCommand loadCmd = new EngineLoadCommand();
                loadCmd.run(
                        OBDConnectionManager.getSocket().getInputStream(),
                        OBDConnectionManager.getSocket().getOutputStream()
                );
                final String load = loadCmd.getFormattedResult();

                // Engine Coolant Temperature
                EngineCoolantTemperatureCommand coolantCmd = new EngineCoolantTemperatureCommand();
                coolantCmd.run(
                        OBDConnectionManager.getSocket().getInputStream(),
                        OBDConnectionManager.getSocket().getOutputStream()
                );
                final String coolant = coolantCmd.getFormattedResult();

                // Fuel System Status
                FuelSystemStatusCommand fuelCmd = new FuelSystemStatusCommand();
                fuelCmd.run(
                        OBDConnectionManager.getSocket().getInputStream(),
                        OBDConnectionManager.getSocket().getOutputStream()
                );
                final String fuelStatus = fuelCmd.getFormattedResult();

                // Battery Voltage
                ModuleVoltageCommand voltageCmd = new ModuleVoltageCommand();
                voltageCmd.run(
                        OBDConnectionManager.getSocket().getInputStream(),
                        OBDConnectionManager.getSocket().getOutputStream()
                );
                final String voltage = voltageCmd.getFormattedResult();

                runOnUiThread(() -> {
                    txtRPM.setText(rpm);
                    txtEngineLoad.setText(load);
                    txtEngineCoolant.setText(coolant);
                    txtFuelStatus.setText(fuelStatus);
                    txtBatteryVoltage.setText(voltage);
                    txtVehicleName.setText("Connected"); // or VIN if you want
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> txtVehicleName.setText("Not Connected"));
            }
        }).start();
    }
}
