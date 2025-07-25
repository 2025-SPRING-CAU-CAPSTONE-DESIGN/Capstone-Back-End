package com.capstone.storyforest.sentencegame;

import com.capstone.storyforest.global.apiPaylod.ApiResponse;
import com.capstone.storyforest.global.apiPaylod.code.status.SuccessStatus;
import com.capstone.storyforest.sentencegame.dto.RandomWordRequestDTO;
import com.capstone.storyforest.sentencegame.dto.SentenceFeedbackResponseDTO;
import com.capstone.storyforest.sentencegame.dto.SentenceScoreResponseDTO;
import com.capstone.storyforest.sentencegame.dto.SentenceSubmitRequestDTO;
import com.capstone.storyforest.sentencegame.service.SentenceService;
import com.capstone.storyforest.user.entity.User;
import com.capstone.storyforest.wordgame.entity.Word;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/sentence")
@RequiredArgsConstructor
public class SentenceController {

    private final SentenceService sentenceService;;

    /* A. 랜덤 단어 7개 */
    @PostMapping("/random")
    public ResponseEntity<ApiResponse<List<String>>> random(
            @RequestBody @Valid RandomWordRequestDTO randomWordRequestDTO){

        List<String> terms = sentenceService.get7Words(randomWordRequestDTO.getLevel())
                .stream().map(Word::getTerm).toList();
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK, terms));
    }




    @PostMapping("/score")
    public ResponseEntity<ApiResponse<SentenceFeedbackResponseDTO>> score(
            @RequestBody @Valid SentenceSubmitRequestDTO sentenceSubmitRequestDTO,
            User user) throws JsonProcessingException {

        return ResponseEntity.ok(
                ApiResponse.onSuccess(
                        SuccessStatus._OK,
                        sentenceService.submitSentence(sentenceSubmitRequestDTO, user)));
    }


    /*@PostMapping("/score")
    public CompletableFuture<ResponseEntity<ApiResponse<SentenceFeedbackResponseDTO>>> score(
            @RequestBody @Valid SentenceSubmitRequestDTO dto,
            User user
    ) {
        return sentenceService.submitSentenceAsync(dto, user)
                .thenApply(feedback ->
                        ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, feedback))
                );
    }
*/
}
