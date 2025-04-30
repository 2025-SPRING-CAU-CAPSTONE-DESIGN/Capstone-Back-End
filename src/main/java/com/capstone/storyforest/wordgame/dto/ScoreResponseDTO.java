package com.capstone.storyforest.wordgame.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScoreResponseDTO {
    // 업데이트 된 누적 점수
    private int totalScore;

    public ScoreResponseDTO() {

    }

    public ScoreResponseDTO(int totalScore) {
        this.totalScore = totalScore;
    }
}
