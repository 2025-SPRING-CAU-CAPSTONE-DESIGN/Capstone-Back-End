package com.capstone.storyforest.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendRequestNotificationDTO {

    private Long requestId;
    private String fromNickname;
}
