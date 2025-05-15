package com.capstone.storyforest.user.repository;

import com.capstone.storyforest.story.entity.Story;
import com.capstone.storyforest.user.entity.UserStory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.capstone.storyforest.user.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserStoryRepository extends JpaRepository<UserStory, Long> {
    int countByUser(User user);

    @Query("SELECT us FROM UserStory us WHERE us.user = :user ORDER BY us.id ASC")
    List<UserStory> findTopNByUser(@Param("user") User user, Pageable pageable);

    @Query("SELECT us.story FROM UserStory us WHERE us.id = :userStoryId")
    Story findStoryByUserStoryId(@Param("userStoryId") Long userStoryId);
}
