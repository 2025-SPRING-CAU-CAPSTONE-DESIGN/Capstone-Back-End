package com.capstone.storyforest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.storyforest.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
