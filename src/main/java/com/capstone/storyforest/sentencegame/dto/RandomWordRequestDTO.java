package com.capstone.storyforest.sentencegame.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class RandomWordRequestDTO {
    @Min(1) @Max(3) private int level;
}