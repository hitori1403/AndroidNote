package com.example.androidnote;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    Button buttonRegister;
    TextView textViewLoginNow;
    ProgressBar progressBarRegister;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.et_email);
        editTextPassword = findViewById(R.id.et_password);
        buttonRegister = findViewById(R.id.btn_register);
        textViewLoginNow = findViewById(R.id.tv_login_now);
        progressBarRegister = findViewById(R.id.pb_register);

        mAuth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(view -> {
            String email = String.valueOf(editTextEmail.getText());
            String password = String.valueOf(editTextPassword.getText());

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(Register.this, "Email address cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(Register.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            buttonRegister.setVisibility(View.GONE);
            progressBarRegister.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                progressBarRegister.setVisibility(View.GONE);
                buttonRegister.setVisibility(View.VISIBLE);

                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "Registration successful! Welcome aboard!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        textViewLoginNow.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });
    }
}