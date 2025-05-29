package com.capstone.storyforest.story.controller;

import com.capstone.storyforest.global.apiPaylod.ApiResponse;
import com.capstone.storyforest.global.apiPaylod.code.status.SuccessStatus;
import com.capstone.storyforest.story.dto.StoryRequestDTO;
import com.capstone.storyforest.story.service.StoryService;
import com.capstone.storyforest.user.dto.StoryResponseDTO;
import com.capstone.storyforest.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;
    private final UserService userService;


    @PostMapping
    public ResponseEntity<ApiResponse<StoryResponseDTO>> createStory(
            @RequestBody @Valid StoryRequestDTO request,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        // "Bearer " 제거
        String accessToken = authorizationHeader.replace("Bearer ", "");
        StoryResponseDTO dto = userService.createUserStory(request, accessToken);
        return ResponseEntity
                .status(SuccessStatus._OK.getHttpStatus())
                .body(ApiResponse.onSuccess(SuccessStatus._OK, dto));
    }
}
