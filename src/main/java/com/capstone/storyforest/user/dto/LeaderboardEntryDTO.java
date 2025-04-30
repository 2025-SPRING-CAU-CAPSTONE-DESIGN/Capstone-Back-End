package com.capstone.storyforest.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LeaderboardEntryDTO {

    private int rank; // 순위
    private String username;
    private int    level;
    private int    totalScore;

}
