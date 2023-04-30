package com.example.birbapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreatePost extends AppCompatActivity {

    private FirebaseFirestore firestore;

    private CollectionReference firebasePosts;

    private TextView newPostContent;

    private String currentUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        this.firestore = FirebaseFirestore.getInstance();
        this.firebasePosts = firestore.collection("posts");

        this.newPostContent = findViewById(R.id.new_post_content);

        this.currentUserEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

    }

    public void newPost(View view) {
        if (newPostContent.getText().toString().length() > 255 || currentUserEmail == null) return;
        firestore.collection("users")
                .whereEqualTo("email", this.currentUserEmail)
                .limit(1)
                .get()
                .addOnSuccessListener(documents -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("author", firestore.document("/users/" + Objects.requireNonNull(documents.getDocuments().get(0).getId())));
                    data.put("content", newPostContent.getText().toString());
                    data.put("date", new Timestamp(new Date()));
                    data.put("likes", 0);
                    data.put("reposts", 0);
                    firebasePosts.add(data).addOnSuccessListener(documentReference -> Toast.makeText(CreatePost.this, "Post created.", Toast.LENGTH_SHORT).show());
                    finish();
                });
    }

    public void cancelNewPost(View view) {
        finish();
    }
}