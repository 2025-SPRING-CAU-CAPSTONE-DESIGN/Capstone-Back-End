package com.capstone.storyforest.user.controller;


import com.capstone.storyforest.global.apiPaylod.ApiResponse;
import com.capstone.storyforest.global.apiPaylod.code.status.SuccessStatus;
import com.capstone.storyforest.user.dto.JoinRequestDTO;
import com.capstone.storyforest.user.dto.JoinResponseDTO;
import com.capstone.storyforest.user.dto.UserResponseDTO;
import com.capstone.storyforest.user.entity.User;
import com.capstone.storyforest.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/admin")
    public String admin() {
        return "admin!!";
    }

    @PostMapping("/user")
    public ResponseEntity<ApiResponse<?>> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {

        String accessToken = authorizationHeader.replace("Bearer ", "");

        User user = userService.getUserInfo(accessToken);

        UserResponseDTO userResponseDTO = new UserResponseDTO(user);

        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, userResponseDTO));
    }



}
