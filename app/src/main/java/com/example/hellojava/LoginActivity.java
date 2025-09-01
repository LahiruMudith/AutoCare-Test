package com.example.hellojava;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText txtEmail, txtPassword;
    private Button btnLogin;

    // Hardcoded user for testing
    private final String correctEmail = "test@gmail.com";
    private final String correctPassword = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); // your XML above

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnSignUp); // "Log in" button id

        btnLogin.setOnClickListener(v -> {
            String email = txtEmail.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();

            if (email.equals(correctEmail) && password.equals(correctPassword)) {
                // Success → go to HomeActivity
                Intent intent = new Intent(LoginActivity.this, NewHomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Wrong credentials
                Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
