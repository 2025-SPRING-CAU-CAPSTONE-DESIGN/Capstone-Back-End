package com.capstone.storyforest.sentencegame.appropriateness.service;

import com.capstone.storyforest.sentencegame.appropriateness.dto.AppropriatenessResult;

import java.util.List;

public interface AppropriatenessService {
    AppropriatenessResult evaluate(String sentence, List<String> requiredWords);
}
