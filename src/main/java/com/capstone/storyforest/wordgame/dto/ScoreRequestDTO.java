package com.capstone.storyforest.wordgame.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ScoreRequestDTO {

    // 라운드에 제시된 단어들의 난이도(1~3)까지 있음
    private int difficulty;

}


