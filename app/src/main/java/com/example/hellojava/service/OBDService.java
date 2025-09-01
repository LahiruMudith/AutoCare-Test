package com.example.hellojava.service;

import android.Manifest;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.github.pires.obd.commands.SpeedCommand;
import com.github.pires.obd.commands.control.VinCommand;
import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.commands.fuel.FuelLevelCommand;
import com.github.pires.obd.commands.temperature.EngineCoolantTemperatureCommand;

import java.io.IOException;
import java.util.UUID;

public class OBDService extends Service {

    private static final String TAG = "OBDService";
    private static final String OBD_MAC_ADDRESS = "1C:A1:35:69:8D:C5"; // Your OBD-II MAC
    private static final UUID OBD_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice obdDevice;
    private BluetoothSocket socket;

    private final IBinder binder = new OBDBinder();

    public interface OBDCallback {
        void onDataReceived(String rpm, String speed, String fuel, String coolant, String vin);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Do not automatically connect here unless you know permissions are granted
        // Instead, let the Activity call connectOBD() after binding
        return binder;
    }


    public class OBDBinder extends Binder {
        public OBDService getService() {
            return OBDService.this;
        }
    }

    /** Call this from Activity to receive OBD data */
    public void readOBDData(OBDCallback callback) {
        new Thread(() -> {
            try {
                if (socket == null || !socket.isConnected()) return;

                while (socket != null && socket.isConnected()) {  // safer loop
                    RPMCommand rpmCmd = new RPMCommand();
                    SpeedCommand speedCmd = new SpeedCommand();
                    FuelLevelCommand fuelCmd = new FuelLevelCommand();
                    EngineCoolantTemperatureCommand coolantCmd = new EngineCoolantTemperatureCommand();
                    VinCommand vinCmd = new VinCommand();

                    rpmCmd.run(socket.getInputStream(), socket.getOutputStream());
                    speedCmd.run(socket.getInputStream(), socket.getOutputStream());
                    fuelCmd.run(socket.getInputStream(), socket.getOutputStream());
                    coolantCmd.run(socket.getInputStream(), socket.getOutputStream());
                    vinCmd.run(socket.getInputStream(), socket.getOutputStream());

                    String rpm = rpmCmd.getFormattedResult();
                    String speed = speedCmd.getFormattedResult();
                    String fuel = fuelCmd.getFormattedResult();
                    String coolant = coolantCmd.getFormattedResult();
                    String vin = vinCmd.getFormattedResult();

                    callback.onDataReceived(rpm, speed, fuel, coolant, vin);

                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                Log.e(TAG, "OBD read error: " + e.getMessage(), e);
            }
        }).start();
    }

    public void connectOBD(OBDConnectionCallback callback) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        obdDevice = bluetoothAdapter.getRemoteDevice(OBD_MAC_ADDRESS);

        new Thread(() -> {
            try {
                callback.onStatusUpdate("Connecting to OBD-II...");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                socket = obdDevice.createRfcommSocketToServiceRecord(OBD_UUID);
                bluetoothAdapter.cancelDiscovery();
                socket.connect();
                callback.onStatusUpdate("Connected to: " + obdDevice.getName());
            } catch (IOException e) {
                callback.onStatusUpdate("Connection failed: " + e.getMessage());
            }
        }).start();
    }

    // Callback interface for connection status
    public interface OBDConnectionCallback {
        void onStatusUpdate(String status);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
