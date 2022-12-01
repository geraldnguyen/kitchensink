package com.example.springweb.multiple_bag_fetch_exception.controller;

import com.example.springweb.multiple_bag_fetch_exception.data.entity.Post;
import com.example.springweb.multiple_bag_fetch_exception.data.entity.PostComment;
import com.example.springweb.multiple_bag_fetch_exception.data.entity.Tag;
import com.example.springweb.multiple_bag_fetch_exception.data.repository.PostCommentRepository;
import com.example.springweb.multiple_bag_fetch_exception.data.repository.PostRepository;
import com.example.springweb.multiple_bag_fetch_exception.data.repository.TagRepository;
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
    private final TagRepository tagRepository;


    public PostController(PostRepository postRepository, PostCommentRepository postCommentRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping("/additional-fetching")
    public List<Post> additionalFetching() {
        return postRepository.findAll_additionalFetching();
    }

    @GetMapping("/fetch-comments")
    public List<Post> fetchComments() {
        return postRepository.findAll_fetchComments();
    }

    @PostMapping("/samples")
    public List<Post> createSample() {
        var tags = List.of(
                Tag.builder().name("java").build(),
                Tag.builder().name("jpa").build()
        );
        tagRepository.saveAllAndFlush(tags);

        int noOfPosts = 10;
        for (int i = 0; i < noOfPosts; i++) {
            var post = Post.builder()
                    .title("post " + i)
                    .tags(tags)
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


        return postRepository.findAll();
    }

}
