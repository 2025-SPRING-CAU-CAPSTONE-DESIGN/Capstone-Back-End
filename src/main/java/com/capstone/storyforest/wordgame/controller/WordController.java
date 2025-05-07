package com.capstone.storyforest.wordgame.controller;

import com.capstone.storyforest.global.apiPaylod.ApiResponse;                // 프로젝트 DTO
import com.capstone.storyforest.global.apiPaylod.code.status.SuccessStatus;
import com.capstone.storyforest.user.entity.User;
import com.capstone.storyforest.wordgame.dto.ScoreResponseDTO;
import com.capstone.storyforest.wordgame.dto.WordRequestDTO;
import com.capstone.storyforest.wordgame.entity.Word;
import com.capstone.storyforest.wordgame.service.WordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* ─────────────────────────────────────────────────────────
   WordController + Swagger(OpenAPI) 문서 주석
   ───────────────────────────────────────────────────────── */
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "WordGame", description = "단어 게임 API")
@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    /* ───────── 1. 랜덤 단어 10개(5:5) ───────── */
    @Operation(
            summary = "단어 섞어 반환",
            description = "난이도 레벨에 맞춰 5:5 섞인 단어 리스트를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "입력 오류")
    })
    @PostMapping("/random")
    public ResponseEntity<ApiResponse<List<Word>>> randomWords(
            @RequestBody(
                    description = "단어 요청 DTO",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = WordRequestDTO.class)
                    )
            )
            @Valid WordRequestDTO dto) {

        List<Word> words = wordService.getMixedWords(dto.getLevel());
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK, words)
        );
    }

    /* ───────── 2. 라운드 점수 누적 ───────── */
    @Operation(
            summary = "단어 라운드 점수",
            description = "단어 퀴즈 라운드를 완료하고 점수를 계산해 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "검증 실패")
    })
    @PostMapping("/score")
    public ResponseEntity<ApiResponse<ScoreResponseDTO>> score(
            @RequestBody(
                    description = "라운드 결과 DTO",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = WordRequestDTO.class)
                    )
            ) WordRequestDTO dto,
            User user) {

        ScoreResponseDTO resp = wordService.addRoundScore(dto, user);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK, resp)
        );
    }
}
