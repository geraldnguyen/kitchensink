package com.example.springweb.multiple_bag_fetch_exception.controller;

import com.example.springweb.multiple_bag_fetch_exception.data.entity.Post;
import com.example.springweb.multiple_bag_fetch_exception.data.entity.PostComment;
import com.example.springweb.multiple_bag_fetch_exception.data.repository.PostCommentRepository;
import com.example.springweb.multiple_bag_fetch_exception.data.repository.PostRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;


    public PostController(PostRepository postRepository, PostCommentRepository postCommentRepository) {
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
    }

    @GetMapping("/")
    public List<Post> all() {
        return postRepository.findAllPosts();
    }

    @PostMapping("/samples")
    public List<Post> createSample() {
        int noOfPosts = 10;
        for (int i = 0; i < noOfPosts; i++) {
            var post = Post.builder()
                    .title("post " + i)
                    .build();
            postRepository.saveAndFlush(post);

            var comments =  List.of(
                    PostComment.builder()
                            .review("review " + i + ".1").post(post)
                            .build(),
                    PostComment.builder()
                            .review("review " + i + ".2").post(post)
                            .build()
            );
            postCommentRepository.saveAllAndFlush(comments);
        }


        return postRepository.findAllPosts();
    }

}
