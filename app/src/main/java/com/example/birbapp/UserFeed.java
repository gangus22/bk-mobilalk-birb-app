package com.example.birbapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.birbapp.posts.PostAdapter;
import com.example.birbapp.posts.PostModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class UserFeed extends AppCompatActivity {

    private RecyclerView recyclerView;

    private RelativeLayout postListHeader;

    private ImageView loadingSpinner;
    private ArrayList<PostModel> postList;
    private PostAdapter postAdapter;
    private FirebaseFirestore firestore;
    private CollectionReference firebasePosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_feed_list);

        this.postListHeader = findViewById(R.id.post_list_header);
        this.loadingSpinner = findViewById(R.id.post_list_loading_icon);

        this.firestore = FirebaseFirestore.getInstance();
        this.firebasePosts = this.firestore.collection("posts");

        this.postList = new ArrayList<>();
        this.postAdapter = new PostAdapter(this, postList);

        this.recyclerView = findViewById(R.id.post_list_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(postAdapter);

        queryPostList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        queryPostList();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void queryPostList() {
        startSpinAnimation();
        this.postList.clear();

        this.firebasePosts.orderBy("date").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                PostModel item = document.toObject(PostModel.class);
                item._setId(document.getId());
                postList.add(item);
            }
            stopSpinAnimation();
            this.postAdapter.notifyDataSetChanged();
        });
    }

    private void startSpinAnimation() {
        postListHeader.setVisibility(View.VISIBLE);
        Animation spin = AnimationUtils.loadAnimation(this, R.anim.spin);
        loadingSpinner.startAnimation(spin);
    };

    private void stopSpinAnimation() {
        postListHeader.setVisibility(View.GONE);
        loadingSpinner.clearAnimation();
    }
}