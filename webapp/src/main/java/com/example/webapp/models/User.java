package com.example.webapp.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    private Long userId;
    private String firstName,lastName,middleName,login,password;
    @ManyToOne
    @JoinColumn(name="postId", referencedColumnName = "postId", nullable = false)
    private Post post;
    public User(){

    }
    public User(Long userId, String firstName, String lastName, String middleName, String login,String password, Post post) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.login = login;
        this.password=password;
        this.post = post;
    }

    @OneToMany
    private List<TimeSheet> timeSheets;

    public User(Long userId) {
        this.userId=userId;
    }

    public User(Long id,String firstname, String lastname, String middlename, String login, String password) {
        this.userId=id;
        this.firstName=firstname;
        this.lastName=lastname;
        this.middleName=middlename;
        this.login=login;
        this.password=password;
    }
    public User(Long id,String firstname, String lastname, String middlename, String login) {
        this.userId=id;
        this.firstName=firstname;
        this.lastName=lastname;
        this.middleName=middlename;
        this.login=login;
    }
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
