package com.capstone.storyforest.sentencegame.filtering.service;

import com.capstone.storyforest.sentencegame.filtering.dto.CurseCheckResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiProfanityService implements ProfanityService {

    private final AiCheckManager aiCheckManager;

    @Override
    public boolean hasProfanity(String text) {
        try {
            CurseCheckResult result = aiCheckManager.requestAiCheck(text);
            return result.isCurse();
        } catch (JsonProcessingException e) {
            log.error("AI 욕설 감지 중 예외 발생", e);
            // 예외 상황에서도 시스템이 죽지 않도록 false 반환 (필요에 따라 커스텀 예외 던져도 됨)
            return false;
        }
    }
}