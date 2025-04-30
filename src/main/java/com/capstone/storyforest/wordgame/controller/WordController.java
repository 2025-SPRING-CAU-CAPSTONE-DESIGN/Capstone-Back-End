package com.capstone.storyforest.wordgame.controller;

import com.capstone.storyforest.global.apiPaylod.ApiResponse;
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

@RestController
@RequestMapping("/api/words")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    @PostMapping("/random")
    public ResponseEntity<ApiResponse<List<Word>>> randomWords(
            @RequestBody @Valid WordRequestDTO dto) {

        List<Word> words = wordService.getMixedWords(dto.getLevel());
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK, words)
        );
    }

    /** 전부 정답 후 점수 누적 */
    @PostMapping("/score")
    public ResponseEntity<ApiResponse<ScoreResponseDTO>> score(
            @RequestBody WordRequestDTO dto,
            User user) {

        ScoreResponseDTO resp = wordService.addRoundScore(dto, user);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK, resp)
        );
    }
}

