package com.example.birbapp.posts;

public class PostModel {
    //TODO: add other shit
    public String authorId;
    public String content;
    public int likes;
    public int reposts;

    public PostModel(String authorId, String content, int likes, int reposts) {
        this.authorId = authorId;
        this.content = content;
        this.likes = likes;
        this.reposts = reposts;
    }

    public PostModel() {

    }
}
