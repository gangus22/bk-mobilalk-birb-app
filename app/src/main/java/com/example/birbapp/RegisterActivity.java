package com.example.birbapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.birbapp.user.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText password;
    private EditText passwordAgain;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.username = findViewById(R.id.register_username);
        this.email = findViewById(R.id.register_email);
        this.password = findViewById(R.id.register_password);
        this.passwordAgain = findViewById(R.id.register_password_again);

        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void attemptRegister(View view) {
        String username = extractString(this.username);
        String email = extractString(this.email);
        String pass1 = extractString(this.password);
        String pass2 = extractString(this.passwordAgain);

        if(!pass1.equals(pass2)) {
            Toast.makeText(RegisterActivity.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(username.isEmpty() || email.isEmpty() || pass1.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        this.firebaseAuth.createUserWithEmailAndPassword(email, pass1)
                .addOnCompleteListener(this, registerOnCompleteListener(email, username));
    }

    public void cancel(View view) {
        finish();
    }

    private String extractString(EditText editText) {
        return editText.getText().toString();
    }

    private OnCompleteListener<AuthResult> registerOnCompleteListener(String email, String username) {
        // TODO: make this run in a separate thread
        return task -> {
            if(task.isSuccessful()) {
                this.firestore.collection("users").add(new UserModel(email, username)).addOnCompleteListener(task1 -> {
                    Toast.makeText(RegisterActivity.this,"Registration successful! Welcome to Birb.app!", Toast.LENGTH_LONG).show();
                    finish();
                });
            } else {
                Toast.makeText(RegisterActivity.this,"Registration error." + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
            }
        };
    }
}