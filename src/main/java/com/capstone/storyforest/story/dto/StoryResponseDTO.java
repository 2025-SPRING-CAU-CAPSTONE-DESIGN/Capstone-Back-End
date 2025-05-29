package com.capstone.storyforest.story.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoryResponseDTO {
    private Long id;
    private String title;
    private String content;
    private int score;
}