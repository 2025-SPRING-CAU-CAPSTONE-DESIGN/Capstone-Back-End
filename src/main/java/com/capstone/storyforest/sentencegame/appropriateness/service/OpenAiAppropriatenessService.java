package com.capstone.storyforest.sentencegame.appropriateness.service;

import com.capstone.storyforest.sentencegame.appropriateness.dto.AppropriatenessResult;
import com.capstone.storyforest.sentencegame.filtering.dto.OpenAiMessage;
import com.capstone.storyforest.sentencegame.filtering.dto.OpenAiRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAiAppropriatenessService implements AppropriatenessService{

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.key}")
    private String apiKey;

    @Override
    public AppropriatenessResult evaluate(String sentence, List<String> requiredWords) {
        try {
            // 1) 프롬프트 제작
            String prompt = String.format(
                    "Evaluate whether the following sentence appropriately uses all required words.\n\n" +
                            "Sentence: \"%s\"\n" +
                            "Required words: %s\n\n" +
                            "Respond *only* in pure JSON:\n" +
                            "{\"isAppropriate\": true/false, \"mismatchedWords\": [/* list of words used incorrectly */]}",
                    sentence, requiredWords);

            // 2) OpenAiRequest 생성 & system 메시지 추가
            OpenAiRequest req = new OpenAiRequest(model, prompt);
            req.getMessages().add(0, new OpenAiMessage("system",
                    "You are a semantic appropriateness evaluator. " +
                            "Return only a JSON object with keys isAppropriate and mismatchedWords."));

            // 3) HTTP 요청
            RequestEntity<OpenAiRequest> request = RequestEntity
                    .post(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .body(req);

            JsonNode root = restTemplate.exchange(request, JsonNode.class).getBody();

            // 4) 응답 파싱
            String content = root.path("choices").path(0).path("message").path("content")
                    .asText().trim()
                    .replaceAll("```", "").replaceAll("`", "");

            log.debug("OpenAI 적절성 raw => {}", content);

            // 5) DTO 변환
            return objectMapper.readValue(content, AppropriatenessResult.class);

        } catch (Exception e) {
            log.error("OpenAI 적절성 평가 오류", e);
            // 실패 시 ‘부적절’로 간주
            return new AppropriatenessResult(false, List.of());
        }
    }

}
