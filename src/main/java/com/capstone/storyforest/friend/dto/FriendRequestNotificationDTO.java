package com.capstone.storyforest.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FriendRequestNotificationDTO {

    private Long requestId;
    private String fromNickname;
    private LocalDateTime sentAt;
}
