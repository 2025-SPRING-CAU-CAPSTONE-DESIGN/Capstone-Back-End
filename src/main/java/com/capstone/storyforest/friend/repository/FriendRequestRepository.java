package com.capstone.storyforest.friend.repository;

import com.capstone.storyforest.friend.domain.FriendRequest;
import com.capstone.storyforest.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findAllByReceiverAndStatus(User receiver, FriendRequest.Status status);
    List<FriendRequest> findAllBySenderAndStatus(User sender, FriendRequest.Status status);

}
