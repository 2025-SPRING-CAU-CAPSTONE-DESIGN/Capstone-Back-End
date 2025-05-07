package com.capstone.storyforest.sentencegame.controller;

import com.capstone.storyforest.global.apiPaylod.ApiResponse;               // 프로젝트 DTO
import com.capstone.storyforest.global.apiPaylod.code.status.SuccessStatus;
import com.capstone.storyforest.sentencegame.dto.RandomWordRequestDTO;
import com.capstone.storyforest.sentencegame.dto.SentenceScoreResponseDTO;
import com.capstone.storyforest.sentencegame.dto.SentenceSubmitRequestDTO;
import com.capstone.storyforest.sentencegame.service.SentenceService;
import com.capstone.storyforest.user.entity.User;
import com.capstone.storyforest.wordgame.entity.Word;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* ─────────────────────────────────────────────────────────
   SentenceController + Swagger(OpenAPI) 문서 주석
   ───────────────────────────────────────────────────────── */
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(name = "Sentence", description = "문장 게임 API")
@RestController
@RequestMapping("/api/sentence")
@RequiredArgsConstructor
public class SentenceController {

    private final SentenceService sentenceService;

    /* ───────── A. 랜덤 단어 7개 ───────── */
    @Operation(
            summary = "랜덤 단어 7개",
            description = "레벨(1~3)을 받아 해당 난이도의 단어 7개를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "성공",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "잘못된 입력")
    })
    @PostMapping("/random")
    public ResponseEntity<ApiResponse<List<String>>> random(
            @RequestBody(
                    description = "레벨 DTO",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RandomWordRequestDTO.class)))
            @Valid RandomWordRequestDTO randomWordRequestDTO) {

        List<String> terms = sentenceService.get7Words(randomWordRequestDTO.getLevel())
                .stream()
                .map(Word::getTerm)
                .toList();

        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK, terms));
    }

    /* ───────── B. 문장 제출 ───────── */
    @Operation(
            summary = "문장 제출 및 점수 계산",
            description = "문장을 제출하고 7개 단어 사용·창의성 평가 후 점수를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "성공",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "검증 실패")
    })
    @PostMapping("/score")
    public ResponseEntity<ApiResponse<SentenceScoreResponseDTO>> score(
            @RequestBody(
                    description = "문장 및 단어 DTO",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = SentenceSubmitRequestDTO.class)))
            @Valid SentenceSubmitRequestDTO sentenceSubmitRequestDTO,
            User user) throws JsonProcessingException {

        return ResponseEntity.ok(
                ApiResponse.onSuccess(
                        SuccessStatus._OK,
                        sentenceService.submitSentence(sentenceSubmitRequestDTO, user)));
    }
}
