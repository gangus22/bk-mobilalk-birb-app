package com.example.birbapp.posts;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birbapp.R;

public class PostHolder extends RecyclerView.ViewHolder {
    private ImageView authorImage;
    private TextView authorUsername;
    private TextView authorEmail;
    private TextView contentText;
    public PostHolder(@NonNull View postView) {
        super(postView);

        this.authorUsername = postView.findViewById(R.id.post_author_username);
        this.authorEmail = postView.findViewById(R.id.post_author_email);
        this.contentText = postView.findViewById(R.id.post_content_text);
        this.authorImage = postView.findViewById(R.id.post_profile_pic);
        // TODO: add likes/reposts

        // TODO: bind buttons and onclick listeners here
    }

    public void bindTo(PostModel post) {
        this.authorUsername.setText("test");
        this.authorEmail.setText("test2");
        this.contentText.setText(post.content);

        //TODO: load image with glide/find other tool
    }
}
