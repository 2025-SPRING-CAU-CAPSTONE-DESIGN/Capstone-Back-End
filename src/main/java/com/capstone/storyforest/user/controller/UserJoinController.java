package com.capstone.storyforest.user.controller;

import com.capstone.storyforest.global.apiPaylod.ApiResponse;
import com.capstone.storyforest.global.apiPaylod.code.status.SuccessStatus;
import com.capstone.storyforest.user.dto.JoinRequestDTO;
import com.capstone.storyforest.user.dto.JoinResponseDTO;

import com.capstone.storyforest.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.capstone.storyforest.user.entity.User;

@RestController
@RequiredArgsConstructor
public class UserJoinController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<ApiResponse<?>> joinProcess(@RequestBody @Valid JoinRequestDTO joinRequestDTO) {

        User user = userService.joinProcess(joinRequestDTO);
        JoinResponseDTO joinResponseDTO = new JoinResponseDTO(user);

        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, joinResponseDTO));
    }
}
