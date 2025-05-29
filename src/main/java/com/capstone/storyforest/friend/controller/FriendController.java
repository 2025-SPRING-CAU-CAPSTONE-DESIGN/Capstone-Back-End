package com.capstone.storyforest.friend.controller;

import com.capstone.storyforest.friend.domain.FriendRequest;
import com.capstone.storyforest.friend.dto.*;
import com.capstone.storyforest.friend.service.FriendService;
import com.capstone.storyforest.friend.service.NotificationService;
import com.capstone.storyforest.user.service.UserService;
import com.capstone.storyforest.user.entity.User;
import com.capstone.storyforest.global.apiPaylod.ApiResponse;
import com.capstone.storyforest.global.apiPaylod.code.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;
    private final NotificationService notificationService;
    private final UserService userService;  // UserService 주입

    /**
     * 요청 헤더에서 토큰을 파싱해 UserService로 User 객체 조회
     */
    private User getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header is missing or invalid");
        }
        String token = authHeader.substring(7);
        return userService.getUserInfo(token);
    }

    @GetMapping("/requests")
    public ApiResponse<List<FriendRequestResponseDTO>> getRequests(HttpServletRequest request) {
        User user = getCurrentUser(request);
        List<FriendRequestResponseDTO> requests = friendService.getPendingRequests(user);
        return ApiResponse.onSuccess(SuccessStatus._OK, requests);
    }

    @PostMapping("/requests")
    public ApiResponse<FriendRequestNotificationDTO> sendFriendRequest(
            HttpServletRequest request,
            @RequestBody FriendRequestDTO dto
    ) {
        User user = getCurrentUser(request);
        FriendRequestNotificationDTO responseDto = friendService.sendRequest(user, dto);
        return ApiResponse.onSuccess(SuccessStatus._OK, responseDto);
    }

    @PatchMapping("/requests/{id}/accept")
    public ApiResponse<FriendRequestStatusDTO> acceptRequest(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        User user = getCurrentUser(request);
        FriendRequest fr = friendService.acceptRequest(user, id);
        FriendRequestStatusDTO dto = new FriendRequestStatusDTO(fr.getId(), fr.getStatus().name());
        return ApiResponse.onSuccess(SuccessStatus._OK, dto);
    }

    @PatchMapping("/requests/{id}/reject")
    public ApiResponse<FriendRequestStatusDTO> rejectRequest(
            HttpServletRequest request,
            @PathVariable Long id
    ) {
        User user = getCurrentUser(request);
        FriendRequest fr = friendService.rejectRequest(user, id);
        FriendRequestStatusDTO dto = new FriendRequestStatusDTO(fr.getId(), fr.getStatus().name());
        return ApiResponse.onSuccess(SuccessStatus._OK, dto);
    }

    @GetMapping
    public ApiResponse<List<FriendResponseDTO>> getFriends(HttpServletRequest request) {
        User user = getCurrentUser(request);
        List<FriendResponseDTO> friends = friendService.getFriends(user);
        return ApiResponse.onSuccess(SuccessStatus._OK, friends);
    }

    @GetMapping("/notifications/stream")
    public SseEmitter subscribeWithToken(@RequestParam("token") String token) {
        // 토큰으로 User 조회
        User user = userService.getUserInfo(token);
        // 해당 유저 ID로 SSE 구독
        return notificationService.subscribe(user.getId());
    }
}
