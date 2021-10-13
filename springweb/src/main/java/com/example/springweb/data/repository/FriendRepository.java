package com.example.springweb.data.repository;

import com.example.springweb.data.entity.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<FriendEntity, Long> {
}
