package com.example.springweb.multiple_bag_fetch_exception.data.repository;

import com.example.springweb.multiple_bag_fetch_exception.data.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
