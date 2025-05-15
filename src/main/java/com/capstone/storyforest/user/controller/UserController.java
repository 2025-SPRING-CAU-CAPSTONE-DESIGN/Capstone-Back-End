package com.capstone.storyforest.user.controller;


import com.capstone.storyforest.global.apiPaylod.ApiResponse;
import com.capstone.storyforest.global.apiPaylod.code.status.SuccessStatus;
import com.capstone.storyforest.user.dto.*;
import com.capstone.storyforest.user.entity.User;
import com.capstone.storyforest.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.NotBlank;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/admin")
    public String admin() {
        return "admin!!";
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse<?>> getUserInfo(@RequestHeader("Authorization") String authorizationHeader) {

        String accessToken = authorizationHeader.replace("Bearer ", "");

        User user = userService.getUserInfo(accessToken);

        UserResponseDTO userResponseDTO = new UserResponseDTO(user);

        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, userResponseDTO));
    }

    @GetMapping("users/{nickname}/check")
    public ResponseEntity<ApiResponse<?>> checkNickname(
            @PathVariable("nickname") @NotBlank String nickname) {

        // 닉네임 사용 가능 여부 체크
        boolean isAble = userService.checkNickname(nickname);

        // 사용 가능하면 성공 응답, 아니면 이미 사용 중인 닉네임
        if (isAble) {
            return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, "사용 가능한 닉네임입니다."));
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.onFailure("400", "이미 사용 중인 닉네임입니다.", null));
        }
    }

    @GetMapping("users/{storyId}/story")
    public ResponseEntity<ApiResponse<?>> getStory(
            @PathVariable("storyId") @NotNull int storyId, @RequestHeader("Authorization") String authorizationHeader) {

        String accessToken = authorizationHeader.replace("Bearer ", "");

        StoryResponseDTO storyResponseDTO = userService.getStory(storyId, accessToken);

        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, storyResponseDTO));

    }

    @GetMapping("/users/tier")
    public ResponseEntity<ApiResponse<?>> getTierInfo(@RequestHeader("Authorization") String authorizationHeader) {

        String accessToken = authorizationHeader.replace("Bearer ", "");

        GetTierResponseDTO getTierResponseDTO = userService.getTierInfo(accessToken);

        return ResponseEntity.ok(ApiResponse.onSuccess(SuccessStatus._OK, getTierResponseDTO));
    }







}
