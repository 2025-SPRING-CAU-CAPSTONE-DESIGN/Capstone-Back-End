package com.capstone.storyforest.sentencegame.creativity.service;

import com.capstone.storyforest.sentencegame.creativity.dto.CreativityCheckResult;
import com.capstone.storyforest.sentencegame.filtering.dto.OpenAiRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class OpenAiCreativityService implements CreativityService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${openai.api.url}")
    private String openAiUrl;

    @Value("${openai.model}")
    private String openAiModel;

    @Value("${openai.api.key}")
    private String openAiKey;

    @Override
    public boolean isCreative(String sentence) {
        try {
            // 1) 창의성 평가를 요청하는 프롬프트
            String prompt = String.format(
                    "다음 문장이 사전적인 '창의성'의 정의에 부합하는지 평가해주세요:\n\n\"%s\"\n\n"
                            + "결과를 순수 JSON 형식으로 {\"isCreative\": true/false} 만 반환해주세요.",
                    sentence
            );

            // 2) OpenAI 요청 생성
            RequestEntity<OpenAiRequest> request = RequestEntity
                    .post(openAiUrl)
                    .header("Authorization", "Bearer " + openAiKey)
                    .body(new OpenAiRequest(openAiModel, prompt));

            // 3) 응답을 JsonNode 로 받기
            JsonNode root = restTemplate
                    .exchange(request, JsonNode.class)
                    .getBody();

            // 4) 텍스트 추출 및 마크다운 제거
            String content = root.path("choices")
                    .path(0)
                    .path("message")
                    .path("content")
                    .asText()
                    .trim()
                    .replaceAll("```", "")
                    .replaceAll("`", "");

            log.debug("OpenAI 창의성 raw content => {}", content);

            // 5) JSON 파싱
            CreativityCheckResult result = objectMapper.readValue(content, CreativityCheckResult.class);

            return Boolean.TRUE.equals(result.getIsCreative());
        } catch (Exception e) {
            log.error("OpenAI 창의성 판단 오류", e);
            // 실패 시에는 false 로 간주
            return false;
        }
    }
}
