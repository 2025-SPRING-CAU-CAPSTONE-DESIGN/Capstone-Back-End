package com.capstone.storyforest.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.storyforest.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    User findByUsername(String username);
}
