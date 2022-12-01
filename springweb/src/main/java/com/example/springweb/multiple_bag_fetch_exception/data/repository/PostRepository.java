package com.example.springweb.multiple_bag_fetch_exception.data.repository;

import com.example.springweb.multiple_bag_fetch_exception.data.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // select Post, following by separate queries for Tag and PostComment for **each** Post
    @Query("select distinct p\n" +
            "    from Post p")
    List<Post> findAll_additionalFetching();

    // select Post AND PostComment, following by separate queries for Tag for **each** Post
    @Query("select distinct p\n" +
    "    from Post p\n" +
    "    join fetch p.comments")
    List<Post> findAll_fetchComments();


//    @Query("select p\n" +
//            "    from Post p\n" +
//            "    left join fetch p.comments\n" +
//            "    left join fetch p.tags")
//    **NOTE**: We need to comment out this query because  of org.hibernate.loader.MultipleBagFetchException:
//    cannot simultaneously fetch multiple bags: [com.example.springweb.multiple_bag_fetch_exception.data.entity.Post.comments,
//    com.example.springweb.multiple_bag_fetch_exception.data.entity.Post.tags]
//    List<Post> findAll_fetchCommentsAndTags();

    @Query("select distinct p\n" +
            "    from Post p\n" +
            "    join fetch p.tags")
    List<Post> findAll_fetchTags();


}
