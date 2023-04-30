package com.example.birbapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.email = findViewById(R.id.main_login_email);
        this.password = findViewById(R.id.main_login_password);
        ImageView logo = findViewById(R.id.birb_logo);
        logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse));

        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public void attemptLogin(View view) {
        this.firebaseAuth.signInWithEmailAndPassword(extractString(this.email), extractString(this.password))
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this,"Login error. Make sure you're registered!", Toast.LENGTH_LONG).show();
                    } else {
                        startActivity(new Intent(this, UserFeed.class));
                    }
                });
    }

    public void registerAction(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private String extractString(EditText editText) {
        return editText.getText().toString();
    }
}