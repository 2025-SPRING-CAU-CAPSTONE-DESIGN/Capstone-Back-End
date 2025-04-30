package com.capstone.storyforest.wordgame.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordRequestDTO {
    @Min(1) @Max(3)
    private int level;
}
