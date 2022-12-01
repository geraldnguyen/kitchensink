package com.example.springweb.multiple_bag_fetch_exception.data.repository;

import com.example.springweb.multiple_bag_fetch_exception.data.entity.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
}
