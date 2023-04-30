package com.example.birbapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Collections;

public class UserFeed extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RelativeLayout postListHeader;
    private ImageView loadingSpinner;
    private ArrayList<PostModel> postList;
    private PostAdapter postAdapter;
    private FirebaseFirestore firestore;
    private CollectionReference firebasePosts;
    private AlarmManager alarmManager;

    public ArrayList<String> likedPosts;

    public ArrayList<String> repostedPosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_feed_list);

        this.postListHeader = findViewById(R.id.post_list_header);
        this.loadingSpinner = findViewById(R.id.post_list_loading_icon);

        this.likedPosts = new ArrayList<>();
        this.repostedPosts = new ArrayList<>();

        this.firestore = FirebaseFirestore.getInstance();
        this.firebasePosts = this.firestore.collection("posts");

        this.postList = new ArrayList<>();
        this.postAdapter = new PostAdapter(this, postList);

        this.recyclerView = findViewById(R.id.post_list_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(postAdapter);

        queryPostList();

        setupPostUpdateAlarm();
    }

    public void toPostCreation(View view) {
        startActivity(new Intent(this, CreatePost.class));
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
            Collections.reverse(postList);
            this.postAdapter.notifyDataSetChanged();
        });
    }

    private void setupPostUpdateAlarm() {
        this.alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, PostUpdateReceiver.class);
        PendingIntent updatePostListIntent = PendingIntent.getBroadcast(this, 666, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME,
                0,
                6000L,
                updatePostListIntent
        );

        registerReceiver(broadcastReceiver, new IntentFilter("REFRESH_POSTS"));
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            queryPostList();
        }
    };

    private void startSpinAnimation() {
        postListHeader.setVisibility(View.VISIBLE);
        Animation spin = AnimationUtils.loadAnimation(this, R.anim.spin);
        loadingSpinner.startAnimation(spin);
    };

    private void stopSpinAnimation() {
        postListHeader.setVisibility(View.INVISIBLE);
        loadingSpinner.clearAnimation();
    }
}