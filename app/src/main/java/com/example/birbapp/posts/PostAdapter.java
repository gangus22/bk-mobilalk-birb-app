package com.example.birbapp.posts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birbapp.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostHolder> {

    private ArrayList<PostModel> posts;

    private Context context;

    public PostAdapter(Context context, ArrayList<PostModel> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostHolder(LayoutInflater.from(this.context).inflate(R.layout.post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        PostModel post = posts.get(position);
        holder.bindTo(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}