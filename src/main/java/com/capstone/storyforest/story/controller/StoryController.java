package com.capstone.storyforest.story.controller;

import com.capstone.storyforest.global.apiPaylod.ApiResponse;
import com.capstone.storyforest.global.apiPaylod.code.status.SuccessStatus;
import com.capstone.storyforest.story.dto.StoryRequestDTO;
import com.capstone.storyforest.story.dto.StoryResponseDTO;
import com.capstone.storyforest.story.service.StoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

    @PostMapping
    public ResponseEntity<ApiResponse<StoryResponseDTO>> createStory(
            @RequestBody @Valid StoryRequestDTO request
    ) {
        StoryResponseDTO response = storyService.createStory(request);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK, response)
        );
    }
}
