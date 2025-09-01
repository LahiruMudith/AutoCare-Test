package com.example.hellojava;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hellojava.service.OBDService;

public class HomeActivity extends AppCompatActivity {

    private TextView txtStatus, txtRPM, txtFuel, txtCoolant;
    private Button btnConnect;

    private OBDService obdService;
    private boolean bound = false;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            OBDService.OBDBinder binder = (OBDService.OBDBinder) service;
            obdService = binder.getService();
            bound = true;
            txtStatus.setText("Service ready");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
            txtStatus.setText("Service disconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // Views
        txtStatus = findViewById(R.id.txtSearch);
        txtRPM = findViewById(R.id.txtRPM);
        txtFuel = findViewById(R.id.txtFuel);
        txtCoolant = findViewById(R.id.txtPeterchurch);
        btnConnect = findViewById(R.id.btnConnect);

        ImageView btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, MenuActivity.class)));

        txtRPM.setText("0");
        txtFuel.setText("0L");
        txtCoolant.setText("0°C");

        btnConnect.setOnClickListener(v -> {
            if (!bound) {
                txtStatus.setText("Service not ready...");
            } else {
                txtStatus.setText("Connecting to OBD-II...");
                obdService.connectOBD(status -> runOnUiThread(() -> txtStatus.setText(status)));

                // Start reading OBD data
                obdService.readOBDData((rpm, speed, fuel, coolant, vin) -> runOnUiThread(() -> {
                    txtRPM.setText(rpm);
                    txtFuel.setText(fuel);
                    txtCoolant.setText(coolant);
                }));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, OBDService.class);
        startService(intent); // ensures service stays alive
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            unbindService(connection);
            bound = false;
        }
    }
}
