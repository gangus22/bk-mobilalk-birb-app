package com.example.birbapp.posts;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class PostModel {
    public String id;
    public DocumentReference author;
    public Timestamp date;
    public String content;
    public int likes;
    public int reposts;

    public PostModel(DocumentReference author, Timestamp date, String content, int likes, int reposts) {
        this.author = author;
        this.content = content;
        this.date = date;
        this.likes = likes;
        this.reposts = reposts;
    }

    public PostModel() {

    }

    public void _setId(String id) {
        this.id = id;
    }
}
