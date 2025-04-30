package com.capstone.storyforest.wordgame.controller;

import com.capstone.storyforest.global.apiPaylod.ApiResponse;
import com.capstone.storyforest.global.apiPaylod.code.status.SuccessStatus;
import com.capstone.storyforest.user.entity.User;
import com.capstone.storyforest.wordgame.dto.ScoreRequestDTO;
import com.capstone.storyforest.wordgame.dto.ScoreResponseDTO;
import com.capstone.storyforest.wordgame.entity.Word;
import com.capstone.storyforest.wordgame.service.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    /** 단어 N개·난이도 무작위 */
    @GetMapping("/random")
    public ResponseEntity<ApiResponse<List<Word>>> randomWords(
            @RequestParam(defaultValue = "10") int count) {

        List<Word> words = wordService.getRandomWords(count);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK, words)
        );
    }

    /** 전부 정답 후 점수 누적 */
    @PostMapping("/score")
    public ResponseEntity<ApiResponse<ScoreResponseDTO>> score(
            @RequestBody ScoreRequestDTO dto,
            @AuthenticationPrincipal User user) {

        ScoreResponseDTO resp = wordService.addRoundScore(dto, user);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK, resp)
        );
    }
}

