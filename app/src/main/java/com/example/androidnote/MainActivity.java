package com.example.androidnote;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    TextView textViewEmail, textViewLogout;
    TextInputEditText editTextNote;
    Button buttonSave;
    ProgressBar progressBarSave;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewEmail = findViewById(R.id.tv_email);
        editTextNote = findViewById(R.id.et_note);
        buttonSave = findViewById(R.id.btn_save);
        textViewLogout = findViewById(R.id.tv_logout);
        progressBarSave = findViewById(R.id.pb_save);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
            return;
        }

        textViewEmail.setText(user.getEmail());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("notes").document(user.getUid());

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    editTextNote.setText(document.getString("note"));
                }
            } else {
                Toast.makeText(MainActivity.this, "Failed to retrieve your note", Toast.LENGTH_SHORT).show();
            }
        });

        buttonSave.setOnClickListener(view -> {
            buttonSave.setVisibility(View.GONE);
            progressBarSave.setVisibility(View.VISIBLE);

            docRef.set(new HashMap<String, String>() {{
                put("note", String.valueOf(editTextNote.getText()));
            }}).addOnCompleteListener(task -> {
                progressBarSave.setVisibility(View.GONE);
                buttonSave.setVisibility(View.VISIBLE);

                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to save", Toast.LENGTH_SHORT).show();
                }
            });
        });

        textViewLogout.setOnClickListener(view -> {
            mAuth.signOut();
            Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });
    }
}