package com.capstone.storyforest.user.repository;

import com.capstone.storyforest.user.entity.UserStory;
import org.springframework.data.jpa.repository.JpaRepository;
import com.capstone.storyforest.user.entity.User;

public interface UserStoryRepository extends JpaRepository<UserStory, Long> {
    int countByUser(User user);
}
