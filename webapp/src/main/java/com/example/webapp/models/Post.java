package com.example.webapp.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {
    @Id
    private Long postId;
    private String post;
    private int oklad;
    @OneToMany
    private List<User> users;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getOklad() {
        return oklad;
    }

    public void setOklad(int oklad) {
        this.oklad = oklad;
    }

    public Post(Long postId) {
        this.postId = postId;
    }

    public Post(){

    }
    public Post(Long postId, String post, int oklad) {
        this.postId = postId;
        this.post = post;
        this.oklad = oklad;
    }
}
