package com.capstone.storyforest.user.dto;

import com.capstone.storyforest.wordgame.config.LevelUtil;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import com.capstone.storyforest.user.entity.User;

@Getter
public class JoinResponseDTO {

    private final Long id;
    private final String username;
    private final LocalDate birthDate;
    private final String role;

    // 추가한 필드
    private final int level;
    private final int totalScore;

    @Builder
    public JoinResponseDTO(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.birthDate = user.getBirthDate();
        this.role = user.getRole();
        this.level = user.getLevel();
        this.totalScore = user.getTotalScore();
    }

}
