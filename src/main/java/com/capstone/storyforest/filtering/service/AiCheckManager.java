package com.capstone.storyforest.filtering.service;

import com.capstone.storyforest.filtering.dto.CurseCheckResult;
import com.capstone.storyforest.filtering.dto.OpenAiMessage;
import com.capstone.storyforest.filtering.dto.OpenAiRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Slf4j
public class AiCheckManager {

    private final RestTemplate restTemplate;
    private final String       openAiUrl;
    private final String       openAiModel;
    private final String       openAiKey;
    private final ObjectMapper objectMapper;

    public AiCheckManager(
            RestTemplate restTemplate,
            @Value("${openai.api.url}") String openAiUrl,
            @Value("${openai.model}") String openAiModel,
            @Value("${openai.api.key}") String openAiKey,
            ObjectMapper objectMapper
    ) {
        this.restTemplate = restTemplate;
        this.openAiUrl = openAiUrl;
        this.openAiModel = openAiModel;
        this.openAiKey = openAiKey;
        this.objectMapper = objectMapper;
    }

    /**
     * GPT에게 문장을 보내 욕설 여부를 체크한다.
     */
    public CurseCheckResult requestAiCheck(String target) throws JsonProcessingException {
        log.debug("AI 검증 시작. target = {}", target);

        // 1) 시스템 메시지 포함하여 RequestEntity 생성
        RequestEntity<OpenAiRequest> request = getOpenAiRequest(target);

        // 2) 응답을 JsonNode로 바로 받기
        JsonNode root = restTemplate
                .exchange(request, JsonNode.class)
                .getBody();

        // 3) JsonNode → CurseCheckResult
        CurseCheckResult curseCheckResult = getCurseCheckResult(root);

        // 4) GPT가 curse=false로 응답하면 그대로 리턴
        if (!curseCheckResult.isCurse()) {
            return curseCheckResult;
        }

        // 5) 단순 포함 여부도 확인
        if (isMatched(target, curseCheckResult.getWords())) {
            log.debug("GPT 정상 target = {}, words = {}", target, String.join(",", curseCheckResult.getWords()));
            return curseCheckResult;
        }

        // 6) 치명적 오류 시 false 로 바꿔서 리턴
        log.error("GPT 오류 target = {}, words = {}", target, String.join(",", curseCheckResult.getWords()));
        return new CurseCheckResult(false, List.of(), true);
    }

    private RequestEntity<OpenAiRequest> getOpenAiRequest(String target) {
        // OpenAiRequest에는 기존 user 메시지만 담으므로, system 메시지를 맨 앞에 추가
        OpenAiRequest req = new OpenAiRequest(openAiModel, target);
        req.getMessages().add(0, new OpenAiMessage("system",
                "You are a profanity detector. Respond _only_ with valid JSON exactly in this shape: " +
                        "{ \"isCurse\": boolean, \"words\": [string, ...] }. " +
                        "Do not wrap in markdown or add any other keys."));

        return RequestEntity
                .post(openAiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiKey)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(req);
    }

    private CurseCheckResult getCurseCheckResult(JsonNode root) throws JsonProcessingException {
        // GPT가 준 content 필드(JSON 문자열)만 추출해서 백틱·``` 등 제거
        String content = root.path("choices")
                .path(0)
                .path("message")
                .path("content")
                .asText()
                .trim()
                .replaceAll("`", "")
                .replaceAll("^json", "")
                .replaceAll("^```", "")
                .replaceAll("```$", "");

        log.debug("GPT raw content => {}", content);

        // content(JSON) → CurseCheckResult 객체
        CurseCheckResult result = objectMapper.readValue(content, CurseCheckResult.class);
        return new CurseCheckResult(result.isCurse(), result.getWords(), true);
    }

    private boolean isMatched(String target, List<String> words) {
        return words.stream().anyMatch(target::contains);
    }
}
