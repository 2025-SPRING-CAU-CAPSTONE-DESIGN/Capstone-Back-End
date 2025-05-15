package com.capstone.storyforest.story.repository;

import com.capstone.storyforest.story.entity.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {
}
