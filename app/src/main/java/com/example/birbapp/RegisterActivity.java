package com.example.birbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText password;
    private EditText passwordAgain;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.username = findViewById(R.id.register_username);
        this.email = findViewById(R.id.register_email);
        this.password = findViewById(R.id.register_password);
        this.passwordAgain = findViewById(R.id.register_password_again);

        this.firebaseAuth = FirebaseAuth.getInstance();
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
        if(username.isEmpty() || email.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        this.firebaseAuth.createUserWithEmailAndPassword(email, pass1)
                .addOnCompleteListener(this, registerOnCompleteListener());

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        Objects.requireNonNull(this.firebaseAuth.getCurrentUser()).updateProfile(profileUpdates);
    }

    public void cancel(View view) {
        finish();
    }

    private String extractString(EditText editText) {
        return editText.getText().toString();
    }

    private OnCompleteListener<AuthResult> registerOnCompleteListener() {
        return new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this,"Registration successful! Welcome to Birb.app!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegisterActivity.this,"Registration error. Something went wrong.", Toast.LENGTH_LONG).show();
                }
                finish();
            }
        };
    }
}