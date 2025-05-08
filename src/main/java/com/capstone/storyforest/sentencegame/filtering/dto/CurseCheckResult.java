package com.capstone.storyforest.sentencegame.filtering.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class CurseCheckResult {

    private final Boolean isCurse;

    private final List<String> words;

    private final boolean isAiChecked;

    public boolean isCurse() {
        return isCurse;
    }

    public boolean isAiChecked() {
        return isAiChecked;
    }

}
