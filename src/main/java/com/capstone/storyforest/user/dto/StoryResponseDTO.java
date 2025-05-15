package com.capstone.storyforest.user.dto;

import lombok.Getter;

@Getter
public class StoryResponseDTO {
    private Long storyId;
    private String title;
    private String content;
    private int score;

    public StoryResponseDTO(Long storyId, String title, String content, int score) {
        this.storyId = storyId;
        this.title = title;
        this.content = content;
        this.score = score;
    }
}
