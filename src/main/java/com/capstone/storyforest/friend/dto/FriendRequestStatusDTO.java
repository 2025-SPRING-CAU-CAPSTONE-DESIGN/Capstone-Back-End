package com.capstone.storyforest.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendRequestStatusDTO {
    private Long requestId;
    private String status;  // "ACCEPTED" 또는 "REJECTED"
}
