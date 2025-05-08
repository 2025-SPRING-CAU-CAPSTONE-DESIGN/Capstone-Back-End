package com.capstone.storyforest.sentencegame.appropriateness.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class AppropriatenessResult {
    private Boolean isAppropriate;
    private List<String> mismatchedWords;
}