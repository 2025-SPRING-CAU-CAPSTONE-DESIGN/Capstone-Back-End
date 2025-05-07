package com.capstone.storyforest.user.controller;

import com.capstone.storyforest.global.apiPaylod.ApiResponse;                // 프로젝트 DTO
import com.capstone.storyforest.global.apiPaylod.code.status.SuccessStatus;
import com.capstone.storyforest.user.dto.LeaderboardEntryDTO;
import com.capstone.storyforest.user.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/* ─────────────────────────────────────────────────────────
   LeaderboardController + Swagger(OpenAPI) 문서 주석
   ───────────────────────────────────────────────────────── */
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Leaderboard", description = "리더보드 조회 API")
@RestController
@RequestMapping("/api/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    @Operation(
            summary = "리더보드 조회",
            description = "page·size 쿼리 파라미터로 리더보드를 페이징 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "잘못된 요청")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<List<LeaderboardEntryDTO>>> getBoard(
            @Parameter(description = "페이지 번호", example = "0")
            @RequestParam(defaultValue = "0")  int page,
            @Parameter(description = "페이지 크기", example = "20")
            @RequestParam(defaultValue = "20") int size) {

        List<LeaderboardEntryDTO> board =
                leaderboardService.getLeaderboard(page, size);

        return ResponseEntity.ok(
                ApiResponse.onSuccess(SuccessStatus._OK, board));
    }
}
