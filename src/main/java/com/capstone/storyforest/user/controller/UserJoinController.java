package com.capstone.storyforest.user.controller;

import com.capstone.storyforest.global.apiPaylod.ApiResponse;                // 프로젝트 DTO
import com.capstone.storyforest.global.apiPaylod.code.status.SuccessStatus;
import com.capstone.storyforest.user.dto.*;

import com.capstone.storyforest.user.entity.User;
import com.capstone.storyforest.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/* ─────────────────────────────────────────────────────────
   UserJoinController + Swagger(OpenAPI) 문서 주석
   ───────────────────────────────────────────────────────── */
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "회원가입 · 로그인 API")
@RestController
@RequiredArgsConstructor
public class UserJoinController {

    private final UserService userService;

    /* ───────── 1. 회원가입 ───────── */
    @Operation(
            summary = "회원가입",
            description = "회원가입을 수행하고 새 사용자 정보를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "입력값 오류")
    })
    @PostMapping("/join")
    public ResponseEntity<ApiResponse<JoinResponseDTO>> joinProcess(
            @RequestBody(
                    description = "회원가입 DTO",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = JoinRequestDTO.class)
                    )
            )
            @Valid JoinRequestDTO joinRequestDTO) {

        User user = userService.joinProcess(joinRequestDTO);
        JoinResponseDTO joinResponseDTO = new JoinResponseDTO(user);

        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK, joinResponseDTO));
    }

    /* ───────── 2. 로그인 ───────── */
    @Operation(
            summary = "로그인",
            description = "JWT 토큰을 포함한 로그인 응답을 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserLoginResponseDTO>> login(
            @RequestBody(
                    description = "로그인 DTO",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = UserLoginRequestDTO.class)
                    )
            ) UserLoginRequestDTO userLoginRequestDTO) {

        UserLoginResponseDTO loginResponseDTO = userService.login(userLoginRequestDTO);
        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK, loginResponseDTO));
    }
}
