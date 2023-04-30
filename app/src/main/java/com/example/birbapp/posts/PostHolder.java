package com.example.birbapp.posts;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.birbapp.R;
import com.example.birbapp.UserFeed;
import com.example.birbapp.user.UserModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class PostHolder extends RecyclerView.ViewHolder {
    private TextView postId;
    private TextView authorUsername;
    private TextView authorEmail;
    private TextView contentText;
    private TextView likeCount;
    private TextView repostCount;
    private TextView date;
    private ImageView likeButton;
    private ImageView repostButton;
    private ImageView deleteButton;
    private FirebaseFirestore firestore;
    private final String currentUserEmail;
    public Context context;

    public PostHolder(@NonNull View postView) {
        super(postView);

        this.firestore = FirebaseFirestore.getInstance();
        this.authorUsername = postView.findViewById(R.id.post_author_username);
        this.authorEmail = postView.findViewById(R.id.post_author_email);
        this.contentText = postView.findViewById(R.id.post_content_text);
        this.likeCount = postView.findViewById(R.id.post_like_counter);
        this.repostCount = postView.findViewById(R.id.post_repost_counter);
        this.date = postView.findViewById(R.id.post_date);
        this.likeButton = postView.findViewById(R.id.post_like_button);
        this.repostButton = postView.findViewById(R.id.post_repost_button);
        this.deleteButton = postView.findViewById(R.id.post_delete_button);
        this.postId = postView.findViewById(R.id.post_id);

        this.currentUserEmail = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
    }

    public void _setContext(Context context) {
        this.context = context;
    }

    public void bindTo(PostModel post) {
        this.authorUsername.setText("...");
        this.authorEmail.setText("...");
        this.postId.setText(post.id);

        firestore.document(post.author.getPath()).get().addOnSuccessListener(documentSnapshot -> {
            UserModel user = documentSnapshot.toObject(UserModel.class);
            if(user == null) return;
            authorUsername.setText(user.username);
            authorEmail.setText(user.email);
            if(Objects.equals(user.email, this.currentUserEmail)) {
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton.setOnClickListener(deletePost());
            }
        });

        this.contentText.setText(post.content);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        this.date.setText(simpleDateFormat.format(post.date.toDate()));
        this.likeCount.setText(String.valueOf(post.likes));
        this.repostCount.setText(String.valueOf(post.reposts));

        likeButton.setOnClickListener(likePost());

        repostButton.setOnClickListener(repostPost());
    }

    private View.OnClickListener likePost() {
        if(((UserFeed)context).likedPosts.contains(postId.getText().toString())) {
            likeButton.setImageResource(R.drawable.likefull);
            return null;
        }
        return view -> {
            DocumentReference postRef = firestore.collection("posts").document(postId.getText().toString());
            postRef.update("likes", FieldValue.increment(1))
                    .addOnSuccessListener(aVoid -> {
                        likeCount.setText(String.valueOf(Integer.parseInt(likeCount.getText().toString()) + 1));
                        likeButton.setOnClickListener(null);
                        Log.d("HOLDER", "post successfully liked!");
                        ((UserFeed)context).likedPosts.add(postId.getText().toString());
                        ((UserFeed)context).queryPostList();
                    })
                    .addOnFailureListener(e -> Log.w("HOLDER", "Error liking post", e));
        };
    }

    private View.OnClickListener repostPost() {
        if(((UserFeed)context).repostedPosts.contains(postId.getText().toString())) {
            repostButton.setImageResource(R.drawable.retweetfull);
            return null;
        }
        return view -> {
            DocumentReference postRef = firestore.collection("posts").document(postId.getText().toString());
            postRef.update("reposts", FieldValue.increment(1))
                    .addOnSuccessListener(aVoid -> {
                        firestore.collection("users")
                                .whereEqualTo("email", this.currentUserEmail)
                                .limit(1)
                                .get()
                                .addOnSuccessListener(documents -> {
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("author", firestore.document("/users/" + Objects.requireNonNull(documents.getDocuments().get(0).getId())));
                                    data.put("content", "RP: " + authorEmail.getText().toString() + "\n \"" + contentText.getText().toString()+"\"");
                                    data.put("date", new Timestamp(new Date()));
                                    data.put("likes", 0);
                                    data.put("reposts", 0);
                                    firestore.collection("posts").add(data).addOnSuccessListener(documentReference -> {
                                        repostCount.setText(String.valueOf(Integer.parseInt(repostCount.getText().toString()) + 1));
                                        repostButton.setOnClickListener(null);
                                        Log.d("HOLDER", "post successfully reposted!");
                                        ((UserFeed)context).repostedPosts.add(postId.getText().toString());
                                        ((UserFeed)context).queryPostList();
                                    });
                                });
                    })
                    .addOnFailureListener(e -> Log.w("HOLDER", "Error reposting", e));
        };
    }

    private View.OnClickListener deletePost() {
        if(!authorEmail.getText().toString().equals(currentUserEmail)) {
            Log.d("HOLDER", authorEmail.getText().toString() + "|" + currentUserEmail);
            return null;
        }
        return view -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete post")
                    .setMessage("Do you really want to delete this post?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> firestore.collection("posts").document(postId.getText().toString()).delete().addOnSuccessListener(unused -> {
                        Toast.makeText(context, "Post deleted.", Toast.LENGTH_SHORT).show();
                        deleteButton.setVisibility(View.GONE);
                        deleteButton.setOnClickListener(null);
                        ((UserFeed)context).queryPostList();
                    }))
                    .setNegativeButton(android.R.string.no, null).show();
        };
    }
}
