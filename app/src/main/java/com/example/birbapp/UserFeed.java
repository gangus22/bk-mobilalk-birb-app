package com.example.birbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.example.birbapp.posts.PostAdapter;
import com.example.birbapp.posts.PostModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserFeed extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<PostModel> postList;
    private PostAdapter postAdapter;

    private FirebaseFirestore firestore;
    private CollectionReference firebasePosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_feed_list);

        this.firestore = FirebaseFirestore.getInstance();
        this.firebasePosts = this.firestore.collection("posts");

        this.postList = new ArrayList<PostModel>();
        this.postAdapter = new PostAdapter(this, postList);

        this.recyclerView = findViewById(R.id.post_list_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(postAdapter);

        queryPostList();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void queryPostList() {
        this.postList.clear();
        //TODO: filter for relevant posts

        this.firebasePosts.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                PostModel item = document.toObject(PostModel.class);
                postList.add(item);
            }
            this.postAdapter.notifyDataSetChanged();
        });
    }
}