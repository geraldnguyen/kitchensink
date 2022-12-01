package com.example.springweb.friend.data.repository;

import com.example.springweb.friend.data.entity.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<FriendEntity, Long> {
    List<FriendEntity> findByName(String name);
}
