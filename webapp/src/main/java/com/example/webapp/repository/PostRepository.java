package com.example.webapp.repository;

import com.example.webapp.models.Post;
import com.example.webapp.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PostRepository extends CrudRepository<Post,Long> {
    @Query("select u.postId from Post u where u.post = :#{#post}")
    Optional<Post> findByPost(String post);
}
