package com.capstone.storyforest.user.dto;

import com.capstone.storyforest.user.entity.User;
import lombok.Builder;
import lombok.Getter;


@Getter
public class UserResponseDTO {
    private String username;
    private int totalScore;
    private int level;
    private Long id;

    @Builder
    public UserResponseDTO(User user){
        this.username = user.getUsername();
        this.level = user.getLevel();
        this.totalScore = user.getTotalScore();
        this.id = user.getId();
    }
}


