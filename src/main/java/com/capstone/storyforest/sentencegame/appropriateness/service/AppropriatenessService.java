package com.capstone.storyforest.sentencegame.appropriateness.service;

import com.capstone.storyforest.sentencegame.appropriateness.dto.AppropriatenessResult;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AppropriatenessService {
    AppropriatenessResult evaluate(String sentence, List<String> requiredWords);

    // 비동기 메서드
    @Async("taskExecutor")
    CompletableFuture<AppropriatenessResult> evaluateAsync(String sentence, List<String> requiredWords);
}
