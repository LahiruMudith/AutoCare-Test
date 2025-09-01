package com.example.hellojava.command;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BluetoothStateReceiver extends BroadcastReceiver {

    private final Context context;

    public BluetoothStateReceiver(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            if (state == BluetoothAdapter.STATE_OFF) {
                showEnableBluetoothDialog();
            }
        }
    }

    private void showEnableBluetoothDialog() {
        new AlertDialog.Builder(context)
                .setTitle("Bluetooth is OFF")
                .setMessage("Please enable Bluetooth to connect to OBD-II")
                .setPositiveButton("Enable", (dialog, which) -> {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Needed if context is not Activity
                    context.startActivity(enableBtIntent);
                })
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .show();
    }
}
