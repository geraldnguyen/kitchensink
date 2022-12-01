package com.example.springweb.multiple_bag_fetch_exception.data.repository;

import com.example.springweb.multiple_bag_fetch_exception.data.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select distinct p\n" +
        "    from Post p\n" +
        "    join fetch p.comments")
    List<Post> findAllPosts();
}
